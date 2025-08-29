package com.aubotmationlab.be.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueTemplateNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTemplateName {
    String message() default "Template name must be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
