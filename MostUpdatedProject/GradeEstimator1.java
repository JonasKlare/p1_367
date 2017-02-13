import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Contains the main method for the program. Also contains constructor 
 * for the main GradeEstimator object used within the program.
 *
 */
public class GradeEstimator1 
{
	/**
	 * The list of all the input scores to the estimator.
	 */
	private ScoreList scoreList;
	
	/**
	 * Different categories avaliable.
	 */
	private String[] categories;
	
	/**
	 * The weights of each category.
	 */
	private int[] weights;
	
	/**
	 * Letter grades in descending order.
	 */
	private char[] letterGrades;
	
	/**
	 * The minimum threshold for each letter grade.
	 */
	private double[] minThreshold;
	
	/**
	 * There are only 3 score parameters.
	 */
	private static final int SCORE_PARAMS = 3;

	public GradeEstimator1(ScoreList scoreList, int[] weights, 
				String[] categories, double[] minThreshold, char[] letterGrades)
	{
		this.scoreList = scoreList;
		this.weights = weights;
		this.categories = categories;			
		this.minThreshold = minThreshold;
		this.letterGrades = letterGrades;
	}
	
	/**
	 * Main method.
	 * @param args Input file containing the necessary information.
	 */
	public static void main(String[] args) throws FileNotFoundException,
		GradeFileFormatException
	{
		// Establish a GradeEstimator variable
		GradeEstimator1 gradeEstimator;
				
		// Ensure an input file exists, and there is only one
		if(args.length > 1 || args.length == 0)
		{
			System.out.println(Config.USAGE_MESSAGE);
		}
		// Attempt to create GradeEstimator from file, else throw exception
		else
		{
			try
			{
				gradeEstimator = createGradeEstimatorFromFile(args[0]);	
				System.out.println(gradeEstimator.getEstimateReport());
			}
			catch (FileNotFoundException e)
			{
				throw new FileNotFoundException();
			}
			catch (GradeFileFormatException e)
			{
				throw new GradeFileFormatException("GradeFileFormatException");
			}
		}
	}
	
	/**
	 * Take a file and convert the information from a collection of Strings
	 * into a GradeEstimator object.
	 * 
	 * @param gradeInfo: name of the file that contains information
	 * @return a new object that has it's fields filled with the files info.
	 * @throws GradeFileFormatException: handled by main, if can't open
	 */
	public static GradeEstimator1 createGradeEstimatorFromFile(String gradeInfo) 
		throws GradeFileFormatException, FileNotFoundException
	{
		// Create file from input text
		File f1 = new File(gradeInfo);
		
		// Create readers
		FileReader fr = new FileReader(f1);
		BufferedReader bufferO = new BufferedReader(fr);
		
		// TODO
		// Add comments on what each variable is for
		String currString = "";
		char[] letterGrades;
		double[] minThresholds;
		double[] tempAssignmentValue;
		int[] assignmentValue;
		String[] names, categories;
		boolean nextScore = true;
		
		ScoreList scoreList;
		
		try 
		{
			//Get first String
			currString = removeTrailing(bufferO.readLine());
			letterGrades = getLetterGrades(currString);
			
			//Second string
			currString = removeTrailing(bufferO.readLine()); 
			minThresholds = getThresholds(currString);
			
			//Check to see if these contain the same # of elements
			if(minThresholds.length != letterGrades.length)
			{
				bufferO.close();
				throw new GradeFileFormatException(); 
			}
			
			//Third String
			currString = removeTrailing(bufferO.readLine());
			names = getNames(currString);
			
			//Create the category from the scores. 
			categories = createCategories(names);
			
			//Value of each assignment
			currString = removeTrailing(bufferO.readLine());
			tempAssignmentValue = getThresholds(currString);
			assignmentValue = new int[tempAssignmentValue.length];
			for(int i = 0; i < assignmentValue.length; i++)
			{
				assignmentValue[i] = (int) tempAssignmentValue[i]; 
			}
			
			scoreList = new ScoreList();
			
			//If it has a next score. 
			while(nextScore == true) 
			{
				//Get the string for the nextScore.
				currString = bufferO.readLine(); 
				
				//Test to see if the current one is null
				if(currString == (null)) 
				{
					nextScore = false;
				}
				//If it isn't, add that.
				else 
				{
					currString = removeTrailing(currString);				
					scoreList.add(getScore(currString));
				}
			}
			bufferO.close();
		} 
		catch (IOException e) 
		{
			throw new GradeFileFormatException();
		}
		
		//Create the object.
		GradeEstimator1 newGrade = new GradeEstimator1(scoreList, 
				assignmentValue, categories, minThresholds, letterGrades);
		
		return newGrade;
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
			while (itr.hasNext()) 
			{
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
	 * Return a character array that contains what the possible letter grades are.
	 *	
	 * @param curr: a string that contains letter grades.
	 * @return an array of characters with letters
	 */
	private static char[] getLetterGrades (String grades) 
	{
		String letterGradesString = removeWhitespace(grades);
		char[] letterGrades = new char[letterGradesString.length()];
		
		//Go through each space, letterGrades populated with info from file
		for(int i = 0; i < letterGradesString.length(); i++)
		{
			letterGrades[i] = grades.charAt(i);
		}
		return letterGrades;
	}
	
	/**
	 * Separate the current string out into each individual name of the grade
	 * by whitespace. 
	 * 
	 * PRECONDITIONS: curr contains each element separated by exactly one space. 
	 * POSTCONDITIONS: A string[] that contains all the elements from curr is 
	 * 				   created. 
	 * 
	 * BUGS/IMPROVEMENTS: Creates an array the size of the amount of 
	 * 					  whitespaces. This could lead to a problem eventually, 
	 * 					  causing the array to be populated by nulls and 
	 * 					  possibly causing nullPointers later on in code. We 
	 * 					  could fix this by implementing a list, or calling an 
	 * 					  exception if the expected and actual size aren't 
	 * 					  equal. 
	 * @param curr: A string containing names, separated by whitespace
	 * @return an array of strings of each category name.
	 * 
	 */
	private static String[] getNames(String curr)
	{
		String[] names; 
		String name = "";
		int size, counter = 0;
		
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
					names[counter] = name;
					counter++;
				}
				name = ""; //Set name back to a blank state.
				
				curr = curr.substring(1); //Take out the whitespace. 
			}
		}
		
		return names;
	}

	//TODO comments for method header below
	
	/**
	 * Get's the scores from an input string that is formatted in the 
	 * correct way. 
	 * @param curr
	 * @return
	 * @throws GradeFileFormatException
	 */
	private static Score getScore(String curr) throws GradeFileFormatException
	{
		Score newScore;
		int counter = 0;
		String[] scoreFields = new String[SCORE_PARAMS];
		double pointsEarned, maxPoints;
		
		//STEP 1. Find all of the variables in the string.
		for(int i = 0; i < SCORE_PARAMS; i++)
		{
			scoreFields[i] = "";
			
			/* Gets the string for the next parameter. 
			 * Set the check condition for curr length first, so then it won't 
			 * do the next if it fails.
			 */
			while(counter <= (curr.length() - 1) && 
					(!Character.isWhitespace(curr.charAt(counter))))
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
		
		return newScore;
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
		//Add a space at the end of the string.
		curr += " "; 
		
		//Count how many whitespaces there are
		int size = countWhitespace(curr);
		
		//Initialize the array with the max number.
		double[] minThresholds = new double[size];  
		
		// Create counter
		int counter = 0;
		
		// Fill array with thresholds
		while(curr.length() > 0)
		{
			//Find where spaces are. 
			int nextSpace = curr.indexOf(' ');
			
			if(nextSpace != -1) 
			{
				String tempMinThreshold = curr.substring(0, nextSpace); 
				try
				{
					minThresholds[counter] = 
							Double.parseDouble(tempMinThreshold);
				}
				catch(NumberFormatException e)
				{
					//If this can't be parsed throw an exception. 
					throw new GradeFileFormatException(); 
				}
				
				//Add one to the counter, reduce the size of the string.
				counter++;
				curr = curr.substring(nextSpace + 1);  
			}
		}
		return minThresholds;
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
		String output = input;
		int markerLocation = (input.length() - 1);
	
		//Step 1. Identify location of the #
		for(int i = 0; i < input.length(); i++)
		{
			if(input.charAt(i) == '#')
			{
				markerLocation = i;
			}
		}
		
		//Step 2. Remove all characters after #. 
		output = input.substring(0, markerLocation);
		
		//Step 3. Remove all whitespace at end of string. 
		for(int i = (output.length() - 1); i >= 0; i--)
		{
			if(Character.isWhitespace(output.charAt(i)))
			{
				output = output.substring(0, i);
			}
			else
			{
				break; 
			}
		}
		return output;
	}
	
	/**
	 * Remove the whitespace from an input. 
	 * @param input a string that may or may not have excessive whitespace 
	 * 		  that needs to be removed. 
	 * @return return the input with the whitespace removed. 
	 */
	private static String removeWhitespace(String input)
	{
		String blackspaceString = "";
	
		for(int i = 0; i < input.length(); i++)
		{
			if(Character.isWhitespace(input.charAt(i)))
			{
				//Remove character, decrement
				input = input.substring(0, i) + input.substring(i+1);
				i--;
			}
		}
		blackspaceString = input;
		return blackspaceString;
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
		String[] categories = new String[names.length];
		for(int i = 0; i < names.length; i++)
		{
			categories[i] = names[i].charAt(0) + "";
		}
		return categories;
	}
	
	/**
	 * Counts the amount of whitespace in a given string. 
	 * @param input: A string that we want to figure out how much
	 * 		whitespace exists in it. 
	 * @return the number of whitespaces in the text above. 
	 */
	private static int countWhitespace(String input)
	{
		int count = 0;
		
		for(int i = 0; i < input.length(); i++)
		{
			if(Character.isWhitespace(input.charAt(i)))
			{
				count++;
			}
		}
		return count;
	}
}
