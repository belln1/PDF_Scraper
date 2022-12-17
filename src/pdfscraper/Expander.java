package pdfscraper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

public class Expander {
	//static String PATH = "/Users/Nicole/eclipse-workspace/PDF_Scraper/data/";
	static String PATH = "/Users/Nicole/eclipse-workspace/PDF_Scraper/data/expand2/";
	//	static String PATH = "/Users/Nicole/switchdrive/DarkRates/";

	static final String delimiter = ";";
	static final String bulletRegex = "[*–•]|(\\d\\.)|([\\(\\s](\\d|[a-z])\\))";

	public static void main(String[] args) {
	//	String filename  = "cartelDecisions_1999_2017_toExpand_2_en.csv";
//		String filename  = "cartelDecisions_1999_2017_toExpand.csv";
		String filename = "test1nd.csv";
		In inFile = new In(PATH + filename);
		Out outFile = new Out(PATH + "cartelDecisions_1999_2017_Expanded_2_en.csv");

		//String beginString = "This Decision is addressed to";
		//String beginString = " Decision is addressed to";
		//String endString = "This Decision shall be";
//		String beginString = "Dieser Beschluss ist gerichtet an";
		String beginAlternative = "Diese Entscheidung ist";
//		String endString = "Dieser Beschluss";
		String endAlternative = "Diese Entscheidung ist ein";
//		String beginString = "Sono destinatarie della presente decisione";
//		String endString = "La presente decisione";
		//String beginString = "de la présente décision";
		//String endString = "La présente décision forme";
		String beginString = "beschikking is gericht tot";
		String endString = "Deze beschikking vormt";
		int i = 0; 
//		if (inFile.readLine().contains("Column")

		while (!inFile.isEmpty()) {
			String inLine = inFile.readLine();
			String [] vars = inLine.split(delimiter);
			int varNum = 0;
			int j = 0;
			String firmVariable = null;
			String firmVarStripped = null;
			String endVariable = null;
			for (String var : vars) {
				if (containsIgnoreCase(var, beginString) || containsIgnoreCase(var, beginAlternative)) {
					System.out.print(i + ": ");
					firmVariable = var;
					varNum = j;
				}
				j++;
			}
			//besser: schleife über array mit möglichen strings
			int beginIndex = indexOfIgnoreCase(firmVariable, beginString) + beginString.length();
			System.out.println("begin: " + beginIndex);
			if (beginIndex < 0)
			{
				beginIndex = indexOfIgnoreCase(firmVariable, beginAlternative) + beginAlternative.length();
			}


			int endIndex = indexOfIgnoreCase(firmVariable.substring(beginIndex), endString);
			if (endIndex < 0)
			{					
				System.out.println("end: " + endIndex);
				endIndex = indexOfIgnoreCase(firmVariable, endAlternative);
			}					
			firmVarStripped = firmVariable.substring(beginIndex, endIndex);
			//System.out.println(firmVariable);
			i++;
			String [] firms = firmVarStripped.split(bulletRegex);

			endVariable = firmVariable.substring(endIndex);
			String dateVar = vars[varNum + 1];
			vars[varNum + 1] = endVariable;


			for (String firm : firms) {
				//System.out.println(firm);
				// if firm contains more than one character a-z

				if (containsRegex(firm, "[a-z]|[A-Z]")) {
					vars[varNum] = firm;
					for (String varOut : vars) {
						System.out.print(varOut + ";");
						outFile.print(varOut + ";");
					}
					System.out.print(dateVar + ";");
					outFile.print(dateVar + ";");
					outFile.println();
				}


			}



		}



	}
	private static boolean containsRegex(String baseString, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(baseString);
		if (matcher.find())
		{
			return true;
		}
		else return false;
	}

	private static int indexOfIgnoreCase(String basestring, String other) {
		return basestring.toLowerCase().indexOf(other.toLowerCase()); 
	}
	private static boolean containsIgnoreCase(String basestring, String other) {
		if (basestring.toLowerCase().contains(other.toLowerCase())) 
			return true;
		else return false;
	}

}
