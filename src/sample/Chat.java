package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.IOException;

/**
 * Controller for the chat window/scene.
 */
public class Chat {
    ConnectClient connection;
    @FXML
    TextArea input_textArea= new TextArea();
    @FXML
    ListView chat_listview = new ListView();
    @FXML
    Label connection_status_label = new Label();


    /**
     * Method to bind and pass the connection object to this window.
     * @param connection
     */
    public void setChatConnection(ConnectClient connection) {
        this.connection = connection;
        chat_listview.setItems(connection.getMessages());
    }

    /**
     * Action method for the "Send" button that sends the user input to server.
     */
    public void sendButtonHandler() {
        if(!input_textArea.getText().trim().isEmpty()){
            connection.sendMessage(input_textArea.getText());
        input_textArea.setText("");
        }
    }

    /**
     * Action Method that handles exiting the applicaiton on button press.
     * @throws IOException
     */
    public void exitButtonHandler() throws IOException {
        connection.disconnect();
        System.exit(0);
    }
}
