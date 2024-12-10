package com.mog.authserver.gcs.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ImageFileValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidImage {
    String message() default "지원하지 않는 파일 형식입니다. jpeg, png, gif 파일만 허용됩니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    /**
     * 파일이 비어있어도 되는지 여부를 설정합니다.
     *
     * @return 빈 파일 허용 여부
     */
    boolean emptyable() default false;
}