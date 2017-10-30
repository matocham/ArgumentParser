import matocham.argParser.arguments.Arguments
import matocham.argParser.parser.StringParser

class Test {
    public static void main(String[] args) {
        Arguments arguments = new StringParser("string/_[string(maxLength=100)]! int/=[int(max=50)]* b[bool()]! double/=[double(min=0,max=100)]!").parse()
        arguments.parse("--string 'ala ma kota' --int=20 -b --double=5.55 --int=12.8")
        println arguments.getArgument("string")?.value
        println arguments.getArgument("int")?.value
        println arguments.getArgument("double")?.value
        println arguments.getArgument("b")?.value
    }
}
