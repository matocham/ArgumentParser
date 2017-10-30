package matocham.argParser.parser

import matocham.argParser.args.Argument
import matocham.argParser.arguments.Arguments
import matocham.argParser.exceptions.ArgumentsException

abstract class Parser {
    private static def FORBIDDEN_NAME_CHARACTERS = ["\$", " ", "\t", "\n", "/"]

    private static Properties lookup = new Properties()

    static {
        lookup.load(Parser.class.getResourceAsStream("/argumentMapping.properties"))
    }

    abstract Arguments parse() throws ParseException

    protected Argument getArgumentInstance(String type, Map argsMap = null) {
        def lookupType = getFromLookup(type)
        if (lookupType != null) {
            type = lookupType
        }
        if (!type) {
            throw new ArgumentsException("Class $type not found")
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

    private static String getFromLookup(String value) {
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
