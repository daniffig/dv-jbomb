package gameStates;

import reference.GameState;

public class RunningGameState extends AbstractGameState {

	
	public RunningGameState(){
		this.Type = GameState.RUNNING;
	}
	
	@Override
	public String toString() {
		
		return "Juego en curso";
	}

}
