package com.gelileo.crud.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ValidationMessage {

    @Setter
    @Getter
    @RequiredArgsConstructor
    public static class ValidationError {
        private final List<String> arguments;
        private final List<String> errorCodes;
        private final String defaultMessage;
        private final String generalMessage;
    }
    private final String message;

    private ValidationError validationError;

    public ValidationMessage(String message) {
        this.message = message;
        validationError = parseErrorMessage(message);
    }

    private static final Pattern ARGUMENTS_PATTERN = Pattern.compile("arguments \\[(.*?)\\]");
    private static final Pattern ERROR_CODES_PATTERN = Pattern.compile("codes \\[(.*?)\\]");
    private static final Pattern DEFAULT_MESSAGE_PATTERN = Pattern.compile("default message \\[(.*?)\\]", Pattern.DOTALL);

    private ValidationError parseErrorMessage(String errorMessage) {
        Matcher argumentsMatcher = ARGUMENTS_PATTERN.matcher(errorMessage);
        Matcher errorCodesMatcher = ERROR_CODES_PATTERN.matcher(errorMessage);
        Matcher defaultMessageMatcher = DEFAULT_MESSAGE_PATTERN.matcher(errorMessage);


        String generalMessage = errorMessage.split("codes")[0];

        List<String> arguments = new ArrayList<>();
        List<String> errorCodes = new ArrayList<>();
        String defaultMessage = "";

        if (argumentsMatcher.find()) {
            String argumentsText = argumentsMatcher.group(1);
            arguments = parseList(argumentsText);
        }

        if (errorCodesMatcher.find()) {
            String errorCodesText = errorCodesMatcher.group(1);
            errorCodes = parseList(errorCodesText);
        }

        while (defaultMessageMatcher.find()) {
            defaultMessage = defaultMessageMatcher.group(1);
        }

        return new ValidationError(arguments, errorCodes, defaultMessage, generalMessage);
    }

    private List<String> parseList(String listText) {
        String[] elements = listText.split(",");
        List<String> parsedList = new ArrayList<>();
        for (String element : elements) {
            parsedList.add(element.trim());
        }
        return parsedList;
    }
}
