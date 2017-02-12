import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class GradeEstimator {
	
	//Variables
	private ScoreList scoreList; //The list of all the input scores to the estimator.
	private String[] categories; //Different categories avaliable. 
	private int[] weights; //The weights of each category
	private char[] letterGrades; //Letter grades in descending order
	private double[] minThreshold; //The minimum threshold for each letter grade
	private static final int SCORE_PARAMS = 3; //There are only 3 score params.
	//Variables
	
	//Constructors
	public GradeEstimator(ScoreList scoreList, int[] weights, 
			String[] categories, double[] minThreshold, char[] letterGrades)
	{
		this.scoreList = scoreList;
		this.weights = weights;
		this.categories = categories;
		this.minThreshold = minThreshold;
		this.letterGrades = letterGrades;
	}
	//Constructors
	
	//Methods
	/**
	 * A file to check and see if the users input is acceptable for reading. If it is
	 * The program will then take the information from the args and use it to construct
	 * a GradeEstimator object. 
	 * 
	 * @param args The arguments that are supplied in the main class. 
	 * @return null if the format is bad, but also returns a message.  
	 * 		   A fully formed GradeEstimator object if the args are in the correct format. 
	 * @throws FileNotFoundException: If the file doesn't exist then it will throw this exception. 
	 * @throws GradeFileFormatException: If the file doesn't contain the correct format for creating
	 * 					an object. 
	 */
	private static GradeEstimator checkInput(String[] args) throws FileNotFoundException, GradeFileFormatException //Change this around.
	{
		//Variables
		String input;
		Scanner in = new Scanner(System.in);
		GradeEstimator ge = null;
		//Variables
		
		//Body
		if(args.length > 1 || args.length == 0)
		{
			System.out.println(Config.USAGE_MESSAGE);
			//TODO is a gradeFileFormatException here?
		}
		else
		{
			ge = createGradeEstimatorFromFile( args[0] );	
		}
		//Body
		return ge;
	}
	/**
	 * @author Jonas (I can finish this)
	 * Take a file and convert the information there into an object.
	 * 
	 * @param gradeInfo: name of the file that contains information
	 * @return a new object that has it's fields filled with the files info.
	 * @throws GradeFileFormatException: handled by main, if can't open
	 */
	public static GradeEstimator createGradeEstimatorFromFile( String gradeInfo ) 
		throws GradeFileFormatException, FileNotFoundException
	{
		//Variables
		File f1 = new File(gradeInfo);
		FileReader fr = new FileReader(f1);
		BufferedReader bufferO = new BufferedReader(fr);
		String currString = "";
		int size, counter = 0;
		char[] letterGrades;
		double[] minThresholds, assignmentValue_;
		int[] assignmentValue;
		String[] names, categories;
		boolean nextScore = true;
		GradeEstimator newGrade;
		ScoreList scoreList;
		//Variables
		
		//Body
		try {
			currString = removeTrailing(bufferO.readLine()); //Get first String
			letterGrades = getLetterGrades(currString);
			
			currString = removeTrailing(bufferO.readLine()); //Second string
			minThresholds = getThresholds(currString);
			
			//Check to see if these contain the same # of elements
			if(minThresholds.length != letterGrades.length)
			{
				throw new GradeFileFormatException(); //These are paired. 
			}
			
			currString = removeTrailing(bufferO.readLine()); //Third String
			names = getNames(currString);
			
			//Create the category from the scores. 
			categories = createCategories(names);
			
			currString = removeTrailing(bufferO.readLine()); //Value of each assignment
			assignmentValue_ = getThresholds(currString);
			assignmentValue = new int[assignmentValue_.length];
			for(int i = 0; i < assignmentValue.length; i++)
			{
				assignmentValue[i] = (int) assignmentValue_[i]; 
			}
			
			scoreList = new ScoreList();
			
			while(nextScore == true) //If it has a next score. 
			{
				currString = bufferO.readLine();
				//Get the string for the nextScore. 
				
				if(currString == (null)) //Test to see if the current one is null
				{
					nextScore = false;
				}
				else //If it isn't, add that. 
				{
					currString = removeTrailing(currString);				
					scoreList.add(getScore(currString));
				}
			}
		} 
		catch (IOException e) 
		{
			throw new GradeFileFormatException();
		}
		
		//Create the object.
		newGrade = new GradeEstimator(scoreList, assignmentValue, categories, minThresholds, letterGrades);
		//Body
		
		//Return
		return newGrade;
	}
	/**
	 * Creates categories based upon the string[] names provided in args. 
	 * This will take the first letter of each of them and make the category
	 * out of that. 
	 * 
	 * @param names: The string of names provided from args. 
	 * @return: Another string[] containing only the first char of each name.
	 */
	private static String[] createCategories(String[] names)
	{
		//Variables
		String[] categories = null;
		//Variables
		
		//Body
		categories = new String[names.length];
		for(int i = 0; i < names.length; i++)
		{
			categories[i] = names[i].charAt(0) + "";
		}
		//Body
		
		//Return
		return categories;
	}
	/**
	 * Get's the scores from an input string that is formatted in the correct way. 
	 * @param curr
	 * @return
	 * @throws GradeFileFormatException
	 */
	private static Score getScore(String curr) throws GradeFileFormatException
	{
		//Variables
		Score newScore = null;
		int counter = 0;
		String[] scoreFields = new String[SCORE_PARAMS];
		double pointsEarned, maxPoints;
		//Variables
		
		//Body
		//STEP 1. Find all of the variables in the string.
		for(int i = 0; i < SCORE_PARAMS; i++)
		{
			scoreFields[i] = "";
			
			//Gets the string for the next parameter. 
			//Set the check condition for curr length first, so then it won't do the next if it fails.
			while(counter <= (curr.length() - 1) && (!Character.isWhitespace(curr.charAt(counter))) )
			{
				
				scoreFields[i] += curr.charAt(counter);
				counter++;
			}
			counter++;
		}
		
		//Step 2. Parse out all of the doubles. 
		try
		{
			pointsEarned = Double.parseDouble(scoreFields[1]);
			maxPoints = Double.parseDouble(scoreFields[2]);
		} 
		catch(NumberFormatException e)
		{
			//If these strings can't be parsed, it's not in the correct format. 
			throw new GradeFileFormatException(); 
		}
		
		//Step 3. Achieve nirvana and create the score. 
		newScore = new Score(scoreFields[0], pointsEarned, maxPoints);
		//Body
		
		//Return
		return newScore;
	}
	/**
	 * Tests if the method "getScores" works as expected.  The testers string
	 * will be the test cases that will be iterated through. 
	 * 
	 * No data is manipulated. 
	 * PRECONDITIONS: N/a
	 * POSTCONDITIOINS: N/a
	 */
	private static void testGetScore()
	{
		String[] testers = {"a1 30 50", "a2 12.5 15", "a3 0 20"};
		
		for(int i = 0; i < testers.length; i++)
		{
			try {
				System.out.println(getScore(testers[i]));
			} catch (GradeFileFormatException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Separate the current string out into each individual name of the grade
	 * by whitespace. 
	 * 
	 * PRECONDITIONS: curr contains each element separated by exactly one space. 
	 * POSTCONDITIONS: A string[] that contains all the elements from curr is created. 
	 * 
	 * BUGS/IMPROVEMENTS: Creates an array the size of the amount of whitespaces.  This could
	 * lead to a problem eventually, causing the array to be populated by nulls and possibly
	 * causing nullPointers later on in code.  We could fix this by implementing a list, or 
	 * calling an exception if the expected and actual size aren't equal. 
	 * 
	 * @param curr: A string containing names, separated by whitespace
	 * @return an array of strings of each category name.
	 * 
	 */
	private static String[] getNames(String curr)
	{
		//Variables
		String[] names; 
		String name = "";
		int size, counter = 0;
		//Variables
		
		//Body
		//Step 1. Create the array that we will be adding to. 
		curr += " "; //Adds a space at the end so there is at least one. 
		size = countWhitespace(curr); //This will be the maxsize of the string[]
		names = new String[size];
		
		//Step 2.
		//Deconstruct the string into parts based upon spaces. 
		//We allow for multiple whitespaces between each one. 
		//The size of names will be the amount of whitespace
		//So we can gather more information. 
		while(curr.length() > 0)
		{
			
			if(!Character.isWhitespace(curr.charAt(0)))
			{
				name += curr.charAt(0);
				curr = curr.substring(1); //Cut off the first letter and continue
			}
			
			else //The character is whitespace.
			{
				if(!name.equals("")) //Check to see if we have something to add. 
				{
					System.out.println("THIS IS NAME: " + name + " :Counter" );
					names[counter] = name;
					counter++;
				}
				name = ""; //Set name back to a blank state.
				
				curr = curr.substring(1); //Take out the whitespace. 
			}
		}
		//TODO use counter to create a new array then set current array to that if
		//length becomes a problem, or call a gradeFormatException. 
		//Body
		
		//Return
		return names;
	}
	/**
	 * Tests if the method "getNames" works as expected.  The testers string
	 * will be the test cases that will be iterated through. 
	 * 
	 * No data is manipulated. 
	 * PRECONDITIONS: N/a
	 * POSTCONDITIOINS: N/a
	 */
	private static void testGetNames()
	{
		//Variables
		String[] testers = {"ALPHA BETA GAMMA KAPPA", "OMEGA RUBY  SAPPHIRE   EMERALD  ", " HI  EK SO  OE W "};
		//Variables
		
		//Body
		for(int i = 0; i < testers.length; i++)
		{			
			getNames(testers[i]);
		}
		//Body
	}
	/**
	 * Return a double array that contains a list of minimum thresholds,
	 * by deleting whitespace and then looking through the String.
	 * 
	 * @param curr: a string that contains the thresholds of grades
	 * @return an array of doubles with minimum thresholds. 
	 * @throws GradeFileFormatException 
	 */
	private static double[] getThresholds(String curr) throws GradeFileFormatException 
	{
		//Variables
		int size, counter = 0, nextSpace;
		double[] minThresholds = null;
		String minThreshold_;
		//Variables
		
		//Body
		curr += " "; //Add a space at the end of the string for counting. 
		size = countWhitespace(curr); //Count how many whitespaces there are (max Size)
		minThresholds = new double[size]; //Initialize the array with the max number. 
		
		while(curr.length() > 0)
		{
			nextSpace = curr.indexOf(' '); //Find where spaces are. 
			
			if(nextSpace != -1) //indexOf returns -1 if there are no more. 
			{
				minThreshold_ = curr.substring(0, nextSpace); 
				try
				{
					minThresholds[counter] = Double.parseDouble(minThreshold_);
				}
				catch(NumberFormatException e)
				{
					//If this can't be parsed throw an exception. 
					throw new GradeFileFormatException(); 
				}
				
				counter++; //Add one to the counter. 
				curr = curr.substring(nextSpace + 1); //Reduce the size of the string. 
			}
		}
		//Body
		
		//Return
		return minThresholds;
	}
	/**
	 * Tests if the method "getThresholds" works as expected.  The testers string
	 * will be the test cases that will be iterated through. 
	 * 
	 * No data is manipulated. 
	 * PRECONDITIONS: N/a
	 * POSTCONDITIOINS: N/a
	 */
	private static void testGetThresholds()
	{
		String[] testers = {"90.3 49.2 48.1", "0.00, 3, 100"};
		
		for(int i = 0; i < testers.length; i++)
		{
			try {
				getThresholds(testers[i]);
			} catch (GradeFileFormatException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Return a character array that contains what the possible letter grades are.
	 *	
	 * @param curr: a string that contains letter grades.
	 * @return an array of characters with letters
	 */
	private static char[] getLetterGrades (String curr) 
	{
		//Variables
		int size;
		char[] letterGrades;
		//Variables
		
		//Body
		curr = removeWhitespace(curr);
		size = curr.length();
		letterGrades = new char[size];
		for(int i = 0; i < size; i++) //Go through each space. 
		{
			letterGrades[i] = curr.charAt(i); //LetterGrades populated with info from file
		}
		//Body
		
		//Return
		return letterGrades;
	}
	/**
	 * Tests if the method "getLetterGrades" works as expected.  
	 * 
	 * No data is manipulated. 
	 * PRECONDITIONS: N/a
	 * POSTCONDITIOINS: N/a
	 */
	private static void testGetLetterGrades()
	{
		String grades1 = "A  D E  ", grades2 = "AB CE F ";
		char[] no1 = new char[3]; 
		char[] no2 = new char[5]; 
		
		
		no1 = getLetterGrades(grades1);
		System.out.println("[0]: " + no1[0] + "[1]: " + no1[1] + "[2]: " + no1[2]);
		
		no2 = getLetterGrades(grades2);
		System.out.println("[0]: " + no2[0] + "[1]: " + no2[1] + "[2]: " + no2[2]);
	}
	/**
	 * Construct a string from the object and return a report for the user.
	 * PRECONDITIONS:
	 * POSTCONDITIONS:
	 * @return a report in the format:
	 * %name \t %Score \n
	 * ....
	 * Grade estimate is based on %num scores
	 * %(percent from homework) = %(percent homework total) for homework
	 * ...
	 * ---------------------------
	 * \t %totalgrade
	 * Letter Grade Estimate: %letterGrade
	 * 
	 */
	/**
	 * @author Zexing Li (Richard)
	 * @return
	 */
	public String getEstimateReport() throws FileNotFoundException, GradeFileFormatException
	{
		//Variables
		String result = "";
		double score = 0; //Points earned
		double max = 0; //Max possible points
		double ratio = 0; //Sum of ratios between score and max in each category
		double count = 0; //Number of assignments
		double sum = 0; //Sum of each category's grade
		double grade = 0; //Final grade result
		//Variables
		
		//Body
		for (int i = 0; i < categories.length; i++)
		{
			ScoreIterator itr = new ScoreIterator(scoreList, categories[i]);
			while (itr.hasNext()) {
				Score temp = itr.next();
				score = temp.getPoints();
				max = temp.getMaxPossible();
				ratio += score / max;
				count++;
			}
			sum = ratio / count * weights[i];
			grade += sum;
			result += "[" + sum + "]% = [" + ratio / count + "]% * [" 
						+ weights[i]+ "]% for " + categories[i] + "\n";
		}
		
		result += "--------------------------------\n";
		result += "[" + grade + "]% weighted percent";
		result += "Letter Grade Estimate:";
		for (int i = 0; i < minThreshold.length; i++)
		{
			if (grade >= minThreshold[i])
			{
				result += letterGrades[i];
				break;
			}
		}
		//Body
		
		//Return
		return result;
	}
	/**
	 * Find where the #comment begins, remove all info after that, and 
	 * remove all of the whitespace that comes before that. Iterating backwards
	 * 
	 * @param input: A string that may have a #comment in the formatting. 
	 * @return A string without the #comment and no trailing whitespace. 
	 */
	private static String removeTrailing(String input)
	{
		//Variables
		String output = input;
		int markerLocation = (input.length() - 1);
		boolean foundChar = false;
		//Variables
		
		//Body
		//Step 1. Identify the #
		for(int i = 0; i < input.length(); i++)
		{
			if(input.charAt(i) == '#')
			{
				markerLocation = i;
			}
		}
		
		//Step 2. Remove all things after that point. 
		output = input.substring(0, markerLocation); //(STRING)#COMMENT only string included. 
		
		//Step 3. Remove all whitespace trailing the actual data. 
		for(int i = (output.length() - 1); i >= 0; i--)
		{
			if(Character.isWhitespace(output.charAt(i)))
			{
				output = output.substring(0, i);
			}
			else
			{
				break; //If it found a non-whitespace character, break out of loop. 
			}
		}
		//Body
		
		//Return
		return output;
	}
	/**
	 * Remove the whitespace from an input. 
	 * @param input a string that may or may not have excessive whitespace that needs to be removed. 
	 * @return return the input with the whitespace removed. 
	 */
	private static String removeWhitespace(String input)
	{
		//Variables
		String blackspaceString = "";
		//Variables
		
		//Body
		for(int i = 0; i < input.length(); i++)
		{
			if(Character.isWhitespace(input.charAt(i)))
			{
				//Remove character
				input = input.substring(0, i) + input.substring(i+1);
				
				//Decrement i.
				i--;
			}
		}
		blackspaceString = input;
		//Body
		
		return blackspaceString;
	}
	/**
	 * Counts the amount of whitespace in a given string. 
	 * @param input: A string that we want to figure out how much
	 * 		whitespace exists in it. 
	 * @return the number of whitespaces in the text above. 
	 */
	private static int countWhitespace(String input)
	{
		//Variables
		int count = 0;
		//Variables
		
		//Body
		for(int i = 0; i < input.length(); i++)
		{
			if(Character.isWhitespace(input.charAt(i)))
			{
				count++;
			}
		}
		//Body
		
		//Return
		return count;
	}
	/**
	 * An easy way to read data from the object.  Could be the toString method, but
	 * we weren't allowed to make public objects. 
	 * 
	 * @return A string that contains information all about the object. 
	 */
	private String read()
	{
		//Variables
		String output = "", newString ="";
		//Variables
		
		//Body
		System.out.println("LINE 467");
		for(int i = 0; i < this.scoreList.size(); i++)
		{
			if(scoreList.get(i)==null)
			{
				//Do nothing
			}
			else
			{
				newString += "\n[" + (i+1) + "]: " + scoreList.get(i).toString() + " ";
			}
		}
		output += newString + "\n";
		
		newString = "WEIGHTS";
		for(int i = 0; i < this.weights.length; i++)
		{
			newString += "[" + (i+1) + "]: " + weights[i] + " ";
		}
		output += newString + "\n";
		
		newString = "CATEGORIES";
		for(int i = 0; i < this.categories.length; i++)
		{
			newString += "[" + (i+1) + "]: " + categories[i] + " ";
		}
		output += newString + "\n";
		
		newString = "MINTHRESHOLDS";
		for(int i = 0; i < this.minThreshold.length; i++)
		{
			newString += "[" + (i+1) + "]: " + minThreshold[i] + " ";
		}
		output += newString + "\n";
		
		newString = "LETTERGRADES";
		for(int i = 0; i < this.letterGrades.length; i++)
		{
			newString += "[" + (i+1) + "]: " + letterGrades[i] + " ";
		}
		output += newString + "\n";	
		//Body
		
		//Return
		return output;
	}
	//Methods
	
	public static void main(String[] args) {
		
		try {
			GradeEstimator ge = checkInput(args);
			System.out.println(ge.read());
		} catch (FileNotFoundException | GradeFileFormatException e) { 
			e.printStackTrace();
		}
	}

}
