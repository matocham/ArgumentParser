import matocham.argParser.arguments.Arguments
import matocham.argParser.parser.StringParser

class Test {
    public static void main(String[] args) {
        Arguments arguments = new StringParser("[string(maxLength=4)]! a/[string(maxLength=1000)]*  ").parse()
        arguments.parse("-a123 -'ala ma kota' -a123 -aco_tam")
        println arguments.getArgument("a").value
    }
}
