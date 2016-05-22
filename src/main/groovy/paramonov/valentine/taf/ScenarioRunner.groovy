package paramonov.valentine.taf

import paramonov.valentine.taf.suite.Scenario
import rx.Observable
import rx.Subscriber

import javax.inject.Inject
import java.util.concurrent.ExecutorService

class ScenarioRunner {
    private final ExecutorService executor
    private final TestCaseRunner testCase

    @Inject
    ScenarioRunner(ExecutorService executor, TestCaseRunner testCase) {
        this.executor = executor
        this.testCase = testCase
    }

    Observable<ScenarioResult> run(Scenario scenario) {
        Observable.create({ Subscriber subscriber ->
            executor.submit {
                scenario.testCases
                        .collect(testCase.&run)
                        .inject { Observable cases, c -> cases.mergeWith(c) }
                        .toList()
                        .subscribe {
                            subscriber.onNext(new ScenarioResult(scenario: scenario, results: it))
                            subscriber.onCompleted()
                        }
            }
        } as Observable.OnSubscribe)
    }
}
