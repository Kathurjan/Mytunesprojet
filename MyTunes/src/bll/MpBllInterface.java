package bll;

import be.Playlist;
import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.util.List;


public interface MpBllInterface { // this interface just exists as an entry point into the BLL layer
    Song addSong(int lengthOfSong, String title, String artist, String category, String filePath) throws SQLServerException;

    List<Song> getAllSongs() throws SQLServerException;


    Playlist addPlaylist(String title);

    Song editSongs(Song selectedSong, int lengthOfSong, String title, String artist, String category, String filePath);

    void deletePlaylist(Playlist selectedPlaylist);

    Playlist updatePlaylist(Playlist selectedPlaylist, String title);


    void deleteSong(Song selectedSong);

    List<Playlist> getAllPlaylist() throws  SQLServerException;

    Song addToPlaylist(Playlist selectedPlaylist, Song selectedSong);

    void deleteFromPlaylist(Playlist selectedPlaylist, Song selectedSong);

    void editPositionInPlaylist(Playlist selectedPlaylist, Song selectedSong, Song exchangeSong);
}
