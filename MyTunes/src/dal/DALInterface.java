package dal;

import be.Playlist;
import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.util.List;

public interface DALInterface {

    List<Song> getAllSongs() throws SQLServerException;

    void deleteSong(Song selectedSong);

    Song editSongs(Song selectedSong, String title, String artist, String category, int lengthOfSong, String filePath);

    Song addSongs(String title, String artist, String category, int lengthOfSong, String filePath) throws SQLServerException;

    void deletePlaylist(Playlist selectedPlaylist);

    Playlist makePlaylist(String title);

    List<Playlist> getAllPlaylist();

    void deleteFromPlaylist(Playlist selectedPlaylist, Song selectedSong);

    Song addToPlaylist(Playlist selectedPlaylist, Song selectedSong);

    Playlist updatePlaylist(Playlist selectedPlaylist, String title);

    void editSongPosition(Playlist selectedPlaylist, Song selectedSong, Song exchangeSong);

    void deleteSongFromAllPlaylists(Song selectedSong);
}
