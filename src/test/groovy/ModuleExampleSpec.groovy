import matocham.arguments.Arguments
import matocham.arguments.JsonParser
import matocham.arguments.Parser
import matocham.arguments.StringParser
import spock.lang.Specification

// those are module tests!
class ModuleExampleSpec extends Specification {
    def "should parse command line provided as string from arguments build from string"() {
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

    def "should parse command line provided as string array from arguments build from string"() {
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

    def "should parse command line provided as string array from arguments build from json file"() {
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
