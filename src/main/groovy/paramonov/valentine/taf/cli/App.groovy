package paramonov.valentine.taf.cli

import dagger.ObjectGraph
import paramonov.valentine.taf.SuiteRunner

import static java.lang.System.err
import static java.lang.System.exit
import static paramonov.valentine.taf.cli.ArgParser.parse

class App {
    static main(args) {
        try {
            final parsedArgs = parse(args)
            ObjectGraph.create(new CliModule(parsedArgs.baseUrl))
                       .get(SuiteRunner)
                       .run(parsedArgs.suite)
        } catch (exception) {
            err.println exception.message
            exit(1)
        }
    }
}
