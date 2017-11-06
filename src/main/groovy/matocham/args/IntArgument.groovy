package matocham.args

import matocham.exceptions.ArgumentsException

class IntArgument extends Argument<Integer> {
    String min = Integer.MIN_VALUE.toString(), max = Integer.MAX_VALUE.toString()
    private int minInt, maxInt

    @Override
    def parseValue(String argValue) {
        def intValue = Integer.parseInt(argValue)
        if (intValue >= minInt && intValue <= maxInt) {
            value.add(intValue)
        } else {
            throw new ArgumentsException("Value should be between $minInt and $maxInt inclusive ($argValue)")
        }
    }

    @Override
    protected build() {
        minInt = Integer.valueOf(min)
        maxInt = Integer.valueOf(max)
        if(minInt > maxInt){
            throw new ArgumentsException("min ($minInt) should be smaller than max ($maxInt)")
        }
    }
}
