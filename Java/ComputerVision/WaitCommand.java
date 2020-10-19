
public class WaitCommand implements Command{

	long timeToSleep;
	
	public WaitCommand(long value) {
		

		timeToSleep = value;
		
	}
	
	@Override
	public void Execute() {
		
	}

	@Override
	public void Execure(ProcessingThread thread) {
		
		try {
			ProcessingThread.sleep(timeToSleep);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
