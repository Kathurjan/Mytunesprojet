package dal;

import be.Playlist;
import be.Song;

import java.util.List;

public class DALController implements DALInterface {

    private SongDAO songDAO;
    private PlaylistDAO playlistDAO;
    private SongsInPlaylistDAO songsInPlaylistDAO;

    public DALController(){
        songDAO = new SongDAO();
        playlistDAO = new PlaylistDAO();
        songsInPlaylistDAO = new SongsInPlaylistDAO();
    }

    @Override
    public List<Song> getAllSongs() {
        return songDAO.getAllSongs();
    }

    @Override
    public void deleteSong(Song selectedSong) {
        songDAO.deleteSong(selectedSong);
        songsInPlaylistDAO.deleteSongFromAllPlaylists(selectedSong);
    }

    @Override
    public Song editSongs(Song selectedSong, String title, String artist, String category, int lengthOfSong, String filePath) {
        return songDAO.editSongs(selectedSong, title, artist, category, lengthOfSong, filePath);
    }

    @Override
    public Song addSongs(String title, String artist, String category, int lengthOfsong, String filePath) {
        return songDAO.addSongs(title, artist, category, lengthOfsong, filePath);
    }

    @Override
    public void deletePlaylist(Playlist selectedPlaylist) {
        playlistDAO.deletePlaylist(selectedPlaylist);
    }

    @Override
    public Playlist makePlaylist(String title) {
        return playlistDAO.makePlaylist(title);
    }

    @Override
    public List<Playlist> getAllPlaylist() {
        return playlistDAO.getAllPlaylist();
    }

    @Override
    public Playlist updatePlaylist(Playlist selectedPlaylist, String title) {
        return playlistDAO.updatePlaylist(selectedPlaylist, title);
    }

    @Override
    public Song addToPlaylist(Playlist selectedPlaylist, Song selectedSong) {
        return songsInPlaylistDAO.addToPlaylist(selectedPlaylist, selectedSong);
    }

    public void deleteFromPlaylist(Playlist selectedPlaylist, Song selectedSong){
        songsInPlaylistDAO.deleteFromPlaylist(selectedPlaylist, selectedSong);
    }

    public void editSongPosition(Playlist selectedPlaylist, Song selectedSong, Song exchangeSong){
        songsInPlaylistDAO.editSongPosition(selectedPlaylist,selectedSong,exchangeSong);
    }

    public void deleteSongFromAllPlaylists(Song selectedSong){
        songsInPlaylistDAO.deleteSongFromAllPlaylists(selectedSong);
    }
}
