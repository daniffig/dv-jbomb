package core;

import gameStates.RoundFinishedGameState;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import reference.GameEvent;

public class Bomb extends Observable{

	private Long DetonationMilliseconds = 0L;
	private Long RemainingMilliseconds = 0L;
	
	private Long TimeOnActivation;
	
	private GamePlayer LastPlayer;
	private GamePlayer CurrentPlayer;
	private GamePlayer TargetPlayer;
	
	private Boolean IsActive;
	private Timer Timer;
	
	private Game Game;
	

	
	public Bomb(Game Game)
	{
		this.Game = Game;
	}
	
	public Bomb(Integer DetonationSeconds)
	{
		this.setDetonationMilliseconds((long)(DetonationSeconds*1000));
	}
	
	public Bomb(Integer DetonationSeconds, Game Game)
	{
		this.setDetonationMilliseconds((long)(DetonationSeconds*1000));
		this.Game = Game;
	}
	
	public void activate()
	{	
		this.setIsActive(true);
		
		this.setTimer(new Timer());
		
		this.getTimer().schedule(this.getDetonationTask(), this.getRemainingMilliseconds());
		
		this.TimeOnActivation = System.currentTimeMillis();
		
		System.out.println("Active la bomba con tiempo restante " + this.getRemainingMilliseconds() + "ml");
	}
	
	public void deactivate()
	{
		this.setIsActive(false);
		
		this.getTimer().cancel();
		
		this.RemainingMilliseconds = this.getRemainingMilliseconds() - (System.currentTimeMillis() - this.TimeOnActivation);
	}
	
	public Long getDetonationMilliseconds() {
		return DetonationMilliseconds;
	}

	public void setDetonationMilliseconds(Long detonationMilliseconds) {
		DetonationMilliseconds = detonationMilliseconds;
		
		this.setRemainingMilliseconds(DetonationMilliseconds);
		
		System.out.println("Setee la bomba con un tiempo de detonación de " + this.getDetonationMilliseconds() + "ms");
	}

	public void setDetonationSeconds(Integer detonationsSeconds){
		this.setDetonationMilliseconds((long)detonationsSeconds*1000);
	}

	public GamePlayer getLastPlayer() {
		return LastPlayer;
	}

	public void setLastPlayer(GamePlayer lastPlayer) {
		LastPlayer = lastPlayer;
	}

	public GamePlayer getCurrentPlayer() {
		return CurrentPlayer;
	}

	public void setCurrentPlayer(GamePlayer currentPlayer) {
		CurrentPlayer = currentPlayer;
		
		setChanged();
		notifyObservers(GameEvent.BOMB_OWNER_CHANGED);
	}
	
	public GamePlayer getTargetPlayer() {
		return TargetPlayer;
	}

	public void setTargetPlayer(GamePlayer targetPlayer) {
		TargetPlayer = targetPlayer;
	}

	public Boolean isDetonated()
	{
		return (this.getRemainingMilliseconds() <= 0);
	}

	public Long getRemainingMilliseconds() {
		return RemainingMilliseconds;
	}

	public void setRemainingMilliseconds(Long RemainingMilliseconds) {
		this.RemainingMilliseconds = RemainingMilliseconds;
	}

	public Boolean getIsActive() {
		return IsActive;
	}

	public void setIsActive(Boolean isActive) {
		IsActive = isActive;
	}

	public Timer getTimer() {
		return Timer;
	}

	public void setTimer(Timer timer) {
		Timer = timer;
	}

	private TimerTask getDetonationTask() {
		return new TimerTask(){
			public void run(){
				CurrentPlayer.scoreBombExploded();
				Game.setState(new RoundFinishedGameState());
				System.out.println("Estoy aca porque exploto la bomba! le voy a avisar a mis " + countObservers() + " observers.");
				setChanged();
				notifyObservers(GameEvent.BOMB_EXPLODED);
			}
		};
	}

}
