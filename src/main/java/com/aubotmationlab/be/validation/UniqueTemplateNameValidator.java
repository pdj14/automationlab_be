package com.aubotmationlab.be.validation;

import com.aubotmationlab.be.repository.Object3DTemplateRepository;
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
        // 초기화 로직이 필요한 경우 여기에 구현
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.trim().isEmpty()) {
            return true; // null이나 빈 값은 다른 검증에서 처리
        }
        
        // 이름이 이미 존재하는지 확인
        return !object3DTemplateRepository.existsByName(name);
    }
}
