package matocham.argParser.args

import matocham.argParser.exceptions.ArgumentsException

abstract class Argument {
    private static def ESCAPED_PATTERN = ~/'.*'/
    private static def STARTING_LONG_PATTERN = /^--/
    private static def STARTING_SHORT_PATTERN = /^-/
    public static final String ALL_CHARS_REGEX = ".*"
    String delimiter = ""
    String name
    List<String> value = []
    Boolean multivalued = false
    Boolean required = false

    protected Argument() {
    }

    def parse(String argValue) throws ArgumentsException {
        argValue = stripDashes(argValue)
        checkDelimiter(argValue)
        String value = getValueFromToken(argValue)
        if (!validateValue(value)) {
            throw new ArgumentsException("Arguments with white characters should be escaped using ''")
        }
        value = stripQuotes(value)
        parseValue(value)
    }

    def stripDashes(String arg) {
        def index = 0
        while (index < arg.length() && arg.charAt(index) == '-') {
            index++
        }
        arg.substring(index)
    }

    abstract def parseValue(String argValue)

    private def getValueFromToken(String stringArgument) {
        if (delimiter.isEmpty()) {
            return stringArgument.substring(name.length())
        } else {
            return stringArgument.substring(stringArgument.indexOf(delimiter) + 1)
        }
    }

    private def checkDelimiter(String argValue) throws ArgumentsException {
        if (!delimiter.isEmpty() && argValue.charAt(name.length()) != delimiter) {
            throw new ArgumentsException("Delimiter $delimiter was not found in $argValue")
        }
    }

    private def validateValue(String value) {
        if (value == null || !value) {
            return true
        }
        if (value.contains(" ") || value.contains("\t") || value.contains("\n")) {
            return value.matches(ESCAPED_PATTERN)
        }
        return true
    }

    private def stripQuotes(String value) {
        if (value.matches(ESCAPED_PATTERN)) {
            value = value.substring(1, value.length() - 1)
        }
        return value
    }

    protected abstract def build()

    def getStartingRegex() {
        if (name.length() < 2) {
            return STARTING_SHORT_PATTERN + name + delimiter + ALL_CHARS_REGEX
        } else {
            return STARTING_LONG_PATTERN + name + delimiter + ALL_CHARS_REGEX
        }
    }
}
