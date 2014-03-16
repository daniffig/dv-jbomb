package core;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.Vector;

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
	private Bomb Bomb = new Bomb();

	private Quiz Quiz;

	private AbstractLinkageStrategy LinkageStrategy;
	private AbstractGameMode Mode;
	private AbstractGameState State;


	public Game()
	{
		this.setState(new NewGameState());		
	}

	public Game (String name)
	{
		this();

		this.setName(name);
	}

	public Game (Game Game)
	{
		this.setName(Game.getName());
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

		this.getBomb().setDetonationMilliseconds((long)(this.RoundDuration.getDuration()*1000));
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

	public synchronized Integer addGamePlayer(GamePlayer p) {
		if (this.canAddPlayer()){
			Integer new_player_id = this.getGamePlayers().size()+1;
			p.setId(new_player_id);
			this.getGamePlayers().add(p);

			return new_player_id;
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
		if(this.CurrentRound == 0) this.getGamePoints().initializeGeneralPoints(this.GamePlayers);

		this.CurrentRound++;

		this.getGamePoints().initializeNewRoundPoints(this.GamePlayers);
		this.getBomb().setLastPlayer(null);
		this.getBomb().setCurrentPlayer(this.getGamePlayers().get((int)(Math.random() * this.getGamePlayers().size())));
	}

	public void configureAdjacentPlayersGraph()
	{
		this.getLinkageStrategy().link(this.getGamePlayers());
	}

	public synchronized void suscribeToBombDetonation(ClientThread ct)
	{
		//Suscribo el clientThread para que observer la bomba para cuando explote
		System.out.println("Agregue el observer " + ct.toString() + " para que chequee la bomba");
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
		v.add(this.getGamePlayersOverMaxGamePlayers());

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
	}

	public String getGamePlayersOverMaxGamePlayers()
	{
		return this.getGamePlayers().size() + "/" + this.getMaxGamePlayersAllowed();
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

	//Utilizado por la librería grafica Jung
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