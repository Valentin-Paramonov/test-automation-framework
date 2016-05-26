package paramonov.valentine.taf

import groovyx.net.http.RESTClient
import paramonov.valentine.taf.suite.Scenario
import paramonov.valentine.taf.suite.ScenarioResult
import paramonov.valentine.taf.suite.Suite
import paramonov.valentine.taf.suite.SuiteResult
import rx.Observable
import spock.lang.Specification

import java.util.concurrent.ExecutorService

class SuiteRunnerSpec extends Specification {
    def printer = Mock(SuiteRunner.Printer)
    def scenarioRunner = Mock(ScenarioRunner)
    def executor = Mock(ExecutorService)
    def client = Mock(RESTClient)
    def suiteRunner = new SuiteRunner(printer, scenarioRunner, executor, client)

    def "Should run all the scenarios, assemble the suite result, and finish"() {
        given:
            def scenario1 = new Scenario()
            def scenarioResult1 = new ScenarioResult()
            def scenario2 = new Scenario()
            def scenarioResult2 = new ScenarioResult()
            def suite = new Suite(scenarios: [scenario1, scenario2])
        when:
            suiteRunner.run(suite)
        then:
            1 * printer.startSuite(suite)
            1 * scenarioRunner.run(scenario1) >> Observable.just(scenarioResult1)
            1 * scenarioRunner.run(scenario2) >> Observable.just(scenarioResult2)
            1 * printer.scenarioCompleted(scenarioResult1)
            1 * printer.scenarioCompleted(scenarioResult2)
            1 * printer.finishSuite(*_) >> { List<SuiteResult> args ->
                def suiteResult = args.first()
                assert suiteResult.suite == suite
                def scenarioResults = suiteResult.scenarioResults
                assert scenarioResults.containsAll(scenarioResult1, scenarioResult2)
            }
            1 * executor.shutdown()
            1 * client.shutdown()
    }
}
