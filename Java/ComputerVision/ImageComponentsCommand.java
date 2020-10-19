
public class ImageComponentsCommand implements Command{

	private ApplicationController controller;
	private int index;
	
	public ImageComponentsCommand(ApplicationController controller, int index) {
		
		this.controller = controller;
		this.index = index;
		
	}
	
	@Override
	public void Execute() {
		
		controller.CCA();
		
	}

	@Override
	public void Execure(ProcessingThread thread) {
		
		controller.CCA();
		
	}
	
}
