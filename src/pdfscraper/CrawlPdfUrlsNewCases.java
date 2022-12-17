package pdfscraper;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
//comment the above line and uncomment below line to use Chrome
import org.openqa.selenium.chrome.ChromeDriver;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.Queue;

public class CrawlPdfUrlsNewCases {
	public static void main(String[] args) {
		//uncomment the above 2 lines and comment below 2 lines to use Firefox
		//System.setProperty("webdriver.firefox.marionette","C:\\geckodriver.exe");
		//WebDriver driver = new FirefoxDriver();
		System.setProperty("webdriver.chrome.driver","/Users/Nicole/eclipse-workspace/library/chromedriver/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		//** Base Urls 1964-1999: http://ec.europa.eu/competition/antitrust/cases/index.html
		//** e.g.: http://ec.europa.eu/competition/antitrust/closed/en/1964.html

		//** Base Urls since 2001: http://ec.europa.eu/competition/cartels/cases/cases.html

		String filename  = "/Users/Nicole/eclipse-workspace/PDF_Scraper/data/pdfUrls_1999_2017.csv";
		Out out = new Out(filename);
		Queue<String> outQueue = new Queue<String>();


		// for all years..
		String baseUrl = "http://ec.europa.eu/competition/cartels/cases/cases.html";
		String year = "" ;

		try {

			driver.get(baseUrl);
			List<WebElement> allLinks = driver.findElements(By.tagName("a"));
			Queue<String> linkQueue = new Queue<String>();

			for (WebElement link : allLinks) {
				if (link.getAttribute("href") != null) {
					if (link.getAttribute("href").indexOf("dec_docs") >= 0) {
						out.println(link.getAttribute("href"));
						System.out.println(link.getAttribute("href"));
					}
					else if(link.getText().contains("Publication of the decision")) {
						linkQueue.enqueue(link.getAttribute("href"));
						System.out.println(link.getAttribute("href"));
					}
				}
			}
			while (!linkQueue.isEmpty()) {
				String link = linkQueue.dequeue();	
				driver.get(link);
				WebElement pdfLink = driver.findElement(By.id("format_language_table_PDF_EN"));
				out.println(pdfLink.getAttribute("href"));
				System.out.println(pdfLink.getAttribute("href"));
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//close Fire fox
		driver.close();  
		out.close();
	}
}
