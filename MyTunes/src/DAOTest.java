import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.DatabaseConnector;
import dal.SongDAO;

public class DAOTest {
    public static void main(String[] args) throws SQLServerException {
        showSong();
    }
    public static void showSong() throws SQLServerException {
        DatabaseConnector db = new DatabaseConnector();
        //SongDAO.showSong();
    }
}
