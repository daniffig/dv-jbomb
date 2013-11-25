package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import linkageStrategies.AbstractLinkageStrategy;

public class Game {

	private String Name;
	private List<GamePlayer> GamePlayers = new ArrayList<GamePlayer>();
	private Integer MaxRounds = 0;
	private Integer CurrentRound;
	private Integer MaxGamePlayersAllowed = 0;
	private Integer RoundDuration;
	private Bomb Bomb = new Bomb();
	private AbstractLinkageStrategy LinkageStrategy;
	
	public Game (String name)
	{
		this.setName(name);
	}
	
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public List<GamePlayer> getGamePlayers() {
		return GamePlayers;
	}

	public void setGamePlayers(List<GamePlayer> gamePlayers) {
		GamePlayers = gamePlayers;
	}

	public Integer getMaxRounds() {
		return MaxRounds;
	}

	public void setMaxRounds(Integer maxRounds) {
		MaxRounds = maxRounds;
	}

	public Integer getCurrentRound() {
		return CurrentRound;
	}

	public void setCurrentRound(Integer currentRound) {
		CurrentRound = currentRound;
	}

	public Integer getMaxGamePlayersAllowed() {
		return MaxGamePlayersAllowed;
	}

	public void setMaxGamePlayersAllowed(Integer maxGamePlayersAllowed) {
		MaxGamePlayersAllowed = maxGamePlayersAllowed;
	}

	public Integer getRoundDuration() {
		return RoundDuration;
	}

	public void setRoundDuration(Integer roundDuration) {
		RoundDuration = roundDuration;
	}

	public Bomb getBomb() {
		return Bomb;
	}

	public void setBomb(Bomb bomb) {
		Bomb = bomb;
	}

	public AbstractLinkageStrategy getLinkageStrategy() {
		return LinkageStrategy;
	}

	public void setLinkageStrategy(AbstractLinkageStrategy linkageStrategy) {
		LinkageStrategy = linkageStrategy;
	}

	public Boolean addGamePlayer(GamePlayer p) {
		if (this.canAddPlayer()) {
			this.getGamePlayers().add(p);

			return true;
		}

		return false;
	}

	public Boolean canAddPlayer() {
		return this.getGamePlayers().size() <= this.getMaxGamePlayersAllowed();
	}

	public Boolean canSendBomb(GamePlayer sourceGamePlayer,
			GamePlayer destinationGamePlayer) {
		// TODO

		return true;
	}

	public void sendBomb(GamePlayer sourceGamePlayer,
			GamePlayer destinationGamePlayer) {
		if (this.canSendBomb(sourceGamePlayer, destinationGamePlayer)) {
			Bomb.setLastPlayer(sourceGamePlayer);
			Bomb.setCurrentPlayer(destinationGamePlayer);
		}
	}
	
	public void start()
	{
		//TODO
		//this.getBomb().setCurrentMilliseconds((new Date()).getTime());
		//this.getBomb().setDetonationMilliseconds((new Date()).getTime() + this.getRoundDuration() * 1000);
		this.getBomb().setLastPlayer(null);
		this.getBomb().setCurrentPlayer(this.getGamePlayers().get((int)(Math.random() * this.getGamePlayers().size())));
		this.getLinkageStrategy().link(this.getGamePlayers());
	}

	public boolean play() {
		//Simulo Pregunta-Respuesta correcto/incorrecta de forma aleatoria
		Random	rnd = new Random();
		Boolean respuesta_correcta = rnd.nextBoolean();
		
		if(respuesta_correcta)
		{
			if (!this.getBomb().isDetonated())
			{
				//Si respondi� bien le paso la bomba a un vecino random
				this.sendBomb(this.Bomb.getCurrentPlayer(), this.Bomb.getCurrentPlayer().getRandomNeighbour());				
			}
		}
		
		return respuesta_correcta;
	}
	
	public Graph<String, String> getGraph()
	{
		Graph<String, String> g = new SparseMultigraph<String, String>();
		
		for(GamePlayer gp: this.GamePlayers)
		{
			g.addVertex(gp.getName());
		}
		
		for(GamePlayer gp: this.GamePlayers)
		{
			for(GamePlayer nb: gp.getNeighbours())
			{
				if(!g.containsEdge(nb.getName()+gp.getName()))
				{
					g.addEdge(gp.getName()+nb.getName() , gp.getName(), nb.getName());
				}
			}
		}
		return g;
	}
	
	
}
