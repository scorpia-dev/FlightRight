package flightright.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = FileValidatorOnUpdate.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FileValidatorConstraintOnUpdate {
	String message() default "Invalid image file type";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
