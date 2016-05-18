package paramonov.valentine.taf

import freemind.Map
import freemind.Node
import org.apache.commons.cli.ParseException

class SuiteParser {
    static Suite parse(Map suite) {
        new Suite(
            name: suite?.node?.TEXT ?: { error('Expected suite name') }(),
            scenarios: childNodes(suite?.node)?.collect { parseScenario(it) })
    }

    private static childNodes(Node node) {
        node?.arrowlinkOrCloudOrEdge?.findAll { it.class == Node }?.collect { it as Node } ?: []
    }

    private static Scenario parseScenario(Node scenario) {
        def childNodes = childNodes(scenario)
        new Scenario(name: scenario.TEXT, request: parseRequest(childNodes.first() as Node), testCases: childNodes.tail().collect { parseTestCase(it as Node) })
    }

    private static Request parseRequest(Node request) {
        if (request.TEXT != 'Request') {
            error("Expected a request node, but got: $request.TEXT")
        }
        def childNodes = childNodes(request)
        if (childNodes.size() != 2) {
            error('Expected the Request node to have two child nodes')
        }
        new Request(method: parseMethod(childNodes.first()), path: parsePath(childNodes.last()))
    }

    private static error(String message) {
        throw new ParseException(message)
    }

    private static String parseMethod(Node method) {
        def textMatcher = method.TEXT =~ /^Method:\s*(GET|POST)$/
        if (!textMatcher.matches()) {
            error('Invalid pattern in method node! Expected "Method: <GET|POST>"')
        }
        textMatcher.group(1)
    }

    private static String parsePath(Node path) {
        def textMatcher = path.TEXT =~ /^Path:\s*((?:\/\w+)*)$/
        if (!textMatcher.matches()) {
            error(/Invalid pattern in path node! Expected "Path: <path>", got: $path.TEXT/)
        }
        textMatcher.group(1)
    }

    private static TestCase parseTestCase(Node testCase) {
        def childNodes = childNodes(testCase)
        new TestCase(description: testCase.TEXT, parameters: childNodes.init().collectEntries { parseParameter(it) }, expectedResult: parseResult(childNodes.last()))
    }

    private static java.util.Map<String, String> parseParameter(Node parameter) {
        def textMatcher = parameter.TEXT =~ /^(\w+):\s*(.+)$/
        if (!textMatcher.matches()) {
            error(/Invalid pattern in result node! Expected "<parameterName>: <value>", got: $parameter.TEXT/)
        }
        [(textMatcher.group(1)): textMatcher.group(2)]
    }

    private static String parseResult(Node result) {
        def textMatcher = result.TEXT =~ /^result:\s*(.+)$/
        if (!textMatcher.matches()) {
            error(/Invalid pattern in result node! Expected "result: <expectedValue>", got: $result.TEXT/)
        }
        textMatcher.group(1)
    }
}
