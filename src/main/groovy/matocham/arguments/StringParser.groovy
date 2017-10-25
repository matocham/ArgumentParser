package matocham.arguments

import matocham.arguments.args.Argument

import java.util.regex.Pattern

class StringParser extends Parser {
    private static def ARGUMENT_PATTERN = ~/([a-zA-Z0-9_]+?(\/.)?\[.+?\])/
    private static def CLASS_DEF_PATTERN = ~/(?<=\[).+(?=\])/
    private static def ARGUMENTS_PATTERN = ~/(?<=\().+(?=\))/

    StringParser(String argumentsString) {
        super(argumentsString)
    }

    @Override
    Arguments parse() throws ParseException {
        def groups = argumentsString.findAll(ARGUMENT_PATTERN)
        def argumentsStringAfterParse = argumentsString

        if (!groups) {
            throw new ArgumentsException("There is no arguments to parse!")
        }
        Arguments arguments = new OrderedArguments()
        groups.each {
            arguments.addArgument(parseArgument(it))
            argumentsStringAfterParse = argumentsStringAfterParse.replaceAll(Pattern.quote(it),"")
        }
        if(argumentsStringAfterParse.trim()){
            throw new ParseException("Pattern parsed, but some text left: $argumentsStringAfterParse")
        }
        return arguments
    }

    Argument parseArgument(String stringToParse) {
        def classDefinition = stringToParse.find(CLASS_DEF_PATTERN).trim()
        def argument = getArgumentFromString(classDefinition)

        def nameAndDelimiter = stringToParse.substring(0, stringToParse.indexOf('[')).split('/')
        if (nameAndDelimiter.length > 2) {
            throw new ParseException("Name and delimiter can't contain / character")
        }
        def name = nameAndDelimiter[0].trim()
        argument.setName(name)
        if (nameAndDelimiter.length == 2) {
            def delimiter = nameAndDelimiter[1].trim()
            if (delimiter.length() != 1) {
                throw new ParseException("Delimiter should contain only one character!")
            }
            if(delimiter == '_'){
                delimiter = ' '
            }
            argument.setDelimiter(delimiter)
        }
        return argument
    }

    private Argument getArgumentFromString(String argument) {
        def className = argument.substring(0, argument.indexOf('(')).trim()
        def arguments = argument.find(ARGUMENTS_PATTERN)
        if (arguments) {
            def argumentsMap = new HashMap<String, String>()
            arguments.split(',').each {
                def keyAndValue = it.split('=')
                if (keyAndValue.length != 2) {
                    throw new ParseException("Argument properties should be key value pair, sepereated by =")
                }
                argumentsMap[keyAndValue[0].trim()] = keyAndValue[1].trim()
            }
            return getArgumentInstance(className, argumentsMap)
        } else {
            return getArgumentInstance(className)
        }
    }
}
