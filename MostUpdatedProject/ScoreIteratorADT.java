import java.util.NoSuchElementException;

/**
 * An iterator over a ScoreList.
 *
 */
public interface ScoreIteratorADT // Not sure on if a parameter type is required
{
	/**
	 * Returns the next score, while advancing the iterator in the ScoreList.
	 * @return The next score in the ScoreList.
	 */
	 Score next();
	
	/**
	 * Returns true if there is another score in the ScoreList beyond the
	 * the current position of the iterator.
	 * @return true if there is another score in the ScoreList beyond the
	 * 		   current position of the iterator.
	 * @throws NoSuchElementException if there are no more scores beyond the
	 * 		   current position of the iterator.
	 */
	boolean hasNext() throws NoSuchElementException;
	
}
