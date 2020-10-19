import java.util.ArrayList;

public class OmnivoreFactory implements AbstractFoodFactory{

	@Override
	public ArrayList<Food> getSides() {
		// TODO Auto-generated method stub
		return createSideDishes();
	}

	@Override
	public ArrayList<Food> getMains() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private ArrayList<Food> createSideDishes() {
		// TODO Auto-generated method stub
		
		ArrayList<OmnivoreFood> sides = new ArrayList<OmnivoreFood>();
		
		sides.add(new OmnivoreFood("Hamburger",1.50,))
		
		return null;
	}
	
	

	
	

}
