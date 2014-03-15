package core;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class GamePoints {

	Vector<HashMap<Integer, Integer>> RoundPoints = new Vector<HashMap<Integer, Integer>>();
	
	HashMap<Integer, Integer> GeneralPoints = new HashMap<Integer, Integer>();
	
	public void initializeNewRoundPoints(List<GamePlayer> GamePlayers)
	{
		HashMap<Integer, Integer> statPoints = new HashMap<Integer, Integer>();
		
		for(GamePlayer gp : GamePlayers)
			statPoints.put(gp.getId(), 0);
		
		RoundPoints.add(statPoints);
	}
	
	public void initializeGeneralPoints(List<GamePlayer> GamePlayers)
	{
		for(GamePlayer gp : GamePlayers)
			GeneralPoints.put(gp.getId(), 0);
	}
	
	
	public void scoreCorrectAnswer(Integer PlayerId)
	{
		this.RoundPoints.lastElement().put(PlayerId, this.RoundPoints.lastElement().get(PlayerId) - 10);
	}
	
	public void scoreWrongAnswer(Integer PlayerId)
	{
		this.RoundPoints.lastElement().put(PlayerId, this.RoundPoints.lastElement().get(PlayerId) - 5);
	}
	
	public void scoreBombDetonated(Integer PlayerId)
	{
		this.RoundPoints.lastElement().put(PlayerId, this.RoundPoints.lastElement().get(PlayerId) - 50);
	}
	
	public Integer getCurrentRoundPointsOfPlayer(Integer PlayerId)
	{
		return this.RoundPoints.lastElement().get(PlayerId);
	}
	
	public Integer getGeneralPointsOfPlayer(Integer PlayerId)
	{
		return this.GeneralPoints.get(PlayerId);
	}
}
