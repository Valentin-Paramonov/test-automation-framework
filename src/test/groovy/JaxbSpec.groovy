import freemind.Map
import spock.lang.Specification

import javax.xml.bind.JAXBContext

class JaxbSpec extends Specification {
    def "Should unmarshall test map"() {
        given:
            def testMap = this.class.classLoader.getResource('test_map.mm')
            def unmarshaller = JAXBContext.newInstance('freemind').createUnmarshaller()
        when:
            def map = unmarshaller.unmarshal(testMap) as Map
        then:
            map.version == '420.69'
    }
}
