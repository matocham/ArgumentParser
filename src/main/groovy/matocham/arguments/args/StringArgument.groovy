package matocham.arguments.args

import matocham.arguments.ArgumentsException

class StringArgument extends Argument{
    String maxLength
    String pattern
    String value

    StringArgument(String maxLength = Integer.MAX_VALUE, String pattern = ".*"){
        this.maxLength = maxLength
        this.pattern = pattern
    }
    @Override
    def isValid() throws ArgumentsException {
        throw new ArgumentsException("Values are not valid")
    }

    @Override
    def parse(String stringArgument) {
        return null
    }
}
