package paramonov.valentine.taf.suite

class SuiteResult {
    Suite suite
    List<ScenarioResult> scenarioResults

    int getScenarioCount() {
        scenarioResults?.size() ?: 0
    }

    int getTestCaseCount() {
        scenarioResults?.inject(0, { total, scenario -> total + scenario.testCaseCount }) ?: 0
    }

    int getPassedTestCaseCount() {
        scenarioResults?.inject(0, { total, scenario -> total + scenario.passedTestCaseCount }) ?: 0
    }
}
