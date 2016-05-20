package paramonov.valentine.taf

import paramonov.valentine.taf.suite.Suite

import javax.inject.Inject

class SuiteRunner {
    interface TestResultPrinter {
    }

    private final resultPrinter

    @Inject
    SuiteRunner(TestResultPrinter resultPrinter) {
        this.resultPrinter = resultPrinter
    }

    void run(Suite scenario) {
    }
}
