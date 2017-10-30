package matocham.argParser.args

import matocham.argParser.exceptions.ArgumentsException

import java.util.regex.Pattern

class StringArgument extends Argument<String> {
    String maxLength = "1000"
    String pattern = /.*/

    private Integer valueMaxLength
    private Pattern valuePattern

    @Override
    def parseValue(String stringArgument) throws ArgumentsException {
        if (stringArgument.length() > valueMaxLength) {
            throw new ArgumentsException("Argument '$name'='$stringArgument' is too long")
        }
        if (!stringArgument.matches(valuePattern)) {
            throw new ArgumentsException("Argument $name does not match pattern ${pattern.toString()}")
        }
        value.add(stringArgument)
    }

    @Override
    protected build() {
        valueMaxLength = Integer.valueOf(maxLength)
        valuePattern = Pattern.compile(pattern)
    }
}
