package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
	
	private final JPanel contentPanel = new JPanel();
	
	private BasicVisualizationServer<String, String> vv;
	
	private Layout<String, String> layout;
	
	private Graph<String, String> Grafo = new SparseMultigraph<String, String>();
	
	private Game Game;
	
	public JBombGamePlayView(JBombServerMainView JBombServerMainView, Game Game)
	{
		this.Game = Game;
		this.Grafo = this.Game.getGraph();
		this.Game.getBomb().addObserver(this);
		
		setTitle("JBomb - Estado del juego " + Game.getName());
		setResizable(false);
		
		layout = new CircleLayout<String,String>(this.Grafo);
		layout.setSize(new Dimension(300, 300));
		
		vv = new BasicVisualizationServer<String, String>(layout);
		vv.setPreferredSize(new Dimension(350, 350)); 
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<String>());
		vv.getRenderer().setVertexRenderer(new MyRenderer(this.Game));

		this.setBounds(100, 100, 500, 500);

		this.getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		contentPanel.add(vv);
		
		this.getContentPane().add(contentPanel);
		
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("SE CERROOOOOO");
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
			if (vertex.equals(this.Game.getBomb().getCurrentPlayer().getName())) {
				color = new Color(0, 255, 0);
			}else{
				color = new Color(255, 0, 0);
			}

			graphicsContext.setPaint(color);
			graphicsContext.fill(new Ellipse2D.Double(center.getX() - 10, center.getY() - 10, 20, 20));
		}
	}

	@Override
	public void update(Observable arg0, Object event) {
		GameEvent g = (GameEvent)event;
		
		if(g.equals(GameEvent.BOMB_OWNER_CHANGED))
		{
			System.out.println("Me notificaron que el dueño de la bomba cambió");
			vv.repaint();
		}
		
	}
}
