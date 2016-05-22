package paramonov.valentine.taf

import paramonov.valentine.taf.suite.TestCase
import rx.Observable
import rx.Subscriber

import javax.inject.Inject
import java.util.concurrent.ExecutorService

class TestCaseRunner {
    private final ExecutorService executor

    @Inject
    TestCaseRunner(ExecutorService executor) {
        this.executor = executor
    }

    Observable<TestCaseResult> run(TestCase testCase) {
        Observable.create({ Subscriber subscriber ->
            executor.submit {
                if (!subscriber.unsubscribed) {
                    subscriber.onNext(new TestCaseResult(name: testCase.description))
                    subscriber.onCompleted()
                }
            }
        } as Observable.OnSubscribe)
    }
}
