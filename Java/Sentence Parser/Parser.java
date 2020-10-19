import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Parser {
	
	Gson jParser = new Gson();
	ArrayList<Lexicon> lexicons = new ArrayList<Lexicon>();
	ArrayList<Rule> rules = new ArrayList<Rule>();
	ArrayList<RuleString> tokens = new ArrayList<RuleString>();
	ArrayList<Phrase> combinedPhrases = new ArrayList<Phrase>();
	
	public Parser() {
		
		JsonReader reader;
		
		try {
			reader = new JsonReader(new FileReader("src/json/lexicons.json"));
			lexicons = jParser.fromJson(reader, new TypeToken<ArrayList<Lexicon>>(){}.getType());
			reader = new JsonReader(new FileReader("src/json/rules.json"));
			rules = jParser.fromJson(reader, new TypeToken<ArrayList<Rule>>(){}.getType());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void checkSentence(String sentence) {
		
		String words = sentence.toLowerCase();
		String[] tokens= words.split("\\s");
		
		if(runLexiconMatch(tokens)){ //success
			
			System.out.println("All entries match with lexicon entries");
			extractPhrases(this.tokens);
			
		}
		
	}
	
	private void extractPhrases(ArrayList<RuleString> tokens) {
		
		ArrayList<Phrase> phrases = new ArrayList<Phrase>();
		Phrase currentPhrase = new Phrase();
		
		for(int i = 0; i < tokens.size(); i++) {
			
			if(findCorrectRule(i, phrases, currentPhrase)) { // need new phrase
				
				phrases.add(currentPhrase);
				currentPhrase = new Phrase();
				
			}
		}
		
		for(int j = 0; j < phrases.size(); j++) {
			
			System.out.println(phrases.get(j).toRuleString());
			if(!checkPhraseNumber(phrases.get(j))) {
				
				System.out.println("Sentence did not match with lexicon entries");//fail
				return;
			}
		}
		
		if(!arePhrasesCorrect(phrases)){
			
			System.out.println("Sentence did not match with lexicon entries2");
			return;
		}
		
		
		Sentence sentence = new Sentence();
		sentence.strings.add(phrases.get(0).toRuleString());
		JoinedPhrase jPhrase = CombinePhrases(phrases);
		sentence.strings.add(jPhrase.toRuleString());
		System.out.println(sentence.printSentence());
		
	}

	private boolean arePhrasesCorrect(ArrayList<Phrase> phrases) {
		
		Queue<Phrase> currentPhrases = new LinkedList<Phrase>();
		currentPhrases.addAll(phrases);
		Lexicon.Number number = currentPhrases.poll().number;
		
		while(!currentPhrases.isEmpty()) {
			
			
			if(number != currentPhrases.peek().number) {
				return false;
			}
			
			if(currentPhrases.peek().phrasePOS == POS.VP) {
				return true;
			}
			number = currentPhrases.poll().number;	
		}
		
		return true;
		
	}

	private JoinedPhrase CombinePhrases(ArrayList<Phrase> phrases) {
		
		Phrase current;
		JoinedPhrase newPhrase = null;

		for(int i = 0; i < phrases.size(); i++) {
			
			current = phrases.get(i);
			
			//phrase in rule file can be looked up with -2 instead of -1, to separate phrase rules from lexicon rules
			for(int j = 0; j < rules.size(); j++) {
				
				Rule currentRule = rules.get(j);
				
				if(current.phrasePOS == currentRule.getName()) {
					
					if(currentRule.getTransition() != -1 && currentRule.getTransition() != -2) {
						
						System.out.println("Found rule" + " " + j + " " + rules.get(j).getTransition());
						if(i != phrases.size() - 1) {
							
							System.out.println("there is another token");//just need to check if transition matches
							if(rules.get(currentRule.getTransition()).getName() == phrases.get(i + 1).phrasePOS) {
								
								newPhrase = new JoinedPhrase();
								newPhrase.pos = current.phrasePOS;
								newPhrase.phrases.add(current);
								newPhrase.phrases.add(phrases.get(i + 1));
								
							}
							
						}
						
					}
					
				}
				
			}
		
		}		
		
		return newPhrase;
		
	}
	

	private boolean findCorrectRule(int index, ArrayList<Phrase> phrases, Phrase currentPhrase) {
		
		RuleString current = tokens.get(index);
		
		for(int i = 0; i < rules.size(); i++) {
				
			Rule currentRule = rules.get(i);
			
			if(isTypeMatch(currentRule, current)) {
				
				
				//if next transition is last
				if(isNextTransitionLast(currentRule)){
					
					currentPhrase.setPOS(rules.get(currentRule.getTransition()).getName());
					currentPhrase.addValue(current);
					currentPhrase.number = rules.get(currentRule.getTransition()).getNumber();
					
					return true;
					
				}
				
				//if not
				//check if there is any words later
				if(index != tokens.size() - 1) {
					
					
					if(isNextTransitionMatch(currentRule, index)) {
						
						currentPhrase.setPOS(rules.get(currentRule.getTransition()).getName());
						currentPhrase.addValue(current);
						
						
						return false;
						
					}
				
				}
				
				else if(index == tokens.size() - 1 && rules.get(currentRule.getTransition()).getTransition() != -1) {//last word and transition is not final!!
					
					System.out.print("Wrong sentence structure");
					System.exit(0);
					
				}
			}
		}
		
		return true;
	}
	
	
	/**
	 * Checks if phrase words have same number value(BOTH is ignored)
	 * If it is not then phrase is incorrect(a men)
	 * @param phrase
	 * @return
	 */
	private boolean checkPhraseNumber(Phrase phrase) {
		
		Lexicon.Number number = phrase.number;
		
		for(int i = 0; i < phrase.values.size(); i++) {
			
			if(phrase.values.get(i).number != number && phrase.values.get(i).number != Lexicon.Number.BOTH) {
				
				return false;
				
			}
			
		}
		
		return true;
		
	}

	/** 
	 * Runs lexicon match with entered sentence and adds them to StringRule objects
	 * If there is a token that does not exist in lexicon table returns false(terminates program)
	 *  
	 * @param tokens - tokenized sentence
	 * @return boolean - sentence match with lexicon entries? true : false
	 */
	private boolean runLexiconMatch(String[] tokens) {
		
		for (int i = 0; i<tokens.length; i++) {
			
			for(int k = 0; k<lexicons.size(); k++) {
				
				if(lexicons.get(k).getValue().equals(tokens[i])) {
					
					this.tokens.add(new RuleString(tokens[i],lexicons.get(k).getPartOfSpeech(),lexicons.get(k).getNumber()));
				}
				
			}
			
		}
		
		if(this.tokens.size() == tokens.length) {
			
			return true;
		}
		
		return false;
		
	}
	
	public boolean isTypeMatch(Rule rule, RuleString string) {
		
		return rule.getName() == string.getType();
		
	}
	
	public boolean isNumberMatch(Rule rule, RuleString string) {
		
		return (rule.getNumber() == string.getNumber()) || (rule.getNumber() == Lexicon.Number.BOTH);
		
	}
	
	public boolean isNextTransitionLast(Rule rule) {
		
		return rules.get(rule.getTransition()).getTransition() == -1;
		
	}
	
	public boolean isNextTransitionMatch(Rule rule, int index) {
		
		
		return rules.get(rule.getTransition()).getName() == tokens.get(index + 1).getType();
		
	}
	
	public boolean isNextTransitionNumberMatch(Rule rule, int index) {
		
		return (rules.get(rule.getTransition()).getNumber() == tokens.get(index + 1).getNumber()) || (rule.getNumber() == Lexicon.Number.BOTH);
	}
	
	public boolean isTypeAndNumberMatch(Rule rule, RuleString string) {
		
		return rule.getName() == string.getType() && (rule.getNumber() == string.getNumber() || rule.getNumber() == Lexicon.Number.BOTH);
		
	}

}
