package paramonov.valentine.taf.cli

import dagger.Module
import dagger.Provides
import groovy.transform.PackageScope
import paramonov.valentine.taf.SuiteRunner
import paramonov.valentine.taf.SuiteRunner.Printer
import paramonov.valentine.taf.TafModule

import javax.inject.Singleton

@PackageScope
@Module(includes = TafModule, injects = SuiteRunner)
class CliModule {
    @Provides @Singleton Printer testResultPrinter() {
        new CliPrinter()
    }
}
