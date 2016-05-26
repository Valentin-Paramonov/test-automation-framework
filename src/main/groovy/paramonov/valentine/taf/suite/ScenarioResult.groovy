package paramonov.valentine.taf.suite

class ScenarioResult {
    Scenario scenario
    List<TestCaseResult> testCaseResults

    int getTestCaseCount() {
        testCaseResults?.size() ?: 0
    }

    int getPassedTestCaseCount() {
        testCaseResults?.count { it.status == 'PASSED' } ?: 0
    }
}
