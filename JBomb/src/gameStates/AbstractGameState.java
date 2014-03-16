package gameStates;

import reference.GameState;

public abstract class AbstractGameState {

	protected GameState Type;
	
	public abstract String toString();
	
	public GameState getType()
	{
		return this.Type;
	}
}
