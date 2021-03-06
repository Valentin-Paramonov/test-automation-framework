package paramonov.valentine.taf

import groovy.transform.PackageScope
import groovyx.net.http.RESTClient
import paramonov.valentine.taf.suite.Request
import paramonov.valentine.taf.suite.TestCase
import paramonov.valentine.taf.suite.TestCaseResult
import rx.Observable
import rx.Subscriber

import javax.inject.Inject
import java.util.concurrent.ExecutorService

@PackageScope
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
                    final request = testCase.request
                    final result = performRequest(request, testCase.parameters)
                    if (!subscriber.unsubscribed) {
                        subscriber.onNext(new TestCaseResult(testCase: testCase, result: result, status: concludeStatus(testCase.expectedResult, result)))
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

    private String performRequest(Request request, Map<String, String> parameters) {
        client."${request.method.toLowerCase()}"(path: request.path, query: parameters).data.result
    }

    private static String concludeStatus(String expected, String got) {
        expected == got ? 'PASSED' : 'FAILED'
    }
}
