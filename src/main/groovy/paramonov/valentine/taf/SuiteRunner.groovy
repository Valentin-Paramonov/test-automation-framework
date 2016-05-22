package paramonov.valentine.taf

import paramonov.valentine.taf.suite.Suite
import rx.Observable

import javax.inject.Inject
import java.util.concurrent.ExecutorService

class SuiteRunner {
    interface TestResultPrinter {
        void startSuite(Suite suite)
        void printError(String message)
        void scenarioCompleted(ScenarioResult scenario)
    }

    private final TestResultPrinter resultPrinter
    private final ScenarioRunner scenarioRunner
    private final ExecutorService executor

    @Inject
    SuiteRunner(TestResultPrinter resultPrinter, ScenarioRunner scenarioRunner, ExecutorService executor) {
        this.resultPrinter = resultPrinter
        this.scenarioRunner = scenarioRunner
        this.executor = executor
    }

    void run(Suite suite) {
        resultPrinter.startSuite(suite)
        suite.scenarios
             .collect(scenarioRunner.&run)
             .inject { Observable scenarios, scenario -> scenarios.mergeWith(scenario) }
             .doOnCompleted(this.&finish)
             .subscribe({ resultPrinter.scenarioCompleted(it) }, { resultPrinter.printError(it.message) })
    }

    private finish() {
        executor.shutdown()
        println 'Finished'
    }
}
