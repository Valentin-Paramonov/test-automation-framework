package paramonov.valentine.taf.suite.parser

import groovy.transform.PackageScope

@PackageScope
class TextMatcher {
    interface ToPattern {
        interface Expect {
            interface Match {
                List<String> matches()
            }
            Match expect(String expectedText)
        }
        Expect toPattern(String pattern)
    }

    static ToPattern match(String text) {
        { pattern ->
            { expectedText ->
                { ->
                    def textMatcher = text =~ pattern
                    if (!textMatcher.matches()) {
                        throw new SuiteParseException(/Invalid pattern in result node! Expected: "$expectedText", got: "$text"/)
                    }
                    (0..textMatcher.groupCount()).collect { int i -> textMatcher.group(i) }
                } as ToPattern.Expect.Match
            } as ToPattern.Expect
        }
    }
}
