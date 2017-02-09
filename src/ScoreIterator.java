import java.util.*;

public class ScoreIterator implements ScoreIteratorADT {
	private ScoreList myList;
	private int currPos;
	private String category;
	
	public ScoreIterator(ScoreList list, String category) {
		this.myList = list;
		this.category = category;
		currPos = 0;
	}
	
	public Score next() {
		if (! hasNext())
			throw new NoSuchElementException();
		
		Score result = myList.get(currPos++);
		
		return result;		
	}
	
	public boolean hasNext() {
		return currPos < myList.size();
	}
	
}
