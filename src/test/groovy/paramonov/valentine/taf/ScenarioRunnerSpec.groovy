package paramonov.valentine.taf

import paramonov.valentine.taf.suite.Scenario
import paramonov.valentine.taf.suite.ScenarioResult
import paramonov.valentine.taf.suite.TestCase
import paramonov.valentine.taf.suite.TestCaseResult
import rx.Observable
import spock.lang.Specification

import java.util.concurrent.ExecutorService

class ScenarioRunnerSpec extends Specification {
    def executor = Mock(ExecutorService)
    def testCaseRunner = Mock(TestCaseRunner)
    def scenarioRunner = new ScenarioRunner(executor, testCaseRunner)

    def "Should invoke all the actors with expected arguments"() {
        given:
            def testCase1 = new TestCase()
            def testCaseResult1 = new TestCaseResult(status: 'FAILED')
            def testCase2 = new TestCase()
            def testCaseResult2 = new TestCaseResult(status: 'PASSED')
            def scenario = new Scenario(testCases: [testCase1, testCase2])
        when:
            ScenarioResult result
            scenarioRunner.run(scenario).subscribe { result = it }
        then:
            1 * executor.submit(*_) >> { it[0]() }
            1 * testCaseRunner.run(testCase1) >> Observable.just(testCaseResult1)
            1 * testCaseRunner.run(testCase2) >> Observable.just(testCaseResult2)
            result != null
            result.scenario == scenario
            result.testCaseResults.containsAll(testCaseResult1, testCaseResult2)
            result.testCaseCount == 2
            result.passedTestCaseCount == 1
    }
}
