package it.polito.tdp.porto;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleCoautori(ActionEvent event) {
    	
    	model.creaGrafo();    	
    	txtResult.clear();
    	Author a = boxPrimo.getValue();
    	txtResult.appendText("Lista dei coautori di "+a+":\n");
    	for(Author co : model.getCoautori(a)) {
    		txtResult.appendText(" - "+co+"\n");
    	}
    	boxSecondo.getItems().addAll(model.getNonCoautori(a));
    	
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	
    	txtResult.clear();
    	Author a = boxPrimo.getValue();
    	Author b = boxSecondo.getValue();
    	if(b.equals(null)) {
    		txtResult.appendText("Selezionare il secondo autore");
    		return;
    	}
    		
    	txtResult.appendText("Lista degli articoli comuni\n\n");
    	for(Paper articolo : model.getArticoliComuni(a, b)) {
    		txtResult.appendText(articolo+"\n");
    	}

    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxPrimo.getItems().addAll(model.getAllAuthors());
    }
}
