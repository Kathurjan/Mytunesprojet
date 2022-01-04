package dal;

import be.Playlist;
import be.Song;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongsInPlaylistDAO {
    private final static DatabaseConnector db = new DatabaseConnector();

    // Method used to getting all the songs in the playlist from the database.
    public List<Song> getPlaylistSongs(int playlistID) {
        List<Song> newSongList = new ArrayList();
        try (Connection connection = db.getConnection()) {
            String query = "SELECT * FROM songsInPlaylistDatabase INNER JOIN musicDatabase ON songsInPlaylistDatabase.songID = musicDatabase.id WHERE songsInPlaylistDatabase.playlistID = ? ORDER by locationInListID desc";
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, playlistID);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Song song = new Song(rs.getString("title"),rs.getString("artist"),rs.getString("category"), rs.getInt("lengthOfSong"), rs.getString("filePath"), rs.getInt("id"));
                song.setLocationInList(rs.getInt("locationInListID"));
                newSongList.add(song);
            }
            return newSongList;
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    // Method used to delete songs from playlists in the database.
    public void deleteSongFromAllPlaylists(Song selectedSong) {
        try (Connection connection = db.getConnection()) {
            String query = "DELETE from songsInPlaylistDatabase WHERE SongID = ?";
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, selectedSong.getID());
            pstm.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    // Method used to add songs to playlists in the database.
    public Song addToPlaylist(Playlist selectedPlaylist, Song selectedSong) {
        String sql = "INSERT INTO songsInPlaylistDatabase(PlaylistID,SongID,locationInListID) VALUES (?,?,?)";
        int Id = -1;
        try (Connection connection = db.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            Id = getNewSongInPlaylist(selectedPlaylist.getID()) + 1;
            pstm.setInt(1, selectedPlaylist.getID());
            pstm.setInt(2, selectedSong.getID());
            pstm.setInt(3, Id);
            pstm.addBatch();
            pstm.executeBatch();
            selectedSong.setLocationInList(Id); //sets up a new location in the list for users to see
            return selectedSong;
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return null;

        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    // Method that gets the newest song in a playlist.
    private int getNewSongInPlaylist(int locationInListID) {
        int newestID = -1;
        try (Connection connection = db.getConnection()) {
            String query = "SELECT TOP(1) * FROM songsInPlaylistDatabase WHERE PlaylistID = ? ORDER by locationInListID desc";
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, locationInListID);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                newestID = rs.getInt("locationInListID");
            }
            System.out.println(newestID);
            return newestID;
        } catch (SQLServerException ex) {
            System.out.println(ex);
            return newestID;
        } catch (SQLException ex) {
            System.out.println(ex);
            return newestID;
        }
    }

    // Method for editing a songs position in a playlist.
    public void editSongPosition(Playlist selectedPlaylist, Song selectedSong, Song exchangeID) {
        try (Connection connection = db.getConnection()) {
            String query = "UPDATE songsInPlaylistDatabase set locationInListID = ? WHERE PlaylistID = ? AND SongID = ? AND locationInListID = ? ";
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, exchangeID.getLocationInList());
            pstm.setInt(2, selectedPlaylist.getID());
            pstm.setInt(3, selectedSong.getID());
            pstm.setInt(4, selectedSong.getLocationInList());
            pstm.addBatch();
            pstm.setInt(1, selectedSong.getLocationInList());
            pstm.setInt(2, selectedPlaylist.getID());
            pstm.setInt(3, exchangeID.getID());
            pstm.setInt(4, exchangeID.getLocationInList());
            pstm.addBatch();
            pstm.executeBatch();
            int temp = selectedSong.getLocationInList(); // Allows us to create a temporary ID in replacement
            selectedSong.setLocationInList(exchangeID.getLocationInList()); // Switches the first song with the replacement
            exchangeID.setLocationInList(temp); // Switches the exchanged song ID with the temporary ID
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    // Method to delete a song from a playlist.
    public void deleteFromPlaylist(Playlist selectedPlaylist, Song selectedSong) {
        try (Connection connection = db.getConnection()) {
            String query = "DELETE from songsInPlaylistDatabase WHERE PlaylistID = ? AND SongID = ? AND locationInListID = ?";
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1, selectedPlaylist.getID());
            pstm.setInt(2, selectedSong.getID());
            pstm.setInt(3, selectedSong.getLocationInList());
            pstm.execute();
        } catch (SQLServerException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}