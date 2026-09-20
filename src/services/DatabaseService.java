package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * DATABASE SERVICE
 * Handles SQLite database connection and automated table schema creation
 * for naval inspection reports and boardings.
 */
public class DatabaseService {
    private static final String URL = "jdbc:sqlite:naval_inspection.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Força o Java a carregar o driver do SQLite
            conn = DriverManager.getConnection(URL);
        } catch (Exception e) {
            System.out.println("❌ Database connection error: " + e.getMessage());
        }
        return conn;
    }

    public static void initializeDatabase() {
        String sqlReports = "CREATE TABLE IF NOT EXISTS reports ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "oms TEXT,"
                + "operation_date TEXT,"
                + "location TEXT,"
                + "vehicle TEXT,"
                + "official_vessel TEXT,"
                + "gasoline REAL,"
                + "diesel REAL"
                + ");";

        String sqlBoardings = "CREATE TABLE IF NOT EXISTS boardings ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "vessel_name TEXT,"
                + "registration TEXT,"
                + "vessel_class TEXT,"
                + "infraction TEXT,"
                + "seizure TEXT,"
                + "custodian TEXT"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlReports);
            stmt.execute(sqlBoardings);
            System.out.println("✅ SQLite Database tables initialized successfully!");
        } catch (Exception e) {
            System.out.println("❌ Error creating database tables: " + e.getMessage());
        }
    }
}