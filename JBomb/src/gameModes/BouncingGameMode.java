package gameModes;

public class BouncingGameMode extends AbstractGameMode {

	@Override
	public String toString() {
		return "Rebote";
	}
	
	public boolean handleNextPlayerRequest(){
		return true;
	}
	
	public boolean sendBomb(boolean IsRightAnswer){
		return true;
	}

}
