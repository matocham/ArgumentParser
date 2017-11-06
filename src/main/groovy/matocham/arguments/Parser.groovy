package matocham.arguments

import matocham.args.Argument
import matocham.exceptions.ArgumentsException

abstract class Parser {

    private static Properties lookup = new Properties()

    static {
        lookup.load(Parser.class.getResourceAsStream("/argumentMapping.properties"))
    }

    abstract Arguments parse() throws ParseException

    protected Argument getArgumentInstance(String type, Map argsMap = [:]) {
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
}
