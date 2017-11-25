import matocham.args.DoubleArgument
import matocham.exceptions.ArgumentsException
import spock.lang.Specification

class DoubleArgumentSpec extends Specification {
    DoubleArgument argument

    def setup() {
        argument = new DoubleArgument()
        argument.build()
        argument.name = "test"
        argument.delimiter = " "
    }

    def "should build constraints"() {
        setup:
        DoubleArgument argument = new DoubleArgument([min: "20.2", max: "120"])

        when:
        argument.build()

        then:
        argument.minDouble == 20.2
        argument.maxDouble == 120
    }

    def "should throw exception on improper constraint"() {
        setup:
        DoubleArgument argument = new DoubleArgument([min: "20", max: "1"])

        when:
        argument.build()

        then:
        def ex = thrown(ArgumentsException)
        ex.getMessage().contains("20")
        ex.getMessage().contains("1")
    }

    def "should check constraints during parse"() {
        setup:
        DoubleArgument argument = new DoubleArgument([min: "-20", max: "20", name: "test", delimiter: " "])
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
        argument.parse("--test -10.99")
        argument.parse("--test 10.99")

        then:
        argument.value.size() == 2
        argument.value == [-10.99, 10.99]
    }
}
