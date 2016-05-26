package paramonov.valentine.taf.cli

import groovy.transform.PackageScope
import paramonov.valentine.taf.suite.ScenarioResult
import paramonov.valentine.taf.SuiteRunner
import paramonov.valentine.taf.suite.Suite

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
    void scenarioCompleted(ScenarioResult result) {
        println "+ $result.scenario.name"
        result.testCaseResults.each {
            println """||- $it.testCase.description
                       ||  ${it.testCase.parameters.collect { k, v -> "$k: $v" }.join(', ')}
                       ||  expected: $it.testCase.expectedResult, got: $it.result""".stripMargin()
        }
    }

    @Override
    void finishSuite() {
        println '+-###-+'
    }
}
