public class ErodeCommand implements Command{

	private ApplicationController controller;
	private int index;
	
	public ErodeCommand(ApplicationController controller, int index) {
		
		this.controller = controller;
		this.index = index;
		
	}
	
	@Override
	public void Execute() {
		
		controller.erodeImage();
		
	}

	@Override
	public void Execure(ProcessingThread thread) {
		
		controller.erodeImage();
		
	}

}