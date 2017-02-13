/**
 * Exception for a incorrectly formatted file, relevant to the required 
 * format for p1 in CS367, as input into GradeEstimator.java
 *
 */
@SuppressWarnings("serial")
public class GradeFileFormatException extends Exception 
{
	/**
	 * 
	 */
	public GradeFileFormatException()
	{
		super();
	}
	
	/**
	 * 
	 */
	public GradeFileFormatException(String message)
	{
		System.out.println(message);
	}
}
