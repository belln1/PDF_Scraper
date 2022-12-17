package pdfscraper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import edu.princeton.cs.algs4.StdOut;

class TestRegex {
	static String decGroup = "(\\d+\\.*\\d*)";
	static String regDec = "\\d+\\.*\\d*"; 
	static String regexCont = "(^\\w*\\s*" + regDec + "%)";	//starts with 1 word or number, continues line



//	@Test
	void test1() {
		//split by %
		String input = "Foodstuffs, alcoholic and other drinks         ";
		String input1 = "Clothing and accessories Thailand  53% China  23%";
		String input2 = "Clothing and accessories Thailand  53% China  23% Czech Rep 5%  \r\n"; 
		String line2 =	"5% \r\n"; 
		String line3 =	"Taiwan  5% Turkey  4% Singapore  4% Others 6%  ";
		
	//	String regex = "\\s*(.*?)\\s(\\s*.*?\\s\\d*%*)\\s(\\s*)";
	//	Matcher matcher = getMatcher(input, regex);
	//	assertEquals("Foodstuffs, alcoholic and other drinks", matcher.group(1));

		if (input2.contains("%")) {
//			String regex = "(.*?)\\s\\s*(\\w+\\s*\\d+%\\s*.*)";  //normal line
			String regex = "(.*?)(\\d+)%";
			Matcher matcherIn = getMatcher(input2, regex);
			assertEquals("Clothing and accessories", matcherIn.group(1));
			String[] all = matcherIn.group(2).split("%");
			assertEquals(4, all.length);
		//	for(String str : all) {
				String regex2 = "\\s*(.*?)\\s+(\\d+)";
				Matcher matcher2 = getMatcher(all[0], regex2);
				assertEquals("Thailand", matcher2.group(1));
				assertEquals("53", matcher2.group(2));
				Matcher matcher3 = getMatcher(all[1], regex2);
				assertEquals("China", matcher3.group(1));
				assertEquals("23", matcher3.group(2));		
				Matcher matcher4 = getMatcher(all[2], regex2);
				assertEquals("Czech Rep", matcher4.group(1));
		}
		//String regex3 = "\\s*(\\w*\\s*\\d+%)";
		String regex3 = "(^\\w*\\s+\\d+%)";
		Pattern pattern = Pattern.compile(regex3);
		Matcher matcher3 = pattern.matcher(line3);
//		Matcher matcher3 = getMatcher(line3, regex3);
		assertTrue(matcher3.find(), "line3");
//		Matcher matcher4 = getMatcher(line2, regex3);
		Matcher matcher4 = pattern.matcher(line2);
		if(matcher4.find())assertEquals("5%", matcher4.group(1), "line2");
		Matcher matcher5 = pattern.matcher(input2);
		assertTrue(!matcher5.find(), "input2");
		
		
		
	}
//	@Test
	void test2() {
		String line3 =	"Taiwan  5% Turkey  4% Singapore  4% Others 6%  ";
		String regex3 = "(^\\w*\\s+\\d+%)";
		Pattern pattern = Pattern.compile(regex3);
		Matcher matcher3 = pattern.matcher(line3);
		assertTrue(matcher3.find(), "line3");
		StdOut.println(matcher3.group(1));

//		String input2 = "Clothing and accessories Thailand  53% China  23% Czech Rep 5%  \r\n"; 
//		String input2 = "CD (audio, games, software, etc.), DVD, cassettes. Thaïland  49% Malaysia  26% China  7% Hong Kong ";
		String regexReg = "(.*?)\\s\\s*(\\w+\\s*" + decGroup + "%\\s*.*)";  //normal line

		String input2 = "CD (audio, games, software, etc.), DVD, cassettes. Thailand  49% Malaysia  26% China  7% Hong Kong ";
		Pattern patternReg = Pattern.compile(regexReg);
		Matcher matcher2 = patternReg.matcher(input2);
		assertTrue(matcher2.find(), "input2");
		StdOut.println(matcher2.group(1));
		StdOut.println(matcher2.group(2));
		String inputCont = "Czech Rep  ";
		Matcher matcherNotReg = patternReg.matcher(inputCont);
		assertTrue(!matcherNotReg.find());
//		String inputCont2 = "Taiwan  5% Turkey  4% Singapore  4% Others 6%  ";
//		Matcher matcherNotReg2 = patternReg.matcher(inputCont2);
//		assertTrue(!matcherNotReg2.find()); => not working
		
		
		String input4 = "Toys and games China  33.5% Thailand ";
		Pattern pattern4 = Pattern.compile(regexReg);
		Matcher matcher4 = pattern4.matcher(input4);
		assertTrue(matcher4.find(), "input4");
		StdOut.println(matcher4.group(1));
		StdOut.println(matcher4.group(2));

		String regexContWord = "(^\\w+\\s*\\w*\\s*$)";	//starts and ends with 1 or 2 words 
		Pattern patternCont = Pattern.compile(regexContWord);
		Matcher matcherCont = patternCont.matcher(inputCont);
		assertTrue(matcherCont.find(), "inputCont");
		StdOut.println(matcherCont.group(1));
		
		Matcher matcherNotCont = patternCont.matcher(input4);
		assertTrue(!matcherNotCont.find());
	}
	
	@Test
	void test3() {
		String[] rownames = {"Foodstuffs, alcoholic and other drinks",
				"Perfumes and cosmetics" , 
				"Clothing and accessories" , 
				" a) Sportswear" , 
				" b) Other clothing (ready-to-wear,  )" , 
				" c) Clothing accessories (bags, sunglasses,  )" , 
				"Electrical equipment" , 
				"Computer equipment (computers, screens,  )" , 
				"CD (audio, games, software, etc.), DVD, cassettes" , 
				"Watches and jewellery" , 
				"Toys and games" , 
				"Other goods" , 
				"Cigarettes" , 
		"TOTAL"};
		String[] texts = {"nullFoodstuffs, alcoholic and other drinks 1 1% 37500 4% - Perfumes and cosmetics 2 1% 163 0% - Clothing and accessories 73 52% 263407 28% - a) Sportswear 32 44% 110563 12% - b) Other clothing (ready-to-wear,  ) 31 42% 115609 12% - c) Clothing accessories (bags, sunglasses,  ) 10 14% 37235 4% - Electrical equipment 1 1% 1957 0% - Computer equipment (computers, screens,  ) 3 2% 6316 1% - CD (audio, games, software, etc.), DVD, cassettes  7 5% 22645 2% - Watches and jewellery 2 1% 61 0% - Toys and games 10 7% 12541 1% - Other goods  40 28% 103833 11% - Cigarettes 2 1% 485925 52% - TOTAL  141 100% 934348 100% -   2 0 0 4 ", 
				"nullFoodstuffs, alcoholic and other drinks Not Com. 100%               Perfumes and cosmetics UAE 100%               Clothing and accessories China 42% Vietnam 18% Turkey 14% Bulgaria 10% Belarus 10% Thailand 3% Hong Kong 1% Others  2% a) Sportswear China  44% Belarus 16% Bulgaria 13% Vietnam 13% Turkey  6% Hong Kong 3% UAE  3% Others  2% b) Other clothing (ready-to-wear,  ) China  35% Turkey  23% Vietnam 23% Bulgaria 10% Belarus  6% Thailand 3%     c) Clothing accessories (bags, sunglasses,  ) China  60% Vietnam 20% Thailand 10% Turkey  10%         Electrical equipment China 100%               Computer equipment (computers, screens,  ) China  66% Vietnam 34%             CD (audio, games, software, etc.), DVD, cassettes  Not com. 100%               Watches and jewellery China 100%               Toys and games Hong Kong 50% China  40% Vietnam 10%           Other goods  Hong Kong 55% China  30% Vietnam 10% Bulgaria 3% Not com. 2%       Cigarettes Poland 50% Not Com. 50%             TOTAL  China  37% Hong Kong 20% Vietnam 13% Turkey  7% Bulgaria 6% Belarus  5% UAE 2% Others 10%   2 0 0 4 ",
				"nullFoodstuffs, alcoholic and other drinks Not Com. 100%               Perfumes and cosmetics Armani  100%               Clothing and accessories Puma  17% Nike   8% Adidas  7% Vuitton  6% T. Hilfiger 6% Diesel  4% GoreTex 4% Others 48% a) Sportswear Puma  25% Nike  19% Adidas  13% Joop!  6% GoreTex 6% Diesel  3% Boss  3% Others  25% b) Other clothing (ready-to-wear,  ) Hilfiger 10% Mercedes 10% CONS  10% Diesel  6% Cricket  6% YKK  6% Puma  3% Others  49% c) Clothing accessories (bags, sunglasses,  ) Puma  30% L. Vuitton 30% H.Boss  0% Ferrari  10% Nike  10% NBB  10%     Electrical equipment Vuitton 100%               Computer equipment (computers, screens,  ) Canon 100%               CD (audio, games, software, etc.), DVD, cassettes  IFPI/MPA. 100%               Watches and jewellery Breitling 50% Mercedes 50%             Toys and games Nintendo 60% EA sports 20% Catwoman 10% H.Potter 10%         Other goods  Nintendo 38% EA sports 20% Tweety  8% Ferrari  5% ZKL  5% Nike  2% Eastpak 2% Others 20% Cigarettes Polo  50% Not Com. 50%             TOTAL  Nintendo 15% Puma  7% EA sports 7% Nike  6% Adidas  4% Vuitton  3% Mercedes 3% Others 55% "};
		for (int i=0; i<rownames.length; i++) {
			rownames[i] = rownames[i].replace('(', ',');
			rownames[i] = rownames[i].replace(')', ',');
			rownames[i] = rownames[i].replace('.', ',');
		}
		for (int j=0; j<texts.length; j++) {
			texts[j] = texts[j].replace('(', ',');
			texts[j] = texts[j].replace(')', ',');
			texts[j] = texts[j].replace('.', ',');
		}
		String regex = rownames[2] + "\\s*(.*?)\\s*" + rownames[3] + "*";
	//	String regex1 = "Clothing and accessories\\s*(.*?)\\s* a) Sportswear*";
	
		StdOut.println(regex);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(texts[0]);
		assertTrue(matcher.find());


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
