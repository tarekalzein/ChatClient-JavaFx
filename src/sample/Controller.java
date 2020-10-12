package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the controller of the first and main scene that takes connection information from user and instantiates
 * the socket connection, on success it will open the chat scene and pass the connection instance with it.
 */
public class Controller {
    @FXML
    Button connect_button = new Button();
    @FXML
    TextField host_textField = new TextField();
    @FXML
    TextField port_textField = new TextField();
    @FXML
    TextField username_textField= new TextField();
    @FXML
    Button exit_button = new Button();

    /**
     * Method to run on initialization.
     */
    public void initialize(){
        connect_button.setDisable(true);
    }

    /**
     * Action method to validate the host and the port text fields and grey out the connect button while the input is invalid.
     */
    public void handleKeyReleased()
    {
        boolean disableButton =
                host_textField.getText().trim().isEmpty() ||
                        port_textField.getText().trim().isEmpty() ||
                        !tryParsePort(port_textField.getText());
        connect_button.setDisable(disableButton);
    }

    /**
     * Method to parse the port number from string.
     * @param portString
     * @return
     */
    private boolean tryParsePort(String portString)
    {
        try{
            Integer.parseInt(portString);
        }catch (NumberFormatException e) {
            Alert alert= new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Message");
            alert.setHeaderText("Wrong Port Number Format");
            alert.setContentText("Port number can only contain digits.\n" +
                    "Please choose a number between 0 and 65535");
            alert.showAndWait();
            return false;
        } return true;
    }

    /**
     * Action method for the "connect" button onAction.
     * It takes the validated user input and creates a socket connection in an instance of the Connection class.
     * Shows an alert window if connection is unsuccessful.
     * @throws IOException
     */
    public void connect_button() throws IOException {

        ConnectClient connection = new ConnectClient(
                host_textField.getText(),
                Integer.parseInt(port_textField.getText())
        );

        if (connection.connect())
        {
            Stage stage= (Stage) connect_button.getScene().getWindow();
            FXMLLoader loader= new FXMLLoader(
                    getClass().getResource("chat.fxml")
            );
            stage.setScene(new Scene(loader.load()));
            Chat chat= loader.getController();
            chat.setChatConnection(connection);
            stage.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection error");
            alert.setHeaderText("Couldn't connect to server");
            alert.setContentText("Unsuccessful attempt to connect to chat server" +
                    "\n Check your connection or contact server admin.");
            alert.showAndWait();
        }
    }
}
