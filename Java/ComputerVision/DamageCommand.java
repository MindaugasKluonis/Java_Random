
public class DamageCommand implements Command{

	private ApplicationController controller;
	private int index;
	
	public DamageCommand(ApplicationController controller, int index) {
		
		this.controller = controller;
		this.index = index;
		
	}
	
	@Override
	public void Execute() {
		
		controller.damageCheck();
	}

	@Override
	public void Execure(ProcessingThread thread) {
		
		controller.damageCheck();
		
	}

}
