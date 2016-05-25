package paramonov.valentine.taf.cli

import org.apache.commons.cli.ParseException
import spock.lang.Specification
import spock.lang.Unroll

class ArgParserSpec extends Specification {
    static String existingFile = 'i-sure-do-exist.mm'

    def setupSpec() {
        new File(existingFile).text = ''
    }

    def cleanupSpec() {
        new File(existingFile).delete()
    }

    @Unroll
    def 'When args are #args, exception message should be "#message"'() {
        when:
            ArgParser.parse(args as String[])
        then:
            def exception = thrown(exceptionType)
            exception.message.contains(message)
        where:
            args                                      | exceptionType            || message
            []                                        | IllegalArgumentException || 'No arguments'
            ['-b', 'xxx', 'xxx.mm']                   | FileNotFoundException    || 'does not exist'
            ['-b', 'xxx', existingFile]               | IllegalArgumentException || 'not a valid URL'
            ['-b', '/d', existingFile]                | IllegalArgumentException || 'not a valid URL'
            ['-b', 'http://', existingFile]           | IllegalArgumentException || 'not a valid URL'
            ['-b', 'http://m', existingFile]          | IllegalArgumentException || 'not a valid URL'
            ['-b', 'http://m.', existingFile]         | IllegalArgumentException || 'not a valid URL'
            ['-b', 'http://m.a', existingFile]        | ParseException           || 'Failed to parse'
            ['-b', 'http://m.a.d', existingFile]      | ParseException           || 'Failed to parse'
            ['-b', 'http://m.a.d/a', existingFile]    | ParseException           || 'Failed to parse'
            ['-b', 'http://m.a.d/a/b', existingFile]  | ParseException           || 'Failed to parse'
            ['-b', 'http://m.a.d/a/b/', existingFile] | ParseException           || 'Failed to parse'
    }
}
