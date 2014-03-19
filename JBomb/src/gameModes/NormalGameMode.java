package gameModes;

import core.GamePlayer;

public class NormalGameMode extends AbstractGameMode {

	@Override
	public String toString() {
		return "Normal";
	}
	
	public boolean handleNextPlayerRequest(GamePlayer TargetPlayer){
		this.getGame().getBomb().setTargetPlayer(TargetPlayer);
		
		return false;
	}
	
	public boolean sendBomb(boolean IsRightAnswer){
		if(IsRightAnswer)
		{
			System.out.println("Estoy en el NormalGameMode y respondió bien - voy a mandar a " + this.getGame().getBomb().getTargetPlayer().getName());
			this.getGame().getBomb().sendToTarget();
			
			return true;
		}
		System.out.println("Estoy en el NormalGameMode y respondió mal");
		
		return false;
	}
	
}
