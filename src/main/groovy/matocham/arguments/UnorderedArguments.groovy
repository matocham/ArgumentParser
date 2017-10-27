package matocham.arguments

import matocham.arguments.args.Argument

class UnorderedArguments extends Arguments{
    @Override
    def parse(String commandLine) throws ArgumentsException {
        String[] tokens = commandLine.trim().split(VARIABLE_START_REGEX)
        tokens.each { token ->
            def tokenConsumed = false
            arguments.each { arg ->
                if(token.startsWith(arg.name)){
                    tokenConsumed = true
                    argument.parse(token)
                }
            }
            if(!tokenConsumed){
                throw new ArgumentsException("Expression $token does not match anything")
            }
        }
        arguments.each {
            if(it.value.isEmpty() && it.required){
                throw new ArgumentsException("Argument $it.name is required")
            }
        }
    }
}
