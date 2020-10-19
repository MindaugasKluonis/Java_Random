import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ApplicationModel {
	
	
	private Mat currentImage;
	private ArrayList<Mat> images;
	
	public static final String IMAGE_PATH = "src/images/";
	
	public ApplicationModel() {
		
		currentImage = new Mat();
		images = new ArrayList<Mat>();

		
	}
	
	public void setMat(Mat mat) {
		
		currentImage = mat;
		mat.copyTo(currentImage);
		
	}
	
	public Mat getMat() {
		
		return currentImage;
		
	}
	
	public void addMat(Mat image) {
		
		images.add(image);
		
	}
	
	public Mat getMat(int index) {
		
		return images.get(index);
		
	}
	
	public int getMatSize() {
		
		return images.size();
		
	}
	
	public ArrayList<Mat> getMatList(){
		
		return images;
		
	}
	
}
