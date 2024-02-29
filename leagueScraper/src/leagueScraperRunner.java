/* Author: Samuel Casto
 * This class is going to be a class with the sole purpose of downloading game logs
 * from a particular MFN league based on the four digits ID in the box score/download
 * URL. We will use this in conjuction with scraperRunner to download CSVs into our
 * parser folder to parse/create an excel spreadsheet. 
 * 
 * 
 * 1 This program will function by taking in inputs of a download path, MFN league homepage,
 * box score number of the first preseason game of the starting season, the number of seasons
 * to get, and the number of preseason weeks for the league
 * 2 Using the number of seasons to loop and the number of preseason weeks, we will determine
 * the amount of times to loop and preseason games to skip
 * 3 Using the league homepage and the box score number, we will build our download string then
 * proceed to download it and updating the file name to be numbers only after downloading to try
 * and get a unique name every time
 * 4 The box score number will be incremented and step 3 will be repeated until we have finished
 * looping through our number of seasons and each season
 * 5 The program will display a message to confirm to the user that it finished successfully
 * 
 * To Do:
 * 		Verify script runs as intended using JUnit tests
 * 		Verify script outputs files correctly by running it to collect most recent USFL season
 * 		Try to optimize the download time somehow
 * 		Make download code into a method since it is reused
 * 		
 */

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class leagueScraperRunner {

	private static int extraNum = 0;

	// method to add an extra number at the end of the fileName after it is
	// downloaded
	// so that we likely never have the same download name
	private static void increaseExtraNum() {
		extraNum++;
		if (extraNum > 20)
			extraNum = 0;
	}

	// method that takes the download url and download path to write the file we
	// want
	// to download into folder specified in the path along with adding a unique
	// number "token"
	// to make sure there are no duplicately named files
	private static void downloadUsingStream(String urlStr, String file) throws IOException {
		// finally fucking works but I want to make sure the names stay unique

		// name of the file after we remove everything except the last 4 characters and
		// adding our extraNum value to it
		String name = urlStr.substring(8,11) + urlStr.substring(urlStr.length() - 4)+ extraNum;
		// increasing extraNum
		increaseExtraNum();
		// adding our download path and file name together with necessary extras
		File namedCSV = new File(file + "/" + name + ".csv");
		// pretty sure this was for testing purposes and could be removed
		//System.out.println(namedCSV);
		// our download url becoming a url connection
		URL url = new URL(urlStr);
		// reading from the url connection
		InputStream in = url.openStream();
		// fileOutputStream to write a new file using the above name
		FileOutputStream fis = new FileOutputStream(namedCSV);
		byte[] buffer = new byte[1024];
		int count = 0;
		while ((count = in.read(buffer)) != -1) {
			fis.write(buffer, 0, count);
		}
		// closing our streams
		fis.close();
		in.close();
	
	}

	public static void main(String[] args) throws IOException {
		// This program will use a strings.txt file like other similar scripts to get
		// wanted info
		// Loading our txt file
		File textFile = new File("strings.txt");
		// Opening a scanner to read from the textFile
		Scanner scnr = null;
		try {
			scnr = new Scanner(textFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error opening strings.txt");
			System.exit(1);
		}
		// Scanner opened the file and now we need to store our variables
		String downloadToFile = scnr.nextLine();// first line is where to download logs to
		String leagueURL = scnr.nextLine();// second line is the league to scrape
		int boxScoreStart = Integer.parseInt(scnr.nextLine());// third line is the box score starting
		int loopValue = Integer.parseInt(scnr.nextLine());// fourth line is the number of seasons
		int numPreWeeks = Integer.parseInt(scnr.nextLine());// fifth line is num of preseason weeks
		
		/*
		 * System.out.println("We will download to this folder: " + downloadToFile);
		 * System.out.println("We will scrape this league: " + leagueURL);
		 * System.out.println("We will start from this box score number: " +
		 * boxScoreStart); System.out.println("We will loop this many times: " +
		 * loopValue);
		 * System.out.println("We will account for this many preseason weeks: " +
		 * numPreWeeks);
		 */
		
		//switch statement based on the number of preseason weeks 
		//the number of preseason weeks changes how many games to skip as well as
		//our verification statements 
		switch(numPreWeeks) {
		case 0:
			System.out.println("0 is an invalid number of preseason weeks.");
			System.exit(1);
			break;
		case 1:
			System.out.println("Are you sure there can be only 1 week of preseason?");
			System.exit(1);
			break;
		case 2:
			//Two weeks of preseason games means we need to skip 32 games then download the rest
			//of the season which is 269 games. So inner loop goes 32 + 269 times
			for(int i = 0; i < loopValue; i++) {
				//looping loopValue number of seasons
				for(int j = 0; j < 269+32; j++) {
					if(j > 31) {//WORKS
						//we know we are skipping the first 32 games 0-31
						//System.out.println("This is j: " + j + " and this is boxScore: " + boxScoreStart);
						
						// we have the right range of box scores and now need to build the string to
						// get to the download page and add our box score number to it
						// we take leagueURL and concat log/download/ and concat boxScoreStart
						String temp = "";
						temp = temp.concat(leagueURL).concat("log/download/").concat(String.valueOf(boxScoreStart));
						//System.out.println(temp);
						
						// temp now holds our url to access the download and we just need to invoke
						// our download method to save it to the specified folder from earlier
						downloadUsingStream(temp,downloadToFile);
					}
					boxScoreStart++;
				}//end of inner loop
			}//end of outer loop
			break;
		case 3:
			//Three weeks of preseason games means we need to skip 48 games then download the rest
			//of the season which is 269 games. So inner loop goes 48 + 269 times
			for(int i = 0; i < loopValue; i++) {
				//looping loopValue number of seasons
				for(int j = 0; j < 269+48; j++) {
					if(j > 47) { //WORKS
						//we know we are skipping the first 48 games 0-47
						//System.out.println("This is j: " + j + " and this is boxScore: " + boxScoreStart);
						
						// we have the right range of box scores and now need to build the string to
						// get to the download page and add our box score number to it
						// we take leagueURL and concat log/download/ and concat boxScoreStart
						String temp = "";
						temp = temp.concat(leagueURL).concat("log/download/").concat(String.valueOf(boxScoreStart));
						//System.out.println(temp);
						
						// temp now holds our url to access the download and we just need to invoke
						// our download method to save it to the specified folder from earlier
						downloadUsingStream(temp,downloadToFile);
					}
					boxScoreStart++;
				}//end of inner loop
			}//end of outer loop
			break;
			
		case 4:
			//Four weeks of preseason games means we need to skip 64 games then download the rest
			//of the season which is 269 games. So inner loop goes 64 + 269 times
			for(int i = 0; i < loopValue; i++) {
				//looping loopValue number of seasons
				for(int j = 0; j < 269+64; j++) {
					if(j > 63) { //WORKS
						//we know we are skipping the first 64 games 0-63
						//System.out.println("This is j: " + j + " and this is boxScore: " + boxScoreStart);
						
						// we have the right range of box scores and now need to build the string to
						// get to the download page and add our box score number to it
						// we take leagueURL and concat log/download/ and concat boxScoreStart
						String temp = "";
						temp = temp.concat(leagueURL).concat("log/download/").concat(String.valueOf(boxScoreStart));
						//System.out.println(temp);
						
						// temp now holds our url to access the download and we just need to invoke
						// our download method to save it to the specified folder from earlier
						downloadUsingStream(temp,downloadToFile);
					}
					boxScoreStart++;
				}//end of inner loop
			}//end of outer loop
			break;
		case 5:
			System.out.println("5 is an invalid number of preseason weeks.");
			System.exit(1);
			break;
		
		}//end of switch
		
		

		// System.out.println(boxScoreStart);
		System.out.println("Finished execution.");
	}
}

//Old Code

// we need to verify loopValue + 1 is divisible by 317 for this to work as
// intended
//if (((Double.valueOf(loopValue) + 1) / 317) % 1 != 0) {
//	System.out.println("Error: loopValue of " + loopValue + " is will not work as intended");
//	System.out.println("loopValue + 1 must be divisible by 317");
//	System.exit(1);
//}

//for (int i = 0; i <= loopValue; i++) {
//// we need to figure out the mathematical formula to remove preseason games
//// and use an if statement to skip those
//// we want to skip 48 games to start, then grab the next 269 that make up
//// the regular season and playoffs
//if ((i % 317) > 47) {
//	// we have the right range of box scores and now need to build the string to
//	// get to the download page and add our box score number to it
//	// we take https://xfl.myfootballnow.com/ and add log/download/boxScoreStart
//	String temp = "";
//	temp = temp.concat(leagueURL).concat("log/download/").concat(String.valueOf(boxScoreStart));
//	//System.out.println(temp);
//	// temp now holds our url to access the download and we just need to invoke
//	// our download method to save it to the specified folder from earlier
//	downloadUsingStream(temp,downloadToFile);
//}
//
//// increment at the end so that we get the championship game AND the first box
//// score wanted
//boxScoreStart++;
//}
