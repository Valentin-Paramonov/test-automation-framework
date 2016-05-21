package paramonov.valentine.taf.cli

import freemind.Map
import groovy.transform.PackageScope
import org.apache.commons.cli.ParseException
import paramonov.valentine.taf.suite.parser.SuiteParser

import javax.xml.bind.JAXBContext

import static java.lang.System.exit

@PackageScope
class ArgParser {
    static suiteFrom(args) {
        def cli = cli()
        def options = cli.parse(args)
        if (!options) {
            exit(1)
        }
        if (options.arguments().size() != 1) {
            cli.usage()
            throw new IllegalArgumentException('No scenario file provided!')
        }
        options.arguments().first().with { suiteFile ->
            if (!new File(suiteFile).exists()) {
                cli.usage()
                throw new FileNotFoundException("$suiteFile does not exist!")
            }
            def suite = parsed(unmarshalled(suiteFile))
            suite.baseUrl = options.b
            return suite
        }
    }

    private static cli() {
        def cli = new CliBuilder(usage: 'taf -b <base-url> scenario.mm')
        cli.b(longOpt: 'base-url', 'The base url of the service to test', args: 1, argName: 'service to test', required: true)
        cli
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
