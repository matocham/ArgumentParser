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

    protected Argument getArgumentInstance(String type, Map argsMap = null){
        def lookupType = getFromLookup(type)
        if(lookupType != null){
            type = lookupType
        }
        Argument instance
        if(argsMap == null){
            instance = Class.forName(type)?.newInstance()
        } else {
            instance = Class.forName(lookupType)?.newInstance(argsMap)
        }
        instance.build()
        return instance
    }

    private static String getFromLookup(value){
        return lookup.getProperty(value)
    }
}
