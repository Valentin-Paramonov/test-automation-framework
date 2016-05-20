package paramonov.valentine.taf.cli

import dagger.ObjectGraph
import paramonov.valentine.taf.SuiteRunner

import static java.lang.System.err
import static java.lang.System.exit
import static paramonov.valentine.taf.cli.ArgParser.scenarioFrom

class App {
    static main(args) {
        try {
            ObjectGraph.create(new CliModule())
                       .get(SuiteRunner)
                       .run(scenarioFrom(args))
        } catch (exception) {
            err.println exception.message
            exit(1)
        }
    }
}
