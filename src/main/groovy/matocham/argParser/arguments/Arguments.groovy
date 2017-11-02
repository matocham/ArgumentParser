package matocham.argParser.arguments

import matocham.argParser.args.Argument
import matocham.argParser.exceptions.ArgumentsException

abstract class Arguments {
    List<Argument> arguments = []

    def addArgument(Argument argument) throws ArgumentsException {
        if (getArgument(argument.name)) {
            throw new ArgumentsException("Argument with name $argument.name already defined")
        }
        arguments.add(argument)
    }

    def parse(String commandLine) throws ArgumentsException {
        arguments.each { arguments.value.clear() }
        doParse(new SpaceTokenizer().split(commandLine))
    }

    abstract protected def doParse(Collection<String> tokens) throws ArgumentsException

    Argument getArgument(String name) {
        arguments.find { it.getName() == name }
    }
}
