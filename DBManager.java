import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private final String host = "tic-tac-toe-db-tic-tac-toe.f.aivencloud.com";
    private final String databaseName = "tictactoe";
    private final String userName = "avnadmin";
    private final String password = "AVNS_0uYXCfSUMyFZfJF69xS";
    private final String port = "25635";
    private final String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?sslMode=REQUIRED";

    public DBManager() {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, userName, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT version() AS version")) {

            while (resultSet.next()) {
                System.out.println("Version: " + resultSet.getString("version"));
            }
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

    public void insertMove(int gameId, char player, int row, int col, int moveNumber) {
        String sql = "INSERT INTO moves (game_id, player, `row`, `col`, move_number) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gameId);
            pstmt.setString(2, String.valueOf(player));
            pstmt.setInt(3, row);
            pstmt.setInt(4, col);
            pstmt.setInt(5, moveNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Move> getMoves(int gameId) {
        ArrayList<Move> moves = new ArrayList<>();
        String sql = "SELECT player, `row`, `col`, move_number FROM moves WHERE game_id = ? ORDER BY move_number ASC";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gameId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                char player = rs.getString("player").charAt(0);
                int row = rs.getInt("row");
                int col = rs.getInt("col");
                int moveNumber = rs.getInt("move_number");
                moves.add(new Move(player, row, col, moveNumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moves;
    }

    public void clearMoves(int gameId) {
        String sql = "DELETE FROM moves WHERE game_id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gameId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int createNewGame(String playerName, boolean isHost) {
        String sql = "";
        if(isHost){
            sql = "INSERT INTO games (player_x) VALUES (?)";
        }else{
            sql = "INSERT INTO games (player_o) VALUES (?)";
        }

        try (Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, playerName);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void insertOppName(int gameId, String oppName) {
        String sql = "UPDATE games SET player_o = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, oppName);
            stmt.setInt(2, gameId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getOppName(boolean isHost) {
        String sql ="";

        if (isHost){
            sql = "SELECT player_o FROM games";
        }else{
            sql = "SELECT player_x FROM games";
        }
        try (Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Player Error";
    }

    public boolean isGamesTableEmpty() {
        String sql = "SELECT COUNT(*) FROM games";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteGame(int gameId) {
        String sql = "DELETE FROM games WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gameId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLatestGameId() {
        int latestGameId = -1;
        String query = "SELECT id FROM games ORDER BY id DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                latestGameId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return latestGameId;
    }
}
