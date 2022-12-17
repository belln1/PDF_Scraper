package pdfscraper;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;

class TestStringCleaner {
	static String PATH = "/Users/Nicole/switchdrive/R/Competition/";
//	static String filename = "cartelDecisions_1999_2017_headings_r.csv";
	static String filename = "test.csv";
//	static String outfile = "cartelDecisions_1999_2017_out.csv";
	static String outfile = "test_out.csv";
	static final String delimiter = ";";
	static final String dateRegex = "\\d+\\s*\\w+\\s*\\d+";	//20 January 2014
	static final String nameRegex = "\\s*([\\w].*?)[,]";


	In inFile;
	Out outFile;


	@BeforeEach
	void setUp() throws Exception {
		inFile = new In(PATH + filename);
		outFile = new Out(PATH + outfile);

	}
	
	@Test
	void testUtf8() {
		FileInputStream fis;
		String str;
		try {
			fis = new FileInputStream(PATH + filename);
			InputStreamReader isr = new InputStreamReader(fis, "UTF8");
			BufferedReader in = new BufferedReader(isr);
			while ((str = in.readLine()) != null) {
			    System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
				        
				      
			
//		String firmname = line[15];
	
	}
	
//	@Test
	void testFirstParagraph() {
		String [] headings = inFile.readLine().split(delimiter);
		String [] line = inFile.readLine().split(delimiter);
		int i = getIndexOf(headings, "FirstParagraph");
		String firstParagraph = line[i];
		assertTrue(i==3, "i not 3");
		String text = "Having regard to the Commission decision of ";
//		String regex = text + "(" + dateRegex + ")";
		String regex = text + "(" + dateRegex + ")" + nameRegex;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(firstParagraph);
		if (matcher.find()) {
			StdOut.println(matcher.group(1));
			StdOut.println(matcher.group(2));
		}

	}

//	@Test
	void testArticle1() {
		String [] headings = inFile.readLine().split(delimiter);
		String [] line = inFile.readLine().split(delimiter);
		int i = getIndexOf(headings, "Article1");
		String article1 = line[i];
		assertTrue(i==6, "i not 6");
		
		int j = getIndexOf(headings,"firmName");
		assertTrue(j>-1, "firmName not found");
		String firmNameInput = line[j];
		assertTrue(firmNameInput.contains("MAN SE"), "wrong firmName");

//		String nameRegex = "\\s*([\\w\\s]+)";


		Pattern pattern = Pattern.compile(nameRegex);
		Matcher firmMatcher = pattern.matcher(firmNameInput);
		if (firmMatcher.find()) {

		String firmName = firmMatcher.group(1);
		assertEquals(firmName, "MAN SE", "no Name match");
		String[] firmArray = firmName.split("\\s*");
		String firmRegex2 = "";
		for (String word : firmArray) {
			firmRegex2 = firmRegex2 + word + "\\s*";
		}
		
		 //dateRegex = "\\d+\\s*\\w+\\s*\\d+";
		String regex = firmRegex2 + ".*?from\\s*(" + dateRegex + ")\\s*until\\s*(" + dateRegex + ")";
		Pattern datePattern = Pattern.compile(regex);
		Matcher dateMatcher = datePattern.matcher(article1);

//		Matcher dateMatcher = getMatcher(article1, regex);
	if (dateMatcher.find()) {
		String startDate = dateMatcher.group(1);
		String endDate = dateMatcher.group(2);
		StdOut.println("endDate: " + endDate);
		
		assertEquals(startDate.substring(0, 2), "17", "startDate");
		assertEquals(endDate.substring(0, 2), "20", "endDate");
	}
		}
		

	}
	
	
	public static Matcher getMatcher(String input, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);

		boolean found = false;
		if (matcher.find()) {
			StdOut.println("I found the text \"" + matcher.group() + "\" starting at " +
					"index " + matcher.start() + " and ending at index " + matcher.end());
			StdOut.println("group 0: " + matcher.group(0));
			StdOut.println("group 1: " + matcher.group(1));
			found = true;
		}
		if(!found){
			StdOut.println("No match found.%n");
		}
		return matcher;
	}


	public int getIndexOf(String[] headings, String searchString) {
		for (int i=0; i< headings.length; i++) {
			if (headings[i].contains(searchString)) {
				return i;
			}
		}
		return -1;
	}

}
