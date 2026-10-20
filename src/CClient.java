
import java.sql.*;
import java.util.Scanner;

public class CClient {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/world";
        String user = "postgres";
        String password = "postgres";

        try (
                Connection conn = DriverManager.getConnection(url, user, password);
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Verbindung zur Datenbank erfolgreich!");
            System.out.println("SQL-Befehle eingeben (x zum Beenden):");
            System.out.println();


            while (true) {
                String sql = scanner.nextLine().trim();

                if (sql.equalsIgnoreCase("x")) {
                    break;
                }

                if (sql.isEmpty()) {
                    continue;
                }

                try (Statement statement = conn.createStatement()) {
                    boolean hasResult = statement.execute(sql);

                    if (hasResult) {
                        try (ResultSet result = statement.getResultSet()) {
                            ResultSetMetaData metadata = result.getMetaData();
                            int columns = metadata.getColumnCount();
                            System.out.println();

                            for (int i = 1; i <= columns; i++) {
                                System.out.printf("%-25s", metadata.getColumnLabel(i));
                            }
                            System.out.println();

                            for (int i = 1; i <= columns; i++) {
                                System.out.printf("%-25s", "----------------");
                            }
                            System.out.println();

                            while (result.next()) {
                                for (int i = 1; i <= columns; i++) {
                                    String value = result.getString(i);
                                    System.out.printf("%-25s",
                                            value == null ? "NULL" : value);
                                }
                                System.out.println();
                            }
                        }
                    } else {
                        System.out.println(
                                "Befehl erfolgreich. Betroffene Zeilen: "
                                        + statement.getUpdateCount()
                        );
                    }

                } catch (SQLException e) {
                    System.out.println("SQL-Fehler: " + e.getMessage());
                    System.out.println("SQL-State: " + e.getSQLState());
                }
            }

            System.out.println("Programm beendet.");

        } catch (SQLException e) {
            System.out.println("Verbindungsfehler: " + e.getMessage());
            System.out.println("SQL-State: " + e.getSQLState());
        }
    }
}
