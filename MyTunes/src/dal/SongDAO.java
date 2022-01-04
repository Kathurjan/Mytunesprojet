package dal;
import be.Song;
import javafx.collections.FXCollections;
import java.sql.*;
import java.util.List;

public class SongDAO {
private final static DatabaseConnector db = new DatabaseConnector();

    // Method used to get all the songs from the database.
    public List<Song> getAllSongs() {
        List<Song> songList = FXCollections.observableArrayList();
        try (Connection connection = db.getConnection()) {
            String sqlStatement = "SELECT * FROM dbo.musicDatabase";
            Statement statement = connection.createStatement();
            if (statement.execute(sqlStatement)) {
                ResultSet rs = statement.getResultSet();
                while (rs.next()) {
                    String title = rs.getString("title");
                    String artist = rs.getString("artist");
                    String category = rs.getString("category");
                    int lengthOfSong = rs.getInt("lengthOfSong");
                    String filepath = rs.getString("filePath");
                    int ID = rs.getInt("id");

                    Song song = new Song(title,artist,category,lengthOfSong,filepath,ID);// Creating a song object from the retrieved values
                    songList.add(song); // Adding the song to  list
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
        return songList;
    }

    // Method used for adding songs from user input into the database.
    public Song addSongs(String title, String artist, String category, int lengthOfSong, String filePath) {
        String sqlStatement = "INSERT INTO musicDatabase(title,artist,category,lengthOfSong,filePath) VALUES (?,?,?,?,?)";
        try(Connection connection = db.getConnection()){
            PreparedStatement pstm = connection.prepareStatement(sqlStatement);
            pstm.setString(1, title); // values we want to insert into the database
            pstm.setString(2, artist);
            pstm.setString(3, category);
            pstm.setInt(4, lengthOfSong);
            pstm.setString(5, filePath);
            pstm.addBatch(); // Adding to the statement
            pstm.executeBatch(); // Executing the added parameters, and  executing the statement
        } catch(SQLException ex) {
            System.out.println(ex);
            return null;
        }
        Song song = new Song(title,artist,category,lengthOfSong,filePath,1); // Creating a new song object
        return song;
    }

    // Method used for editing the songs in the database.
    public Song editSongs(Song selectedSong, String title, String artist, String category, int lengthOfsong, String filepath) {
            try (Connection connection = db.getConnection()) {
                String query = "UPDATE musicDataBase set title = ?,artist = ?,category = ?,lengthOfsong = ?,filepath = ? WHERE id = ?";
                PreparedStatement pstm = connection.prepareStatement(query);
                pstm.setString(1, title);
                pstm.setString(2, artist);
                pstm.setString(3, category);
                pstm.setInt(4, lengthOfsong);
                pstm.setString(5, filepath);
                pstm.setInt(6, selectedSong.getID());
                pstm.executeUpdate();
                return new Song(title,artist,category,lengthOfsong,filepath,selectedSong.getID());
            } catch (SQLException ex) {
                System.out.println(ex);
                return null;
        }
    }

    // Method used for deleting the songs in the database. ATTENTION SHOULD USE ID TO IDENTIFY SONG.
    public void deleteSong(Song selectedSong){
        try(Connection connection = db.getConnection()){
            String query = "DELETE FROM musicDataBase WHERE id = ?";
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setInt(1,selectedSong.getID());
            pstm.executeUpdate(); // Executing the statement
        } catch(SQLException ex){
            System.out.println(ex);
        }
    }
}