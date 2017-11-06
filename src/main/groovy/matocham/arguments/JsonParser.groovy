package matocham.arguments

import groovy.json.JsonSlurper
import matocham.args.Argument

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

    private def parseArgument(def argObject) {
        def type = argObject.type
        def multivalued = argObject.multivalued ?: false
        def required = argObject.required ?: false
        def name = argObject.name
        def delimiter = argObject.delimiter ?: ""

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
}
