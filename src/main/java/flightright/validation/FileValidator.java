package flightright.validation;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<FileValidatorConstraint, File> {

	@Override
	public void initialize(FileValidatorConstraint file) {
	}

	@Override
	public boolean isValid(File file, ConstraintValidatorContext context) {

		if (file == null) {
			// the @NotNull annotation in the Member class protects us from null value problems
			return true;
		}

		MimetypesFileTypeMap mtftp = new MimetypesFileTypeMap();
		mtftp.addMimeTypes("image png jpg jpeg");

		String mimetype = mtftp.getContentType(file).split("/")[0];
		if (mimetype.equals("image")) {
			return true;
		} else {
			return false;
		}
	}

}