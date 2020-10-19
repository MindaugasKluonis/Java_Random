import java.util.ArrayList;

public class JoinedPhrase {
	
	ArrayList<Phrase> phrases;
	POS pos;
	
	public JoinedPhrase() {
		
		phrases = new ArrayList<Phrase>();
		
	}
	
public String toRuleString() {
		
		String result = pos + "[";
		
		for(int i = 0; i < phrases.size(); i++) {
			
			result +=  "[" + phrases.get(i).toRuleString() + "]";
			
		}
		
		result += "]";
		
		return result;
		
	}

}
