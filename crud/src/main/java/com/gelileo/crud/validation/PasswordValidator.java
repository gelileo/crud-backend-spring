package com.gelileo.crud.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.sort.ArraysSort;

import java.util.Arrays;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(final ValidPassword arg0) {}

    @Override
    public boolean isValid(final String password, ConstraintValidatorContext context) {
        org.passay.PasswordValidator validator = new org.passay.PasswordValidator(
                Arrays.asList(
                        new LengthRule(8,14), // 8-14 characters
                        new CharacterRule(EnglishCharacterData.UpperCase, 1), // minimum 1 uppercase
                        new CharacterRule(EnglishCharacterData.LowerCase, 1), // minimum 1 lowercase
                        new CharacterRule(EnglishCharacterData.Digit, 1),  // minimum 1 number
                        new CharacterRule(new CharacterData() {
                            public String getErrorCode() { return EnglishCharacterData.Special.getErrorCode(); }
                            // Use a subset of EnglishCharacterData.Special
                            public String getCharacters() { return "!@#$%^&*()"; }
                        }, 1), // minimum one special char
                        new IllegalSequenceRule(EnglishSequenceData.USQwerty, 4, false), // prohibit qwerty sequence
                        new WhitespaceRule(), // no white spaces
                        commonPasswordRule() // prohibit common passwords
                )
        );

        final RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }

        // disable the default "Invalid Password" msg
        // concat all the violation errors as message
        if (context != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.join(",", validator.getMessages(result))
            ).addConstraintViolation();
        }


        return false;
    }

    private DictionaryRule commonPasswordRule() {
        // common or guessable password list
        String[] dictionaryWords = new String[]{
                "password",
                "drowssap"};

        ArraysSort sorter = new ArraysSort();
        sorter.sort(dictionaryWords);
        WordListDictionary wordListDictionary = new WordListDictionary(new ArrayWordList(dictionaryWords));

        return new DictionaryRule(wordListDictionary);
    }
}
