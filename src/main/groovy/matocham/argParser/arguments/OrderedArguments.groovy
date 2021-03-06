package matocham.argParser.arguments

import matocham.argParser.exceptions.ArgumentsException
import matocham.argParser.args.Argument

class OrderedArguments extends Arguments {

    @Override
    def doParse(Collection<String> tokens) throws ArgumentsException {
        int tokenIndex = 0
        for (def i = 0; i < arguments.size(); i++) {
            Argument arg = arguments[i]
            def emptyName = arg.name.isEmpty()

            def tokenConsumed = false
            for (; tokenIndex < tokens.size(); tokenIndex++) {
                String token = tokens[tokenIndex].trim()
                if (token.matches(arg.getStartingRegex())) {
                    if (emptyName && (i + 1 < arguments.size())) {
                        // if name is empty additional check is needed to prevent endless matching
                        Argument nextArgument = arguments[i + 1]
                        if(token.matches(nextArgument.getStartingRegex())){
                            break // this is arg with empty name, and next arg matches this token
                            // so this matching should be stoped
                        }
                    }
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
            for (; tokenIndex < tokens.size(); tokenIndex++) {
                leftInputs.add(tokens[tokenIndex])
            }
            throw new ArgumentsException("Some inputs where not matched to any argument: $leftInputs")
        }
    }
}
