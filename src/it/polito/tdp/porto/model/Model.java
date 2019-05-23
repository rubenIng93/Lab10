package it.polito.tdp.porto.model;


import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private List<Author> autori;
	private Graph<Author, DefaultEdge> grafo;
	
	public List<Author> getAllAuthors(){
		PortoDAO dao = new PortoDAO();
		return dao.getAllAuthorsDiArticoli();
	}
	
	public void creaGrafo() {
		
		PortoDAO dao = new PortoDAO();
		
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getAllAuthorsDiArticoli());
		
		for(Author autore : grafo.vertexSet()) {
			List<Author> coautori = dao.getCoautoriList(autore);
			for(Author coautore : coautori) {
				if(grafo.containsVertex(coautore) && grafo.containsVertex(autore))
					grafo.addEdge(autore, coautore);
			}
		}
	}
	
	public List<Author> getCoautori(Author a){
		return Graphs.neighborListOf(this.grafo, a);
	}
	
	

	public Graph<Author, DefaultEdge> getGrafo() {
		return grafo;
	}
	
	
	
	
	
	

}
