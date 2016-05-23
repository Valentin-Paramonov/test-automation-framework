package paramonov.valentine.taf

import paramonov.valentine.taf.suite.Suite
import rx.Observable

import javax.inject.Inject
import java.util.concurrent.ExecutorService

class SuiteRunner {
    interface Printer {
        void startSuite(Suite suite)
        void printError(String message)
        void scenarioCompleted(ScenarioResult scenario)
    }

    private final Printer printer
    private final ScenarioRunner scenarioRunner
    private final ExecutorService executor

    @Inject
    SuiteRunner(Printer printer, ScenarioRunner scenarioRunner, ExecutorService executor) {
        this.printer = printer
        this.scenarioRunner = scenarioRunner
        this.executor = executor
    }

    void run(Suite suite) {
        printer.startSuite(suite)
        suite.scenarios
             .collect(scenarioRunner.&run)
             .inject { Observable scenarios, scenario -> scenarios.mergeWith(scenario) }
             .doOnCompleted(this.&finish)
             .subscribe({ printer.scenarioCompleted(it) }, { printer.printError(it.message) })
    }

    private finish() {
        executor.shutdown()
        println 'Finished'
    }
}
