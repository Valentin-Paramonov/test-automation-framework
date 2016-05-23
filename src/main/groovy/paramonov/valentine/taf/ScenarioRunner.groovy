package paramonov.valentine.taf

import paramonov.valentine.taf.suite.Scenario
import rx.Observable
import rx.Subscriber

import javax.inject.Inject
import java.util.concurrent.ExecutorService

class ScenarioRunner {
    private final ExecutorService executor
    private final TestCaseRunner testCaseRunner

    @Inject
    ScenarioRunner(ExecutorService executor, TestCaseRunner testCaseRunner) {
        this.executor = executor
        this.testCaseRunner = testCaseRunner
    }

    Observable<ScenarioResult> run(Scenario scenario) {
        Observable.create({ Subscriber subscriber ->
            executor.submit {
                scenario.testCases
                        .collect(testCaseRunner.&run)
                        .inject { Observable cases, testCase -> cases.mergeWith(testCase) }
                        .toList()
                        .subscribe {
                            subscriber.onNext(new ScenarioResult(scenario: scenario, results: it))
                            subscriber.onCompleted()
                        }
            }
        } as Observable.OnSubscribe)
    }
}
