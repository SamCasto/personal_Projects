
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/*
 * Author: Samuel Casto
 * Last update: 3/17 - Added comments to the file to improve readability. Have not used the script
 * 				in months. Judging by the folder, last time this program ran there were no issues.
 * 
 * 
 * Description: This code will take input from a file named strings.txt and use the input to 
 * go to myfootballnow league home page/landing page and download game logs and convert them 
 * into uniquely named csv files. The input should be broken into separate lines. 
 * The first line of the input should specify where to download/write the csv files to. The next
 * line(s) will specify which leagues to download game logs from. The program's current 
 * functionality is to only download the most recent games played which are accessed through 
 * the league home page/landing page. It will not download any other previously simulated games.
 * This makes it ideal if you want to consistently add new games as they are simulated. There
 * are no issues downloading game logs depending on game type(preseason/regular/playoffs.)
 * 
 * Intended use: 
 * 			1. Add the correct input to strings.txt where the first line is the intended 
 * 				download destination and each line after it is a unique league home page/landing
 * 				page from the website MyFootballNow.
 * 			2. Run the code or .exe file while your device is connected to the internet and 
 * 				wait until the confirmation message at the end is displayed showing that the 
 * 				program ran with no issues or errors.
 * 			3. Manually confirm that the files downloaded/written were done so successfully and 
 * 				can be opened by your csv processing application.
 * 			4. Do what you want with the downloaded csv files. 
 * 
 * Current ideas for improvement:
 * 	1. Add a way to handle downloading duplicates. As of 3/17 I do not remember the interaction
 * 			as I have not ran the program in some time so this might be a non-issue
 *  2. Add functionality that allows users to enter a box score number to start with and continue
 *  		X amount of times so that users can get previously simulated games/or a specific
 *  		subset of games played
 *  3. Look for runtime improvements particularly in the downloading method and the breakpoint
 *  		method. 
 *  4. Update the name to better reflect what it does.
 *  5. Add error handling in the event strings.txt does not get valid information.
 *  
 *  Requirements: 
 *  	JRE installed.(until exe is made)
 *  	Local file strings.txt with valid, relevant information inside it.
 *  	Leagues exist on MFN.
 *  	
 *  	
 */

public class scraperRunner{
	
	//Need to add and update final string's of what we are searching for whenever we can get them
	
	//not sure what I meant with the earlier comment but these regex statements will be used with
	//the patterns below to get to the webpage where we can download our wanted files
	private final static String BOX_LINK =  "https://[a-zA-Z-]+.myfootballnow.com/box/[0-9]{4}";
	private final static String DOWNLOAD_LINK = "https://[a-zA-Z-]+.myfootballnow.com/log/download/[0-9]{4}";
	private static Pattern boxPattern = Pattern.compile(BOX_LINK);
	private static Pattern downloadPattern = Pattern.compile(DOWNLOAD_LINK);
	//a Queue to hold our URLs that the program will get from the strings.txt file while the
	//ArrayLists exist to hold our visited URLs and which ones we want to download from
	private static Queue<String> leagueURL = new LinkedList<>();//holds the URLs we will visit
	private static ArrayList<String> visitedURL = new ArrayList<>();//holds our visited URLs
	private static ArrayList<String> downloadURLs = new ArrayList<>();//holds our URL download links
	private static int breakpoint = 10000;//arbitrary number that limits the amount of urls we visit
	private static int extraNum = 0;//number used to make our downloaded files unique
	
	
	//method to add an extra number at the end of the fileName after it is downloaded
	//so that we likely never have the same download name
	private static void increaseExtraNum() {
		extraNum++;
		if(extraNum > 20)
			extraNum = 0;
	}
	
	//method that does the actual crawling. Code taken from section.io. The article name is
	// how to build a web crawler with java
	private static int getBreakpoint(int breakpoint, Matcher box) {
	    while(box.find()){
	        String actualURL = box.group();

	        if(!visitedURL.contains(actualURL)){
	            visitedURL.add(actualURL);
	            leagueURL.add(actualURL);
	        }
	        
	        // exit the loop if it reaches the breakpoint.
	        if(breakpoint == 0){
	            break;
	        }
	        breakpoint--;
	    }
	    return breakpoint;
	}
	
	//method that takes the download url and download path to write the file we want 
	//to download into folder specified in the path along with adding a unique number "token"
	//to make sure there are no duplicately named files
	private static void downloadUsingStream(String urlStr, String file) throws IOException{
		//finally fucking works but I want to make sure the names stay unique
		
		//name of the file after we remove everything except the last 4 characters and
		//adding our extraNum value to it
		String name = urlStr.substring(urlStr.length()-5,urlStr.length()) + extraNum;
		//increasing extraNum
		increaseExtraNum();
		//adding our download path and file name together with necessary extras
		File namedCSV = new File(file + "/" + name + ".csv");
		//pretty sure this was for testing purposes and could be removed
		//System.out.println(namedCSV);
		//our download url becoming a url connection
        URL url = new URL(urlStr);   
        //reading from the url connection
        InputStream in = url.openStream();
        //fileOutputStream to write a new file using the above name
        FileOutputStream fis = new FileOutputStream(namedCSV);
        byte[] buffer = new byte[1024];
        int count=0;
        while((count = in.read(buffer)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        //closing our streams
        fis.close();
        in.close();
    }
	
	public static void main(String[] args) throws IOException {
		//starting over for a second with the meat of our program
		
		//first we need to read from the strings text file and get our download
		//folder path
		File textFile = new File("strings.txt");//file that holds our file download path and leagues to crawl 
		Scanner scnr = new Scanner(textFile);//for reading the file
		String downloadPath = scnr.nextLine();//holds our path to save the game logs in
		//adds all the leagues we are going to scrape from our strings file
		while(scnr.hasNextLine())
			leagueURL.add(scnr.nextLine());
		
		//we have our URLs for homepages to visit, and now we need to visit the pages,
		//look for our box score links, and then go to the box scores link to get
		//the download links
		while(!leagueURL.isEmpty()) {
			//removing our first league from the queue
			String temp = leagueURL.remove();
			String rawHTML = "";//holding the whole html file
			try {
				//getting our next url from the item from the queue
				URL url = new URL(temp);
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String inputLine = in.readLine();
				
				//reading every line from the webpage and adding it into rawHTML
				while(inputLine != null) {
					rawHTML += inputLine;
					inputLine = in.readLine();
				}
				//closing the connection
				in.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			//looking for our matches
			Matcher downloadMatcher = downloadPattern.matcher(rawHTML);
			Matcher boxMatcher = boxPattern.matcher(rawHTML);
			//boolean to see if we need to use breakpoint box/download
			boolean matchFound = downloadMatcher.find();
			if(matchFound) {
				String actualURL = downloadMatcher.group();
				downloadURLs.add(actualURL);	
				
				//System.out.println(actualURL);
				
				continue;
			}
			
			// Each time the regex matches a URL in the HTML,
	        // add it to the queue for the next traverse and the list of visited URLs.
	        breakpoint = getBreakpoint(breakpoint, boxMatcher);

	        // exit the outermost loop if it reaches the breakpoint.
	        if(breakpoint == 0)
	            break;
			
			
			
			
		}//end of the while loop
		
		
		//now downloadURLs has each url for game log to download so
		//we will call the method for each url
		downloadURLs.forEach((n) -> {
			try {
				downloadUsingStream(n,downloadPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		//closing our scanner
		scnr.close();
		
		
		//so we know we are finished parsing game logs
		System.out.println("Finished with no crashes");
	}
	
}