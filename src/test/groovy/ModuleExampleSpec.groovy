import matocham.arguments.Arguments
import matocham.arguments.JsonParser
import matocham.arguments.Parser
import matocham.arguments.StringParser
import spock.lang.Specification

class ModuleExampleSpec extends Specification {
    def "perform basic module test"() {
        setup:
        StringParser parser = new StringParser("productId/_[string()]*! withShipping[bool]! quantity/=[int(min=10,max=20)]*! price/_[double(max=99.99)] >")
        when:
        Arguments arguments = parser.parse()
        arguments.parse("--productId \"'12345x'\" --withShipping --quantity=12 --price 55.67")

        then:
        arguments.getArgument("productId")?.value == ["'12345x'"]
        arguments.getArgument("withShipping")?.value == [true]
        arguments.getArgument("quantity")?.value == [12]
        arguments.getArgument("price")?.value == [55.67]
    }

    def "perform basic module test with arguments array"() {
        setup:
        StringParser parser = new StringParser("productId/_[string()]*! withShipping[bool]! quantity/=[int(min=10,max=20)]*! price/_[double(max=99.99)] >")
        String[] cmdArguments = ["--productId", "'12345x'", "--withShipping", "--quantity=12", "--price", "55.67"]
        when:
        Arguments arguments = parser.parse()
        arguments.parse(cmdArguments)

        then:
        arguments.getArgument("productId")?.value == ["'12345x'"]
        arguments.getArgument("withShipping")?.value == [true]
        arguments.getArgument("quantity")?.value == [12]
        arguments.getArgument("price")?.value == [55.67]
    }

    def "perform basic module test with arguments array and Json file"() {
        setup:
        Parser parser = new JsonParser("arguments.json")
        String[] cmdArguments = ["--productId", "'12345x'", "--withShipping", "--quantity=12", "--price", "55.67"]
        when:
        Arguments arguments = parser.parse()
        arguments.parse(cmdArguments)

        then:
        arguments.getArgument("productId")?.value == ["'12345x'"]
        arguments.getArgument("withShipping")?.value == [true]
        arguments.getArgument("quantity")?.value == [12]
        arguments.getArgument("price")?.value == [55.67]
    }
}
