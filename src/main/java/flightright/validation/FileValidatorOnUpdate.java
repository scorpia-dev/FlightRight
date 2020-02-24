package flightright.validation;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileValidatorOnUpdate implements ConstraintValidator<FileValidatorConstraintOnUpdate, File> {

	@Override
	public void initialize(FileValidatorConstraintOnUpdate file) {
	}

	@Override
	public boolean isValid(File file, ConstraintValidatorContext context) {
		return true;
	}
}
/*		if (file == null) {
			return true;
		}
		String mimetype = new MimetypesFileTypeMap().getContentType(file);
		String type = mimetype.split("/")[0];
		if (type.equals("image")) {
			return true;
		} else {
			return false;
		}
	}*/

