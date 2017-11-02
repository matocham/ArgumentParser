import matocham.argParser.arguments.Arguments
import matocham.argParser.parser.JsonParser
import matocham.argParser.parser.StringParser

class Test {
    public static void main(String[] args) {

        Arguments arguments2 = new StringParser("productId/_[string()]*! withShipping[bool]! quantity/=[int(min=10,max=20)]*! price/_[double(max=99.99)] >").parse()
        arguments2.parse("--productId \"'12345x'\" --withShipping --quantity=12 --price 55.67")
        //arguments2.parse(args)
        println arguments2.getArgument("productId")?.value
        println arguments2.getArgument("withShipping")?.value
        println arguments2.getArgument("quantity")?.value
        println arguments2.getArgument("price")?.value
    }
}
