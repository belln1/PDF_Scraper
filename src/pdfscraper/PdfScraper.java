package pdfscraper;

//Todo: select all urls which are not in order in new file, delete lines
//convert by acrobat pro
//redo for converted texts

//Done: split IV from firm


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

public class PdfScraper {
	//static String PATH = "/Users/Nicole/eclipse-workspace/PDF_Scraper/data/";
	static String PATH = "/Users/Nicole/eclipse/workspace/FirstTest/pdfscraper/";

	static String delimiter = ";";
	//String urlname = "http://eur-lex.europa.eu/legal-content/DE/TXT/PDF/?uri=CELEX:31972D0026&from=EN";
	//xx


	public static void main(String[] args) {
		//In inFile = new In(PATH + "pdfUrls.csv");
		//In inFile = new In(PATH + "pdfUrls_EN_1993_1998_cleaned.csv");
		In inFile = new In(PATH + "pdfUrls_EN_1964_1972_cleaned.csv");
		
		Out outFile = new Out(PATH + "cartelDecisions_new.csv");

		while (!inFile.isEmpty()) {
			String inLine = inFile.readLine();
			inLine = inLine.replace(" - ", ";"); //extract IV

			Queue outQueue = new Queue();
			outQueue.enqueue(inLine);	//year; IV; url incl. cenex

			//getUrlname
			String [] inCase = inLine.split(delimiter);
			String urlname = inCase[3];
			boolean english = false;
			if (urlname.contains("EN/TXT/PDF")) {english = true;}

			try {
				String pdfFilename = savePdf(urlname);
				File file = new File(pdfFilename);

				PDDocument doc = PDDocument.load(file);
				PDFTextStripper stripper = new PDFTextStripper();
				// stripper.setSortByPosition( true );
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

				String[] startersDe = {"DIE KOMMISSION DER", "HAT FOLGENDE ENTSCHEIDUNG ERLASSEN", "Artikel", ", den"};
				String[] endersDe = {"nachstehender"};
				String attachmentDe = "ANHANG";
				String[] startersEn = {"THE COMMISSION OF", "HAS ADOPTED THIS DECISION", "article", "Done "};
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

					for(String line : textLines) {
						line = line.replace(";", "");
						if (i==0) {	//the commission of
						if (startTag) {
						if (line.contains(starters[i])) {
								outQueue.enqueue(line);
								startTag = false;
								printing = true;
							}
						}
						else if (printing){
							outQueue.enqueue(line);
							if (containsIgnoreCase(line, enders[i])) {	//whereas
								outQueue.enqueue(";");
								i++;
								startTag = true;
								printing = false;
							}
							
						}
					}
						//hat folgende
						else if (i==1) {	//has adopted this decision
							//if line.contains(regex))
							paragNr = getParagraphNr(line, paragNr);

							if (containsIgnoreCase(line, starters[i])) {
								outQueue.enqueue(paragNr + ";");
								outQueue.enqueue(line);
								i++;
							}
						}
						//Artikel 1 bis n
						else if (i>=2) {
							if (containsIgnoreCase(line, starters[i] + " " + artNr)) {
								outQueue.enqueue(";");
								artNr++;
							}
								if(containsIgnoreCase(line, starters[3])) {
									outQueue.enqueue(";");
									outQueue.enqueue(line);
									break;
//								i++;									
								}
								outQueue.enqueue(line);
//								if(containsIgnoreCase(line, attachment)) {
//										break;
//								}
						}
						
					}

				//Diese Entscheidung ist an ... gerichtet
				//Br�ssel, den 30. Juli 1964
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
				e1.printStackTrace();
			}
		}


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
