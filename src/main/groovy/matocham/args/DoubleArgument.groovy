package matocham.args

import matocham.exceptions.ArgumentsException

class DoubleArgument extends Argument<Double> {
    String min = "-1000000", max = "1000000"

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

        if(minDouble > maxDouble){
            throw new ArgumentsException("min ($minDouble) should be smaller than max ($maxDouble)")
        }
    }
}
