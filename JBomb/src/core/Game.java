package core;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import network.Player;

import concurrency.ClientThread;

import roundDurations.AbstractRoundDuration;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import gameModes.AbstractGameMode;
import gameStates.*;
import linkageStrategies.AbstractLinkageStrategy;

public class Game {

	private Integer UID;

	private String Name;

	private Integer MaxGamePlayersAllowed = 0;
	private List<GamePlayer> GamePlayers = new ArrayList<GamePlayer>();

	private GamePoints GamePoints = new GamePoints();

	private Integer CurrentRound = 0;
	private Integer MaxRounds = 0;

	private AbstractRoundDuration RoundDuration;
	private Bomb Bomb;

	private Quiz Quiz;

	private AbstractLinkageStrategy LinkageStrategy;
	private AbstractGameMode Mode;
	private AbstractGameState State;


	public Game()
	{
		this.setBomb(new Bomb(this));
		this.setState(new NewGameState());		
	}

	public Integer getUID() {
		return UID;
	}

	public void setUID(Integer uID) {
		UID = uID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public synchronized List<GamePlayer> getGamePlayers() {
		return GamePlayers;
	}

	public synchronized void setGamePlayers(List<GamePlayer> gamePlayers) {
		GamePlayers = gamePlayers;
	}

	public synchronized GamePlayer getGamePlayerById(Integer Id){
		for(GamePlayer gp: this.GamePlayers)
			if(gp.getId().equals(Id)) return gp;

		return null;
	}

	public synchronized Vector<Player> getGamePlayersAsPlayers(){
		Vector<Player> Players = new Vector<Player>();
		for(GamePlayer gp: this.GamePlayers)
			Players.add(gp.toPlayer());
		
		return Players;
	}
	
	public Integer getMaxRounds() {
		return MaxRounds;
	}

	public void setMaxRounds(Integer maxRounds) {
		MaxRounds = maxRounds;
	}

	public synchronized Integer getCurrentRound() {
		return CurrentRound;
	}

	public synchronized void setCurrentRound(Integer currentRound) {
		CurrentRound = currentRound;
	}

	public Integer getMaxGamePlayersAllowed() {
		return MaxGamePlayersAllowed;
	}

	public void setMaxGamePlayersAllowed(Integer maxGamePlayersAllowed) {
		MaxGamePlayersAllowed = maxGamePlayersAllowed;
	}

	public AbstractRoundDuration getRoundDuration() {
		return RoundDuration;
	}

	public void setRoundDuration(AbstractRoundDuration roundDuration) {
		RoundDuration = roundDuration;
	}

	public synchronized Bomb getBomb() {
		return Bomb;
	}

	public synchronized void setBomb(Bomb bomb) {
		Bomb = bomb;
	}

	public AbstractLinkageStrategy getLinkageStrategy() {
		return LinkageStrategy;
	}

	public void setLinkageStrategy(AbstractLinkageStrategy linkageStrategy) {
		LinkageStrategy = linkageStrategy;
	}

	public synchronized Integer addGamePlayer(GamePlayer GamePlayer) {
		if (this.canAddPlayer())
		{
			GamePlayer.setId((this.getGamePlayers().size()+1));
			this.getGamePlayers().add(GamePlayer);

			return GamePlayer.getId();
		}

		return -1;
	}

	public synchronized Boolean canAddPlayer() {
		return this.getGamePlayers().size() <= this.getMaxGamePlayersAllowed();
	}

	public synchronized Boolean existPlayer(String username)
	{
		for(GamePlayer gp : this.GamePlayers)
			if(gp.getName().equals(username)) return true;

		return false;
	}

	public Integer getTotalGamePlayers(){
		return this.getGamePlayers().size();
	}

	public Boolean canSendBomb(GamePlayer sourceGamePlayer,	GamePlayer destinationGamePlayer)
	{
		return true;
	}

	public synchronized void sendBomb(GamePlayer sourceGamePlayer,GamePlayer destinationGamePlayer)
	{
		if (this.canSendBomb(sourceGamePlayer, destinationGamePlayer))
		{
			Bomb.setLastPlayer(sourceGamePlayer);
			Bomb.setCurrentPlayer(destinationGamePlayer);
		}
	}

	public void start()
	{
		this.initializeNewRoundScores();
	
		this.getBomb().initializeBomb(this.RoundDuration.getDuration(), this.getGamePlayers());
		
		this.setState(new RunningGameState());
	}

	public void initializeNewRoundScores()
	{
		if(this.CurrentRound == 0) 
			for(GamePlayer gp : this.getGamePlayers()) 
				gp.setGeneralPoints(0);
		
		this.CurrentRound++;
		
		for(GamePlayer gp : this.getGamePlayers())
			gp.InitializeNewRoundPoints();
	}
	
	public void configureAdjacentPlayersGraph()
	{
		this.getLinkageStrategy().link(this.getGamePlayers());
	}

	public synchronized void suscribeToBombDetonation(ClientThread ct)
	{
		System.out.println("[Game] Agrego el observer " + ct.toString() + " para que chequee la bomba");
		this.getBomb().addObserver(ct);
	}

	public Boolean isValid()
	{
		return true;
	}

	public void deepCopy(Game newGame)
	{
		this.setName(newGame.getName());
	}

	public Vector<Object> toVector()
	{
		Vector<Object> v = new Vector<Object>();

		v.add(this.getName());
		v.add(this.getMode());
		v.add(this.getState());
		v.add(this.getGamePlayers().size() + "/" + this.getMaxGamePlayersAllowed());

		return v;
	}

	public Quiz getQuiz() {
		return Quiz;
	}

	public void setQuiz(Quiz quiz) {
		Quiz = quiz;
	}

	public AbstractGameMode getMode() {
		return Mode;
	}

	public void setMode(AbstractGameMode mode) {
		Mode = mode;
		
		mode.setGame(this);
	}

	public AbstractGameState getState() {
		return State;
	}

	public void setState(AbstractGameState state) {
		State = state;
	}

	public GamePoints getGamePoints() {
		return GamePoints;
	}

	public void setGamePoints(GamePoints gamePoints) {
		GamePoints = gamePoints;
	}

	//Genera el grafo para ser mostrado en el JBombGamePlayView
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
				g.addEdge(gp.getName()+nb.getName() , gp.getName(), nb.getName(), EdgeType.DIRECTED);
			}
		}
		return g;
	}
}