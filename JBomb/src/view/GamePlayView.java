package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import core.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

public class GamePlayView {
	private final JPanel contentPanel = new JPanel();
	
	private JDialog dialog = new JDialog();
	
	public Graph<String, String> Grafo = new SparseMultigraph<String, String>();

	private Game Game; 
	
	private BasicVisualizationServer<String, String> vv;
	
	public GamePlayView(Game g) {
		this.Game = g;
		// Prueba del grafo
		this.Grafo = new SparseMultigraph<String, String>();
		this.Grafo.addVertex("Andres");
		this.Grafo.addVertex("Lucio");
		this.Grafo.addVertex("Jugador 1");
		this.Grafo.addVertex("Duilio");
		this.Grafo.addEdge("Edge-A", "Andres", "Lucio");
		this.Grafo.addEdge("Edge-B", "Lucio", "Jugador 1");
		this.Grafo.addEdge("Edge-c", "Jugador 1", "Duilio");
		this.Grafo.addEdge("Edge-D", "Duilio", "Andres");
		//this.Grafo = this.Game.getGraph();
		
		Layout<String, String> layout = new CircleLayout(this.Grafo);
		layout.setSize(new Dimension(300, 300));
		vv = new BasicVisualizationServer<String, String>(layout);
		vv.setPreferredSize(new Dimension(350, 350)); 
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().setVertexRenderer(new MyRenderer(this.Game));

		this.dialog.setBounds(100, 100, 500, 500);
		Container contentPane = this.dialog.getContentPane();

		contentPane.setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JButton okButton = new JButton("Siguiente Jugada");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO
				//boolean resultado = Game.play();
				//Grafo = Game.getGraph();
			}
		});
		contentPanel.add(vv);
		contentPane.add(contentPanel, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		buttonPane.add(okButton);

	}

	public JDialog getDialog()
	{
		return this.dialog;
	}
	
	public void setDialog(JDialog dialog)
	{
		this.dialog = dialog;
	}
	
	static class MyRenderer implements Renderer.Vertex<String, String> {
		Game Game = new Game();
		
		public MyRenderer(Game g)
		{
			super();
			this.Game = g;
		}
		
		@Override
		public void paintVertex(RenderContext<String, String> rc,
				Layout<String, String> layout, String vertex) {

			GraphicsDecorator graphicsContext = rc.getGraphicsContext();
			Point2D center = layout.transform(vertex);
			Color color = null;
			if (vertex.equals("Jugador 1")) {
				color = new Color(0, 255, 0);
			} else {
				color = new Color(255, 0, 0);
			}

			graphicsContext.setPaint(color);
			graphicsContext.fill(new Ellipse2D.Double(center.getX() - 10,
					center.getY() - 10, 20, 20));
		}
	}
}
