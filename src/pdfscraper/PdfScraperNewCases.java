package pdfscraper;

//Todo: find IV, Celex, year
//split for all firms
//german: http://ec.europa.eu/competition/antitrust/cases/dec_docs/39452/39452_3336_8.pdf
//FR: http://ec.europa.eu/competition/antitrust/cases/dec_docs/36212/36212_471_5.pdf


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URI;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.Queue;

public class PdfScraperNewCases {
	static String PATH = "/Users/Nicole/eclipse-workspace/PDF_Scraper/data/expand2/";
	//	static String PATH = "/Users/Niki/a/eclipse/workspace/FirstTest/pdfscraper/";

	static String delimiter = ";";
	//String urlname = "http://eur-lex.europa.eu/legal-content/DE/TXT/PDF/?uri=CELEX:31972D0026&from=EN";


	public static void main(String[] args) {
//		In inFile = new In(PATH + "pdfUrls_1999_2017.csv");
		In inFile = new In(PATH + "pdfUrls_3.csv");

		Out outFile = new Out(PATH + "cartelDecisions_3.csv");

		while (!inFile.isEmpty()) {
			String urlname = inFile.readLine();
			outFile.print(urlname + ";");

			Queue outQueue = new Queue();
			//outQueue.enqueue(urlname);	//url incl. celex
			boolean english = true;

			try {
				String pdfFilename = savePdf(urlname);
				File file = new File(pdfFilename);

				PDDocument doc = PDDocument.load(file);
				PDFTextStripper stripper = new PDFTextStripper();
				stripper.setStartPage( 0 );
				stripper.setEndPage( doc.getNumberOfPages() );


				String text = stripper.getText(doc);
				//System.out.println(text.substring(0, 600));
				doc.close();


				//Extract content
				//Die Kommission.. xxx ... in Erwaegung nachstehender Gruende
				String[] textLines = text.split("\n");
				boolean startTag = true;
				boolean printing = false;

				String[] starters;
				String[] enders;
				String attachment;

				String[] startersDe = {"comp", "DIE KOMMISSION DER", "HAT FOLGENDE ENTSCHEIDUNG ERLASSEN", "Artikel", ", den"};
				String[] endersDe = {"nachstehender"};
				String attachmentDe = "ANHANG";
				String[] startersEn = {"comp", "THE COMMISSION OF", "HAS ADOPTED THIS DECISION", "article", "Done at"};
		//		String[] startersEn = {"AT.", "THE COMMISSION,", "HAS ADOPTED THIS DECISION", "article", "Done at"};
				String[] alternative = {"case", "THE EUROPEAN COMMISSION", "HAS DECIDED AS FOLLOWS"};
				String[] endersEn = {"WHEREAS"};			
				String attachmentEn = "footnotes";

				if (!english) {
					starters = startersDe;
					enders = endersDe;
					attachment = attachmentDe;
				}
				else {
					starters = startersEn;
					enders = endersEn;
					attachment = attachmentEn;

				}


				int i = 0;
				int paragNr = 0;
				int artNr = 1; //depends on right order of artikel in pdf
				char[] dashes = {'-', '–', '—'};
				char[] brackets = {'(', ')'};

				for(String line : textLines) {
					line = line.replace(";", "");
					if (i==0) {  //comp/case
						if (containsIgnoreCase(line, "competition")) {;} 
						else if (containsIgnoreCase(line, starters[i]) || containsIgnoreCase(line, alternative[i])) {
							line = replaceArray(line, dashes, ';');
							line = replaceArray(line, brackets, ' ');

							outQueue.enqueue(line);
							outQueue.enqueue(";");
							i++;
						}
					}
					if (i==1) {	//the commission of
						if (startTag) {
							if (line.contains(starters[i]) || line.contains(alternative[i])) {
								outQueue.enqueue(line);
								startTag = false;
								printing = true;
							}
						}
						else if (printing){
							outQueue.enqueue(line);
							if (containsIgnoreCase(line, enders[0])) {	//whereas
								outQueue.enqueue(";");
								i++;
								startTag = true;
								printing = false;
							}

						}
					}
					//hat folgende
					else if (i==2) {	//has adopted this decision
						//if line.contains(regex))
						paragNr = getParagraphNr(line, paragNr);

						if (containsIgnoreCase(line, starters[i])  || containsIgnoreCase(line, alternative[i])) {
							outQueue.enqueue(paragNr + ";");
							outQueue.enqueue(line);
							i++;
						}
					}
					//Artikel 1 bis n
					else if (i>=3) {
						if (containsIgnoreCase(line, starters[i] + " " + artNr)) {
							outQueue.enqueue(";");
							artNr++;
						}
						if(containsIgnoreCase(line, starters[4])) {
							outQueue.enqueue(";");
							outQueue.enqueue(line);
							break;
							//									i++;									
						}
						outQueue.enqueue(line);
					}

				}

				//Diese Entscheidung ist an ... gerichtet
				//Brüssel, den 30. Juli 1964
				//doc.close();
				String v = "";

				while (!outQueue.isEmpty()) {
					v = v + outQueue.dequeue().toString();
				}
				String endString = v.replace("\r", " ");
				outFile.print(endString + ";");
				System.out.print(endString + ";");
				outFile.println();
				System.out.println();

			} catch (Exception e1) {
				outFile.print(e1.toString());
				outFile.println();
				System.out.print(e1.toString());
				System.out.println();

			}
		}	//while


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

	private static boolean containsIgnoreCase(String basestring, String other) {
		if (basestring.toLowerCase().contains(other.toLowerCase())) 
			return true;
		else return false;
	}


	private static String savePdf(String urlname) throws MalformedURLException, IOException, FileNotFoundException {
		URL url = new URL(urlname);
		String filename = PATH + "output.pdf";
		InputStream in = url.openStream();
		OutputStream fos = new FileOutputStream(filename);
		int length = -1;
		byte[] buffer = new byte[1024];
		while((length = in.read(buffer)) > -1) {
			fos.write(buffer, 0, length);
		}
		fos.close();
		in.close();
		return filename;
	}
}
