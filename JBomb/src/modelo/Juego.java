package modelo;

import java.awt.Dimension;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class Juego {
	private int CantidadRondas;
	private int MinimoJugadores;
	private int CantidadJugadores;
	public Graph<Integer, String> g;

	public Juego() {
		this.g = new SparseMultigraph<Integer, String>();
		this.g.addVertex((Integer) 1);
		this.g.addVertex((Integer) 2);
		this.g.addVertex((Integer) 3);
		this.g.addEdge("Edge-A", 1, 2); // Note that Java 1.5 auto-boxes
										// primitives
		this.g.addEdge("Edge-B", 2, 3);
		System.out.println("The graph g = " + g.toString());
		// Note that we can use the same nodes and edges in two different
		// graphs.
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Juego a = new Juego();

		// SimpleGraphView sgv = new SimpleGraphView(); //We create our graph in
		// here
		// The Layout<V, E> is parameterized by the vertex and edge types
		Layout<Integer, String> layout = new CircleLayout(a.g);
		layout.setSize(new Dimension(300, 300)); // sets the initial size of the
													// space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(
				layout);
		vv.setPreferredSize(new Dimension(350, 350)); // Sets the viewing area
														// size

		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

	}

}
