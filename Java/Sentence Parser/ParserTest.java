import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class ParserTest {
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter a sentence");
		String sentence = scanner.nextLine();
		scanner.close();
		
		Parser parser = new Parser();
		parser.checkSentence(sentence);
		
		
	}

}
