package gui.Model;

import be.Playlist;
import be.Song;
import bll.BLLManager;
import bll.MpBllInterface;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlaylistModel {

    private ObservableList<Playlist> playlists;
    private MpBllInterface logiclayer;

    // There is no real logic in this class other than passing through data to the BLL layer
    public PlaylistModel(){
        playlists = FXCollections.observableArrayList();
        logiclayer = new BLLManager();
    }

    public void addPlaylist(String title){
        logiclayer.addPlaylist(title);
    }

    // fetches all the playlist and adds them to the object list
    public ObservableList<Playlist> getAllPlaylist() throws SQLServerException {
        playlists = FXCollections.observableArrayList();
        playlists.addAll(logiclayer.getAllPlaylist());
        return playlists;
    }

    public void updatePlaylist(Playlist selectedPlaylist, String title) {
        logiclayer.updatePlaylist(selectedPlaylist, title);
    }

    public void deletePlaylist(Playlist selectedPlaylist){
        logiclayer.deletePlaylist(selectedPlaylist);
    }

    public void addToPlaylist(Playlist selectedPlaylist, Song song){
        logiclayer.addToPlaylist(selectedPlaylist, song);
    }

    public void deleteFromPlaylist(Playlist selectedPlaylist, Song selectedSong){
        logiclayer.deleteFromPlaylist(selectedPlaylist, selectedSong);
    }

    public void editPositionInPlaylist(Playlist selectedPlaylist, Song SelectedSong, Song exchangeSong){
        logiclayer.editPositionInPlaylist(selectedPlaylist, SelectedSong, exchangeSong);
    }
}
