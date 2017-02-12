import java.util.*;

/**
 * 
 * @author Bryan
 *
 */
public class ScoreIterator implements ScoreIteratorADT {
	
	//Variables
	private ScoreList myList; //List of scores
	private int currPos; //Tracks the current position of the iterator
	private String category; //The type of score in the list
	//Variables
	
	/**
	 * 
	 * @param list
	 * @param category
	 */
	public ScoreIterator(ScoreList list, String category) {
		//Set variables
		this.myList = list;
		this.category = category;
		currPos = 0;
	}
	
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
	
	/*
	 * Checks to see if there is another score in the list and check if the
	 * next score is in the same category.
	 * 
	 * @see ScoreIteratorADT#next()
	 */
	public boolean hasNext() {
		return (currPos < myList.size() || 
				myList.get(currPos).getCategory() != category);
	}
	
}
