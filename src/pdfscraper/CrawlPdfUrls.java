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

	public class CrawlPdfUrls {

		public static void main(String[] args) {
			//uncomment the above 2 lines and comment below 2 lines to use Firefox
			//System.setProperty("webdriver.firefox.marionette","C:\\geckodriver.exe");
			//WebDriver driver = new FirefoxDriver();
			System.setProperty("webdriver.chrome.driver","/Users/Nicole/eclipse-workspace/library/chromedriver/chromedriver.exe");
			WebDriver driver = new ChromeDriver();
			//** Base Urls 1964-1999: http://ec.europa.eu/competition/antitrust/cases/index.html
			//** Base Urls since 2001: http://ec.europa.eu/competition/cartels/cases/cases.html

			String filename  = "/Users/Nicole/eclipse-workspace/PDF_Scraper/data/pdfUrls.csv";
			Out out = new Out(filename);
			Queue<String> outQueue = new Queue<String>();


			// for all years..
			for (int i=1964; i<1999; i++) {
				String baseUrl = "http://ec.europa.eu/competition/antitrust/closed/en/" + i + ".html";
				String year = Integer.toString(i) ;
				//http://ec.europa.eu/competition/antitrust/closed/en/1964.html

				try {
					
					driver.get(baseUrl);
					List<WebElement> allLinks = driver.findElements(By.tagName("a"));
					Queue<String> linkQueue = new Queue<String>();

					for (WebElement link : allLinks) {
						if (link.getAttribute("href") != null) {
							if (link.getAttribute("href").indexOf("celexplus") >= 0) {
								linkQueue.enqueue(link.getAttribute("href"));
							}
						}
					}
					while (!linkQueue.isEmpty()) {
						String link = linkQueue.dequeue();	
						driver.get(link);
						WebElement title = driver.findElement(By.id("translatedTitle"));
						WebElement strong = title.findElement(By.tagName("strong"));
						String thisCase = strong.getText();
						Pattern pattern = Pattern.compile("[(](.*?)[)]");
						Matcher matcher = pattern.matcher(thisCase);
						if (matcher.find())
						{
							outQueue.enqueue(year);
							outQueue.enqueue(matcher.group(1));
						}
						WebElement linkLang = null;
						String decisionMarker = "";

						try {
							linkLang = driver.findElement(By.id("format_language_table_PDF_EN"));
							decisionMarker = "HAS ADOPTED THIS DECISION";
									
						} catch (Exception e1) {
							try {
								linkLang = driver.findElement(By.id("format_language_table_PDF_DE"));
								decisionMarker = "HAT FOLGENDE ENTSCHEIDUNG ERLASSEN";
							} catch (Exception e2) {
								outQueue.enqueue("*** PDF not found");
							}
						}
						if (linkLang != null) {
							String urlname = linkLang.getAttribute("href");
							outQueue.enqueue(urlname);  
						} 
						while (!outQueue.isEmpty()) {
							String v = outQueue.dequeue();
							out.print(v + ";");
							System.out.print(v + ";");
						}
						out.println();
						System.out.println();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			//close Fire fox
			driver.close();  
			out.close();
		}
	}