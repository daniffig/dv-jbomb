package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import network.GameServer;

import reference.GameEvent;

import core.Game;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;


public class JBombGamePlayView extends JFrame implements Observer {
		
	private static final long serialVersionUID = 1L;
	
	//Atributos de la vista
	private final JPanel contentPanel = new JPanel();
	
	private BasicVisualizationServer<String, String> vv;
	
	private Layout<String, String> layout;

	private JTable ScoreTable;
	
	//Atributos del modelo
	private Graph<String, String> Grafo = new SparseMultigraph<String, String>();
	
	public final Game Game;
	
	public JBombGamePlayView(JBombServerMainView JBombServerMainView, Game Game)
	{
		this.Game = Game;
		this.Grafo = this.Game.getGraph();
		this.Game.getBomb().addObserver(this);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(JBombGamePlayView.class.getResource("/images/icon.png")));
		setTitle("JBomb - Estado del juego " + Game.getName());
		setResizable(false);
		setBounds(100, 100, 500, 600);
		
		layout = new CircleLayout<String,String>(this.Grafo);
		layout.setSize(new Dimension(300, 300));
		
		vv = new BasicVisualizationServer<String, String>(layout);
		vv.setPreferredSize(new Dimension(350, 350)); 
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
		vv.getRenderer().setVertexRenderer(new MyRenderer(this.Game));

		this.getContentPane().setLayout(new FlowLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		contentPanel.add(vv);
				
		Vector<String> ScoreTableFields = new Vector<String>();
		ScoreTableFields.add("Jugador");
		ScoreTableFields.add("Puntaje Ronda actual");
		ScoreTableFields.add("Puntaje General");
		
		JScrollPane scrollPane = new JScrollPane();
		this.getContentPane().add(scrollPane);
	/*	
		ScoreTable = new JTable(new DefaultTableModel(this.Game.getGamePoints().getVectorScoreBoard(), ScoreTableFields){
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		});
		
		scrollPane.setViewportView(ScoreTable);
		*/
		this.getContentPane().add(new JLabel("<html><i>El vertice de color <font color='red'>rojo</font> indica el poseedor de la bomba.</i></html>"));
		this.getContentPane().add(contentPanel);
		this.getContentPane().add(scrollPane);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JBombGamePlayView JBGPV = JBombGamePlayView.this;
				
				JBGPV.Game.getBomb().deleteObserver(JBGPV);
		    }
		});
	}
	
	static class MyRenderer implements Renderer.Vertex<String, String> {
		Game Game = new Game();
		
		public MyRenderer(Game g)
		{
			super();
			this.Game = g;
		}
		
		@Override
		public void paintVertex(RenderContext<String, String> rc, Layout<String, String> layout, String vertex)
		{
			GraphicsDecorator graphicsContext = rc.getGraphicsContext();
			Point2D center = layout.transform(vertex);
			Color color = null;
			
			if (vertex.equals(this.Game.getBomb().getCurrentPlayer().getName())) color = new Color(0, 255, 0);
			else color = new Color(255, 0, 0);

			graphicsContext.setPaint(color);
			graphicsContext.fill(new Ellipse2D.Double(center.getX() - 10, center.getY() - 10, 20, 20));
		}
	}

	@Override
	public void update(Observable arg0, Object event) {
		GameEvent g = (GameEvent)event;
		
		switch(g){
			case BOMB_OWNER_CHANGED:
				System.out.println("[Server-GamePlayView] Me notificaron que el dueño de la bomba cambió");
				vv.repaint();
				break;
			case BOMB_EXPLODED:
				System.out.println("[Server-GamePlayView] Me notificaron que exploto todo");
				this.refreshScoreTable();
				break;
			default:
				break;
		}
	}
	
	public void refreshScoreTable()
	{
		Vector<String> ScoreTableFields = new Vector<String>();
		ScoreTableFields.add("Jugador");
		ScoreTableFields.add("Puntaje en la ronda actual");
		ScoreTableFields.add("Puntaje General");
				
		//JScrollPane scrollPane = new JScrollPane();
		//scrollPane.setBounds(12, 12, 570, 320);
		//frmJbombV.getContentPane().add(scrollPane);
		/*
		ScoreTable.setModel(new DefaultTableModel(this.Game.getGamePoints().getVectorScoreBoard(), ScoreTableFields){
			private static final long serialVersionUID = 1L;

		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		});
		*/
		((DefaultTableModel)this.ScoreTable.getModel()).fireTableDataChanged();
	}
}
