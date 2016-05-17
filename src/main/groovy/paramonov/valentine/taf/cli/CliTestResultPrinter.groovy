package paramonov.valentine.taf.cli

import groovy.transform.PackageScope
import paramonov.valentine.taf.Taf
import paramonov.valentine.taf.TestCaseResult

@PackageScope
class CliTestResultPrinter implements Taf.TestResultPrinter {
    @Override
    void print(TestCaseResult result) {
        println result
    }
}
