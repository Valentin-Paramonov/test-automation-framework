package paramonov.valentine.taf

import freemind.Map
import groovy.transform.PackageScope

import javax.xml.bind.JAXBContext

@PackageScope
class MapLoader {
    static Map loadMapFrom(String resourcePath) {
        def map = MapLoader.classLoader.getResource(resourcePath)
        JAXBContext.newInstance('freemind').createUnmarshaller().unmarshal(map) as Map
    }
}
