import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import javax.swing.JOptionPane;

public class DatabaseManager {
    private static Connection conn;

    private static Properties chargerConfiguration() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            props.load(fis);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Fichier db.properties introuvable à la racine du projet !", 
                "Erreur Configuration", JOptionPane.ERROR_MESSAGE);
        }
        return props;
    }

    public static Connection getConnection() throws Exception {
        if (conn == null || conn.isClosed()) {
            Properties config = chargerConfiguration();
            String url = config.getProperty("db.url");
            String user = config.getProperty("db.user");
            String pass = config.getProperty("db.password");

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Connexion PostgreSQL sécurisée établie !");
        }
        return conn;
    }
}
