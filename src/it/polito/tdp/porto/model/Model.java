package it.polito.tdp.porto.model;


import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
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
		
		autori = new ArrayList<>(grafo.vertexSet());
	}
	
	public List<Author> getCoautori(Author a){
		return Graphs.neighborListOf(this.grafo, a);
	}
	
	

	public Graph<Author, DefaultEdge> getGrafo() {
		return grafo;
	}
	
	public List<Author> getNonCoautori(Author autore){
		List<Author> nonCoautori = new ArrayList<>();
		for(Author a : autori) {
			if(!this.getCoautori(autore).contains(a))
				nonCoautori.add(a);
		}
		return nonCoautori;
	}
	
	public List<Paper> getArticoliComuni(Author a1, Author a2){
		
		PortoDAO dao = new PortoDAO();
		
		List<Paper> articoli = new ArrayList<>();
		
		ShortestPathAlgorithm<Author, DefaultEdge> dijkstra = new DijkstraShortestPath<>(this.grafo);
		
		GraphPath<Author, DefaultEdge> path = dijkstra.getPath(a1, a2);
		List<DefaultEdge> archi = path.getEdgeList();
		
		for(DefaultEdge de : archi) {
			
			Author a = grafo.getEdgeSource(de);
			Author b = grafo.getEdgeTarget(de);
			
			Paper p = dao.getArticoloComune(a, b);
			if(p != null)
				articoli.add(p);			
			
		}
		
		return articoli;
		
		
	}
	
	
	
	
	
	

}
