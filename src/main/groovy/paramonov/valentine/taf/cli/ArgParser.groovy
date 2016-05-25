package paramonov.valentine.taf.cli

import freemind.Map
import groovy.transform.PackageScope
import org.apache.commons.cli.ParseException

import javax.xml.bind.JAXBContext

import static paramonov.valentine.taf.suite.parser.SuiteParser.parsed

@PackageScope
class ArgParser {
    static parse(args) {
        final cli = cli()
        final options = cli.parse(args)
        if (!options) {
            throw new IllegalArgumentException('No arguments provided!')
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
            final baseUrl = options.b
            if (!baseUrl.matches(/^http(s)?:\/\/(\w+\.)+\w+((\/\w+)*)?\/?$/)) {
                cli.usage()
                throw new IllegalArgumentException("$baseUrl is not a valid URL")
            }
            return new ParsedArgs(suite: parsed(unmarshalled(suiteFile)), baseUrl: baseUrl)
        }
    }

    private static cli() {
        final cli = new CliBuilder(usage: 'taf -b <base-url> suite.mm')
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
}
