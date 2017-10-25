package matocham.arguments

import matocham.arguments.args.Argument

abstract class Parser {
    private static Properties lookup = new Properties()
    static{
        lookup.load(Parser.class.getResourceAsStream("/argumentMapping.properties"))
    }
    protected String argumentsString

    Parser(String argumentsString){
        this.argumentsString = argumentsString
    }
    abstract Arguments parse() throws ParseException

    protected Argument getArgumentInstance(type, argsMap = [] as Map){
        def lookupType = getFromLookup(type)
        Argument instance = lookupType ? Class.forName(lookupType)?.newInstance(argsMap) : Class.forName(type)?.newInstance(argsMap)
        return instance
    }

    private static String getFromLookup(value){
        return lookup.getProperty(value)
    }
}
