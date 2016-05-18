package paramonov.valentine.taf

import freemind.Node
import spock.lang.Specification

import static MapLoader.loadMapFrom

class MapFormatSpec extends Specification {
    def "Should unmarshall test map"() {
        given:
            def testMap = loadMapFrom('test_map.mm')
        expect:
            testMap.version == '420.69'
            def suiteNode = testMap.node
            suiteNode.TEXT == 'Test Suite'
            def scenarioNode = suiteNode.arrowlinkOrCloudOrEdge.first() as Node
            scenarioNode.TEXT == 'Test Scenario'
            def requestNode = scenarioNode.arrowlinkOrCloudOrEdge.first() as Node
            requestNode.TEXT == 'Request'
            requestNode.arrowlinkOrCloudOrEdge.collect { (it as Node).TEXT } == ['Method: GET', 'Path: /a/b/c']
            def testCaseNode = scenarioNode.arrowlinkOrCloudOrEdge.last() as Node
            testCaseNode.TEXT == 'Test Case'
            testCaseNode.arrowlinkOrCloudOrEdge.collect { (it as Node).TEXT } == ['param1: 420', 'param2: 69', 'result: 1337']
    }
}
