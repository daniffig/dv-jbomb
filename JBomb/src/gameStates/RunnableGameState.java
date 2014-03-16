package gameStates;

import reference.GameState;

public class RunnableGameState extends AbstractGameState {

	
	public RunnableGameState(){
		this.Type = GameState.RUNNABLE;
	}
	
	@Override
	public String toString() {
		
		return "Listo para comenzar";
	}

}
