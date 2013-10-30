package core;

import java.util.ArrayList;
import java.util.List;

public class GamePlayer {

	/*
	 * private Player Player;
	 * 
	 * public String toString() { return this.getPlayer().toString(); }
	 * 
	 * public Player getPlayer() { return Player; }
	 * 
	 * public void setPlayer(Player player) { Player = player; }
	 */

	private String Name;
	private List<GamePlayer> Neighbours = new ArrayList<GamePlayer>();

	public GamePlayer() {
	}

	public GamePlayer(String name) {
		this.setName(name);
	}

	public String toString() {
		return this.getName();
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public List<GamePlayer> getNeighbours() {
		return Neighbours;
	}

	public void setNeighbours(List<GamePlayer> neighbours) {
		Neighbours = neighbours;
	}
	
	public void addNeighbour(GamePlayer gamePlayer)
	{
		this.getNeighbours().add(gamePlayer);
		gamePlayer.getNeighbours().add(this);
	}
	
	public GamePlayer getNeighbour(Integer index)
	{
		if (index < this.getNeighbours().size())
		{
			return this.getNeighbours().get(index);
		}
		
		return null;
	}
	
	public GamePlayer getRandomNeighbour()
	{
		return this.getNeighbour((int)(Math.random() * this.getNeighbours().size()));
	}
}
