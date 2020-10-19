
public class ThresholdCommand implements Command{

	private ApplicationController controller;
	private int index;
	
	public ThresholdCommand(ApplicationController controller, int index) {
		
		this.controller = controller;
		this.index = index;
		
	}
	
	@Override
	public void Execute() {
		
		controller.thresholdImage();
		
	}

	@Override
	public void Execure(ProcessingThread thread) {
	
		controller.thresholdImage();
		
	}
	
}
