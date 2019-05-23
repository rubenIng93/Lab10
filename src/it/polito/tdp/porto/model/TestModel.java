package it.polito.tdp.porto.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		System.out.println("TODO: write a Model class and test it!");
		
		model.creaGrafo();
		System.out.println("Numero vertici: "+model.getGrafo().vertexSet().size()+
				" Numero archi: "+model.getGrafo().edgeSet().size());
		
	}

}
