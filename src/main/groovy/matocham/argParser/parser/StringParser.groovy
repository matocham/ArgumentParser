package matocham.argParser.parser

import matocham.argParser.exceptions.ArgumentsException
import matocham.argParser.args.Argument
import matocham.argParser.arguments.Arguments
import matocham.argParser.arguments.OrderedArguments
import matocham.argParser.arguments.UnorderedArguments

import java.util.regex.Pattern

class StringParser extends Parser {
    private static def ARGUMENT_PATTERN = ~/([a-zA-Z0-9_]*?-?[a-zA-Z0-9_]*?(\/.*?)?\[.+?])\*?!?/
    private static def CLASS_DEF_PATTERN = ~/(?<=\[).+(?=])/
    private static def ARGUMENTS_PATTERN = ~/(?<=\().+(?=\))/
    private static def ORDERED_MARK = ">"
    private static String MULTIVALUED_CHARACTER = "*"
    private static String REQUIRED_MARK = "!"

    StringParser(String argumentsString) {
        super(argumentsString)
    }

    @Override
    Arguments parse() throws ParseException {
        def isOrdered = argumentsString.endsWith(ORDERED_MARK)
        def argumentsStringAfterParse = argumentsString
        Arguments arguments

        if (isOrdered) {
            argumentsStringAfterParse = argumentsString.substring(0, argumentsString.length() - 1)
            arguments = new OrderedArguments()
        } else {
            arguments = new UnorderedArguments()
        }
        def groups = argumentsStringAfterParse.findAll(ARGUMENT_PATTERN)
        if (!groups) {
            throw new ArgumentsException("There is no arguments to parse!")
        }
        groups.each {
            arguments.addArgument(parseArgument(it))
            argumentsStringAfterParse = argumentsStringAfterParse.replaceAll(Pattern.quote(it), "")
        }
        if (argumentsStringAfterParse.trim()) {
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
        checkNameValue(name)

        argument.setName(name)
        if (nameAndDelimiter.length == 2) {
            def delimiter = nameAndDelimiter[1].trim()
            if (delimiter.length() != 1) {
                throw new ParseException("Delimiter should contain only one character!")
            }
            if (delimiter == '_') {
                delimiter = ' '
            }
            argument.delimiter = delimiter
        }

        def modifiers = stringToParse.substring(stringToParse.lastIndexOf("]")+1)
        if (modifiers.contains(MULTIVALUED_CHARACTER)) {
            argument.multivalued = true
            modifiers = modifiers.replaceAll(Pattern.quote(MULTIVALUED_CHARACTER), "")
        }
        if (modifiers.contains(REQUIRED_MARK)) {
            argument.required = true
            modifiers = modifiers.replaceAll(Pattern.quote(REQUIRED_MARK), "")
        }
        if (!modifiers.isEmpty()) {
            throw new ArgumentsException("Unknown modifier: $modifiers")
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
                    throw new ParseException("Argument properties should be key=value pair")
                }
                argumentsMap[keyAndValue[0].trim()] = keyAndValue[1].trim()
            }
            return getArgumentInstance(className, argumentsMap)
        } else {
            return getArgumentInstance(className)
        }
    }
}
