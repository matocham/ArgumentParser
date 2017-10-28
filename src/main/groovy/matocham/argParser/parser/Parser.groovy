package matocham.argParser.parser

import matocham.argParser.args.Argument
import matocham.argParser.arguments.Arguments

abstract class Parser {
    private static def FORBIDDEN_NAME_CHARACTERS = ["-", "\$", " ", "\t", "\n"]

    private static Properties lookup = new Properties()

    static {
        lookup.load(Parser.class.getResourceAsStream("/argumentMapping.properties"))
    }
    protected String argumentsString

    Parser(String argumentsString) {
        this.argumentsString = argumentsString
    }

    abstract Arguments parse() throws ParseException

    protected Argument getArgumentInstance(String type, Map argsMap = null) {
        def lookupType = getFromLookup(type)
        if (lookupType != null) {
            type = lookupType
        }
        Argument instance
        if (argsMap == null) {
            instance = Class.forName(type)?.newInstance()
        } else {
            instance = Class.forName(lookupType)?.newInstance(argsMap)
        }
        instance.build()
        return instance
    }

    private static String getFromLookup(value) {
        return lookup.getProperty(value)
    }

    protected def checkNameValue(String name) {
        FORBIDDEN_NAME_CHARACTERS.each {
            if (name.contains(it)) {
                throw new ParseException("Name can't contain '$it' character");
            }
        }
    }
}
