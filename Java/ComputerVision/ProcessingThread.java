import java.util.ArrayList;

public class ProcessingThread extends Thread{

	ArrayList<Command> commands;
	ApplicationController controller;
	boolean running = true;
	
	public ProcessingThread(ArrayList<Command> commands, ApplicationController controller) {
			
		this.commands = commands;
		this.controller = controller;
	}
	
    public void run() {
        
    	int imageIncrement = 0;
    	int commandSize = commands.size();
        
    	while(running) {
    		
    		controller.addImageForProcessing(imageIncrement%15);
    		
    		for(int i = 0; i < commandSize;i++) {
    			
    			commands.get(i).Execure(this);
    			
    		}
    		
    		imageIncrement++;
    		
    	}
    	
    	//enable buttons
    	controller.getView().setEnabledButtons(true);
        	
    }
    
    public void stopProcess() {
    	
    	running = false;
    	
    }
}

