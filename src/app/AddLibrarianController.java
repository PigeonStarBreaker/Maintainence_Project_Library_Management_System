package app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * A librarian Controller class
 *
 * @author Mikias
 */
public class AddLibrarianController {

    @FXML
    private Button addbtn;

    @FXML
    private Button backbtn;

    @FXML
    private TextField LibName;

    @FXML
    private PasswordField libPass;

    @FXML
    private TextField libEmail;

    @FXML
    private TextField libAddress;

    @FXML
    private TextField LibCity;

    @FXML
    private TextField libContactNo;
    ArrayList<Librarian> listOfLib = new ArrayList<>();
    ArrayList<Librarian> libList = new ArrayList<>();
    Random rand = new Random();

    /**
     * add method adds a librarian onto a file
     * the add method expects an Arraylist of Librarian form a dataBase loader class
     * It uses the arraylist to check if the new librarian already existed or not. if not it will add the new librarian
     * and write the new object to a file.
     * the method uses the librarian phone number as a unique key to prevent duplicate librarian
     *
     * @param event
     * @throws IOException
     * @author
     */
    @FXML
    public void add(ActionEvent event) throws IOException {
    	Librarian librarian; // move to here
        listOfLib = FileLoader.loadLibrarianFromFile();

        // Validate input fields
        if (LibName.getText().isEmpty() || libPass.getText().isEmpty() || libEmail.getText().isEmpty() ||
            LibCity.getText().isEmpty() || libAddress.getText().isEmpty() || libContactNo.getText().isEmpty()) {
            showError("You have to fill all text fields.");
            return;
        }

        // Validate name length
        if (LibName.getText().length() < 3) {
            showError("Name must be at least 3 characters long.");
            return;
        }

        // Validate email format
        if (!libEmail.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showError("Invalid email format. Please enter a valid email address.");
            return;
        }

        // Validate password length
        if (libPass.getText().length() < 8) {
            showError("Password must be at least 8 characters long.");
            return;
        }

        // Validate contact number format (e.g., starts with a digit, 10-15 digits total)
        if (!libContactNo.getText().matches("^\\+?\\d{10,15}$")) {
            showError("Invalid contact number. Please enter a valid phone number (10-15 digits).");
            return;
        }

        // Create librarian object from the text fields
        librarian = new Librarian(LibName.getText(), libPass.getText(), libEmail.getText(), libAddress.getText(),
                LibCity.getText(), libContactNo.getText());

        int id = rand.nextInt(100) + 1;
        librarian.setID(id);

        FileOutputStream filename = new FileOutputStream("Librarian.txt");
        ObjectOutputStream libObj = new ObjectOutputStream(filename);

        // Check if the librarian already exists in the system
        boolean found = false;
        for (Librarian lib : listOfLib) {
            if (lib.getContactNum().equals(libContactNo.getText())) {
                duplicateLibrarian();
                found = true;
                break;
            }
        }

        if (!found) {
            listOfLib.add(librarian);
            libObj.writeObject(listOfLib);
            libObj.close();
            addedSuccessfully();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }


    public void addedSuccessfully() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Adding Completed");
        alert.setContentText("The Librarian is already successfully added");
        alert.showAndWait();
    }

    public void duplicateLibrarian() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Can not add Librarian");
        alert.setTitle("Librarian Error");
        alert.setContentText("The Librarian is existed in the Database");
        alert.showAndWait();
    }

    @FXML
    void back(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("AdminSection.fxml"));
        Scene scene = new Scene(root, 600, 600);
        Stage stage = (Stage) backbtn.getScene().getWindow();
        stage.setScene(scene);
    }
    
}

