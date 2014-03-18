package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GamePlayer {

	private Integer Id;
	private String Name;
	private Vector<Integer> RoundPoints = new Vector<Integer>();
	private Integer GeneralPoints = 0;
	private List<GamePlayer> Neighbours = new ArrayList<GamePlayer>();

	public GamePlayer() {
	}

	public GamePlayer(String name) {
		this.RoundPoints.add(-1);//Agregamos un valor dummy para la posición 0 del Vector
		this.setName(name);
	}

	public String toString() {
		return this.getName();
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	//Puntajes
	public Integer getGeneralPoints() {
		return GeneralPoints;
	}

	public void setGeneralPoints(Integer generalPoints) {
		GeneralPoints = generalPoints;
	}

	public Vector<Integer> getRoundPoints() {
		return RoundPoints;
	}

	public void setRoundPoints(Vector<Integer> roundPoints) {
		RoundPoints = roundPoints;
	}
	
	public void InitializeNewRoundPoints(){
		RoundPoints.add(0);
	}
	
	public Integer getCurrentRoundPoints(){
		return RoundPoints.lastElement();
	}

	public Integer getPointsFromRound(Integer Round){
		return RoundPoints.get(Round);
	}
	
	public void scoreWrongAnswer(){
		this.RoundPoints.set(this.RoundPoints.size()-1, this.getCurrentRoundPoints()-5);
	}
	
	public void scoreRightAnswer(){
		this.RoundPoints.set(this.RoundPoints.size()-1, this.getCurrentRoundPoints()+10);
	}
	
	public void scoreBombExploded(){
		this.RoundPoints.set(this.RoundPoints.size()-1, this.getCurrentRoundPoints()-50);
	}
	
	//Jugadores adyacentes
	public List<GamePlayer> getNeighbours() {
		return Neighbours;
	}

	public void setNeighbours(List<GamePlayer> neighbours) {
		Neighbours = neighbours;
	}
	
	public void addNeighbour(GamePlayer gamePlayer)
	{
		if (!this.equals(gamePlayer) && !this.getNeighbours().contains(gamePlayer))
		{
			this.getNeighbours().add(gamePlayer);
			gamePlayer.getNeighbours().add(this);
		}
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
