package matocham.argParser.args

import matocham.argParser.exceptions.ArgumentsException

class DoubleArgument extends Argument<Double> {
    String min = Double.MIN_VALUE.toString(), max = Double.MAX_VALUE.toString()

    private double minDouble, maxDouble

    @Override
    def parseValue(String argValue) {
        def doubleValue = Double.parseDouble(argValue)
        if (doubleValue >= minDouble && doubleValue <= maxDouble) {
            value.add(doubleValue)
        } else {
            throw new ArgumentsException("Value should be between $minDouble and $maxDouble inclusive ($argValue)")
        }
    }

    @Override
    protected build() {
        minDouble = Double.parseDouble(min)
        maxDouble = Double.parseDouble(max)
    }
}
