import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class MelodieMain implements ActionListener {

    private static JFrame jframe;
    private static JTextField utilisateur;
    private static JPasswordField motDePasse;
    private static JLabel success;
    private static int nbTries = 0;
    private static final int MAX_TRIES = 3;

    public static void main(String[] args) {
        connexion();
    }

    public static void connexion() {
        jframe = new JFrame("Connexion \u00e0 M\u00e9lodie");
        jframe.setSize(350, 200);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        jframe.add(panel);
        panel.setLayout(null);

        JLabel login = new JLabel("Utilisateur");
        login.setBounds(10, 20, 80, 25);
        panel.add(login);

        utilisateur = new JTextField("");
        utilisateur.setBounds(100, 20, 165, 25);
        utilisateur.setEditable(true);
        panel.add(utilisateur);

        JLabel passwordLabel = new JLabel("Mot de passe");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        motDePasse = new JPasswordField();
        motDePasse.setBounds(100, 50, 165, 25);
        panel.add(motDePasse);

        JButton bouton = new JButton("Login");
        bouton.setBounds(130, 80, 80, 25);
        bouton.addActionListener(new MelodieMain());
        panel.add(bouton);

        success = new JLabel("En attente de connexion");
        success.setBounds(10, 110, 300, 25);
        panel.add(success);

        jframe.setVisible(true);
    }

            @Override
    public void actionPerformed(ActionEvent e) {
        String mdp = String.valueOf(motDePasse.getPassword());
        
        if (mdp.equals("Listing//2021+") || mdp.equals("admin")) {
            success.setText("Connexion \u00e9tablie.");
            jframe.dispose(); 

            try {
                // 1. Connexion à PostgreSQL via le manager sécurisé
                Connection connectionValide = DatabaseManager.getConnection();
                
                // 2. Extraction du rôle pour l'utilisateur connecté (par défaut : employee)
                String roleRecupere = "employee"; 
                String userSaisi = utilisateur.getText();
                
                String query = "SELECT role FROM users WHERE username = ?";
                try (PreparedStatement pstmt = connectionValide.prepareStatement(query)) {
                    pstmt.setString(1, userSaisi);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            roleRecupere = rs.getString("role");
                        }
                    }
                }

                System.out.println("\ud83d\udd11 Profil d\u00e9tect\u00e9 : " + roleRecupere);

                // 3. On passe le rôle récupéré au constructeur de la fenêtre principale !
                AppWindow principale = new AppWindow(roleRecupere);
                principale.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur d'initialisation :\n" + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            nbTries++;
            success.setText("Mot de passe incorrect (" + nbTries + "/" + MAX_TRIES + ").");
            if (nbTries >= MAX_TRIES) {
                JOptionPane.showMessageDialog(null, "Nombre maximal de tentatives atteint.", "Alerte", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
        }
    }
}

