import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class ApplicationController {
	
	private ApplicationModel model;
	private ApplicationView view;
	
	private OtsuThreshold otsu;
	private ArrayList<Command> commandList;
	ProcessingThread thread;
	
	public ApplicationController() {
		
		model = new ApplicationModel();
		view = new ApplicationView(this);
		otsu = new OtsuThreshold();
		commandList = new ArrayList<Command>();
		
		loadImages();
		
	}
	
	public void loadImages() {
		
		List<Path> matList = null;
		
		//skip(1) to skip directory path
		try (Stream<Path> paths = Files.walk(Paths.get(ApplicationModel.IMAGE_PATH)).skip(1)){
		      matList = paths.collect(Collectors.toList());
		} catch (IOException e) {
		      e.printStackTrace();
		}
		
		int size = matList.size();
		
		for(int i = 0; i < size; i++) {
			
			Mat mat = Highgui.imread(matList.get(i).toString());
			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
			model.addMat(mat);
			
		}
		
		//Imgproc.cvtColor(model.matImage, model.matImage, Imgproc.COLOR_GRAY2BGR);
		view.CreateImageGallery(Mats2BufferedImage(model.getMatList()));		
		//calculateOtsu(matToByteArray((model.getMat(0))));
	}
	
	public static BufferedImage Mat2BufferedImage(Mat m)
    {
	// source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
	// Fastest code
	// The output can be assigned either to a BufferedImage or to an Image

	    int type = BufferedImage.TYPE_BYTE_GRAY;
	    if ( m.channels() > 1 ) {
	        type = BufferedImage.TYPE_3BYTE_BGR;
	    }
	    int bufferSize = m.channels()*m.cols()*m.rows();
	    byte [] b = new byte[bufferSize];
	    m.get(0,0,b); // get all the pixels
	    BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
	    final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	    System.arraycopy(b, 0, targetPixels, 0, b.length);  
	    return image;

	}
	
	public static BufferedImage[] Mats2BufferedImage(ArrayList<Mat> array)
	   {
		// source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
		// Fastest code
		// The output can be assigned either to a BufferedImage or to an Image
		
		int size = array.size();
		BufferedImage[] images = new BufferedImage[size];
		
		for(int i = 0; i < size; i++) {
			
			Mat m = array.get(i);
			
		    int type = BufferedImage.TYPE_BYTE_GRAY;
		    
		    if ( m.channels() > 1 ) {
		        type = BufferedImage.TYPE_3BYTE_BGR;
		    }
		    int bufferSize = m.channels()*m.cols()*m.rows();
		    byte [] b = new byte[bufferSize];
		    m.get(0,0,b); // get all the pixels
		    BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
		    final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		    System.arraycopy(b, 0, targetPixels, 0, b.length);  
		    images[i] = image;
		}
		
		return images;
	}
	
	public Mat threshold(Mat img, int t)
	{
	   /* threshold the image (img), note here that we need to do an
	* & with 0xff. this is because Java uses signed two's complement
	* types. The & operation will give us the pixel in the range we are
	* used to, 0..255
	*/
		Mat imgToProcess = img;
		
		byte data[] = new byte[imgToProcess.rows()*imgToProcess.cols()*imgToProcess.channels()];
		imgToProcess.get(0, 0, data);
		for (int i=0;i<data.length;i++)
		{
			int unsigned = (data[i] & 0xff);
			if (unsigned > t)
				data[i] = (byte)0;
			else
				data[i] = (byte)255;
		}
		
		imgToProcess.put(0, 0, data);
		   
		return imgToProcess;
	}
	
	public void addImageForProcessing(int index) {
		
		Mat currentMat = new Mat();
		model.getMat(index).copyTo(currentMat);
		
		view.AddCurrentImageToView(Mat2BufferedImage(currentMat));
		model.setMat(currentMat);
		
	}
	
	public void thresholdImage() {
		
		Mat mat = model.getMat();
		
        double before = (double)System.nanoTime()/1000000000;//secs
		mat = threshold(mat, otsu.calculateOtsu(matToByteArray(mat)));
		double after = (double)System.nanoTime()/1000000000;//secs
		//Core.putText(mat, "Processing Time: " + String.format("%.4f",after-before) + " secs", new Point(20,20), Core.FONT_HERSHEY_PLAIN, 0.8, new Scalar(255,255,255));
		view.updateTime(String.format("%.4f",after-before));
		view.AddCurrentImageToView(Mat2BufferedImage(mat));
	}
	
	public void showHistogram() {
				
		Mat histim = new Mat(256,256, CvType.CV_8UC3);
		int [] h = hist(model.getMat());
		
		view.addHistogram(Mat2BufferedImage(drawHist(histim,h)));
		//view.AddCurrentImageToView(Mat2BufferedImage(threshold(withTH, otsu.calculateOtsu(matToByteArray(withTH)))),
		//Mat2BufferedImage(drawHist(histim,h)));
		
	}
	
	public void dilateImage() {
		 
		Mat img = model.getMat();
		
        int cols = img.cols();
        int rows = img.rows();
       
        byte[] data = new byte[cols*rows];
        img.get(0, 0, data);
       
        byte[] data_copy = data.clone();
       
        for(int i = 0; i< data.length; i++)
        {
            // is current pixel a foreground pixel
            if((data[i] & 0xff) == 255)            
            {
           
               
                // an array of neighbor pixel locations in a 1D array
                int[] neighbours = {i + 1, i-1, i - cols, i+ cols, i - cols - 1,  i - cols + 1, i + cols -1, i+ cols +1};          
 
                // Iterate though neighbors checking for background pixel value
                for(int j =0 ; j<neighbours.length; j++)
                {
                   
                    // checking the bounds of the array
                    if((neighbours[j] > -1) && (neighbours[j] < data.length))
                    {
                       
                        // if we get a 0 value make it current pixel also 0
                        //for dilation make that a 1
                        if((data[neighbours[j]] & 0xff) == 0)
                        {
                           
                            //this is for dilation
                            data_copy[neighbours[j]] = (byte)255;
                        }
                    }
                }
            }
        }
       
        img.put(0, 0, data_copy);
        view.AddCurrentImageToView(Mat2BufferedImage(img));
    }
	
	 public void erodeImage() {
		 
		 	Mat img = model.getMat();
		 	
	        int cols = img.cols();
	        int rows = img.rows();
	       
	        byte[] data = new byte[cols*rows];
	        img.get(0, 0, data);
	       
	        byte[] data_copy = data.clone();
	       
	        for(int i = 0; i< data.length; i++)
	        {
	            // is current pixel a foreground pixel
	            if((data[i] & 0xff) == 255)            
	            {
	            	
	                // an array of neighbor pixel locations in a 1D array
	                int[] neighbours = {i + 1, i-1, i - cols, i+ cols, i - cols - 1,  i - cols + 1, i + cols -1, i+ cols +1};          
	 
	                // Iterate though neighbors checking for background pixel value
	                for(int j =0 ; j<neighbours.length; j++)
	                {
	                   
	                    // checking the bounds of the array
	                    if((neighbours[j] > -1) && (neighbours[j] < data.length))
	                    {
	                       
	                        // if we get a 0 value make it current pixel also 0
	                        //for dilation make that a 1
	                        if((data[neighbours[j]] & 0xff) == 0)
	                        {
	                            data_copy[i] = (byte)0;
	                           
	                            //this is for dilation
	                            //data_copy[neighbours[j]] = (byte)255;
	                        }
	                    }
	                }
	            }
	        }
	       
	        img.put(0, 0, data_copy);
	        view.AddCurrentImageToView(Mat2BufferedImage(img));
	    }

	public static int [] hist(Mat img)
	{
		   int hist [] = new int[256];
		   byte data[] = new byte[img.rows()*img.cols()*img.channels()];
		   img.get(0, 0, data);
		   for (int i=0;i<data.length;i++)
		   {
			   hist[(data[i] & 0xff)]++;
			   
		   }
		   
		   return hist;
	 }
	   
	public static Mat drawHist(Mat img, int [] hist)
	{
		   
	   Mat histogram = img;
	   //get max hist value for range adjustment
	   int max = 0;
	   for(int i=0;i<hist.length;i++)
	   {
		   if(hist[i] > max)
			   max = hist[i];
	   }
	   int scale = max/256;
	   for(int i=0;i<hist.length-1;i++)
	   {
		   
		   //Core.circle(img, new Point(i*2+1,img.rows()-(hist[i]/scale)+1), 1, new Scalar(0,255,0));
		   Core.line(histogram, new Point(i+1,histogram.rows()-(hist[i]/scale)+1), new Point(i+2,histogram.rows()-(hist[i+1]/scale)+1), new Scalar(0,0,255));
	   }
	   
	   return img;
		
	}
	
	
	public byte[] matToByteArray(Mat mat) {
		
		int bufferSize = mat.channels()*mat.cols()*mat.rows();
	    byte [] byteArray = new byte[bufferSize];
	    mat.get(0,0,byteArray);
	    
	    return byteArray;
		
	}
	
    public void CCA() //connectedness should be 4 or 8
    {
    	
    	Mat mat = model.getMat();
    	BufferedImage img = Mat2BufferedImage(mat);

    	int num_rows = img.getHeight();  //gets pixel height
    	int num_cols = img.getWidth();  // gets pixel width
    	int [] data = new int[num_rows*num_cols]; // created array that stores all pixels in images by multiplying height and width of the image
    	int [] labels = new int[num_rows*num_cols]; // same size as data
    	int currlabel = 0; // integer to be stored in labels array
    	try {
			img.getData().getPixels(0,0,num_cols,num_rows,data);
    	}
    	catch (Exception ex)
    	{	
    	}
    	
    	CustomQueue q = new CustomQueue(data.length); // creating Queue that will store unvisited pixels
    	
    	
	    	for(int i = 0; i < data.length; i++){ //for loop to traverse thru all pixels in an image
	    		
	    		if(labels[i] == 0){  // finds first pixel that is white in an image
	    			
	    			int color = data[i];
	    			currlabel++; //increase label by one, when another pixel will be found that was not checked, increment it again
	    			q.enqueue(i);// putting first white pixel in the queue for reference
	    			
	    			while(!q.isEmpty()){ //looping until queue is empty, usually it is empty when all white pixels are visited
	    				
	    				int front = q.dequeue(); // dequeue first pixel in the queue in "front" variable of type integer
	    				labels[front] = currlabel; // front value is the position in data array so labels[front] = currlabel; means that we put currlabel value in labels array where front is the position for labels array
	    				
	    				try{
		    				if(data[front-1] == color && labels[front-1] == 0){ // finding left neighbor and checking if it is white and if it was visited before or not
		    					try{
			    					q.enqueue(front-1); //if it was not visited then we put it in the queue
			    					labels[front-1] = currlabel; // and assign label to the left neighbor
			    				}catch(NullPointerException e){}
	    				}
	    				}catch(ArrayIndexOutOfBoundsException e){}
	    				
	    				try{
		    				if(data[front+1] == color && labels[front+1] == 0){//  finding right neighbor and checking if it is white and if it was visited before or not
		    					try{
			    					q.enqueue(front+1);//if it was not visited then we put it in the queue
			    					labels[front+1] = currlabel;// and assign label to the right neighbor
		    					}catch(NullPointerException e){}
		    				}
	    				}catch(ArrayIndexOutOfBoundsException e){
	    					
	    					
	    				}
	    				
	    				try{
		    				if(data[front-num_cols] == color && labels[front-num_cols] == 0){ // finding top neighbor and checking if it is white and if it was visited before or not
		    					try{
			    					q.enqueue(front-num_cols);//if it was not visited then we put it in the queue
			    					labels[front-num_cols] = currlabel;// and assign label to the top neighbor
		    					}catch(NullPointerException e){}
		    					
		    				}
	    				}catch(ArrayIndexOutOfBoundsException e){}
	    				
	    				
	    				try{
	    					if(data[front+num_cols] == color && labels[front+num_cols] == 0){ // finding bottom neighbor and checking if it is white and if it was visited before or not
		    					try{
		    					q.enqueue(front+num_cols);//if it was not visited then we put it in the queue
		    					labels[front+num_cols] = currlabel;// and assign label to the bottom neighbor
		    					}catch(NullPointerException e){}
	    					
	    					}
	    				}catch(ArrayIndexOutOfBoundsException e){}
	    				
	    			}	
	    		}
    	}
    	
    	BufferedImage labelImage = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
    	Color [] colorArray = new Color[9];
    	colorArray[0] = Color.MAGENTA; colorArray[1] = Color.RED; colorArray[2] = Color.GREEN; 
    	colorArray[3] = Color.BLUE; colorArray[4] = Color.CYAN;colorArray[5] = Color.YELLOW; 
    	colorArray[6] = Color.DARK_GRAY; colorArray[7] = Color.ORANGE; colorArray[8] = Color.PINK;
    	for(int i=0; i<labels.length;i++)
    	{
    		labelImage.setRGB(i%img.getWidth(), i/img.getWidth(), colorArray[labels[i]%colorArray.length].getRGB());
    	}
    	
    	
    	view.AddCurrentImageToView(labelImage);
    	view.UpdateComponentLabel(numComponents(labels));
    	
    }
    
    public int numComponents(int labels [])
    {
	//this method takes in the label array and return the number of components in it
    	
		int numComponents = 0;
		
		/** 
		 * I used ArrayList class in here because it has function called contains() that checks if the same value is in the ArrayList already exist
		 * 
		 */
		
		ArrayList<Integer> diffLabels = new ArrayList<>(); // declaring and initializing ArrayList
		
		for(int i = 0; i<labels.length;i++){ // looping all labels in an array
			if(!diffLabels.contains(labels[i])){// if statement to check if that label value is already in ArrayList, if yes then skip it	
					diffLabels.add(labels[i]);	// adding label value to ArrayList
			}	
		}
			
		numComponents = diffLabels.size(); // size() function returns the size of the ArrayList which is the number of components
		
		
		return numComponents; // returning num of components
		
    }

	public void hideComponentLabel() {
		
		view.HideComponentLabel();
		
	}
	
	public void damageCheck() {
		
		Mat mat = model.getMat();
		int width = mat.width();
		int height = mat.height();
		
		byte[] data = new byte[height*width];
		
		mat.get(0, 0, data);
		
		int current = 0;
		int previous = 0;
		int changeCount = 0;
		boolean isIncreasing = true;
		
		
		for(int i = 0; i < width; i++) {
			
			previous = current;
			current = downwardDamageCheck(i,width,height,data);
			
			if(current != -1 && previous != -1) {	
					
					//is it increasing or decreasing
					if(current > previous && isIncreasing == false) {
						
						isIncreasing = true;
						changeCount++;
					}
					
					else if(current < previous && isIncreasing == true) {
						
						isIncreasing = false;
						changeCount++;
					}
			}
			
		}
		
		current = 0;
		previous = 0;
		isIncreasing = false;
		
		for(int i = 0; i < width; i++) {
			
			previous = current;
			current = upwardDamageCheck(i,width,height,data);
			
			if(current != -1 && previous != -1) {	
					
					//is it increasing or decreasing
					if(current > previous && isIncreasing == false) {
						
						isIncreasing = true;
						changeCount++;
					}
					
					else if(current < previous && isIncreasing == true) {
						
						isIncreasing = false;
						changeCount++;
					}
			}
			
		}
		
		current = 0;
		previous = 0;
		isIncreasing = true;
		
		for(int i = 0; i < height; i++) {
			
			previous = current;
			current = rightSideDamageCheck(i,width,height,data);
			
			if(current != -1 && previous != -1) {	
					
					//is it increasing or decreasing
					if(current > previous && isIncreasing == false) {
						
						isIncreasing = true;
						changeCount++;
					}
					
					else if(current < previous && isIncreasing == true) {
						
						isIncreasing = false;
						changeCount++;
					}
			}
			
		}
		
		current = 0;
		previous = 0;
		isIncreasing = true;
		
		for(int i = 0; i < height; i++) {
			
			previous = current;
			current = leftSideDamageCheck(i,width,height,data);
			
			if(current != -1 && previous != -1) {	
					
					//is it increasing or decreasing
					if(current > previous && isIncreasing == false) {
						
						isIncreasing = true;
						changeCount++;
					}
					
					else if(current < previous && isIncreasing == true) {
						
						isIncreasing = false;
						changeCount++;
					}
			}
			
		}
		
		System.out.println(changeCount);
		
		view.UpdateConditionLabel(changeCount == 8 ? "PASS" : "FAIL");
		
	}
	
	public int downwardDamageCheck(int index, int width, int height,byte[] data) {
				
		for(int i = 0; i<height; i++) {
			
			if((data[index+(width*i)] & 0xff) == 255) {
				
				return i;
			}
			
		}
		
		return -1;
		
	}
	
	public int upwardDamageCheck(int index, int width, int height,byte[] data) {
		
		for(int i = width - 1; i>0; i--) {
			
			if((data[index+(width*i)] & 0xff) == 255) {
				
				System.out.println(i);
				return i;
			}
			
		}
		
		return -1;
		
	}
	
	public int rightSideDamageCheck(int index, int width, int height,byte[] data) {
		
		int position = width+(width*index);
		
		for(int i = 0; i<width; i++) {
			
			if((data[position - i - 1] & 0xff) == 255) {
				
				System.out.println(i);
				return i;
			}
			
		}
		
		return -1;
		
	}
	
	public int leftSideDamageCheck(int index, int width, int height,byte[] data) {
		
		int position = height * index;
		System.out.println(position);
		
		for(int i = 0; i < height - 1; i++) {
			
			if((data[position + i] & 0xff) == 255) {
				
				
				return i;
			}
			
		}
		
		return -1;
		
	}
	
	public void addCommandToTheList(Command command) {	
		commandList.add(command);
	}
	
	public void removeCommandFromTheList(int index) {
		commandList.remove(index);
	}
	
	public void ExecuteCommandLoop() {
		
		view.setEnabledButtons(false);
		thread = new ProcessingThread(commandList,this);
		thread.start();
		
	}
	
	public void StopCommandLoop() {
		
		if(thread.isAlive()) {
			
			thread.stopProcess();
			
		}
	}
	
	public ApplicationView getView() {
		
		return view;
		
	}
}
