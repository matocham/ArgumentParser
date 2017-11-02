package matocham.argParser.arguments

import matocham.argParser.args.Argument
import matocham.argParser.exceptions.ArgumentsException

class UnorderedArguments extends Arguments {
    @Override
    def doParse(Collection<String> tokens) throws ArgumentsException {
        arguments.sort({ a, b -> (b.name <=> a.name) })
        for (int i = 0; i < tokens.size(); i++) {
            def tokenConsumed = false
            for (Argument argument : arguments) {
                def token = tokens[i]
                def whiteSpaceMerged = false

                if (argument.isWhiteSpaceDelimiter() && i + 1 < tokens.size()) {
                    token += " " + tokens[i + 1]
                    whiteSpaceMerged = true
                }
                if (token.matches(argument.getStartingRegex())) {
                    tokenConsumed = true
                    if (whiteSpaceMerged) {
                        i++
                    }
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
