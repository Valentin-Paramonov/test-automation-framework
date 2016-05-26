package paramonov.valentine.taf

import groovyx.net.http.RESTClient
import paramonov.valentine.taf.suite.Request
import paramonov.valentine.taf.suite.TestCase
import paramonov.valentine.taf.suite.TestCaseResult
import spock.lang.Specification

import java.util.concurrent.ExecutorService

class TestCaseRunnerSpec extends Specification {
    def executor = Mock(ExecutorService)
    def client = Mock(RESTClient)
    def runner = new TestCaseRunner(executor, client)

    def "Should perform the request and assemble the test case result"() {
        given:
            def params = [a: '1', b: '2']
            def testCase = new TestCase(request: new Request(method: 'PATCH', path: '/a/b'), parameters: params, expectedResult: '3')
        when:
            TestCaseResult result
            runner.run(testCase).subscribe { result = it }
        then:
            1 * executor.submit(*_) >> { it[0]() }
            1 * client.patch(_) >> [data: [result: '3']]
            result != null
            result.testCase == testCase
            result.result == '3'
            result.status == 'PASSED'
    }
}
