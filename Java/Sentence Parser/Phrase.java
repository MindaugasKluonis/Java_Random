import java.util.ArrayList;

public class Phrase {
	
	POS phrasePOS;
	ArrayList<RuleString> values;
	Lexicon.Number number;
	
	public Phrase() {
		
		values = new ArrayList<RuleString>();
		
	}
	
	public void addValue(RuleString value) {
		
		values.add(value);
		
	}
	
	public ArrayList<RuleString> getPhrase() {
		
		return values;
		
	}
	
	public void setPOS(POS pos) {
		
		phrasePOS = pos;
		
	}
	
	public String toRuleString() {
		
		String result = phrasePOS + "[";
		
		for(int i = 0; i < values.size(); i++) {
			
			result +=  "[" + values.get(i).getType().toString() + " " + values.get(i).getValue()  + "]";
			
		}
		
		result += "]";
		
		return result;
		
	}

}
