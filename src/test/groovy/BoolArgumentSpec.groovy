import matocham.args.BoolArgument
import matocham.exceptions.ArgumentsException
import spock.lang.Specification

class BoolArgumentSpec extends Specification {
    BoolArgument argument

    def setup() {
        argument = new BoolArgument()
        argument.name = "switch"
        argument.delimiter = ""
    }

    def "should throw exception when multivalued set to true"() {
        when:
        argument.multivalued = true
        then:
        thrown(ArgumentsException)
    }

    def "should parse empty value"() {
        expect:
        argument.parse("--switch") == true
    }

    def "should parse boolean values"() {
        setup:
        argument.delimiter = "="
        when:
        argument.parse(arg)
        then:
        argument.value == [val]

        where:
        arg              | val
        "--switch=false" | false
        "--switch=true"  | true
        "--switch=f"     | false
        "--switch=t"     | true
        "--switch=n"     | false
        "--switch=y"     | true
    }

    def "should parse boolean value without delimiter"() {
        when:
        argument.parse(arg)
        then:
        argument.value == [val]

        where:
        arg             | val
        "--switchfalse" | false
        "--switchtrue"  | true
        "--switchf"     | false
        "--switcht"     | true
        "--switchn"     | false
        "--switchy"     | true
    }
}
