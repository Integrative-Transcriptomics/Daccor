package userinterface;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class EventHandlingController {

	@FXML
	private Button inputBrowse;
	
	@FXML
	private CheckBox myCheckBox;
	
	@FXML
	private TextField kmerValue;
	
	@FXML
	private TextField mismatchValue;
	
	@FXML
	private TextField lengthValue;

	/**
	 * The constructor (is called before the initialize()-method).
	 */
	public EventHandlingController() {

	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		
		
		// Handle CheckBox event.
		myCheckBox.setOnAction((event) -> {
			boolean selected = myCheckBox.isSelected();
			System.out.println("CheckBox Action (selected: " + selected + ")\n");
		});
		
		kmerValue.textProperty().addListener((observable, oldValue, newValue) -> {
		    System.out.println("kmer Text Changed (newValue: " + newValue + ")");
		});

		// Handle TextField enter key event.
		kmerValue.setOnAction((event) -> {
		    System.out.println("TextField Action");
		});
		
		mismatchValue.textProperty().addListener((observable, oldValue, newValue) -> {
		    System.out.println("TextField Text Changed (newValue: " + newValue + ")");
		});

		// Handle TextField enter key event.
		mismatchValue.setOnAction((event) -> {
		    System.out.println("TextField Action");
		});
		
		lengthValue.textProperty().addListener((observable, oldValue, newValue) -> {
		    System.out.println("TextField Text Changed (newValue: " + newValue + ")");
		});

		// Handle TextField enter key event.
		lengthValue.setOnAction((event) -> {
		    System.out.println("TextField Action");
		});
	}


}
