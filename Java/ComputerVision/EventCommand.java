
public class EventCommand implements Command{

	private ApplicationController controller;
	private int index;
	
	public EventCommand(ApplicationController controller, int index) {
		
		this.controller = controller;
		this.index = index;
		
	}
	
	@Override
	public void Execute() {
		
		controller.addImageForProcessing(index);
		controller.hideComponentLabel();
		
	}

	@Override
	public void Execure(ProcessingThread thread) {
		
		controller.addImageForProcessing(index);
		controller.hideComponentLabel();
		
	}

}
