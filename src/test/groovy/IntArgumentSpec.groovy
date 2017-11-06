import matocham.args.IntArgument
import matocham.exceptions.ArgumentsException
import spock.lang.Specification

class IntArgumentSpec extends Specification {
    IntArgument argument

    def setup() {
        argument = new IntArgument()
        argument.build()
        argument.name = "test"
        argument.delimiter = " "
    }

    def "should build constraints"() {
        setup:
        IntArgument argument = new IntArgument([min: "2", max: "12"])

        when:
        argument.build()

        then:
        argument.minInt == 2
        argument.maxInt == 12
    }

    def "should throw exception on improper constraint"() {
        setup:
        IntArgument argument = new IntArgument([min: "20", max: "1"])

        when:
        argument.build()

        then:
        def ex = thrown(ArgumentsException)
        ex.getMessage().contains("20")
        ex.getMessage().contains("1")
    }

    def "should check constraints during parse"() {
        setup:
        IntArgument argument = new IntArgument([min: "-20", max: "20", name: "test", delimiter: " "])
        argument.build()

        when:
        argument.parse("--test -100")
        then:
        def ex = thrown(ArgumentsException)
        ex.getMessage().contains("-100")

        when:
        argument.parse("--test 100")
        then:
        def ex2 = thrown(ArgumentsException)
        ex2.getMessage().contains("100")
    }

    def "should handle multiple values"() {
        setup:
        argument.multivalued = true

        when:
        argument.parse("--test -10")
        argument.parse("--test 10")

        then:
        argument.value.size() == 2
        argument.value == [-10, 10]
    }
}
