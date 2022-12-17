package pdfscraper;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestPdfToCsvConverter {

	static String[] urlnames = {"http://ec.europa.eu/competition/antitrust/cases/dec_docs/39600/39600_2147_3.pdf",
			"http://ec.europa.eu/competition/antitrust/cases/dec_docs/39605/39605_2700_3.pdf",
			"http://ec.europa.eu/competition/antitrust/cases/dec_docs/38511/38511_1813_5.pdf",
	"http://ec.europa.eu/competition/antitrust/cases/dec_docs/36756/36756_74_4.pdf"}; 
	static String PATH = "/Users/Nicole/eclipse-workspace/PDF_Scraper/data/";


	
	//@Test
	void testgetFilename() {
		System.out.println(PdfToCsvConverter.getFilename(urlnames[0]));
		assertEquals("39600_2147_3.pdf", PdfToCsvConverter.getFilename(urlnames[0]));
	}
	//@Test
	void testRemoveFileExtension() {
		System.out.println("xx");
		assertEquals("39600_2147_3", PdfToCsvConverter.removeFileExtension("39600_2147_3.pdf"));
	}
	@Test
	void testExtractTextFromPdf() {
	//	String urlname = "http://ec.europa.eu/competition/antitrust/cases/dec_docs/37766/37766_610_5.pdf";
		String urlname = "http://ec.europa.eu/competition/antitrust/cases/dec_docs/39600/39600_2147_3.pdf";
		try {
			PdfToCsvConverter.savePdf(urlname, PATH, PdfToCsvConverter.getFilename(urlname));
			String text = PdfToCsvConverter.extractTextFromPdf(new File(PATH + PdfToCsvConverter.getFilename(urlname)));
			assert(text.contains("Brussel"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	@AfterEach
	void tearDown() throws Exception {
	}

}
