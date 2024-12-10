package com.mog.authserver.gcs.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {
    private boolean emptyable;

    @Override
    public void initialize(ValidImage constraintAnnotation) {
        this.emptyable = constraintAnnotation.emptyable();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if(file==null || file.isEmpty()) {
            return emptyable; // 이미지가 없으면 검증하지 않음
        }
        // MIME 타입 확인
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif");
    }
}
