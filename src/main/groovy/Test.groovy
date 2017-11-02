import matocham.argParser.arguments.Arguments
import matocham.argParser.parser.JsonParser
import matocham.argParser.parser.StringParser

class Test {
    public static void main(String[] args) {
        Arguments arguments = new JsonParser("arguments.json").parse()
        arguments.parse("--productId a12345x --withShipping --quantity=12 --price 55.67 --quantity=10")
        println arguments.getArgument("productId")?.value
        println arguments.getArgument("withShipping")?.value
        println arguments.getArgument("quantity")?.value
        println arguments.getArgument("price")?.value

        Arguments arguments2 = new StringParser("productId/_[string()]! withShipping[bool]! quantity/=[int(min=10,max=20)]*! price/_[double(max=99.99)] >").parse()
        arguments2.parse("--productId a12345x --withShipping --quantity=12 --price 55.67")
        println arguments2.getArgument("productId")?.value
        println arguments2.getArgument("withShipping")?.value
        println arguments2.getArgument("quantity")?.value
        println arguments2.getArgument("price")?.value
    }
}
