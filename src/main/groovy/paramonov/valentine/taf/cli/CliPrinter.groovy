package paramonov.valentine.taf.cli

import groovy.transform.PackageScope
import paramonov.valentine.taf.suite.ScenarioResult
import paramonov.valentine.taf.SuiteRunner
import paramonov.valentine.taf.suite.Suite
import paramonov.valentine.taf.suite.SuiteResult

@PackageScope
class CliPrinter implements SuiteRunner.Printer {
    @Override
    void startSuite(Suite suite) {
        println "o $suite.name"
    }

    @Override
    void printError(String message) {
        System.err.println "Error: $message"
    }

    @Override
    void finishSuite(SuiteResult suite) {
        println "|o +:$suite.scenarioCount -:$suite.passedTestCaseCount/$suite.testCaseCount"
    }

    @Override
    void scenarioCompleted(ScenarioResult result) {
        println "+ $result.scenario.name"
        result.testCaseResults.each {
            println "|- $it.testCase.description"
            println "|  ${it.testCase.parameters.collect { k, v -> "$k: $v" }.join(', ')}"
            println "|  expected: $it.testCase.expectedResult, got: $it.result"
            println "|= $it.status"
        }
        println "|+ $result.passedTestCaseCount/$result.testCaseCount"
    }
}
