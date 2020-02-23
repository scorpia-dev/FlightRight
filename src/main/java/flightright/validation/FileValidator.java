package flightright.validation;
import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileValidator implements
ConstraintValidator<FileValidatorConstraint, File> {

  @Override
  public void initialize(FileValidatorConstraint file) {
  }


@Override
public boolean isValid(File file, ConstraintValidatorContext context) {
/*	        String filepath = value;
	        File f = new File(filepath);*/
	        String mimetype= new MimetypesFileTypeMap().getContentType(file);
	        String type = mimetype.split("/")[0];
	        if(type.equals("image")) {
	        return true;
	        }
	        else {
		        return false;
	    }
}

}