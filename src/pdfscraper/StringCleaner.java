package pdfscraper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdOut;

public class StringCleaner {
	static String PATH = "/Users/Nicole/switchdrive/R/Competition/";
//	static String filename = "test.csv";

//	static String filename = "cartelDecisions_utf8.csv";
	static String filename = "cartelDecisions_1999_2017_headings_3r.csv";
	static String outfilename = "cartelDecisions_1999_2017_out.csv";
	static final String delimiter = ";";
	static final String dateRegex = "\\d+\\s*\\w+\\s*\\d+";
	static final String nameRegex = "\\s*([\\w].*?)[,.]";

	
//	static In inFile;
	static Out outFile;

	public static void main(String[] args) {
//		inFile = new In(PATH + filename);
		outFile = new Out(PATH + outfilename);

		FileInputStream fis;
		String str;
		try {
			fis = new FileInputStream(PATH + filename);
			InputStreamReader isr = new InputStreamReader(fis, "UTF8");
			BufferedReader in = new BufferedReader(isr);
			String headLine = in.readLine();
			String [] headings = headLine.split(delimiter);
			outFile.println(headLine + ";startDate" + ";endDate" + ";startDateCase" + ";reportRoute");
			int num = 0;
			while ((str = in.readLine()) != null) {
				num++;
			    
				if (num == 1134) {
					System.out.println(str);
				}
				String [] line = str.split(delimiter);
				for (String varOut : line) {
						//	System.out.print(varOut + ";");
					outFile.print(varOut + ";");
				}
				getArticle1(headings, line);
				getFirstParagraph(headings, line);
				outFile.println();				
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void getFirstParagraph(String[] headings, String[] line) {
		int k = getIndexOf(headings, "FirstParagraph");
		String startDateCase = "";
		String reportRoute = "";
		String firstParagraph = line[k];
		String text = "Having regard to the Commission decision of ";
//		String regex = text + "(" + dateRegex + ")";
		String regex = text + "(" + dateRegex + ")" + nameRegex;
		try {
			Pattern startPattern = Pattern.compile(regex);
			Matcher matcher = startPattern.matcher(firstParagraph);
			if (matcher.find()) {
				startDateCase = matcher.group(1);
				reportRoute = matcher.group(2);
				StdOut.println(startDateCase);
				StdOut.println(reportRoute);
			}
			else {
				startDateCase = "not found";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outFile.print(startDateCase + ";");
		outFile.print(reportRoute + ";");
	}


	public static void getArticle1(String[] headings, String[] line) {
		String startDate;
		String endDate;
		startDate = "";
		endDate = "";
		int i = getIndexOf(headings, "Article1");
		String article1 = line[i];

		int j = getIndexOf(headings,"firmName");
		String firmNameInput = line[j];

		try {
			Pattern pattern = Pattern.compile(nameRegex);
			Matcher firmMatcher = pattern.matcher(firmNameInput);
			if (firmMatcher.find()) {
				String firmName = firmMatcher.group(1);
				String[] firmArray = firmName.split("\\s");
				String firmRegex2 = "";
				for (String word : firmArray) {
					firmRegex2 = firmRegex2 + word + "\\s*";
				}

				String regex = firmRegex2 + ".*?from\\s*(" + dateRegex + ")\\s*until\\s*(" + dateRegex + ")";
				Pattern datePattern = Pattern.compile(regex);
				Matcher dateMatcher = datePattern.matcher(article1);
				if (dateMatcher.find()) {
					startDate = dateMatcher.group(1);
					endDate = dateMatcher.group(2);
				}
				else {
					startDate = "date not found";
					endDate =  "date not found";
				}
				
			}
			else {
				startDate = "firm not found";
				endDate =  "firm not found";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outFile.print(startDate + ";");
		outFile.print(endDate + ";");
	}


//	public static Matcher getMatcher(String input, String regex) {
//		Pattern pattern = Pattern.compile(regex);
//		Matcher matcher = pattern.matcher(input);
//
//		boolean found = false;
//		if (matcher.find()) {
//			StdOut.println("I found the text \"" + matcher.group() + "\" starting at " +
//					"index " + matcher.start() + " and ending at index " + matcher.end());
//			StdOut.println("group 0: " + matcher.group(0));
//			StdOut.println("group 1: " + matcher.group(1));
//			found = true;
//		}
//		if(!found){
//			StdOut.println("No match found.%n");
//		}
//		return matcher;
//	}


	public static int getIndexOf(String[] headings, String searchString) {
		for (int i=0; i< headings.length; i++) {
			if (headings[i].contains(searchString)) {
				return i;
			}
		}
		return -1;
	}

}
