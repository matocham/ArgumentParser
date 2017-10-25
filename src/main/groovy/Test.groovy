import matocham.arguments.Arguments
import matocham.arguments.StringParser

class Test {
    public static void main(String[] args) {
        Arguments arguments = new StringParser("a/_[string(maxLength=1000)] b/=[string(maxLength = 123,pattern=^abcd\$)]").parse()
        arguments.getClass()
    }
}
