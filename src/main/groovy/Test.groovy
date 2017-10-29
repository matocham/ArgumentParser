import matocham.argParser.arguments.Arguments
import matocham.argParser.parser.StringParser

class Test {
    public static void main(String[] args) {
        Arguments arguments = new StringParser("[string(maxLength=4)]! -a/[string(maxLength=1000)]*  ").parse()
        arguments.parse("---a123 -5 ---ab123 ---a'co_tam'")
        println arguments.getArgument("")?.value
    }
}
