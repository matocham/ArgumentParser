package matocham.argParser.arguments

import matocham.argParser.exceptions.ArgumentsException
import matocham.argParser.args.Argument

class UnorderedArguments extends Arguments {
    @Override
    def parse(String commandLine) throws ArgumentsException {
        arguments.sort({a,b -> (b.name <=> a.name) })
        Collection<String> tokens = tokenizeArguments(commandLine).findAll { !it.trim().isEmpty() }
        tokens.each { token ->
            token = token.trim()
            def tokenConsumed = false
            for ( Argument argument : arguments){
                if (token.matches(argument.getStartingRegex())) {
                    tokenConsumed = true
                    argument.parse(token)
                    break
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
