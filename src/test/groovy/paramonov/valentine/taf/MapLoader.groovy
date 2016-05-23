package paramonov.valentine.taf

import freemind.Map

import javax.xml.bind.JAXBContext

class MapLoader {
    static Map loadMapFrom(String resourcePath) {
        final map = MapLoader.classLoader.getResource(resourcePath)
        JAXBContext.newInstance('freemind').createUnmarshaller().unmarshal(map) as Map
    }
}
