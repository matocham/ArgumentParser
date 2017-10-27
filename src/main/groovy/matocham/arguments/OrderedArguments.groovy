package matocham.arguments

import matocham.arguments.args.Argument

class OrderedArguments extends Arguments {

    @Override
    def parse(String commandLine) throws ArgumentsException {
        String[] tokens = commandLine.trim().split(VARIABLE_START_REGEX)
        tokens.eachWithIndex { token, index ->
            Argument argument = arguments.get(index)
            def argumentParts = token.split(argument.delimiter)
            def name = argumentParts[0]
            if (name != argument.name) {
                throw new ArgumentsException("$index argument name is $name, but should be $argument.name")
            }
            def parameterValue = token.substring(token.indexOf(argument.delimiter))
            argument.parse(parameterValue)
        }
        int tokenIndex = 0
        arguments.each { arg ->
            def tokenConsumed = false
            for (; tokenIndex < tokens.length; tokenIndex++) {
                String token = tokens[tokenIndex]
                if (token.startsWith(arg.name)) {
                    tokenConsumed = true
                    arg.parse(token)
                }
                if (!tokenConsumed) { // if token was not consumed - break
                    break
                }
            }
            if (arg.required && arg.value.isEmpty()) {
                throw new ArgumentsException("Argument $arg.name is required")
            }
        }
        if (tokenIndex < tokens.length) {
            throw new ArgumentsException("Some input where not matched to any argument: ${tokens[tokenIndex]}")
        }
    }
}
