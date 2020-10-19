
public class HistogramCommand implements Command{
	
	private ApplicationController controller;
	private int index;
	
	public HistogramCommand(ApplicationController controller, int index) {
		
		this.controller = controller;
		this.index = index;
		
	}
	
	@Override
	public void Execute() {
		
		controller.showHistogram();
		
	}

	@Override
	public void Execure(ProcessingThread thread) {

		controller.showHistogram();		
	}

}
