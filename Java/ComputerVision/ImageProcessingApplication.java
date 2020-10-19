import org.opencv.core.Core;

public class ImageProcessingApplication {

	public static void main(String[] args) {
		
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		ApplicationController controller = new ApplicationController();
		

	}

}
