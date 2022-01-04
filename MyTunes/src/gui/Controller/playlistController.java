package gui.Controller;

import be.Playlist;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import gui.Model.PlaylistModel;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class playlistController {
    Stage stage;
    @FXML
    private AnchorPane scenePane2;
    @FXML
    private TextField titleString;

    private final PlaylistModel playlistModel;
    private boolean isEditing = false;
    private Playlist playlistToBeEdited;
    private MyTunesController myTunesController;


    public playlistController(){
        playlistModel = new PlaylistModel();
    }
    public void setMyController(MyTunesController myTunesController){
        this.myTunesController = myTunesController;
    }
    // adds or edits a playlist dependent on the button pushed from the mytunescontroller
    public void addorEditPlaylist(ActionEvent event) throws SQLServerException {
        String name = titleString.getText().trim();
        if(name.length() > 0 && name.length() < 50 ){
            if(!isEditing){
                playlistModel.addPlaylist(name);
            }
            else {
                playlistModel.updatePlaylist(playlistToBeEdited, name);
            }
        }
        myTunesController.refreshPlayList();
        stage = (Stage) scenePane2.getScene().getWindow();
        stage.close();
    }

    // Fetches the info regarding selected playlist to enable editing. If nothing is selected returns error message
    public void setEdit(Playlist selectedPlaylist) throws SQLServerException {
        if (selectedPlaylist != null){
            playlistToBeEdited = selectedPlaylist;
            isEditing = true;
            titleString.setText(selectedPlaylist.getTitle());
        }
        else{
            titleString.setText("No playlist selected, try again");
        }
        myTunesController.refreshPlayList();
    }

    // closes window
    public void closePlaylistViewMethod(ActionEvent event) {
        stage = (Stage) scenePane2.getScene().getWindow();
        stage.close();
    }
}
