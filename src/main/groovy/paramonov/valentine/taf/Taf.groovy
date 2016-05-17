package paramonov.valentine.taf

import freemind.Map

class Taf {
    interface TestResultPrinter {
        void print(TestCaseResult result)
    }

    private final resultPrinter

    Taf(TestResultPrinter resultPrinter) {
        this.resultPrinter = resultPrinter
    }

    void run(Scenario scenario) {
    }
}
