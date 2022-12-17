package pdfscraper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Testclass {

	public static void main(String[] args) {
		String[] starters = {"THE COMMISSION OF", "HAS ADOPTED THIS DECISION", "article"};	
	//	String line = "The Commission of";
		String basestring = "The Commission of";
		String other = "COMMISSION";
//		System.out.println(containsIgnoreCase(starters, line));
//		System.out.println(containsIgnoreCase(basestring, other));
		String line = "(221) With regard to these aggravating factors, an ";
//		System.out.println(getParagraphNr("(123)"));
	//	System.out.println(getParagraphNr("456"));
	//	System.out.println(getParagraphNr(line, 456));
		
		String test1 = "CASE AT.39960 – Thermal Systems  ";
		String test2 = "COMP/E-2/37.784 FINE ART AUCTION HOUSES";
		String test3 = "(CaseCOMP/36.571/D-1: Austrianbanks — ‘LombardClub’)";
		String test4 = "(Case COMP.D.2 37.444 — SAS Maersk Air and Case COMP.D.2 37.386 — Sun-Air versus SAS and Maersk Air)";
		
		char[] arr1 = {'-', '–', '—'};
		char[] arr = {'(', ')'};
//		test1 = test3.replace(")", "");
		/*System.out.println(replaceArray(test3, arr, ' '));
		System.out.println(replaceArray(test1, arr1, ';'));
		System.out.println(replaceArray(test3, arr1, ';'));
		*/
		String bulletRegex = "[*–•]"; 
		String firmString = "t-Stevens- Woluwe, Belgium    – General Química SA, Zubillaga-Lantaron, Alava, Apartado 13, 09200  Miranda de Ebro, Spain    – Repsol Química SA, Paseo de la Castellana 278-280, 28046 Madrid,  Spain    – Repsol ";
		String firmString2 = "E-10300 Navalmoral de la Mata, Cáceres, Spain 4. Agroexpansión, S.A. (Agroexpansión), calle Suero de Quiñones, 42, E-28002 Madrid, Spain 5. Deltafina SpA (Deltafina), Via Gaetano Donizetti 10, I-00198 Roma, Italy 6. Dimon Incorporated (Dimon), ";
		String firmString3 = "(a) Barclays plc, 1 Churchill Place, London E14 5HP, United Kingdom  (b) Barclays Bank plc, 1 Churchill Place, London E14 5HP, United Kingdom  (c) Barclays Directors Limited, 1 Churchill Place, ";
		String firmString4 = " (1) Grupo Riberebro Integral S.L. Polígono Industrial La Llanada, 26540 Alfaro,  La Rioja, Spain  (2) Riberebro Integral S.A.U. Polígono Industrial La Llanada, 26540 Alfaro, La  Rioja, Spain.  ";
		//String [] firms = firmString.split(bulletRegex);
		//String [] firms = firmString2.split("\\d\\.");
		//String [] firms = firmString2.split("[*–•]|(\\d\\.)"); //match *, –, • or a digit with dot like 4.
		
		//String [] firms = firmString2.split("[\\(\\s][a-z]\\)"); //match (a) or a)
	
		//	String [] firms = firmString3.split("\\(?(\\d|[a-z])\\)"); //match (1) or 1)
		//String [] firms = firmString3.split("[*–•]|(\\d\\.)|(\\(?[a-z]\\))"); 
		
/*		String [] firms = firmString4.split("[*–•]|(\\d\\.)|([\\(\\s](\\d|[a-z])\\))");
		for (String firm : firms) {
			System.out.println(firm);
		}
*/		

		String test = "http://ec.europa.eu/competition/antitrust/cases/dec_docs/39600/39600_2147_3.pdf;";
		System.out.println(getFilename(test));


	
	}
	
	private static String getFilename(String urlString) {
		String delimiter = "/";
		String [] slashes = urlString.split(delimiter);
		return slashes[slashes.length-1];
	}
	private static String replaceArray(String text, char[] oldChar, char newChar) {
		for (char ch : oldChar) {
			text = text.replace(ch, newChar);
		}
		return text;
	}
	
	private static int getParagraphNr(String line, int oldPar) {
		int par = oldPar;
		Pattern pattern = Pattern.compile("[(](\\d+)[)]");
		Matcher matcher = pattern.matcher(line);
		if (matcher.find())
		{
			String paragNr = matcher.group(1);
			int p = Integer.parseInt(paragNr);
			if (p > par) par = p;
		}
		return par;
	}

	private static boolean containsIgnoreCase(String[] starters, String line) {
		if (line.toLowerCase().contains(starters[0].toLowerCase())) 
			return true;
		else return false;
}
	private static boolean containsIgnoreCase(String basestring, String other) {
		if (basestring.toLowerCase().contains(other.toLowerCase())) 
			return true;
		else return false;
	}

}
