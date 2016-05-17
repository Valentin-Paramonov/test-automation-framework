package paramonov.valentine.taf.cli

import freemind.Map
import groovy.transform.PackageScope
import org.apache.commons.cli.ParseException
import paramonov.valentine.taf.ScenarioParser

import javax.xml.bind.JAXBContext

@PackageScope
class ArgParser {
    static scenarioFrom(args) {
        def cli = cli()
        def options = cli.parse(args)
        if (options.arguments().size() != 1) {
            cli.usage()
            throw new ParseException('No scenario file provided!')
        }
        options.arguments().first().with { scenarioFile ->
            if (!new File(scenarioFile).exists()) {
                cli.usage()
                throw new FileNotFoundException("$scenarioFile does not exist!")
            }
            return parsed(unmarshalled(scenarioFile))
        }
    }

    private static cli() {
        new CliBuilder(usage: 'taf scenario.mm')
    }

    private static unmarshalled(String scenarioFilePath) {
        try {
            JAXBContext.newInstance('freemind').createUnmarshaller().unmarshal(new File(scenarioFilePath)) as Map
        } catch (exception) {
            throw new ParseException("Failed to parse $scenarioFilePath: $exception.cause.message")
        }
    }

    private static parsed(Map scenario) {
        ScenarioParser.parse(scenario)
    }
}
