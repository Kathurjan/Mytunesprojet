package gui.Controller;

import be.Playlist;
import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;


import gui.Model.PlaylistModel;

import gui.Model.SongModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.List;
import java.util.ResourceBundle;


public class MyTunesController implements Initializable {

    @FXML
    private TextField Seachbar;

    @FXML
    private Label SongDisplay;

    @FXML
    private Slider VolumeSlider;

    @FXML

    private TableView<Playlist> playlist;

    @FXML
    private TableView<Song> songlist;


    public TableColumn<Song, String> MusicTitleSet;
    @FXML
    private TableColumn<Song, String> MusicArtistset;

    @FXML
    private TableColumn<Song, String> MusicCategortset;

    @FXML
    private TableColumn<Song, Integer> Musictimeset;

    @FXML
    private TableColumn<Playlist, String> PlaylistName;

    @FXML
    private TableColumn<Playlist, Integer> PlaylistSongAmount;

    @FXML
    private TableColumn<Playlist, Integer> PlaylistTime;

    @FXML
    private TableView<Song> songsInPlaylist;

    @FXML
    private TableColumn<Song, Integer> SongInListNumber;

    @FXML
    private TableColumn<Song, String> SongInListName;

    @FXML
    private AnchorPane mainAnchorPane;


    Stage stage;
    private SongModel songModel;
    private PlaylistModel playListModel;
    private ObservableList<Song> observableListSongs = FXCollections.observableArrayList();
    private ObservableList<Playlist> observableListPlayist = FXCollections.observableArrayList();
    private MediaPlayer mediaPlayer;
    private boolean playing;
    private boolean nextsong = false;
    private boolean previoussong = false;

    // Constructor
    public MyTunesController() {
        songModel = new SongModel();
        playListModel = new PlaylistModel();
        try {
            observableListSongs = songModel.getAllSongs();
            observableListPlayist = playListModel.getAllPlaylist();
        } catch (SQLServerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateSongTableview();
        populatePlaylistTableview();
        populateSongsinplaylistTableview();
    }

    // Handles the action event on the add song button and passes it to the setup method
    public void NewSongWindow(ActionEvent event) throws IOException, SQLServerException {
        SetupSongWindow(false);
    }

    // Handles the actionevent on the edit button and passes it to the setup method.
    public void EditSongWindow(ActionEvent event) throws IOException, SQLServerException {
        SetupSongWindow(true);
    }

    // Sets up the Song window
    private void SetupSongWindow(boolean edit) throws IOException, SQLServerException {
        FXMLLoader fxmloader = new FXMLLoader();
        fxmloader.setLocation(getClass().getResource("/gui/view/SongAddView.fxml"));
        Parent part = fxmloader.load();
        SongController songController = fxmloader.getController();
        songController.setMyController(this);
        if (edit) { // checks if the popup is supposed to edit, if not it will just go to the normal add song method.
            fxmloader.<SongController>getController().setEdit(songlist.getSelectionModel().getSelectedItem());
        }
        fxmloader.<SongController>getController();
        Stage stage = new Stage();
        Scene scene = new Scene(part);
        stage.setScene(scene);
        stage.show();
    }


    // method used for populating the song view, as well as the search filter method at the bottom of the method.
    private void populateSongTableview() {
        try {
            MusicTitleSet.setCellValueFactory(new PropertyValueFactory<>("title"));
            MusicArtistset.setCellValueFactory(new PropertyValueFactory<>("artist"));
            MusicCategortset.setCellValueFactory(new PropertyValueFactory<>("category"));
            Musictimeset.setCellValueFactory(new PropertyValueFactory<>("timeToString"));

            songlist.setItems(observableListSongs);

            //initial filtered list
            FilteredList<Song> seachfilter = new FilteredList<>(observableListSongs, b -> true);
            Seachbar.textProperty().addListener((observable, oldValue, newValue) -> {
                seachfilter.setPredicate(song -> {

                    // if search value is empty then it displays the songs as it is.
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        try {
                            refreshSongList();
                        } catch (SQLServerException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                    String seachWord = newValue.toLowerCase();
                    if (song.getTitle().toLowerCase().indexOf(seachWord) > -1) {
                        return true; // data will change if song found
                    } else if (song.getArtist().toLowerCase().indexOf(seachWord) > -1) {
                        return true;// data will change if author is found
                    } else if (song.getCategory().toLowerCase().indexOf(seachWord) > -1) {
                        return true;// data will change if category is found
                    } else
                        return false;
                });
            });
            SortedList<Song> sorteddata = new SortedList<>(seachfilter);
            // binds the sorted result set with the table view;
            sorteddata.comparatorProperty().bind(songlist.comparatorProperty());
            songlist.setItems(sorteddata);


        } catch (NullPointerException e) {
            System.out.println(e);

        }

    }

    //method used for the populating the playlist tableview.
    private void populatePlaylistTableview() {
        try {// populates the tableview for playlist.

            PlaylistName.setCellValueFactory(new PropertyValueFactory<>("title"));
            PlaylistTime.setCellValueFactory(new PropertyValueFactory<>("songCount"));
            PlaylistSongAmount.setCellValueFactory(new PropertyValueFactory<>("Time"));

            playlist.setItems(observableListPlayist);

        } catch (NullPointerException e) {
            System.out.println(e);

        }

    }

    //Method used for initialising the columns in the songs in playlist tableview
    private void populateSongsinplaylistTableview() {
        try {// populates the tableview for the songs in the selected playlist
            SongInListName.setCellValueFactory(new PropertyValueFactory<>("title"));
            SongInListNumber.setCellValueFactory(new PropertyValueFactory<>("IDinsideList"));

        } catch (NullPointerException e) {
            System.out.println(e);

        }

    }

    //Method for selecting and
    @FXML
    public void PlaylistSelect(MouseEvent mouseEvent) throws IOException {
       try{ List<Song> songsInThePlaylist =  playlist.getSelectionModel().getSelectedItem().getSonglist();
           if(songsInThePlaylist.size() != 0) {
               for (int i = songsInThePlaylist.size() - 1; i >= 0; i--) { // for loop for getting each element of the playlist into the tableview and sets the ID for each one
                   songsInThePlaylist.get(i).setIDinsideList(songsInThePlaylist.size() - i);
                   songsInPlaylist.setItems(FXCollections.observableArrayList(songsInThePlaylist));
               }
           }
       else{
           songsInPlaylist.getItems().clear();
       }
       }
       catch(NullPointerException ex){
           System.out.println(ex);
            }
        }

    private void fillCurrentPlaylist(){
        try{ List<Song> songsInThePlaylist = playlist.getSelectionModel().getSelectedItem().getSonglist();
            if(songsInThePlaylist.size() != 0) {
                for (int i = songsInThePlaylist.size() - 1; i >= 0; i--) { // for loop for getting each element of the playlist into the tableview and sets the ID for each one
                    songsInThePlaylist.get(i).setIDinsideList(songsInThePlaylist.size() - i);
                    songsInPlaylist.setItems(FXCollections.observableArrayList(songsInThePlaylist));
                }
            }
            else {
                songsInPlaylist.getItems().clear();
            }
        }
        catch(NullPointerException ex){
            System.out.println(ex);
        }
    }

    // Handles the new playlist button and passes it to the setup method
    public void newPlaylistWindow(MouseEvent mouseEvent) throws IOException, SQLServerException {
        setupPlaylistWindow(false);
    }

    // Handles the edit playlist button and passes it to the setup method
    public void editPlaylistWindow(ActionEvent event) throws IOException, SQLServerException {
        setupPlaylistWindow(true);
    }

    private void setupPlaylistWindow(boolean edit) throws IOException, SQLServerException {
        FXMLLoader fxmloader = new FXMLLoader();
        fxmloader.setLocation(getClass().getResource("/gui/View/PlayListaddView.fxml"));
        Parent part = fxmloader.load();
        playlistController playlistController = fxmloader.getController();
        playlistController.setMyController(this);
        if (edit) { // checks if the window is supposed to edit, if not it will just go to the normal add playlist method.
            fxmloader.<playlistController>getController().setEdit(playlist.getSelectionModel().getSelectedItem());
        }
        fxmloader.<playlistController>getController();
        Stage stage = new Stage();
        Scene scene = new Scene(part);
        stage.setScene(scene);
        stage.show();
    }


    // Plays the next song in the current playlist
    public void nextSong(ActionEvent event) throws IOException {
        if(songsInPlaylist.getSelectionModel().getSelectedItem() !=null) {
            if (songsInPlaylist.getSelectionModel().getSelectedIndex() != -1) {
                if (songsInPlaylist.getSelectionModel().getSelectedIndex() + 1 == songsInPlaylist.getItems().size())
                    stopPlaying();

                else {
                    stopPlaying();
                    nextsong = true;
                    play();
                }
            }
        }else {
            if (songlist.getSelectionModel().getSelectedItem() != null) {
                if (songlist.getSelectionModel().getSelectedIndex() != -1) {
                    if (songlist.getSelectionModel().getSelectedIndex() + 1 == songlist.getItems().size())
                        stopPlaying();

                    else {
                        stopPlaying();
                        nextsong = true;
                        play();
                    }
                }
            }
        }
    }

    // Plays the previous song in the current playlist
    public void previousSong(ActionEvent event) throws IOException {
        if(songsInPlaylist.getSelectionModel().getSelectedItem() !=null){
        if (songsInPlaylist.getSelectionModel().getSelectedIndex() != -1) {
            if (songsInPlaylist.getSelectionModel().getSelectedIndex() - 1 == -1)
                stopPlaying();

            else {
                stopPlaying();
                previoussong = true;
                play();
            }
        }
        }else
            if(songlist.getSelectionModel().getSelectedItem() != null) {
                if (songlist.getSelectionModel().getSelectedIndex() != -1) {
                    if (songlist.getSelectionModel().getSelectedIndex() - 1 == -1)
                        stopPlaying();

                    else {
                        stopPlaying();
                        previoussong = true;
                        play();
                    }
                }
            }

    }

    // add selected song to currently selected playlist
    public void AddSongToPlaylist(ActionEvent event) throws SQLServerException {
        try{
        if (songlist.getSelectionModel().getSelectedIndex() != -1) {
            playListModel.addToPlaylist(playlist.getSelectionModel().getSelectedItem(), songlist.getSelectionModel().getSelectedItem());
            refreshPlayList();
            songsInPlaylist.getItems().clear();
            fillCurrentPlaylist();

        }}
        catch (NullPointerException ex){
            System.out.println("ERROR: NO PLAYLIST SELECTED");
        }
    }

    // Deletes song from currently selected playlist
    public void deleteFromPlaylist(ActionEvent event) throws SQLServerException {
        try {
            if (songsInPlaylist.getSelectionModel().getSelectedIndex() != -1) {
                playListModel.deleteFromPlaylist(playlist.getSelectionModel().getSelectedItem(), songsInPlaylist.getSelectionModel().getSelectedItem());
                refreshPlayList();
                fillCurrentPlaylist();

            }
        }
        catch(NullPointerException ex){
            System.out.println(ex);
        }
    }

    // Moves song up the currently selected playlist
    public void editPositionInPlaylistUP(ActionEvent event) throws SQLServerException {
        if(songsInPlaylist.getSelectionModel().getSelectedIndex() >= 1){
            int exchangeID = songsInPlaylist.getSelectionModel().getSelectedIndex() - 1;
            playListModel.editPositionInPlaylist(playlist.getSelectionModel().getSelectedItem(), songsInPlaylist.getSelectionModel().getSelectedItem(), songsInPlaylist.getItems().get(exchangeID));
            refreshPlayList();
            fillCurrentPlaylist();
        }
    }

    // Moves song down the currently selected playlist
    public void editPositionInPlaylistDOWN(ActionEvent event) throws SQLServerException {
        if(songsInPlaylist.getSelectionModel().getSelectedIndex() <= songsInPlaylist.getItems().size() - 1){
            int exchangeID = songsInPlaylist.getSelectionModel().getSelectedIndex() + 1;
            playListModel.editPositionInPlaylist(playlist.getSelectionModel().getSelectedItem(), songsInPlaylist.getSelectionModel().getSelectedItem(), songsInPlaylist.getItems().get(exchangeID));
            refreshPlayList();
            fillCurrentPlaylist();
        }
    }


    // Deletes the currently selected playlist
    public void deletePlaylist(ActionEvent event) throws SQLServerException {
        if (playlist.getSelectionModel().getSelectedIndex() != -1) {
            playListModel.deletePlaylist(playlist.getSelectionModel().getSelectedItem());
            refreshPlayList();
        }
    }

    // plays selected song
    public void playSong(ActionEvent actionEvent) {
        play();
    }

    //play song after pressing the play button
    private void play() {
        int currentSong1 = songsInPlaylist.getSelectionModel().getSelectedIndex();
        int currentSong2 = songlist.getSelectionModel().getSelectedIndex();
        if (songsInPlaylist.getSelectionModel().getSelectedItem() != null) {
            if (nextsong == false) {
                if (previoussong == false) {
                    if (playing == false) {
                        playing = true;
                    } else {
                        mediaPlayer.stop();
                    }
                } else {
                    songsInPlaylist.getSelectionModel().select(currentSong1 - 1);
                    previoussong = false;
                }
            } else {
                songsInPlaylist.getSelectionModel().select(currentSong1 + 1);
                nextsong = false;
            }
            String songToPlay = "file:///" + songsInPlaylist.getSelectionModel().getSelectedItem().getFilePath().replace("\\", "/").replace(" ", "%20");
            mediaPlayer = new MediaPlayer(new Media(songToPlay));
            SongDisplay.setText(String.valueOf(songsInPlaylist.getSelectionModel().getSelectedItem().getTitle()));
            makeSound();
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(() -> {
                if (songsInPlaylist.getSelectionModel().getSelectedIndex()+1 < songsInPlaylist.getItems().size()) {
                    songsInPlaylist.getSelectionModel().select(currentSong1 + 1);
                    String songToPlay1 = "file:///" + songsInPlaylist.getSelectionModel().getSelectedItem().getFilePath().replace("\\", "/").replace(" ", "%20");
                    mediaPlayer = new MediaPlayer(new Media(songToPlay1));
                    SongDisplay.setText(String.valueOf(songsInPlaylist.getSelectionModel().getSelectedItem().getTitle()));
                    makeSound();
                    mediaPlayer.play();
                    play();
                }
                else
                {
                    stopPlaying();
                }
            });

        }
        else {
            if (nextsong == false) {
                if (previoussong == false) {
                    if (playing == false) {
                        playing = true;
                    } else {
                        mediaPlayer.stop();
                    }
                } else {
                    songlist.getSelectionModel().select(currentSong2 - 1);
                    previoussong = false;
                }
            } else {
                songlist.getSelectionModel().select(currentSong2 + 1);
                nextsong = false;
            }
            String songToPlay = "file:///" + songlist.getSelectionModel().getSelectedItem().getFilePath().replace("\\", "/").replace(" ", "%20");
            mediaPlayer = new MediaPlayer(new Media(songToPlay));
            SongDisplay.setText(String.valueOf(songlist.getSelectionModel().getSelectedItem().getTitle()));
            makeSound();
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(() -> {
                stopPlaying();
            });

        }
    }
    // method for refreshing the playlist
    public void refreshPlayList() throws SQLServerException {
        if(playlist.getSelectionModel().getSelectedItem() != null){
            int toSet = playlist.getSelectionModel().getSelectedIndex();

            playlist.setItems(playListModel.getAllPlaylist());

            playlist.getSelectionModel().select(toSet);
        }
        else{
            System.out.println("Could not refresh playlist, please select one");
        }
    }

    //refresh songlist tableview
    public void refreshSongList() throws SQLServerException {
        songlist.setItems(songModel.getAllSongs());

    }


    //stop playing song after pressing the stop button
    public void stopSong(ActionEvent actionEvent) {
        stopPlaying();
    }

    // method for stopping the mediaplayer
    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            SongDisplay.setText("-----");
            mediaPlayer = null;
            playing = false;
        }
    }

    //if mediaPlayer is not empty then set sound
    @FXML
    private void setSound(MouseEvent event) {
        if (mediaPlayer != null)
            makeSound();
    }

    // set sound based on the slider
    private void makeSound() {
        mediaPlayer.setVolume(VolumeSlider.getValue());
    }

    // Closes the application
    public void closeAppButton(ActionEvent actionEvent) {
        stage = (Stage) mainAnchorPane.getScene().getWindow();
        stage.close();
    }

    // Fully deletes the song
    public void DeleteSong(ActionEvent event) throws SQLServerException {
        if (songlist.getSelectionModel().getSelectedIndex() != -1) { // when the song that is needed to be deleted is selected
            songModel.deleteSong(songlist.getSelectionModel().getSelectedItem()); //uses the seong delete methods from the dao.
            refreshSongList(); //refreshes the list.
            refreshPlayList();
            songsInPlaylist.getItems().clear();// refresh the songs in playlist.
            fillCurrentPlaylist();
            songlist.refresh(); // will refresh the songlist.'

        }


    }
    //select only in songsInPlaylist table or in songs table
    public void selectSongInPlaylist(MouseEvent mouseEvent) {
        songlist.getSelectionModel().select(null);
    }
    public void selectSong(MouseEvent mouseEvent) {
        songsInPlaylist.getSelectionModel().select(null);
    }
}

