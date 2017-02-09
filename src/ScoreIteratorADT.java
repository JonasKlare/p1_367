import java.util.*;

public interface ScoreIteratorADT<E> {
	
	public E next();
	public boolean hasNext() throws NoSuchElementException;
	
}
