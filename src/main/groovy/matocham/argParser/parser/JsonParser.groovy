package matocham.argParser.parser

import groovy.json.JsonSlurper
import matocham.argParser.args.Argument
import matocham.argParser.arguments.Arguments
import matocham.argParser.arguments.OrderedArguments
import matocham.argParser.arguments.UnorderedArguments

class JsonParser extends Parser {
    private static final def COMMON_FIELDS = ["name", "delimiter", "required", "multivalued", "type"]
    private File file

    JsonParser(String filePath) {
        this.file = new File(filePath)
        if (!file.exists()) {
            throw new ParseException("Can't find file ${file.toString()}")
        }
    }

    @Override
    Arguments parse() throws ParseException {
        JsonSlurper slurper = new JsonSlurper()
        def jsonParsedFile = slurper.parse(file)
        Arguments arguments
        if (jsonParsedFile.ordered) {
            arguments = new OrderedArguments()
        } else {
            arguments = new UnorderedArguments()
        }
        jsonParsedFile.arguments.each {
            Argument arg = parseArgument(it)
            arguments.addArgument(arg)
        }
        return arguments
    }

    def parseArgument(def argObject) {
        def type = argObject.type
        def multivalued = argObject.multivalued ?: false
        def required = argObject.required ?: false
        def name = argObject.name
        def delimiter = argObject.delimiter ?: ""

        checkNameValue(name)
        delimiter = preprocesDelimiter(delimiter)

        Map constructorArguments = [:]
        argObject.each { key, val ->
            if (!(key in COMMON_FIELDS)) {
                constructorArguments."$key" = val
            }
        }
        Argument argument = getArgumentInstance(type, constructorArguments)
        argument.name = name
        argument.multivalued = multivalued
        argument.required = required
        argument.delimiter = delimiter
        return argument
    }

    private String preprocesDelimiter(String delimiter) {
        if (delimiter.length() > 1 || delimiter.contains("/")) {
            throw new ParseException("Delimiter should contain only one character!")
        }
        if (delimiter == "/") {
            throw new ParseException("Delimiter can't contain / character!")
        }
        if (delimiter == '_') {
            delimiter = ' '
        }
        delimiter
    }
}
