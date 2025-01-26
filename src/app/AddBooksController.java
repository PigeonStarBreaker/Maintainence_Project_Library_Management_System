package app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddBooksController {

	@FXML
	private AnchorPane pane;

	@FXML
	private TextField name;

	@FXML
	private TextField CallNo;

	@FXML
	private TextField author;

	@FXML
	private TextField publisher;

	@FXML
	private TextField quantity;

	@FXML
	private Button addBtn;

	@FXML
	private Button backBtn;

	@FXML
    void add(ActionEvent event) throws IOException {
        Alert alert;

        // Check for empty fields
        if (name.getText().isBlank() || CallNo.getText().isBlank()
                || author.getText().isBlank() || publisher.getText().isBlank()
                || quantity.getText().isBlank()) {
            alert = new Alert(Alert.AlertType.ERROR, "All fields must be completed.");
            alert.showAndWait();
            return;
        }

        // Validate name (at least 3 alphabetic characters)
        if (!name.getText().matches("[a-zA-Z]{3,}")) {
            alert = new Alert(Alert.AlertType.ERROR, "Name must contain at least 3 alphabetic characters.");
            alert.showAndWait();
            return;
        }

        // Validate call number (alphanumeric, minimum 3 characters, no spaces)
        if (!CallNo.getText().matches("[0-9]{10,}")) {
            alert = new Alert(Alert.AlertType.ERROR, "Call Number must be alphanumeric and at least 3 characters long.");
            alert.showAndWait();
            return;
        }

        // Validate author name (at least 3 alphabetic characters)
        if (!author.getText().matches("[a-zA-Z]{3,}")) {
            alert = new Alert(Alert.AlertType.ERROR, "Author name must contain at least 3 alphabetic characters.");
            alert.showAndWait();
            return;
        }

        // Validate publisher name (at least 3 alphabetic characters)
        if (!publisher.getText().matches("[a-zA-Z]{3,}")) {
            alert = new Alert(Alert.AlertType.ERROR, "Publisher name must contain at least 3 alphabetic characters.");
            alert.showAndWait();
            return;
        }

        // Validate quantity (must be a positive integer)
        int q;
        try {
            q = Integer.parseInt(quantity.getText());
            if (q <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            alert = new Alert(Alert.AlertType.ERROR, "Quantity must be a positive integer.");
            alert.showAndWait();
            return;
        }

        // Create a book from the inputs
        Book book = new Book(name.getText(), CallNo.getText(), author.getText(), publisher.getText(), q);

        // Get the book data as an array list
        ArrayList<Book> bookData = getData("src/books.ser");

        // Add the new object into the book data
        bookData.add(book);

        // Store this array list
        storeData(bookData, "src/books.ser");

        alert = new Alert(Alert.AlertType.CONFIRMATION, "Book added successfully.");
        alert.showAndWait();
    }

	@FXML
	void back(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("LibrarianSection.fxml"));
		Scene scene = new Scene(root, 600, 600);
		Stage stage = (Stage) pane.getScene().getWindow();
		stage.setScene(scene);
	}

	public static void main(String[] args) {
		System.out.println(getData("src/books.ser"));
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<Book> getData(String fileName) {
		
		ArrayList<Book> books2 = new ArrayList<>();
		// FileInputStream fis;
		try {
			FileInputStream fis = new FileInputStream("src/books.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			books2 = (ArrayList<Book>) ois.readObject();				// SuppressWarnings
			
			fis.close();
			ois.close();
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return books2;
	}

	public static void storeData(ArrayList<Book> books, String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream("src/books.ser");
			ObjectOutputStream bos = new ObjectOutputStream(fos);
			bos.writeObject(books);
			fos.close();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
