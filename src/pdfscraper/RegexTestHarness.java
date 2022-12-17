package pdfscraper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.princeton.cs.algs4.StdOut;

public class RegexTestHarness {

	public static void main(String[] args) {
		//String regex = "until";
		//String regex = "\\d{2}";
		//String regex = "until\\s*\\d{2}";	//"until 20"
		//String regex = "until\\s*(\\d+\\s*\\w+\\s*\\d+)";	//until 20 September 2010
		String input = "Article 1  By colluding on pricing and gross price increases in the EEA for medium and heavy trucks  and the timing and the passing on of costs for the introduction of emission technologies for  medium and heavy trucks required by EURO 3 to 6 standards, the following undertakings  infringed Article 101 TFEU and Article 53 of the EEA Agreement during the periods  indicated:   (a) MAN SE, from 17 January 1997 until 20 September 2010 MAN Truck  & Bus AG, from 17 January 1997 until 20 September 2010 MAN Truck  & Bus Deutschland GmbH, from 3 May 2004 until 20 September 2010   (b) AB Volvo (publ), from 17 January 1997 until 18 January 2011 Volvo  Lastvagnar AB, from 17 January 1997 until 18 January 2011 Volvo  Group Trucks Central Europe GmbH, from 20 January 2004 until 18  January 2011 Renault Trucks SAS, from 17 January 1997 until 18  January 2011  (c) Daimler AG, from 17 January 1997 until 18 January 2011  (d) Fiat Chrysler Automobiles N.V., from 17 January 1997 until 31  December 2010 CNH Industrial N.V., from 1 January 2011 until 18  January 2011 Iveco S.p.A., from 17 January 1997 until 18 January 2011  Iveco Magirus AG, from 26 June 2001 until 18 January 2011   (e) PACCAR Inc., from 17 January 1997 until 18 January 2011 DAF  Trucks N.V., from 17 January 1997 until 18 January 2011 DAF Trucks  Deutschland GmbH, from 20 January 2004 until 18 January 2011  ";
		//String nameInput = " MAN SE, ";
		//String firmName = "Volvo Lastvagnar AB";
		String firmName = "Daimler AG";
//		String nameRegex = "\\s*([\\w\\s]+)";
		String nameRegex = "\\s*(.*?)[,]";
		//	Matcher firmMatcher = getMatcher(nameInput, nameRegex);
//		String firmName = firmMatcher.group(1);
		String[] firmArray = firmName.split("\\s");
		String firmRegex2 = "";
		for (String word : firmArray) {
			firmRegex2 = firmRegex2 + word + "\\s*";
		}
//		String input = "MAN SE, from 17 January 1997 until 20 September 2010 ";
//		String regex = firmName + ".*?from(.*?)until\\s*(\\d+\\s*\\w+\\s*\\d+)";	//startDate, endDate
		String dateRegex = "\\d+\\s*\\w+\\s*\\d+";
//		String regex = firmName + ".*?from\\s*(" + dateRegex + ")\\s*until\\s*(" + dateRegex + ")";
		String regex = firmRegex2 + ".*?from\\s*(" + dateRegex + ")\\s*until\\s*(" + dateRegex + ")";

		String decGroup = "(\\d+\\.*\\d*)";
		String signIntGroup = "(-*\\+*\\d+)";
		char points = '…';
		input = input.replace(points, '.');
//		if (input.contains(points)) {
//			int x = input.indexOf(points);
//			input.r
//		}
		Matcher matcher = getMatcher(input, regex);
	//	StdOut.println("startDate: " + matcher.group(1));
	//	StdOut.println("endDate: " + matcher.group(2));
	}

	public static Matcher getMatcher(String input, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		boolean found = false;
		if (matcher.find()) {
			for(int i=0; i<matcher.groupCount()+1; i++) {
			StdOut.println("group " + i + ": " + matcher.group(i));
			}			
			found = true;
		}
		if(!found){
			StdOut.println("No match found.");
		}
		return matcher;
	}

}
