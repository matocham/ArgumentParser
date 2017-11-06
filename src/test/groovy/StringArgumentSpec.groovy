import matocham.args.StringArgument
import matocham.exceptions.ArgumentsException
import spock.lang.Specification

class StringArgumentSpec extends Specification {
    StringArgument argument

    def setup() {
        argument = new StringArgument()
        argument.build()
        argument.name = "test"
        argument.delimiter = " "
    }

    def "should build constraints"() {
        setup:
        StringArgument argument = new StringArgument([maxLength: "20", pattern: "^a.*"])


        when:
        argument.build()

        then:
        argument.valueMaxLength == 20
        argument.valuePattern.toString().equals("^a.*")
    }

    def "should check constraints during parse"() {
        setup:
        StringArgument argument = new StringArgument([maxLength: "20", pattern: "^a.*", name: "test", delimiter: " "])
        argument.build()

        when:
        argument.parse("--test a text longer than expected")
        then:
        def ex = thrown(ArgumentsException)
        ex.getMessage().contains("20")

        when:
        argument.parse("--test notMatch")
        then:
        def ex2 = thrown(ArgumentsException)
        ex2.getMessage().contains("^a.*")
    }

    def "should handle multiple values"() {
        setup:
        argument.multivalued = true

        when:
        argument.parse("--test a")
        argument.parse("--test b")

        then:
        argument.value.size() == 2
        argument.value == ["a", "b"]
    }

    def "should throw exception on second argument if not multivalued"() {
        setup:
        argument.multivalued = false

        when:
        argument.parse("--test a")
        argument.parse("--test b")

        then:
        def ex = thrown(ArgumentsException)
        ex.getMessage().contains("not multivalued")
    }

    def "should return proper matching regex depending on name length"() {
        setup:
        StringArgument argument = new StringArgument()

        when:
        argument.name = "test"
        then:
        argument.getStartingRegex().startsWith("^--test")

        when:
        argument.name = "a"
        then:
        argument.getStartingRegex().startsWith("^-a")
    }

    def "should throw exception if name contain forbidden characters"() {
        when:
        argument.name = "forbidden name\t"

        then:
        thrown(ArgumentsException)
    }
}
