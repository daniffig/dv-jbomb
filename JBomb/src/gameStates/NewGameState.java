package gameStates;

import reference.GameState;

public class NewGameState extends AbstractGameState {

	
	public NewGameState(){
		this.Type = GameState.NEW;
	}
	
	@Override
	public String toString() {
		return "Nuevo";
	}

}
