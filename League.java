/*
 * <p> Title: CSC 230 Project 2: Electoral College Basketball League
 * 
 * <p> Description: This program is meant to take children who entered into a basketball league,
 * 					and add them into the league. If the child was 6, it would add them into the
 * 					league based off of the date of entry and if they were 7, it would add them
 * 					based off of their birthdates. However it would only add kids ages 6 and 7 and
 * 					it would add all the 6 year olds first and if there was space, it would add the 
 *  				7 year olds. It also must have done this with a runtime of O(nlogn).</p>
 *  
 *  <p> Due 30 November, 2016 11:59 pm</p>
 *  
 *  @author Steven Turner (N00836867@students.ncc.edu)
 *  
 *  
 * 
 * 
 * 
 * 
 */
import java.io.*;
public class League{
	// These Strings would hold all players that were either elligible but the league is full or inelligible
	//to enter the league
	private String leagueFull = "Elligible Players that were not added: \n"; 
	private String inelligible = "Inellgible Entries: \n";
	//The Priority Queue is the chosen data type for the league.
	private PriorityQueue league;
	private Child[] inelligibleEntries = new Child[100]; //Array of ineligible entries
	private int capacity = 48;
	// Constructor creates a new Priority Queue of size 48.
	public League(){
		
		league = new PriorityQueue(capacity);
	}
	//fillLeague is the method that will fill the league from the Entries.txt file.
	public void fillLeague(){
		try{
			BufferedReader br = new BufferedReader(new FileReader("Entries.txt"));
			String line; //A line from the Entries.txt file
			int i = 0;// Holds number value for ineligible kid
			while((line = br.readLine()) != null){ //While the BufferedReader is still reading lines
				Child newChild = new Child(line); // creates a new child from the line
				//Automatically checks if the child is eligible for the league and then adds them to the
				//list if they aren't
				if(newChild.isTooOld() || !newChild.isOldEnough()){
					inelligibleEntries[i] = newChild;
					i++;
				}
				//Inserts all elligible kids into the league
				else{
					league.insert(newChild);

				}
				}
			
			} catch(Exception ex){
				ex.printStackTrace();
			}
	}
	
	/*
	 * Prints Inelligible Entries with a two decimal format system.
	 */
	public void printInelligibleEntires(){
		System.out.printf("%s", inelligible);
		int i = 0;
		int count = 1;
		String str = "";
		//For every ineligible entry print it if it isn't null.
		for(i = 0; i < inelligibleEntries.length; i++){
			if(inelligibleEntries[i] != null){
			System.out.printf("%2d", count);
			str = ". " + inelligibleEntries[i].toString() + "\n";
			System.out.printf("%s", str);
			str ="";
			count++;
			}
			
		}
	}


	/*
	 * Prints league players with a two decimal format system.
	 * If league has more players than the initial capacity, it stops printing out entries after the 
	 * cut off
	 */
	public void printLeaguePlayers(){
		String str = "League Players \n";
		int count = 1; // Generic counting to make sure that you don't count pass capacity
		System.out.printf("%s", str);
		//if league is greater than initial capacity print up to capacity
		if(league.size() > capacity){
			while(count <= capacity){
			System.out.printf("%2d", count);
			str = ". ";
			str += league.delMin().toString();
			str += "\n";
			System.out.printf("%s", str);
			count++;
			}
		}
		else{
			league.printQueue();
		}
	}

/*
 * This combines the printInelligibleEntries and printLeaguePlayers methods. Then if there are any other
 * players in the league, it prints those players out in a cut off list
 */
	public void printFullLeague(){
		int count = 1; //Beginning number in a numbered list
		String str = "";
		printLeaguePlayers();
		printInelligibleEntires();
		System.out.printf("%s", leagueFull);
		while(!league.isEmpty()){
			System.out.printf("%2d", count); //Prints out the number in a 2 decimal format
			str = ". " + league.delMin().toString() + "\n";
			System.out.printf("%s", str); //Prints out child data
			count++;
			str = "";
			
		}
		
		
	}
	
	

	
	public static void main(String args[]){
		League EBB = new League();
		EBB.fillLeague();
		EBB.printFullLeague();
	}
	
}