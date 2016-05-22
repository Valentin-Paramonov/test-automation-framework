package paramonov.valentine.taf

import paramonov.valentine.taf.suite.Scenario
import paramonov.valentine.taf.suite.Suite
import spock.lang.Specification

import java.util.concurrent.ExecutorService

import static rx.Observable.empty

class SuiteRunnerSpec extends Specification {
    def printer = Mock(SuiteRunner.TestResultPrinter)
    def scenarioRunner = Mock(ScenarioRunner)
    def executor = Mock(ExecutorService)
    def suiteRunner = new SuiteRunner(printer, scenarioRunner, executor)

    def "Should print that suite is started, execute scenarioRunner for each scenario, and shutdown executor"() {
        given:
            def scenario1 = new Scenario()
            def scenario2 = new Scenario()
            def suite = new Suite(scenarios: [scenario1, scenario2])
        when:
            suiteRunner.run(suite)
        then:
            1 * printer.startSuite(suite)
            1 * scenarioRunner.run(scenario1) >> empty()
            1 * scenarioRunner.run(scenario2) >> empty()
            1 * executor.shutdown()
    }
}
