public class Lexicon{

	private String value;
	private POS partOfSpeech;
	private Number number;
	
	
	public POS getPartOfSpeech() {
		return partOfSpeech;
	}
	
	public void setPartOfSpeech(POS partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
	
	public Number getNumber() {
		return number;
	}
	
	public void setNumber(Number number) {
		this.number = number;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public boolean isMatching(String token) {
		
		return token.equals(value) ? true : false;
		
	}
	
	public enum Number {

		SINGULAR,
		PLURAL,
		BOTH
		
	}
	
}


