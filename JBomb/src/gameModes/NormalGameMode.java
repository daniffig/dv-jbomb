package gameModes;

public class NormalGameMode extends AbstractGameMode {

	@Override
	public String toString() {
		return "Normal";
	}
	
	public boolean handleNextPlayerRequest(){
		return true;
	}
	
	public boolean sendBomb(boolean IsRightAnswer){
		return true;
	}
	
}
