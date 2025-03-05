package com.mog.authserver.company.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    // 대한민국 지역번호 목록
    private static final Set<String> AREA_CODES = new HashSet<>(
            Arrays.asList("02", "031", "032", "033", "041", "042", "043", "044", "051", "052", "053", "054", "055",
                    "061", "062", "063", "064"));

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        // 초기화 코드가 필요하면 여기에 추가
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // null 및 empty는 @NotNull 등 별도 검증
        }

        // 형식 검증: 지역번호-xxx-xxxx
        String regex = "^(\\d{2,3})-\\d{3}-\\d{4}$";
        if (!value.matches(regex)) {
            return false;
        }

        // 지역번호 유효성 검증
        String areaCode = value.substring(0, value.indexOf('-'));
        return AREA_CODES.contains(areaCode);
    }
}