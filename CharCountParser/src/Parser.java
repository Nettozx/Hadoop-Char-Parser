import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Parser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		HashSet<String> uniqueDocs = new HashSet<String>();
		HashSet<String> uniqueTerms = new HashSet<String>();
		TreeMap<Integer,ArrayList<String>> wordMap = new TreeMap<Integer,ArrayList<String>>();
		TreeMap<String, Integer> matchMap = new TreeMap<String, Integer>();
		//Read an input file
		File file = new File("big_file.txt");
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			//While there is a line read it
			for(String line; (line = br.readLine()) != null; ) {
				int  docFrequency = 0; // keep track of document frequency for a word
				// Split the string into the word and a string of doc:freq seperated by a comma
				String[] s = line.split("\\s+"); //s[0] is the word s[1] is string
				uniqueTerms.add(s[0]);
				//create a string tokenizer to parse the doc:freq string 
				StringTokenizer st = new StringTokenizer(s[1], ",");
				while(st.hasMoreTokens()){
					docFrequency++;
					//Split the doc:freq on ":"
					String[] split =  st.nextToken().split(":");
					uniqueDocs.add(split[0]);
				}

				if(wordMap.containsKey(docFrequency)){
					wordMap.get(docFrequency).add(s[0]);
				}
				else{
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(s[0]);
					wordMap.put(docFrequency, temp);
				}
				//check if matches certain words
				if(s[0].matches("(zuzuf)|(fixed)|(rabbit)|(musicians)|(fences)")){
					matchMap.put(s[0], docFrequency);
				}

			}
			// line is not visible here.
		}
		catch(IOException e){
			System.out.println(e);
		}

		//Output
		System.out.println("Unique Documents: " + uniqueDocs.size());
		System.out.println("Unique Terms: " + uniqueTerms.size());
		int tracker = 0;
		System.out.println("\nTop 10 Document Frequency Words");
		for(Entry<Integer, ArrayList<String>> e : wordMap.descendingMap().entrySet()){
			if(tracker >= 10){
				break;
			}
			for(String s : e.getValue()){
				if(tracker >= 10){
					break;
				}
				System.out.println(e.getKey() + ":" + s);
				tracker++;
			}
		}
		tracker = 0;
		System.out.println("\nLowest 10 Document Frequency Words");
		for(Entry<Integer, ArrayList<String>> e : wordMap.entrySet()){
			if(tracker >= 10){
				break;
			}
			for(String s : e.getValue()){
				if(tracker >= 10){
					break;
				}
				System.out.println(e.getKey() + ":" + s);
				tracker++;
			}
		}
		
		System.out.println("\nDocument frequency of the following words: zuzuf, fixed, rabbit, musicians,fences");
		for(Entry<String, Integer> e : matchMap.entrySet()){
			System.out.println(e.getValue() + ":" + e.getKey());
		}
	}
}