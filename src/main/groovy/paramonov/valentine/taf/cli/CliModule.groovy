package paramonov.valentine.taf.cli

import dagger.Module
import dagger.Provides
import groovy.transform.PackageScope
import paramonov.valentine.taf.SuiteRunner
import paramonov.valentine.taf.SuiteRunner.TestResultPrinter

@Module(injects = SuiteRunner)
@PackageScope
class CliModule {
    @Provides TestResultPrinter provideTestResultPrinter() {
        new CliTestResultPrinter()
    }
}
