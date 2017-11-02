package matocham.argParser.arguments

import matocham.argParser.exceptions.ArgumentsException

class SpaceTokenizer implements Tokenizer{
    private static def ESCAPED_PATTERN = ~/(".*")|('.*')/

    List<String> split(String commandLineArguments) {
        def tokens = []
        List indexes = getSpaceIndexes(commandLineArguments)

        for (def i = 0; i < indexes.size(); i++) {
            def startingCharIndex = indexes[i]
            while (i + 1 < indexes.size() && indexes[i + 1] == startingCharIndex + 1) {
                i++ // if next character is also ' ' jump to next position
            }
            def endingCharIndex = startingCharIndex
            def j = indexes[i] + 1
            for (; j < commandLineArguments.length(); j++) { // start from character after '-'
                if (commandLineArguments.charAt(j) == "\"") { // characters inside '' are escaped
                    j++ // move j to next character, so it won't point at "'"
                    def closingCharIndex = commandLineArguments.indexOf("\"", j)
                    if (closingCharIndex == -1) {
                        throw new ArgumentsException(" '\"'' closing tag not found")
                    }
                    j = closingCharIndex //go to the end of escaped block
                }
                if (commandLineArguments.charAt(j) == ' ') {
                    endingCharIndex = j
                    break
                }
                if (j + 1 == commandLineArguments.length()) {
                    endingCharIndex = j + 1
                }
            }
            while (i + 1 < indexes.size() && indexes[i + 1] < endingCharIndex) {
                i++ // skip all ' ' characters that were included into current token
            }
            if (j == commandLineArguments.length()) { // if end of string was reached endingCharIndex have to be set
                endingCharIndex = j
            }
            def token = commandLineArguments.substring(startingCharIndex, endingCharIndex).trim()
            token = stripQuotes(token)
            tokens.add(token)
        }
        return tokens.findAll { !it.trim().isEmpty() }
    }

    private List getSpaceIndexes(String commandLineArguments) {
        def indexes = [0]
        for (def i = 0; i < commandLineArguments.length(); i++) {
            if (commandLineArguments.charAt(i) == ' ') {
                indexes.add(i)
            }
        }
        indexes
    }

    private def stripQuotes(String value) {
        if (value.matches(ESCAPED_PATTERN)) {
            value = value.substring(1, value.length() - 1)
        }
        return value
    }
}
