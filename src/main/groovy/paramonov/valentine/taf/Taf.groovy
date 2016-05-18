package paramonov.valentine.taf

import paramonov.valentine.taf.suite.Suite

class Taf {
    interface TestResultPrinter {
        void print(TestCaseResult result)
    }

    private final resultPrinter

    Taf(TestResultPrinter resultPrinter) {
        this.resultPrinter = resultPrinter
    }

    void run(Suite scenario) {
    }
}
