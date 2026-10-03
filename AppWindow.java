import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AppWindow extends JFrame {

    public AppWindow() {
        setTitle("M\u00e9lodie en sous-sol");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar barreMenu = new JMenuBar();

        // --- MENU FICHIER ---
        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem ouvrir = new JMenuItem("Ouvrir csv");
        ouvrir.setIcon(new ImageIcon("icons/open.png"));
        JMenuItem quitter = new JMenuItem("Quitter");
        quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        quitter.addActionListener(e -> System.exit(0));

        menuFichier.add(ouvrir);
        menuFichier.add("Enregistrer...");
        menuFichier.add("Enregistrer sous...");
        menuFichier.addSeparator();
        menuFichier.add(quitter);

        // --- MENU ÉDITION ---
        JMenu menuEdition = new JMenu("Édition");
        JMenuItem itemAfficherTout = new JMenuItem("Afficher tout");
        JMenuItem itemChercherTitre = new JMenuItem("Chercher par Titre");
        JMenuItem itemChercherArtiste = new JMenuItem("Chercher par Artiste");
        
        // Appel de la recherche
        itemChercherArtiste.addActionListener(e -> rechercheArtiste());

        menuEdition.add(itemAfficherTout);
        menuEdition.add(itemChercherTitre);
        menuEdition.add(itemChercherArtiste);

        // --- MENU AIDE ---
        JMenu menuAide = new JMenu("Aide");
        JMenuItem aPropos = new JMenuItem("À propos");
        aPropos.addActionListener(e -> JOptionPane.showMessageDialog(this, "Mélodie en sous-sol - Gestionnaire de Stock\nVersion Java/Swing Refactorisée 2026", "À propos", JOptionPane.INFORMATION_MESSAGE));
        menuAide.add(aPropos);

        barreMenu.add(menuFichier);
        barreMenu.add(menuEdition);
        barreMenu.add(menuAide);
        setJMenuBar(barreMenu);

        // Panneau central vide par défaut
        add(new JPanel(), BorderLayout.CENTER);
    }

    private void rechercheArtiste() {
        try {
            String nomArtiste = JOptionPane.showInputDialog(this, "Nom de l'artiste/groupe :", "Recherche", JOptionPane.PLAIN_MESSAGE);
            if (nomArtiste == null || nomArtiste.trim().isEmpty()) return;

            Connection conn = DatabaseManager.getConnection();
            String queryAlbum = "SELECT id, artist, title, label, price FROM melodie WHERE artist ILIKE ? ORDER BY artist, title";
            
            PreparedStatement pstmt = conn.prepareStatement(queryAlbum);
            pstmt.setString(1, "%" + nomArtiste.trim() + "%");
            
            ResultSet res = pstmt.executeQuery();
            ResultSetMetaData metaData = res.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            String[] colonnes = { "ID", "Artiste", "Titre", "Label", "Prix" };
            DefaultTableModel tableModel = new DefaultTableModel(colonnes, 0);
            
            boolean aucunResultat = true;
            while (res.next()) {
                aucunResultat = false;
                Object[] ligneData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    ligneData[i - 1] = res.getObject(i);
                }
                tableModel.addRow(ligneData);
            }
            
            if (aucunResultat) {
                JOptionPane.showMessageDialog(this, "Aucun disque trouvé.");
            } else {
                JFrame frameResultats = new JFrame("Résultats - " + nomArtiste);
                frameResultats.setSize(700, 400);
                frameResultats.setLocationRelativeTo(this);
                
                JTable table = new JTable(tableModel);
                frameResultats.add(new JScrollPane(table), BorderLayout.CENTER);
                frameResultats.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur SQL :\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
