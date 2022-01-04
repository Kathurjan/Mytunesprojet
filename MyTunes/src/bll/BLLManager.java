package bll;

import be.Playlist;
import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.DALController;
import dal.DALInterface;

import java.util.List;

public class BLLManager implements MpBllInterface{ // This whole class is basically just a pass through to the DAL layer

    private DALInterface dalController;

    public BLLManager() {
        dalController = new DALController();
    }
    @Override
    public List<Playlist> getAllPlaylist(){
        return dalController.getAllPlaylist();
    }

    @Override
    public Song addToPlaylist(Playlist selectedPlaylist, Song selectedSong) {
        return dalController.addToPlaylist(selectedPlaylist, selectedSong);
    }

    @Override
    public Song addSong(int lengthOfSong, String title, String artist, String category, String filePath) throws SQLServerException {
        return dalController.addSongs(title, artist, category, lengthOfSong, filePath);
    }

    @Override
    public void deleteFromPlaylist(Playlist selectedPlaylist, Song selectedSong){
        dalController.deleteFromPlaylist(selectedPlaylist, selectedSong);
    }

    @Override
    public void editPositionInPlaylist(Playlist selectedPlaylist, Song selectedSong, Song exchangeSong){
        dalController.editSongPosition(selectedPlaylist,selectedSong,exchangeSong);
    }

    @Override
    public void deletePlaylist(Playlist playlistToBeDeleted){
        dalController.deletePlaylist(playlistToBeDeleted);
    }

    @Override
    public List<Song> getAllSongs() throws SQLServerException {
        return dalController.getAllSongs();
    }

    @Override
    public Playlist addPlaylist(String title) {
        return dalController.makePlaylist(title);
    }

    @Override
    public Song editSongs(Song selectedSong, int lengthOfSong, String title, String artist, String category, String filePath){
        return dalController.editSongs(selectedSong, title, artist, category, lengthOfSong, filePath);
    }

    @Override
    public Playlist updatePlaylist(Playlist selectedPlaylist, String title){
        return dalController.updatePlaylist(selectedPlaylist, title);
    }

    @Override
    public void deleteSong(Song selectedSong) {
        dalController.deleteSong(selectedSong);
        dalController.deleteSongFromAllPlaylists(selectedSong);
    }


}



