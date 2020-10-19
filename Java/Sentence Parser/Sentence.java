import java.util.ArrayList;

public class Sentence {
	
	ArrayList<String> strings;
	
	public Sentence() {
		
		strings = new ArrayList<String>();
		
	}
	
	public String printSentence() {
		
		String result =  "S[";
		
		for(int i = 0; i < strings.size(); i++) {
			
			result +=    strings.get(i) ;
			
		}
		
		result += "]";
		
		return result;
		
	}

}
