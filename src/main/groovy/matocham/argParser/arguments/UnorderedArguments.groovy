package matocham.argParser.arguments

import matocham.argParser.args.Argument
import matocham.argParser.exceptions.ArgumentsException

class UnorderedArguments extends Arguments {
    @Override
    def doParse(Collection<String> tokens) throws ArgumentsException {
        arguments.sort({ a, b -> (b.name <=> a.name) })
        for (String token : tokens) {
            token = token.trim()
            def tokenConsumed = false
            for (Argument argument : arguments) {
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
