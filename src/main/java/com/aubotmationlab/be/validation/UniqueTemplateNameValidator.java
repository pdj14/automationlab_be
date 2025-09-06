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
        // 초기??로직???�요??경우 ?�기??구현
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.trim().isEmpty()) {
            return true; // null?�나 �?값�? ?�른 검증에??처리
        }
        
        // ?�름???��? 존재?�는지 ?�인
        return !object3DTemplateRepository.existsByName(name);
    }
}
