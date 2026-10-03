import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

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
        jframe = new JFrame("Connexion à Mélodie");
        jframe.setSize(350, 200);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        jframe.add(panel);
        panel.setLayout(null);

        JLabel login = new JLabel("Utilisateur");
        login.setBounds(10, 20, 80, 25);
        panel.add(login);

        utilisateur = new JTextField("eric");
        utilisateur.setBounds(100, 20, 165, 25);
        utilisateur.setEditable(false);
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
            success.setText("Connexion établie.");
            jframe.dispose(); // Ferme le login

            try {
                // ?? Déclenchement de la base et de la fenêtre générale clean !
                DatabaseManager.getConnection();
                
                AppWindow principale = new AppWindow();
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
