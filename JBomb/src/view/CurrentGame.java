package view;

import core.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

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

public class CurrentGame extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public Graph<String, String> g;

	private Game Game; 
	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { try { CurrentGame dialog = new
	 * CurrentGame(); dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	 * dialog.setVisible(true); } catch (Exception e) { e.printStackTrace(); } }
	 */

	/**
	 * Create the dialog.
	 */
	public CurrentGame() {
		// Prueba del grafo
		this.g = new SparseMultigraph<String, String>();
		this.g.addVertex("Jugador 1");
		this.g.addVertex("Jugador 2");
		this.g.addVertex("Jugador 3");
		this.g.addEdge("Edge-A", "Jugador 1", "Jugador 2");
		this.g.addEdge("Edge-B", "Jugador 2", "Jugador 3");

		Layout<String, String> layout = new CircleLayout(this.g);
		layout.setSize(new Dimension(300, 300));
		BasicVisualizationServer<String, String> vv = new BasicVisualizationServer<String, String>(
				layout);
		vv.setPreferredSize(new Dimension(350, 350)); // Sets the viewing area
														// size
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().setVertexRenderer(new MyRenderer());

		setBounds(100, 100, 500, 500);
		Container contentPane = getContentPane();

		contentPane.setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JButton okButton = new JButton("Siguiente Jugada");

		contentPanel.add(vv);
		contentPane.add(contentPanel, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		buttonPane.add(okButton);

	}

	static class MyRenderer implements Renderer.Vertex<String, String> {
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
