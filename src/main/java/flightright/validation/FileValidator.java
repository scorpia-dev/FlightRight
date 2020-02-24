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
	
			if (file == null) {
				return true;
			}
			
/*            MimetypesFileTypeMap mftp = new MimetypesFileTypeMap();
            mftp.addMimeTypes("image png jpg jpeg");
            String mimeType = mftp.getContentType(file).split("/")[0];
            result = mimeType.equals("image");
            */
            
			MimetypesFileTypeMap mtftp = new MimetypesFileTypeMap();
			mtftp.addMimeTypes("image png jpg jpeg");
			
	        String mimetype=  mtftp.getContentType(file).split("/")[0];
	        if(mimetype.equals("image")) {
	        return true;
	        }
	        else {
		        return false;
	    }
}

}