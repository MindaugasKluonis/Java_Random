import java.awt.Image;

public class Food {
	
	private String name;
	private double price;
	private Image image;
	
	public Food(String name, double price, Image image) {
		super();
		this.name = name;
		this.price = price;
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

}
