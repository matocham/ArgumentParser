package matocham.arguments.args

import matocham.arguments.ArgumentsException

import java.util.regex.Pattern

class StringArgument extends Argument {
    String maxLength = "1000"
    String pattern = /.*/

    Integer valueMaxLength
    Pattern valuePattern

    @Override
    def parseValue(String stringArgument) throws ArgumentsException {
        if (stringArgument.length() > valueMaxLength) {
            throw new ArgumentsException("Argument $name is too long")
        }
        if (!stringArgument.matches(valuePattern)) {
            throw new ArgumentsException("Argument $name does not match pattern ${pattern.toString()}")
        }
        if (!multivalued && !value.isEmpty()) {
            throw new ArgumentsException("Can't add second value to argument, that is not multivalued")
        }
        value.add(stringArgument)
    }

    @Override
    protected build() {
        valueMaxLength = Integer.valueOf(maxLength)
        valuePattern = Pattern.compile(pattern)
    }
}
