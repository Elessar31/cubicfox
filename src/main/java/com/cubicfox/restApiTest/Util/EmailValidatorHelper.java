package com.cubicfox.restApiTest.Util;

import lombok.experimental.UtilityClass;
import org.apache.commons.validator.routines.EmailValidator;

@UtilityClass
public class EmailValidatorHelper {
    public static boolean emailIsValid(String email) {
       return EmailValidator.getInstance().isValid(email);
    }
}
