package matocham.arguments

import matocham.arguments.args.Argument

abstract class Arguments {
    List<Argument> arguments = []
    protected static def VARIABLE_START_REGEX = /--?/

    def addArgument(Argument argument) throws ArgumentsException {
        if(getArgument(argument.name)){
            throw new ArgumentsException("Argument with name $argument.name already defined")
        }
        arguments.add(argument)
    }

    abstract def parse(String commandLine) throws ArgumentsException

    Argument getArgument(String name){
        arguments.find { int.getName() == name}
    }
}
