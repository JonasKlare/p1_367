import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GradeEstimator {
	
	private ScoreList scoreList;
	private int[] weights;
	private String[] categories;
	private double[] minThreshold;
	private char[] letterGrades;
	
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
	
	private void checkInput(String[] args) throws FileNotFoundException, GradeFileFormatException //Change this around.
	{
		//Variables
		String input;
		Scanner in = new Scanner(System.in);
		//Variables
		
		//Body
		if(args.length > 1 || args.length == 0)
		{
			System.out.println(Config.USAGE_MESSAGE);
		}
		else
		{
			createGradeEstimatorFromFile( args[0] );
		}
		//Body
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
		//Variables
		
		//Body
		try {
			currString = bufferO.readLine(); //Get first String
			letterGrades = getLetterGrades(currString);
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		//Body
		
		//Return
		return null;
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
	 * Return a double array that contains a list of minimum thresholds,
	 * by deleting whitespace and then looking through the String.
	 * @param curr: a string that contains the thresholds of grades
	 * @return an array of doubles with minimum thresholds. 
	 */
	public static double[] getThresholds(String curr)
	{
		//Variables
		int size;
		double[] minThresholds;
		//Variables
		
		//Body
		curr = removeWhitespace(curr);
		size = curr.length();
		minThresholds = new double[size];
		for(int i = 0; i < size; i++)
		{
			minThresholds[i] = (double) curr.charAt(i);
		}	
		//Body
		
		//Return
		return minThresholds;
	}
	/**
	 * Tests to see if the method "getLetterGrades" works.
	 * PRECONDITIONS: N/a
	 * POSTCONDITIONS: N/a
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
	 * PRECONDITIONS:Given file names
	 * POSTCONDITIONS:Report printed and returned
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
	 * @return EstimateReport
	 * @throws GradeFileFormatException 
	 * @throws FileNotFoundException 
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
	
	public static void main(String[] args) throws FileNotFoundException, GradeFileFormatException  {
		
		testGetLetterGrades();
		
	}

}
