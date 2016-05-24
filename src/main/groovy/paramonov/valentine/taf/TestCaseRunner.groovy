package paramonov.valentine.taf

import groovyx.net.http.RESTClient
import paramonov.valentine.taf.suite.Request
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
                try {
                    final response = performRequest(testCase.request)(query: testCase.parameters)
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

    private static Closure performRequest(Request request) {
        new RESTClient(request.path).&"${request.method.toLowerCase()}"
    }
}
