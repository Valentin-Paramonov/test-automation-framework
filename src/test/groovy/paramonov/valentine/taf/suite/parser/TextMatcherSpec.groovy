package paramonov.valentine.taf.suite.parser

import spock.lang.Specification

class TextMatcherSpec extends Specification {
    def "Should return matched text"() {
        when:
            def match = TextMatcher.match('aba').toPattern('^aba$').expect('aba').matches()[0]
        then:
            match == 'aba'
    }

    def "Should return matched groups"() {
        when:
            def matches = TextMatcher.match('aba').toPattern('^(a(b))a$').expect('aba').matches()
        then:
            matches[1] == 'ab'
            matches[2] == 'b'
    }

    def "Should throw ParseException when text doesn't match"() {
        when:
            TextMatcher.match('xxx').toPattern('^aba$').expect('aba').matches()
        then:
            def exception = thrown SuiteParseException
            exception.message.endsWith('Expected: "aba", got: "xxx"')
    }
}
