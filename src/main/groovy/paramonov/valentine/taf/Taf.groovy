package paramonov.valentine.taf

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
