public class DilateCommand implements Command{

	private ApplicationController controller;
	private int index;
	
	public DilateCommand(ApplicationController controller, int index) {
		
		this.controller = controller;
		this.index = index;
		
	}
	
	@Override
	public void Execute() {
		
		controller.dilateImage();
		
	}

	@Override
	public void Execure(ProcessingThread thread) {
		
		controller.dilateImage();
		
	}

}