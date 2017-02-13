/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2016 
// PROJECT:          ScoreList
// FILE:             Score.java
//
// Authors: Jonas
// Author1: Jonas, klare@wisc.edu, klare, Lec001
//
// ---------------- OTHER ASSISTANCE CREDITS 
// Persons: N/A
// 
// Online sources: 
//https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html
//Used to find documentation on arrays. 
//////////////////////////// 80 columns wide //////////////////////////////////
/**
 * A basic class that holds name, pointsEarned, and pointsPossible
 * in a single structure. 
 *
 * <p>Bugs: None found so far.
 *
 * @author Jonas Klare
 */
public class Score {
	
	//Variables
	String name; //Name of the score
	double pointsEarned, //Points earned on this assignment
		pointsPossible; //Total points possible on this assignment
	//Variables
	
	//Constructors
	public Score(String name, double pointsEarned, double pointsPossible)
	{
		//Check validity
		checkString(name);
		checkDoubles(pointsEarned, pointsPossible);
		
		//Set variables
		this.name = name;
		this.pointsEarned = pointsEarned;
		this.pointsPossible = pointsPossible;
	}
	//Constructors

	//Getters
	/**
	 * Getter for name
	 * 
	 * PRECONDITIONS: A name exists
	 * POSTCONDTIONS: the name is returned.
	 * 
	 * @return the name of the score.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for pointsEarned
	 * 
	 * PRECONDITIONS: pointsEarned exists.
	 * POSTCONDTIONS: pointsEarned is returned.
	 * 
	 * @return the points earned on this score
	 */
	public double getPoints() {
		return pointsEarned;
	}

	/**
	 * Getter for pointsPossible
	 * 
	 * PRECONDITIONS: pointsPossible exists.
	 * POSTCONDTIONS: pointsPossible is returned. 
	 * 
	 * @return the max amounts of points for this score
	 */
	public double getMaxPossible() {
		return pointsPossible;
	}
	//Getters
	
	//Methods
	/**
	 * Say what category the score is part of
	 * 
	 * PRECONDITIONS: name exists.
	 * POSTCONDTIONS: the first character is returned.
	 * 
	 * @return first character in score's name
	 */
	public String getCategory()
	{
		//Return the first character of the name
		return name.charAt(0) + "";
	}
	
	/**
	 * Calculate the percent of the score.
	 * 
	 * PRECONDITIONS: pointsEarned and pointsPossible exist.
	 * POSTCONDTIONS: the percentage is returned.
	 *  
	 * @return the percent score. 
	 */
	public double getPercent()
	{
		//Return the percent that the person got. 
		return  pointsEarned/pointsPossible * 100;
	}
	
	/**
	 * Checks to see if the name is valid (not null)
	 * 
	 * PRECONDITIONS: It is unknown if name exists.
	 * POSTCONDTIONS: It's existance is now known.
	 * 
	 * @param input : the name input for the score. 
	 * @throws IllegalArgumentException
	 */
	private static void checkString(String input) 
			throws IllegalArgumentException
	{
		if(input == null)
		{
			throw new IllegalArgumentException("Wrong typing bud");
		}
	}
	
	/**
	 * Checks to see if the inputs given to the score constructor are valid
	 * Throws an exception if they aren't. 
	 * If num1 > 0, then we know in order to pass the second test, num2 > 0.
	 * 
	 * PRECONDITIONS: It is uncertain if nums are correct.
	 * POSTCONDTIONS: It is certain they are not incorrect.
	 * 
	 * @param num1 the numerator when dividing (pointsEarned)
	 * @param num2 the denominator when calculating score (total points)
	 * @throws IllegalArgumentException
	 */
	private static void checkDoubles(double num1, double num2) 
			throws IllegalArgumentException
	{
		//test 1
		if(num1 < 0) //Can't have a negative score
		{
			throw new IllegalArgumentException();
		}
		//test 2
		else if(num1 > num2) //Can't have a score over the max score
		{
			throw new IllegalArgumentException();
		}	
	}
	
	/**
	 * Return data from this object as a readable string. 
	 * 
	 * PRECONDITIONS: A Score contains non-null values
	 * POSTCONDTIONS: A string is returned.
	 * 
	 * @return a string that represents this data.
	 */
	public String toString()
	{
		//Variables
		String stringed = "";
		//Variables
		
		//Body
		stringed = "Name: " + name + 
				"\nPointsEarned/Total: " + pointsEarned + "/" + pointsPossible; 
		//Body
		
		//Return
		return stringed;
	}
	//Methods

}