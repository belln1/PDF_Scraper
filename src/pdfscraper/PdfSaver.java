package pdfscraper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class PdfSaver {
	static String PATH = "/Users/Nicole/eclipse-workspace/PDF_Scraper/data/";
	static String delimiter = ";";

	public static void main(String[] args) {
		In inFile = new In(PATH + "pdfsToClean.csv");
		while (!inFile.isEmpty()) {
			String inLine = inFile.readLine();

			String [] inCase = inLine.split(delimiter);
			String urlname = inCase[0];
			String outFile = getFilename(urlname);

			try {
				savePdf(urlname, outFile);
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	private static String getFilename(String urlString) {
		String delimiter = "/";
		String [] slashes = urlString.split(delimiter);
		return slashes[slashes.length-1];
	}
	
	private static void savePdf(String urlname, String outFile) throws MalformedURLException, IOException, FileNotFoundException {
		URL url = new URL(urlname);
		InputStream in = url.openStream();
		OutputStream fos = new FileOutputStream(PATH + outFile);
		int length = -1;
		byte[] buffer = new byte[1024];
		while((length = in.read(buffer)) > -1) {
			fos.write(buffer, 0, length);
		}
		System.out.println(outFile);
		fos.close();
		in.close();
	}

}
