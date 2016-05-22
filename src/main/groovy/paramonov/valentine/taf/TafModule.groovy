package paramonov.valentine.taf

import dagger.Module
import dagger.Provides

import javax.inject.Singleton
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Module(library = true)
class TafModule {
    @Provides @Singleton ExecutorService executor() {
        Executors.newFixedThreadPool(Runtime.runtime.availableProcessors())
    }
}
