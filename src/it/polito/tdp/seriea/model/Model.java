package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	// Variabili utilizzate dalla ricorsione
	private List<Team> bestDomino ;
	private Set<DefaultWeightedEdge> usedEdges ;
	
	private Map<String, Team> idMap;
	private List<String> annate;
	private SerieADAO dao;
	private SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge> grafo;
	private List<Team> squadre;
	private List<Insieme> lista;
	private List<Team> classifica;
	private List<Team> tutte;
	
	public Model() {
		this.annate = new ArrayList<String>();
		this.dao = new SerieADAO();
		this.squadre = new LinkedList<Team>();
		this.lista = new LinkedList<Insieme>();
		this.classifica = new LinkedList<Team>();
		this.idMap = new HashMap<String, Team>();
		this.tutte = new LinkedList<Team>();
	}
	
	public List<String> getAnnate() {
		this.annate = dao.caricaAnnate();
		
		return annate;
	}
	
	public void creaGrafo(String stagione, Map<String, Team> idMap) {
		this.grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.tutte = dao.listaSquadreMap(idMap);
		this.squadre = dao.getSquadreForAnno(stagione, idMap);
		this.lista = dao.getPuntiClassifica(stagione, idMap);
		
		Graphs.addAllVertices(grafo, squadre);
		
		for(Insieme i : lista) {
			DefaultWeightedEdge e = grafo.getEdge(i.getT1(), i.getT2()); 
			if(e == null && i.getFtr().equals("H")) {
				Graphs.addEdge(grafo, i.getT1(), i.getT2(), 1);
			}
			else if((e == null && i.getFtr().equals("A"))) {
				Graphs.addEdge(grafo, i.getT1(), i.getT2(), -1);
			}
			else if((e == null && i.getFtr().equals("D"))) {
				Graphs.addEdge(grafo, i.getT1(), i.getT2(), 0);
			}
		}
		
		System.out.println("# vertici: " + grafo.vertexSet().size());
		System.out.println("# archi: " + grafo.edgeSet().size());
	}
	
	public List<Team> getClassifica(String stagione) {
		// azzero i punteggi
		for(Team t : squadre)
			t.setPunti(0);
		 
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			Team home = grafo.getEdgeSource(e);
			Team away = grafo.getEdgeTarget(e);
			
			switch ((int) grafo.getEdgeWeight(e)) {
			case +1:
				home.setPunti(home.getPunti() + 3);
				break;
			case -1:
				away.setPunti(away.getPunti() + 3);
				break;
			case 0:
				home.setPunti(home.getPunti() + 1);
				away.setPunti(away.getPunti() + 1);
				break;
			}
		}
		
		this.classifica = new ArrayList<Team>(grafo.vertexSet());
		Collections.sort(classifica, new Comparator<Team>() {

			@Override
			public int compare(Team o1, Team o2) {
				return -(o1.getPunti() - o2.getPunti());
			}
		});

		System.out.println(classifica);
		return classifica;
	}
	
	public List<Team> calcolaDomino() {
		this.bestDomino = new ArrayList<>() ;
		this.usedEdges = new HashSet<>() ;
		
		List<Team> path = new ArrayList<>() ;
		this.riduciGrafo(8);
		
		for(Team initial : grafo.vertexSet()) {
			path.add(initial) ;
			dominoRecursive(1, initial, path) ;
			path.remove(initial) ;
		}
		
		return this.bestDomino ;
	}
	
	private void dominoRecursive(int step, Team t1, List<Team> path) {
		
		if(path.size() > this.bestDomino.size()) {
			this.bestDomino.clear();
			this.bestDomino.addAll(path);
		}
		
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(t1)) {
			Team t2 = grafo.getEdgeTarget(e) ;
			
			if(grafo.getEdgeWeight(e) == +1 && !this.usedEdges.contains(e)) {
				path.add(t2) ;
				usedEdges.add(e) ;
				dominoRecursive(step+1, t2, path);
				usedEdges.remove(e) ;
				path.remove(path.size()-1) ; // togli l'ultimo aggiunto
			}
		}
	}

	private void riduciGrafo(int dim) {
		Set<Team> togliere = new HashSet<>() ;
		
		Iterator<Team> iter = grafo.vertexSet().iterator() ;
		for(int i=0; i<grafo.vertexSet().size()-dim; i++) {
			togliere.add(iter.next()) ;
		}
		
		grafo.removeAllVertices(togliere) ;
		System.err.println("Attenzione: cancello dei vertici dal grafo");
		System.err.println("Vertici rimasti: "+grafo.vertexSet().size()+"\n");
	}

}
