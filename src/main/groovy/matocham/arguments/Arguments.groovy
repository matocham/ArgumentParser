package matocham.arguments

import matocham.arguments.args.Argument

abstract class Arguments {
    List<Argument> arguments = []

    def addArgument(Argument argument) throws ArgumentsException {
        if (getArgument(argument.name)) {
            throw new ArgumentsException("Argument with name $argument.name already defined")
        }
        arguments.add(argument)
    }

    abstract def parse(String commandLine) throws ArgumentsException

    Argument getArgument(String name) {
        arguments.find { it.getName() == name }
    }

    protected def tokenizeArguments(String commandLineArguments) {
        def tokens = []
        List indexes = getDashIndexes(commandLineArguments)

        for (def i = 0; i < indexes.size(); i++) {
            def startingCharIndex = indexes[i]
            while (i + 1 < indexes.size() && indexes[i + 1] == startingCharIndex + 1) {
                i++ // if next character is also '-' jump to next position
            }
            def endingCharIndex = startingCharIndex
            def j = indexes[i] + 1
            for (; j < commandLineArguments.length(); j++) { // start from character after '-'
                if (commandLineArguments.charAt(j) == "'") { // characters inside '' are escaped
                    j++ // move j to next character, so it won't point at "'"
                    for (;j < commandLineArguments.length() && commandLineArguments.charAt(j) != "'"; j++); //got to the end of escaped block
                }
                if (commandLineArguments.charAt(j) == '-' && commandLineArguments.charAt(j - 1) == ' ') {
                    endingCharIndex = j
                    break
                }
                if(j+1 == commandLineArguments.length()){
                    endingCharIndex = j+1
                }
            }
            while (i + 1 < indexes.size() && indexes[i + 1] < endingCharIndex) {
                i++ // skip all '-' characters that were included into current token
            }
            if(j == commandLineArguments.length()){ // if end of string was reached endingCharIndex have to be set
                endingCharIndex = j
            }
            tokens.add(commandLineArguments.substring(startingCharIndex, endingCharIndex).trim())
        }
        return tokens
    }

    private List getDashIndexes(String commandLineArguments) {
        def indexes = []
        for (def i = 0; i < commandLineArguments.length(); i++) {
            if (commandLineArguments.charAt(i) == '-') {
                indexes.add(i)
            }
        }
        indexes
    }
}
