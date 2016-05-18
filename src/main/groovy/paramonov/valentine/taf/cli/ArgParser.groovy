package paramonov.valentine.taf.cli

import freemind.Map
import groovy.transform.PackageScope
import org.apache.commons.cli.ParseException
import paramonov.valentine.taf.SuiteParser

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
        options.arguments().first().with { suiteFile ->
            if (!new File(suiteFile).exists()) {
                cli.usage()
                throw new FileNotFoundException("$suiteFile does not exist!")
            }
            return parsed(unmarshalled(suiteFile))
        }
    }

    private static cli() {
        new CliBuilder(usage: 'taf scenario.mm')
    }

    private static unmarshalled(String suiteFilePath) {
        try {
            JAXBContext.newInstance('freemind').createUnmarshaller().unmarshal(new File(suiteFilePath)) as Map
        } catch (exception) {
            throw new ParseException("Failed to parse $suiteFilePath: $exception.cause.message")
        }
    }

    private static parsed(Map suite) {
        SuiteParser.parse(suite)
    }
}
