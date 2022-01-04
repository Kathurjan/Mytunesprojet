package gui.Model;

import be.Song;
import bll.BLLManager;
import bll.MpBllInterface;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.SongDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongModel {

    private ObservableList<Song> songs;
    private MpBllInterface logiclayer;

    // There is no real logic in this class other than passing through data to the BLL layer
    public SongModel(){
        songs = FXCollections.observableArrayList();
        logiclayer =  new BLLManager();
    }

    public void addSong(int length, String title, String author, String category, String path) throws SQLServerException {
        logiclayer.addSong(length, title, author, category, path);
    }

    //Fetches all songs from and adds them to the object list
    public ObservableList<Song> getAllSongs() throws SQLServerException {
        songs = FXCollections.observableArrayList();
        songs.addAll(logiclayer.getAllSongs());
        return songs;
    }
    public void deleteSong(Song deleteSong) {
        logiclayer.deleteSong(deleteSong);
    }

    public void editSongs (Song songToBeEdited,int length, String name, String author, String category, String path) {
        logiclayer.editSongs(songToBeEdited, length, name, author, category, path);
    }
}
