package pdfscraper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

import edu.princeton.cs.algs4.Out;

//import edu.princeton.cs.algs4.In;

public class PdfToCsvConverter {
	//static String PATH = "/Users/Nicole/eclipse-workspace/PDF_Scraper/data/expand2/";
	static String PATH = "/Users/Nicole/eclipse-workspace/PropertyRights_Scraper/data/";
	
	public static void main(String[] args) {
		String[] urlnames = {
				"http://ec.europa.eu/taxation_customs/sites/taxation/files/resources/documents/customs/customs_controls/counterfeit_piracy/statistics/2014_ipr_statistics_en.pdf",
				"http://ec.europa.eu/taxation_customs/sites/taxation/files/resources/documents/customs/customs_controls/counterfeit_piracy/statistics/2015_ipr_statistics.pdf"
//				"http://ec.europa.eu/competition/antitrust/cases/dec_docs/40018/40018_2611_3.pdf",
//				"http://ec.europa.eu/competition/antitrust/cases/dec_docs/40018/40018_2611_3.pdf",
//				"http://ec.europa.eu/competition/antitrust/cases/dec_docs/39780/39780_3528_6.pdf",
//				"http://ec.europa.eu/competition/antitrust/cases/dec_docs/39914/39914_8648_3.pdf",
//				"http://ec.europa.eu/competition/antitrust/cases/dec_docs/39092/39092_7226_4.pdf"
				}; 
		for (String urlname : urlnames) {
		try {
			String filename = getFilename(urlname);

			savePdf(urlname, PATH, filename);
			File file = new File(PATH + filename);
			
			String text = extractTextFromPdf(file);

			//save filename.txt;
			Out outFile = new Out(PATH + removeFileExtension(filename) + ".txt");
			outFile.print(text);

			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		}
	}
	public static String extractTextFromPdf(File file) throws InvalidPasswordException, IOException {
		PDDocument doc = PDDocument.load(file);
		PDFTextStripper pdfStripper = new PDFTextStripper();
		String text = pdfStripper.getText(doc);
		doc.close();
		return text;
	}
	public static String getFilename(String urlString) {
		String delimiter = "/";
		String [] slashes = urlString.split(delimiter);
		return slashes[slashes.length-1];	
	}
	public static String removeFileExtension(String filename) {
		return filename.split("\\.")[0];
	}


	public static void savePdf(String urlname, String path, String filename) throws MalformedURLException, IOException, FileNotFoundException {
		URL url = new URL(urlname);
		InputStream in = url.openStream();
		OutputStream fos = new FileOutputStream(path + filename);
		int length = -1;
		byte[] buffer = new byte[1024];
		while((length = in.read(buffer)) > -1) {
			fos.write(buffer, 0, length);
		}
		fos.close();
		in.close();
	}
}
