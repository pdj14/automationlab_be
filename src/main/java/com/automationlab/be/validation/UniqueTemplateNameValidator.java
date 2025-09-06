package com.automationlab.be.validation;

import com.automationlab.be.repository.Object3DTemplateRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueTemplateNameValidator implements ConstraintValidator<UniqueTemplateName, String> {

    @Autowired
    private Object3DTemplateRepository object3DTemplateRepository;

    @Override
    public void initialize(UniqueTemplateName constraintAnnotation) {
        // Initialize logic if needed
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.trim().isEmpty()) {
            return true; // Let other validations handle null or empty values
        }
        
        // Check if name already exists
        return !object3DTemplateRepository.existsByName(name);
    }
}