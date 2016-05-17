package paramonov.valentine.taf.cli

import paramonov.valentine.taf.Taf

import static java.lang.System.err
import static java.lang.System.exit
import static paramonov.valentine.taf.cli.ArgParser.scenarioFrom

class App {
    static main(args) {
        try {
            new Taf(new CliTestResultPrinter()).run(scenarioFrom(args))
        } catch (exception) {
            err.println exception.message
            exit(1)
        }
    }
}
