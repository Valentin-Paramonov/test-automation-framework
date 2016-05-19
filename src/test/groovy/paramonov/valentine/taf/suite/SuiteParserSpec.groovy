package paramonov.valentine.taf.suite

import paramonov.valentine.taf.suite.parser.SuiteParser
import spock.lang.Specification

import static paramonov.valentine.taf.MapLoader.loadMapFrom

class SuiteParserSpec extends Specification {
    def "Should parse freemind map to scenario"() {
        given:
            def rawSuite = loadMapFrom('test_map.mm')
        when:
            def suite = SuiteParser.parse(rawSuite)
        then:
            suite.name == 'Test Suite'
            def scenario = suite.scenarios.first()
            scenario.name == 'Test Scenario'
            scenario.request.method == 'GET'
            scenario.request.path == '/a/b/c'
            def testCase = scenario.testCases.first()
            testCase.description == 'Test Case'
            testCase.parameters == [param1: '420', param2: '69']
            testCase.expectedResult == '1337'
    }
}
