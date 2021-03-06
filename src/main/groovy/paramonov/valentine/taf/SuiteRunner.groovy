package paramonov.valentine.taf

import groovyx.net.http.RESTClient
import paramonov.valentine.taf.suite.ScenarioResult
import paramonov.valentine.taf.suite.Suite
import paramonov.valentine.taf.suite.SuiteResult
import rx.Observable

import javax.inject.Inject
import java.util.concurrent.ExecutorService

class SuiteRunner {
    interface Printer {
        void startSuite(Suite suite)
        void finishSuite(SuiteResult suite)
        void printError(String message)
        void scenarioCompleted(ScenarioResult scenario)
    }

    private final Printer printer
    private final ScenarioRunner scenarioRunner
    private final ExecutorService executor
    private final RESTClient client

    @Inject
    SuiteRunner(Printer printer, ScenarioRunner scenarioRunner, ExecutorService executor, RESTClient client) {
        this.printer = printer
        this.scenarioRunner = scenarioRunner
        this.executor = executor
        this.client = client
    }

    void run(Suite suite) {
        printer.startSuite(suite)
        suite.scenarios
             .collect(scenarioRunner.&run)
             .inject { Observable scenarios, scenario -> scenarios.mergeWith(scenario) }
             .doOnNext(printer.&scenarioCompleted)
             .toList()
             .doOnNext({ printer.finishSuite(new SuiteResult(suite: suite, scenarioResults: it)) })
             .doOnCompleted(this.&finish)
             .doOnError(this.&onError)
             .subscribe()
    }

    private onError(Throwable error) {
        finish()
        printer.printError(error.message)
    }

    private finish() {
        executor.shutdown()
        client.shutdown()
    }
}
