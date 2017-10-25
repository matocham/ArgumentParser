package matocham.arguments.args

import matocham.arguments.ArgumentsException

abstract class Argument {
    String delimiter
    String name

    protected Argument() {
    }

    abstract def isValid()

    abstract def parse(String argument) throws ArgumentsException
}
