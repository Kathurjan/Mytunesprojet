package gui.Controller;

import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import gui.Model.SongModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.control.Label;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class SongController {

    Stage stage;

    @FXML
    private TextField titleString;
    @FXML
    private TextField artistString;
    @FXML
    private TextField lengthOfSongString;
    @FXML
    private TextField categoryString;
    @FXML
    private TextField filePathString;
    @FXML
    private AnchorPane scenePane;
    @FXML
    private Label ErrorSuccesLabel;

    private int length = 0;
    private boolean isEditing = false;
    private MyTunesController myTunesController;
    private final SongModel songmodel;
    private Song songToBeEdited;


    public SongController(){
        songmodel = new SongModel();
    }


    public void setMyController(MyTunesController myTunesController){
        this.myTunesController = myTunesController;
    }
    @FXML // Handles the filechooser and also calculates the length of the song. Probably should separate this.
    private void openFileMethod(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        String path = file.getPath();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File filestring = new File(path);
        Media file1 = new Media(filestring.toURI().toString());
        filePathString.setText(filestring.getAbsolutePath());

        MediaPlayer mediaPlayer = new MediaPlayer(file1);

        mediaPlayer.setOnReady(new Runnable() {

            @Override
            public void run() {
                int hours = 0;
                int minutes = 0;
                int seconds = 0;
                String hoursString;
                String minutesString;
                String secondsString;
                String finalTimeString;
                hours = (int) file1.getDuration().toHours();
                if(hours < 10){
                    hoursString = "0" + hours;
                }
                else{
                    hoursString = "" + hours;
                }
                minutes = (int) file1.getDuration().toMinutes() % 60;
                if(minutes < 10){
                    minutesString = "0" + minutes;
                }
                else{
                    minutesString = "" + minutes;
                }
                seconds = (int) file1.getDuration().toSeconds() % 60;
                if(seconds < 10){
                    secondsString = "0" + seconds;
                }
                else{
                    secondsString = "" + seconds;
                }


                finalTimeString = hoursString + ":" + minutesString + ":" + secondsString;

                length = (int) file1.getDuration().toSeconds();
                if(hours + minutes + seconds == 0 || finalTimeString == null){
                    lengthOfSongString.setText("00:00:00");
                }
                else{
                    lengthOfSongString.setText(finalTimeString);
                }

                System.out.println(hours + ":" + minutes + ":" + seconds);
            }
        });
    }

    // Misleading title, it acts as both the CREATE and UPDATE method by checking the isEditing boolean
    public void addSongMethod(ActionEvent event) throws SQLServerException {

        String name = titleString.getText().trim();
        if(!isEditing){
            if (name.length() > 0 && name.length() < 50 && filePathString != null && filePathString.getText().length() != 0){
                songmodel.addSong(length, name, artistString.getText(), categoryString.getText(), filePathString.getText());
                titleString.clear();
                artistString.clear();
                categoryString.clear();
                filePathString.clear();
                lengthOfSongString.clear();
                songmodel.getAllSongs().setAll();

                myTunesController.refreshSongList();
                ErrorSuccesLabel.setText("Succesfully added song, congrats");


            }
            else {
                ErrorSuccesLabel.setText("Something went wrong, try again");
            }
        }
        else{
            songmodel.editSongs(songToBeEdited, length, name, artistString.getText(), categoryString.getText(), filePathString.getText());
        }
    }

    // checks the user had a song selected when pressing edit, if not then it displays error message and acts like the normal add song window.
    public void setEdit(Song song) throws SQLServerException {
        if(song != null){
            songToBeEdited = song;
            isEditing = true;
            titleString.setText(song.getTitle());
            artistString.setText(song.getArtist());
            filePathString.setText(song.getFilePath());
            categoryString.setText(song.getCategory());
            lengthOfSongString.setText(song.getTimeToString());
            lengthOfSongString.setEditable(false);
            myTunesController.refreshSongList();
        }
        else{
            ErrorSuccesLabel.setText("No song selected, please try again");
        }

    }

    // closes the window
    public void closeSongViewMethod(ActionEvent event) throws SQLServerException {

        stage = (Stage) scenePane.getScene().getWindow();
        stage.close();
    }

}
