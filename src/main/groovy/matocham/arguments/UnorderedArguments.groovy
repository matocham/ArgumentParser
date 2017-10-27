package matocham.arguments

class UnorderedArguments extends Arguments {
    @Override
    def parse(String commandLine) throws ArgumentsException {
        Collection<String> tokens = commandLine.trim().split(VARIABLE_START_REGEX).findAll { !it.trim().isEmpty() }
        tokens.each { token ->
            token = token.trim()
            def tokenConsumed = false
            arguments.each { arg ->
                if (token.matches(arg.getStartingRegex())) {
                    tokenConsumed = true
                    arg.parse(token)
                }
            }
            if (!tokenConsumed) {
                throw new ArgumentsException("Expression $token does not match anything")
            }
        }
        arguments.each {
            if (it.value.isEmpty() && it.required) {
                throw new ArgumentsException("Argument $it.name is required")
            }
        }
    }
}
