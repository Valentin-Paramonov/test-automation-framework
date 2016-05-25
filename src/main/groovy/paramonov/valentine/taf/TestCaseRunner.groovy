package paramonov.valentine.taf

import groovyx.net.http.RESTClient
import paramonov.valentine.taf.suite.TestCase
import rx.Observable
import rx.Subscriber

import javax.inject.Inject
import java.util.concurrent.ExecutorService

class TestCaseRunner {
    private final ExecutorService executor
    private final RESTClient client

    @Inject
    TestCaseRunner(ExecutorService executor, RESTClient client) {
        this.executor = executor
        this.client = client
    }

    Observable<TestCaseResult> run(TestCase testCase) {
        Observable.create({ Subscriber subscriber ->
            executor.submit {
                try {
                    final response = client."${testCase.request.method.toLowerCase()}"(query: testCase.parameters)
                    if (!subscriber.unsubscribed) {
                        subscriber.onNext(new TestCaseResult(testCase: testCase, result: response.data.result))
                        subscriber.onCompleted()
                    }
                } catch (exception) {
                    if (!subscriber.unsubscribed) {
                        subscriber.onError(exception)
                    }
                }
            }
        } as Observable.OnSubscribe)
    }
}
