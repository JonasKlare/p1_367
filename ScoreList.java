/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Spring 2016 
// PROJECT:          ScoreList
// FILE:             ScoreList
//
// Authors: Jonas Klare
// Author1: Jonas Klare, klare@wisc.edu, klare, lec001
//
// ---------------- OTHER ASSISTANCE CREDITS 
// Persons: N/A
// 
// Online sources: 
//https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html
//Used to find documentation on arrays. 
//////////////////////////// 80 columns wide //////////////////////////////////
/**
 * ScoreList is a data structure that stores Scores, allowing them to be
 * removed, have more Scores added, and also retrieve scores. 
 *
 * <p>Bugs: In cases where multiple things have been removed without
 * 		running the removeNullSpace method, it may not always return
 * 		correctly. 
 *
 * @author Jonas
 */
public class ScoreList implements ScoreListADT{
	
	//Variables
	private final int START_SIZE = 5;
	int numItems;
	Score[] list;
	//Variables
	
	//Constructors
	public ScoreList()
	{
		numItems = 0;
		list = new Score[START_SIZE];
	}
	//Constructors

	@Override
	/**
	 * Getter for numItems
	 * 
	 * PRECONDITIONS: A scoreList has been created and numItems
	 * is equivalent to the number of items in the ScoreList
	 * POSTCONDITIONS: Returned the number of items. 
	 * 
	 * @return the number of items in the scorelist
	 */
	public int size() {
		return numItems;
	}
	@Override
	/**
	 * This will add a new Score to the list at the last position
	 * in the list that is currently avaliable. If the internal 
	 * array is too small, it will run an expansion method. 
	 * 
	 * PRECONDITIONS: A score s has data that is correct for the 
	 * user's intent. 
	 * POSTCONDTIONS: The list now contains a score s at the end
	 * of it. 
	 *
	 * @param Score s : The new score to add to the ScoresList
	 * 
	 */
	public void add(Score s) throws IllegalArgumentException {
	
		//Check for exception
		if(s.equals(null)) throw new IllegalArgumentException();
		
		//Check to see if array is full
		if(numItems == this.list.length) 
			expandArray();
		else
		{
			list[numItems] = s;
			numItems++; //Add an item to the total.
		}
		
	}
	@Override
	/**
	 * Remove a single score from the list, then shift the scores back 
	 * to the correct position, and lower the numItems by 1. 
	 * 
	 * EX (each integer is representing a new, unique score)
	 * [1, 2, 3, 4, 5] <-- list
	 * list.remove(2);
	 * [1, 2, null, 4, 5] --> [1, 2, 4, 5, null]
	 * and then shrink numItems down one, so we only use the first 4.
	 * 
	 * PRECONDITIONS: A score at index i currently exists
	 * POSTCONDITIONS: The array now contains n-1 items, and has no 
	 * null space in the n-1 items. 
	 * 
	 * @param int i : the index of the score that is to be removed. 
	 * @return the Score that was just removed from the list. 
	 */
	public Score remove(int i) throws IndexOutOfBoundsException {
		
		//Variables
		Score s = null;
		//Variables
		
		//Body
		//Check for exception
		if(i > numItems - 1) throw new IndexOutOfBoundsException();
		
		s = list[i]; //create a variable to store the Score
		list[i] = null; //Set score to null. 
		
		removeNullSpace(); 
		numItems--; // -1 because an item was removed. 
		//Body
		
		//Return
		return s;
	}

	@Override
	/**
	 * @author Jonas
	 * 
	 * Return a score from the internal array at a specific
	 * index
	 * 
	 * PRECONDITIONS: A score at the index i exists. 
	 * POSTCONDITIONS: A score at index i is returned. 
	 * 
	 * @param int i : index of the Score to be returned
	 * @return the Score at index i. 
	 */
	public Score get(int i) throws IndexOutOfBoundsException {
	
		//Variables
		Score s = null;
		//Variables
		
		//Body
		//Check for exception
		if(i > numItems - 1) throw new IndexOutOfBoundsException();
		
		s = list[i];
		//Body
		
		//Return
		return s;
	}
	
	/**
	 * Go through the list and find every instance of null within in the 
	 * list that's expected to contain actual values. Whenever an instance 
	 * of null is found in this list, it adds it's position to an array 
	 * containing indices of null values. 
	 * 
	 * That array is then searched through, and removed from right to left, 
	 * shifting the scores down along with it. 
	 * 
	 * PRECONDITIONS: The incoming list is a list that contains at least 1
	 * null value. 
	 * 
	 * POSTCONDITIONS: The list has had all null values placed at the end
	 * of the list, numItems making sure they don't get found. 
	 */
	private void removeNullSpace()
	{
		//Variables
		int[] nullItems = new int[list.length];
		int totalNulls = 0;
		//Variables
		
		//Body
		//STEP 1: Find the positions in which things are null
		for(int i = 0; i < numItems; i++)
		{
			if(list[i] == null)
			{
				nullItems[totalNulls++] = i;
			}
		}
		
		//STEP 2: Go through each element in the null Array, and remove.
		for(int i = totalNulls; i > 0; i--)
		{
			int currNull = nullItems[i];
			//Shift everything over for the rightmost null number. 
			for(int j = 0; j < (list.length - currNull - 1); j++)
			{
				//If it is at the end of the list, set it to be null. 
				if(j == list.length - 1)
				{
					list[currNull + j] = null;
				}
				//Else, move the number one over.
				else
				{
					list[currNull + j] = list[currNull + j + 1];					
				}
 			}
		}
		//Body
	}
	/**
	 * Take the current list and expand the size of it by a factor of 2,
	 * creating a new array double the size and then transferring 
	 * information from old to new and re-referencing to the new list. 
	 * 
	 * PRECONDITIONS: An array that is too small for the user.
	 * 
	 * POSTCONDITIONS: Return an array that is double the size of the 
	 * last one. 
	 */
	private void expandArray() 
	{
		//Possible improvement for this would be implementation of
		//A shadow array in order smooth out time in case the user
		//Noticed this and N got large. 
		
		//Variables
		//New array with double the size of the last one. 
		Score[] newList = new Score[list.length * 2];
		//Variables
		
		//Body
		//Populate the new array with the values of the old. 
		for(int i = 0; i < list.length; i++)
		{
			newList[i] = list[i];
		}
		//Body
		
		//Return
		//Set the list this ds uses to be the newly created list. 
		list = newList;
	}
	
	

}
