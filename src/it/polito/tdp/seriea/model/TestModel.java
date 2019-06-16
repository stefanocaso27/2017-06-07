package it.polito.tdp.seriea.model;

import java.util.HashMap;
import java.util.Map;

public class TestModel {

	public static void main(String[] args) {
		Model model = new Model();
		Map<String, Team> mappa = new HashMap<String, Team>();
		
		model.creaGrafo("2004/2005", mappa);
		model.getClassifica("2004/2005");
		
		
	}

}
