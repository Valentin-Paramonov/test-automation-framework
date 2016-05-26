package paramonov.valentine.taf

import dagger.Module
import dagger.Provides
import groovyx.net.http.RESTClient

import javax.inject.Singleton
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Module(library = true, complete = false)
class TafModule {
    @Provides @Singleton ExecutorService executor() {
        Executors.newFixedThreadPool(Runtime.runtime.availableProcessors())
    }

    @Provides @Singleton RESTClient client(@BaseUrl String baseUrl) {
        new PoolingRestClient(baseUrl)
    }
}
