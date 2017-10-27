package matocham.arguments

class OrderedArguments extends Arguments {

    @Override
    def parse(String commandLine) throws ArgumentsException {
        Collection<String> tokens = commandLine.trim().split(VARIABLE_START_REGEX).findAll { !it.trim().isEmpty() }
        int tokenIndex = 0
        arguments.each { arg ->
            def tokenConsumed = false
            for (; tokenIndex < tokens.size(); tokenIndex++) {
                String token = tokens[tokenIndex].trim()
                if (token.matches(arg.getStartingRegex())) {
                    tokenConsumed = true
                    arg.parse(token)
                }
                if (!tokenConsumed) { // if token was not consumed - break
                    break
                }
                tokenConsumed = false
            }
            if (arg.required && arg.value.isEmpty()) {
                throw new ArgumentsException("Argument $arg.name is required")
            }
        }
        if (tokenIndex < tokens.size()) {
            def leftInputs = []
            for(;tokenIndex<tokens.size();tokenIndex++){
                leftInputs.add(tokens[tokenIndex])
            }
            throw new ArgumentsException("Some inputs where not matched to any argument: $leftInputs")
        }
    }
}
