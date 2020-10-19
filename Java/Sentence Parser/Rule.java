import java.util.ArrayList;

public class Rule {
	
	private int id;
	private POS name;
	private int transition;
	private Lexicon.Number number;

	public Lexicon.Number getNumber() {
		return number;
	}
	public void setNumber(Lexicon.Number number) {
		this.number = number;
	}
	public int getTransition() {
		return transition;
	}
	public void setTransition(int transition) {
		this.transition = transition;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public POS getName() {
		return name;
	}
	public void setName(POS name) {
		this.name = name;
	}
	
}
