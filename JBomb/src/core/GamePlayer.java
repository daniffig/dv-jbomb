package core;

public class GamePlayer {

	/*
	private Player Player;
	
	public String toString()
	{
		return this.getPlayer().toString();
	}

	public Player getPlayer() {
		return Player;
	}

	public void setPlayer(Player player) {
		Player = player;
	}
	*/
	
	private String Name;
	
	public GamePlayer(){}
	
	public GamePlayer(String name)
	{
		this.setName(name);
	}
	
	public String toString()
	{
		return this.getName();
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
}
	 
