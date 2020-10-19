
public class RuleString{
	
	String value;
	POS type;
	Lexicon.Number number;
		
	public RuleString(String value, POS type, Lexicon.Number number) {
		
		this.value = value;
		this.type = type;
		this.number = number;
	}
	
	public RuleString() {
		
		value = "";

	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public POS getType() {
		return type;
	}
	public void setType(POS type) {
		this.type = type;
	}
	
	public Lexicon.Number getNumber() {
		return number;
	}

	public void setNumber(Lexicon.Number number) {
		this.number = number;
	}
	
	public String toRuleString() {
		
		return "[" + type.toString() + " " + value  + "]";
		
	}
	
	public void appendRuleString(String value) {
		
		this.value += value;
	}
	
}
