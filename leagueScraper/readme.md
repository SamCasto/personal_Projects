ReadMe for most recent leagueScraper build. 

Tool should validate and accept user input to download regular and postseason games from specified MyFootballNow. Accepts 
download path and validates folder exists and can be reached. Removes " from beginning and ending of download path, if present.
Accepts and validates league URL that it is compliant with RC 2396. Accepts box score starting number and validates that
it is actually a number and larger than 0.  Accepts number of seasons number and validates that it is a number and larger
than 0. Accepts number of preseason games and validates that the number is or between 2 and 4.

Current version does not validate that the numbers input are integers. Will be updated in the next version. 

Value definitions:
  Download Path - the system path to the folder that will hold the download files
  League URL - the URL for the specific MFN league to download season(s) worth of game logs from
  Box Score Starting Number - the first box score number of the starting season, should be from the first preseason game
  Number of Seasons - the number of seasons to download consecutively
  Number of Preseason Games - the number of games played in preseason so the scraper will not download them

Once start is clicked and all values are acceptable, scraper should run to download the specified game logs. There will
be a pop up message upon successfully starting and upon finishing. The current time I have noticed is roughly 10 minutes
to download each season but this may depend on your own setup. 

Current Roadmap:
  1. Update input validation to be more strict
  2. Add more ways for the tool to give the user feedback about errors and download status
  3. Optimize download class for better performance
