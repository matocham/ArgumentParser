import matocham.arguments.Arguments
import matocham.arguments.StringParser

class Test {
    public static void main(String[] args) {
        Arguments arguments = new StringParser("[string()]! a/[string(maxLength=1000)]*  ").parse()
        arguments.parse("-a123 -'ala ma kota' -a123 -aco_tam")
        println arguments.getArgument("a").value
    }
}
