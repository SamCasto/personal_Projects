'''
Created on Jun 21, 2022

@author: Samuel Casto

Goal: Import MFN player data into sqlite3 tables quickly to sort through players
either a draft class, players to trade for, or free agents to sign

'''
import pandas as pd
import sqlite3 as sql


''' 6/23
    sqlite3 allows us to just quickly import everything into sql where we can
    easily manipulate the tables using select statements to find players where
    we want without having to launch another program. Long term we will want
    to find a way to view the data other than using the console, but that
    could also just be whenever the script is finished and turned into an exe
'''

''' 6/24
    With the ease of use for making a database, we can easily create several 
    databases/tables using user input to determine which one we are in. This
    change will change our main module into a looped statement that constantly
    runs until we are finished choosing a db/table, adding players, and then
    sorting through the players based on our predetermined player requirements.
    Currently rookie vol growth should be easy to track once we implement
    above changes by tracking the same players through Firstname, Lastname, and
    playerid.
'''

#module that use the user input to select players based on input
def command(n):
    #looking for Free agent WR's
    if(n == "FA-WR"):
        #execute select statement selecting players with correct attrib/weight
        cur.execute('''
            SELECT FirstName, Lastname, Pos, Max, MaxSpeedMax
            FROM testPlayers
            WHERE MaxSpeedMax > 75 AND Weight < 225 AND AccelerationMax > 40 AND 
            PassCatchingMax > 75 AND 'B&RAvoidanceMax' > 75 AND RouteRunningMax > 75 AND
            PassRecCourageMax > 75
            ORDER BY MaxSpeedMax desc
        ''')
        for row in cur.fetchall():
            print(row)
    #looking for free agent DB's
    elif(n == "FA-DB"):
        cur.execute('''
            Select FirstName, LastName, Pos, Max, MaxSpeedMax
            FROM testPlayers
            WHERE (Pos = "CB" OR Pos = "FS" OR Pos = "SS") AND MaxSpeedMax > 75 AND
            Weight < 225 AND 'B&RCoverageMax' > 75 AND 'M2MCoverageMax' > 75 AND
            AccelerationMax > 50
            ORDER BY MaxSpeedMax desc
            ''')
        for row in cur.fetchall():
            print(row)
    #looking for free agent OL
    elif(n == "FA-OL"):
        cur.execute('''
            SELECT FirstName, LastName, Pos, Max, StrengthMax
            From testPlayers
            WHERE (Pos = "LT" OR Pos = "RT" OR Pos = "C" OR
            Pos = "LG" OR Pos = "RG") AND StrengthMax > 70 AND MaxSpeedMax > 40 AND
            AccelerationMax > 40 AND RunBlockingMax > 40 AND PassBlockingMax > 80
            ORDER BY StrengthMax desc
            ''')
        for row in cur.fetchall():
            print(row)
    #looking for draftable players
    elif(n == "D-WR"):
        #STILL NEED TO UNDERSTAND VOL IMPACT TO GET CORRECT RATINGS
        print("We are in D-WR")
    elif(n == "D-DB"):
        #same as bove
        print("We are in D-DB")
    elif(n == "D-OL"):
        print("We are in D-OL")
    
        
    else:
        print("That is not a valid input")
        
#main part of the program
if __name__ == '__main__':
    #currently working thanks to the r for raw input
    rawData = pd.read_csv(r'C:\Users\samue\Documents\VSC Projects\Python\Players.csv',header=0)
    #!!!!!!!We should eventually change this to only use the downloads folder!!!!
    
    #we can store this data into a dataframe with hopefully
    #only the relevant columns selected EDIT: WE CAN!!!
    df = pd.DataFrame(rawData, columns= ['FirstName','LastName'])

    '''This is currently proof of concept that we could slice the data
    without using SQL. However, long term SQL will be much easier to use as we 
    will be able to use different tables to measure rookie classes vol growth. 
    Currently the next goal is to quickly import this into SQL tables either
    through this program or simply making the tables and accepting input for
    the table name'''
    
    #creating our database using sqlite3
    conn = sql.connect('test_db')
    #creating a cursor for the db
    cur = conn.cursor()
    
    #getting our column names for the sql server  !!!Dope this worked
    colNames = rawData.columns.values.tolist()
    #creating the test table for the db
    cur.execute('CREATE TABLE IF NOT EXISTS testPlayers(colNames)')
    
    #putting our data into the table
    rawData.to_sql('testPlayers', conn, if_exists='replace', index= False)
    
    command('D-WR')
    
    print('Completed')
    #committing before closing our connection to the db
    conn.commit()
    #closing our db connection
    conn.close()
    
    
    
    
    
    
    