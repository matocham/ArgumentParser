import matocham.arguments.Arguments
import matocham.arguments.StringParser

class Test {
    public static void main(String[] args) {
        Arguments arguments = new StringParser("a/[string(maxLength=1000)]* b/=[string()]! ").parse()
        arguments.parse("-axxx -b='ala ma kota' -a123")
        println arguments.getArgument("a").value
    }
}
