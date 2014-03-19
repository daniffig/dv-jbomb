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
			this.getGame().getBomb().sendToTarget();
			
			return true;
		}
		
		return false;
	}
	
}
