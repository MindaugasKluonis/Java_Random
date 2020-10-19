
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.*;
import javax.swing.*;

/**
 * This class demonstrates how to load an Image from an external file and do some analysis
 */


public class ImageAnalysis extends Component {

    BufferedImage img;
    
    
    int componentSize;
    String values;
         
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

    public ImageAnalysis() {
       try {
           img = ImageIO.read(new File("images/coins.png"));
           thresholdImage(80);
           img = labels2Image(CCA(8));
       } catch (IOException e) {
       }

    }

    public Dimension getPreferredSize() {
        if (img == null) {
             return new Dimension(100,100);
        } else {
           return new Dimension(img.getWidth(null), img.getHeight(null));
       }
    }

    public void thresholdImage(int t)
    {
		try {
			int [] pixels = new int[img.getWidth()*img.getHeight()];
			img.getData().getPixels(0,0,img.getWidth(),img.getHeight(),pixels);
			System.out.println(pixels[0]);
			for (int i=0;i<pixels.length;i++)
			{

				if (pixels[i] > t)
					pixels[i] = -1;// this is the equivalent integer value to an unsigned byte value of 255 
				else
					pixels[i] = 0;
			}
			img.setRGB(0,0,img.getWidth(),img.getHeight(),pixels,0,img.getWidth());

		}
		catch (Exception ex)
		{
			System.out.println("Oops! Something went wrong!");
			System.out.println(ex.toString());
		}
	}
    
    public BufferedImage labels2Image(int [] labels)
    {
    	BufferedImage labelImage = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
    	Color [] colorArray = new Color[9];
    	colorArray[0] = Color.DARK_GRAY; colorArray[1] = Color.RED; colorArray[2] = Color.GREEN; 
    	colorArray[3] = Color.BLUE; colorArray[4] = Color.CYAN;colorArray[5] = Color.YELLOW; 
    	colorArray[6] = Color.MAGENTA; colorArray[7] = Color.ORANGE; colorArray[8] = Color.PINK;
    	for(int i=0; i<labels.length;i++)
    	{
    		labelImage.setRGB(i%img.getWidth(), i/img.getWidth(), colorArray[labels[i]%colorArray.length].getRGB());
    	}
    	return labelImage;
    }
    
    public int [] CCA(int connectedness) //connectedness should be 4 or 8
    {
    	/* In here you need to implement the connected component analysis of the thresholded binary image
    	 * Here are the steps involved:
    	 * Get the pixel data into an array and create a new array to store the labels (done for you)
    	 * Loop through every pixel in the image 
    	 * 			When you find an unvisited node (value of 255 that you have not seen before - its label value is 0)
    	 * 			This is a new connected component so you must:
    	 *			Increment the currentLabel variable
    	 *  			Create a queue of integers and enqueue the above node 
    	 * 			While this queue is not empty 
    	 * 				dequeue the front, mark it as visited by assigning its label value to be equal currLabel
    	 * 				enqueue all adjacent nodes to the front that are unvisited and assign their label value to be equal currLabel
    	 * 			End While
    	 * End Loop
    	 */
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
    	
    	QueueArrayBased q = new QueueArrayBased(); // creating Queue that will store unvisited pixels
    	
    	
    	if(connectedness == 4){  // if connectedness is 4 then pixel will have up to 4 neighbors
	    	for(int i = 0; i < data.length; i++){ //for loop to traverse thru all pixels in an image
	    		
	    		if(data[i] == 0 && labels[i] == 0){  // finds first pixel that is white in an image
	    			
	    			currlabel++; //increase label by one, when another pixel will be found that was not checked, increment it again
	    			q.enqueue(i);// putting first white pixel in the queue for reference
	    			
	    			while(!q.isEmpty()){ //looping until queue is empty, usually it is empty when all white pixels are visited
	    				
	    				int front = q.dequeue(); // dequeue first pixel in the queue in "front" variable of type integer
	    				labels[front] = currlabel; // front value is the position in data array so labels[front] = currlabel; means that we put currlabel value in labels array where front is the position for labels array
	    				
	    				try{
		    				if(data[front-1] == 0 && labels[front-1] == 0){ // finding left neighbor and checking if it is white and if it was visited before or not
		    					try{
			    					q.enqueue(front-1); //if it was not visited then we put it in the queue
			    					labels[front-1] = currlabel; // and assign label to the left neighbor
			    				}catch(NullPointerException e){}
	    				}
	    				}catch(ArrayIndexOutOfBoundsException e){}
	    				
	    				try{
		    				if(data[front+1] == 0 && labels[front+1] == 0){//  finding right neighbor and checking if it is white and if it was visited before or not
		    					try{
			    					q.enqueue(front+1);//if it was not visited then we put it in the queue
			    					labels[front+1] = currlabel;// and assign label to the right neighbor
		    					}catch(NullPointerException e){}
		    				}
	    				}catch(ArrayIndexOutOfBoundsException e){
	    					
	    					
	    				}
	    				
	    				try{
		    				if(data[front-num_cols] == 0 && labels[front-num_cols] == 0){ // finding top neighbor and checking if it is white and if it was visited before or not
		    					try{
			    					q.enqueue(front-num_cols);//if it was not visited then we put it in the queue
			    					labels[front-num_cols] = currlabel;// and assign label to the top neighbor
		    					}catch(NullPointerException e){}
		    					
		    				}
	    				}catch(ArrayIndexOutOfBoundsException e){}
	    				
	    				
	    				try{
	    					if(data[front+num_cols] == 0 && labels[front+num_cols] == 0){ // finding bottom neighbor and checking if it is white and if it was visited before or not
		    					try{
		    					q.enqueue(front+num_cols);//if it was not visited then we put it in the queue
		    					labels[front+num_cols] = currlabel;// and assign label to the bottom neighbor
		    					}catch(NullPointerException e){}
	    					
	    					}
	    				}catch(ArrayIndexOutOfBoundsException e){}
	    				
	    			}
	    			
	    		}
	    		
	    	}
    	
    	}
    	
    	
    
    	
    	if(connectedness == 8){// if it is 8 connectedness then pixel will have up to 8 neighbors
        	//for loop to traverse thru all pixels in an image
    		
    		/** first 4 if statements are the same as before to find first 4 neighbors(left, right, top, bottom)
    		 *  using the same calculation
    		 */
    		
    	    	for(int i = 0; i < data.length; i++){
    	    		    	    		
    	    		if(data[i] == 255 && labels[i] == 0){
    	    			
    	    			currlabel++;
    	    			q.enqueue(i);
    	    			
    	    			while(!q.isEmpty()){
    	    				
    	    				int front = q.dequeue();
    	    				labels[front] = currlabel;
    	    				
    	    				try{
	    	    				if(data[front-1] == 255 && labels[front-1] == 0){
	    	    					try{
		    	    					q.enqueue(front-1);
		    	    					labels[front-1] = currlabel;
	    	    					}catch(NullPointerException e){}
	    	    				}
    	    				
    	    				}catch(ArrayIndexOutOfBoundsException e){}
    	    				
    	    				try{
	    	    				if(data[front+1] == 255 && labels[front+1] == 0){
	    	    					try{
		    	    					q.enqueue(front+1);
		    	    					labels[front+1] = currlabel;
	    	    					}catch(NullPointerException e){}	
	    	    				}
    	    				}catch(ArrayIndexOutOfBoundsException e){}
    	    				
    	    				try{
    		    				if(data[front-num_cols] == 255 && labels[front-num_cols] == 0){
    		    					try{
	    		    					q.enqueue(front-num_cols);
	    		    					labels[front-num_cols] = currlabel;
    		    					}catch(NullPointerException e){}
    		    				}
    	    				}catch(ArrayIndexOutOfBoundsException e){}
    	    				
    	    				
    	    				/*******************************************************************************************************
    	    				 * This 4 statements find another 4 neighbors that are (top-left, top-right, bottom-left, bottom-right)
    	    				 * 
    	    				 *****************************************************************************************************8*/
    	    				try{
    		    				if(data[front-num_cols+1] == 255 && labels[front-num_cols+1] == 0){
    		    					try{
	    		    					q.enqueue(front-num_cols+1);
	    		    					labels[front-num_cols+1] = currlabel;
    		    					}catch(NullPointerException e){}
    		    				}
    	    				}catch(ArrayIndexOutOfBoundsException e){}
    	    				
    	    				try{
    		    				if(data[front-num_cols-1] == 255 && labels[front-num_cols-1] == 0){
    		    					try{
	    		    					q.enqueue(front-num_cols-1);
	    		    					labels[front-num_cols-1] = currlabel;
    		    					}catch(NullPointerException e){}
    		    				}
    	    				}catch(ArrayIndexOutOfBoundsException e){}
    	    				
    	    				try{
    	    					if(data[front+num_cols] == 255 && labels[front+num_cols] == 0){
	    	    					try{
		    	    					q.enqueue(front+num_cols);
		    	    					labels[front+num_cols] = currlabel;
	    	    					}catch(NullPointerException e){}
    	    					
    	    					}
    	    				}catch(ArrayIndexOutOfBoundsException e){}
    	    				
    	    				try{
    	        				if(data[front+num_cols+1] == 255 && labels[front+num_cols+1] == 0){
    	        					try{
	    	        					q.enqueue(front+num_cols+1);
	    	        					labels[front+num_cols+1] = currlabel;
    	        					}catch(NullPointerException e){}
    	        					
    	        				}
    	        			}catch(ArrayIndexOutOfBoundsException e){}
    	    				
    	    				try{
    	        				if(data[front+num_cols-1] == 255 && labels[front+num_cols-1] == 0){
    	        					try{
	    	        					q.enqueue(front+num_cols-1);
	    	        					labels[front+num_cols-1] = currlabel;
    	        					}catch(NullPointerException e){}
    	        					
    	        				}
    	        			}catch(ArrayIndexOutOfBoundsException e){}
    	    				
    	    				
    	    				
    	    			}
    	    			
    	    		}
    	    		
    	    	}
        	
        	}
    	
    int num_Components = numComponents(labels);//After all pixels are found and labelled, label data is passed to numComponents() function to calculate how many seperate components there are
    values = populateTextArea(componentAreas(labels, num_Components)); // calculating each component areas before populating the TextField
    
	return labels;
	
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
    
    
    public int[] componentAreas(int [] labels,int numComponents)
    {
    	
    	int [] areas = new int[numComponents]; // declaring and initializing an area array same size as num of components
    	/*
	 * This method should take in the array of labels and return another array 
	 * that has the areas of the connected components in it.
	 */

    	for(int i = 0; i<areas.length;i++){ // for loop that loops same amount as number of components
    		for(int j = 0; j < labels.length;j++){ // for loop that loops same amount as labels size
    			if(labels[j] == i){			// if statement to check if j value is the same as i if yes then increment integer value in of area in position i
    				areas[i]++;	
    			}	
    		}	
    	}// all areas calculated
    	
			
    	return areas; //returning areas

    }
    
    
    public String getValues(){// helper function to return all values in string format
    	
    	return values;
    	
    }
    
    public String populateTextArea(int [] areas){// populating TextArea with all component areas
    	
    	String s = "";
    	int total = 0;
    	
    	for(int i = 1;i<areas.length;i++){
    		s +="Area for component " + i + ": " + areas[i] + "\n";
    		total += areas[i];// populating string with correct formatting and values
    	}
    	s +="TOTAL BACKGROUND PIXEL COUNT " + total;
    	
    	return s; //returning all areas as single string
    }
    

    public static void main(String[] args) {
    	
    	
    	ImageAnalysis analysis;
    	
    	System.out.println(System.getProperty("user.dir"));

        JFrame f = new JFrame("Image Analysis");
        
        JPanel imgPanel = new JPanel(); // panel that will show image in GUI
        JPanel labelPanel = new JPanel(); // panel that will be populated with results
        
        
       
        f.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        
        f.setLayout(new BorderLayout());
        
        imgPanel.add(analysis = new ImageAnalysis());
        TextArea areaField = new TextArea(analysis.getValues()); // after analysis is complete we populate this TextArea with image results
        areaField.setEditable(false); //to protect data being changed by accident
        labelPanel.add(areaField);
        
        f.add(imgPanel,BorderLayout.NORTH);
        f.add(labelPanel,BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }
}

