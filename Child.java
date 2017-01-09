import java.util.Calendar;
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
*/
public class Child {
	/* 
	 * The below variables are created to either hold information about a Child or to use in 
	 * order to decide if a child is too young or too old.
	 */
	private String Firstname;
	private String Lastname;
	private int dobday;
	private int dobmonth;
	private int dobyear;
	private int doeday;
	private int doemonth;
	private int doeyear;
	private long cutOff = 189388800022L;
	private long mtooOld = 252460799988L;
	Calendar dob = Calendar.getInstance();
	Calendar finDate = Calendar.getInstance();
	Calendar doe = Calendar.getInstance();
	
	/*
	 * Child Constructor takes a String called Entry. 
	 */
	public Child(String Entry){
		String Entrysplit[] = Entry.split(" ");//Splits entry by space first
		Firstname = Entrysplit[0];
		Lastname = Entrysplit[1];
		String DOB = Entrysplit[2];
		String DOE = Entrysplit[3];
		String DOBsplit[] = DOB.split("-");//Splits Date of Birth and Date of Entry by the hyphen
		String DOEsplit[] = DOE.split("-");
		dobyear = Integer.parseInt(DOBsplit[0]);
		dobmonth = Integer.parseInt(DOBsplit[1]);
		dobday = Integer.parseInt(DOBsplit[2]);
		
		doeyear = Integer.parseInt(DOEsplit[0]);
		doemonth = Integer.parseInt(DOEsplit[1]);
		doeday = Integer.parseInt(DOEsplit[2]);
		doe.set(doeyear, doemonth, doeday); //Sets the Date of Entry Calendar type and Date of Birth Calendar type 
		dob.set(dobyear, dobmonth, dobday);	
		finDate.set(2016, 12, 30); //Final Date is December 30
	}
	/*
	 * This method verifies that the child will be at least6 by the December 30, 2016. It does this by looking at the Calendar dates in milliseconds 
	 * and figuring out if difference between the Date of Birth and the cutoff date is at least 189388800022.
	 */
	public boolean isOldEnough(){
		boolean isOld = false;
		long finTime = finDate.getTimeInMillis();
		long dobTime = dob.getTimeInMillis();
		if((finTime - dobTime) >= cutOff){
			isOld = true;
		}
		return isOld;
	}
	
	/*
	 * This method verifies that the child will be at max 7 by the December 30, 2016. It does this by looking at the Calendar dates in milliseconds 
	 * and figuring out if difference between the Date of Birth and the cutoff date is at most 252460799988.
	 */
	public boolean isTooOld(){
		boolean isTooOld = true;
		long finTime = finDate.getTimeInMillis();
		long dobTime = dob.getTimeInMillis();
		
		if((finTime - dobTime) <= mtooOld){
			isTooOld = false;
		}
		
		return isTooOld;
	}
	/*
	 * This method returns the Date of Entry in Millis for comparison if they are only six.
	 */
	public long DOEinMillis(){
		return doe.getTimeInMillis();
	}
	/*
	 * Concatenates String of First and Last Name
	 */
	public String fullName(){
		return Lastname + ", "+Firstname ;
	}
	/*
	 * returns Age in years by 2016
	 */
	public int ageInYears(){
		return 2016 - dobyear;
	}
	/*
	 * Returns age in Millis for comparison
	 */
	public long ageInMillis(){
		return finDate.getTimeInMillis() - dob.getTimeInMillis();
	}
	//Returns the Child's information in a string format
	public String toString(){
		String str = fullName();
		str += " ("+ageInYears()+")";
		return str;
	}
}

