package matocham.argParser.args

import matocham.argParser.exceptions.ArgumentsException

abstract class Argument<T> {
    private static def STARTING_LONG_PATTERN = /^--/
    private static def STARTING_SHORT_PATTERN = /^-/
    public static final String ALL_CHARS_REGEX = ".*"
    String delimiter = ""
    String name
    private List<T> value = []
    Boolean multivalued = false
    Boolean required = false

    protected Argument() {
    }

    def parse(String argValue) throws ArgumentsException {
        argValue = stripDashes(argValue)
        checkDelimiter(argValue)
        String value = getValueFromToken(argValue)
        canAddMultivalued() // if all validations were completed successfully, check if this is first value or if second can be added
        parseValue(value)
    }

    private def canAddMultivalued(){
        if (!multivalued && !value.isEmpty()) {
            throw new ArgumentsException("Can't add second value to argument, that is not multivalued ($name)")
        }
    }

    private def stripDashes(String arg) {
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
            throw new ArgumentsException("Delimiter '$delimiter' was not found in $argValue")
        }
    }

    protected abstract def build()

    def getStartingRegex() {
        if (name.length() < 2) {
            return STARTING_SHORT_PATTERN + name + delimiter + ALL_CHARS_REGEX
        } else {
            return STARTING_LONG_PATTERN + name + delimiter + ALL_CHARS_REGEX
        }
    }

    List<String> getValue() {
        return value
    }

    def isWhiteSpaceDelimiter(){
        !delimiter.isEmpty() && delimiter.isAllWhitespace()
    }
}
