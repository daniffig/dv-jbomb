package reference;

public enum AdjacentDirections {
	FORWARD(0),
	RIGHT(1),
	DOWN(2),
	LEFT(3);
	
	private int index;
	
	AdjacentDirections(int index)
	{
		this.index = index;
	}
	
	public int index()
	{
		return index;
	}
}
