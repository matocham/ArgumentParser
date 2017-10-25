package matocham.arguments

import matocham.arguments.args.Argument

abstract  class Arguments {
    List<Argument> arguments = []

    def addArgument(argument){
        arguments.add(argument)
    }

    abstract def validate(String commandLine)
}
