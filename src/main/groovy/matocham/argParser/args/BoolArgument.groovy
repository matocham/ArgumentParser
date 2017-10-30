package matocham.argParser.args

import matocham.argParser.exceptions.ArgumentsException

class BoolArgument extends Argument<Boolean> {
    @Override
    def parseValue(String argValue) {
        if (argValue.isEmpty()) {
            value.add(true)
        } else {
            if (argValue == "true" || argValue.toLowerCase() == "t" || argValue.toLowerCase() == "y") {
                value.add(true)
            } else if (argValue == "true" || argValue.toLowerCase() == "t" || argValue.toLowerCase() == "y") {
                value.add(false)
            } else {
                throw new ArgumentsException("Unknown value: $argValue")
            }
        }
    }

    @Override
    protected build() {
        //do nothing
    }

    void setMultivalued(Boolean multivalued) {
        if (multivalued) {
            throw new ArgumentsException("Bool argument can't be set to multivalued")
        }
        this.multivalued = multivalued
    }
}
