package paramonov.valentine.taf.suite.parser

import spock.lang.Specification

import static paramonov.valentine.taf.MapLoader.loadMapFrom

class SuiteParserSpec extends Specification {
    def "Should parse freemind map to scenario"() {
        given:
            def rawSuite = loadMapFrom('test_map.mm')
        when:
            def suite = SuiteParser.parse(rawSuite, 'http://base/')
        then:
            suite.name == 'Test Suite'
            def scenario = suite.scenarios.first()
            scenario.name == 'Test Scenario'
            def testCase = scenario.testCases.first()
            testCase.request.method == 'GET'
            testCase.request.path == 'http://base/a/b/c'
            testCase.description == 'Test Case'
            testCase.parameters == [param1: '420', param2: '69']
            testCase.expectedResult == '1337'
    }
}
