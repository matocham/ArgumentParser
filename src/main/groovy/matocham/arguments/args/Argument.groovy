package matocham.arguments.args

import matocham.arguments.ArgumentsException

abstract class Argument {
    private static def ESCAPED_PATTERN = ~/'.*'/

    String delimiter = ""
    String name
    List<String> value = []
    Boolean multivalued = false
    Boolean required = false

    protected Argument() {
    }

    def parse(String argValue) throws ArgumentsException {
        checkDelimiter(argValue)
        String value = getValueFromToken(argValue)
        if (!validateValue(value)) {
            throw new ArgumentsException("Arguments with white characters should be escaped using ''")
        }
        value = stripQuotes(value)
        parseValue(value)
    }

    abstract def parseValue(String argValue) throws ArgumentsException

    private def getValueFromToken(String stringArgument) {
        if (delimiter.isEmpty()) {
            return stringArgument.substring(name.length())
        } else {
            return stringArgument.substring(stringArgument.indexOf(delimiter) + 1)
        }
    }

    private def checkDelimiter(String argValue) throws ArgumentsException{
        if (!delimiter.isEmpty() && argValue.charAt(arg.name.length()) != delimiter) {
            throw new ArgumentsException("Delimiter $delimiter was not found in $argValue")
        }
    }

    private def validateValue(String value) throws ArgumentsException {
        if (value == null || !value) {
            return true
        }
        if (value.contains(" ") || value.contains("\t") || value.contains("\n")) {
            return value.matches(ESCAPED_PATTERN)
        }
        return true
    }

    private def stripQuotes(String value){
        if (value.matches(ESCAPED_PATTERN)) {
            value = value.substring(1, value.length() - 1)
        }
        return value
    }

    protected abstract def build()
}
