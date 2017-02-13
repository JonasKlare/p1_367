import java.util.*;


public class ScoreIterator implements ScoreIteratorADT {
	
	//Variables
	private ScoreList myList; //List of scores
	private int currPos; //Tracks the current position of the iterator
	private String category; //The type of score in the list
	//Variables
	
	//Constructors
	public ScoreIterator(ScoreList list, String category) {
		//Set variables
		this.myList = list;
		this.category = category;
		currPos = 0;
	}
	//Constructors
	
	/**
	 * Gets the current position and advances the current position
	 * to next.
	 * 
	 * @see ScoreIteratorADT#next()
	 */
	public Score next() {
		//Test
		if (! hasNext()) //Can't call next() when there is no next.
			throw new NoSuchElementException();
		
		//Body
		//Assigns the current position to result, then advances current position.
		Score result = myList.get(currPos++);
		//Body
		
		//Return
		return result;		
	}
	
	/**
	 * Checks to see if there is another score in the list and check if the
	 * next score is in the same category.
	 * 
	 * @see ScoreIteratorADT#next()
	 */
	public boolean hasNext() {
		//Variables
		boolean m = false, n = false;
		//Variables
		
		//Body
		if(currPos < myList.size())
			m = true;
		
		//If m isn't true, then we will get an OOB error. 
		if(m && myList.get(currPos).getCategory() != category)
		{
			n = true;
		}
		//Body
		
		//Return 
		return (n); //If n is true, m is also true.
	}
	
}
