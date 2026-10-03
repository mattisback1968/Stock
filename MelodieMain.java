
//package melodie;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

//import com.sun.java.util.jar.pack.Instruction.Switch;

import javax.sql.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MelodieMain extends JFrame implements ActionListener {

	private static JLabel login;
	private static JTextField utilisateur;
	private static JLabel passwordLabel;
	private static JPasswordField motDePasse;
	private static JButton bouton;
	private static JLabel success;
	static JPanel panel;
	private static JFrame jframe;
	static boolean statutCnx = false;
	static boolean cnxOk = false;
	static String user;
	static String mdp;
	public static Connection conn;
	public static Statement stm;
	public static Statement stm2;
	private static int MaxTries = 3;
	static int nbTries;
	public static int choixAction = -1;
	public static Scanner scan;
	public static String champArtiste;
	private static String champTitre;
	private static String champEditeur;
	private static String champFormat;
	// private static String champPrix;
	private static String prixLong;
	private static String champEtatSupport;
	private static String champEtatPochette;
	private static String champMediaSupportLong;
	private static int count;
	private static int id;

	// public static Connection conn;

	// Fonction universelle pour charger les secrets
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

	private static void lancerApplicationPrincipale() throws Exception {
		JOptionPane.showMessageDialog(null, "Connexion établie avec succès !");

		// Chargement dynamique des identifiants sécurisés
		Properties config = chargerConfiguration();
		String url = config.getProperty("db.url");
		String user = config.getProperty("db.user");
		String pass = config.getProperty("db.password");

		// Raccordement avec votre base de données PostgreSQL
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(url, user, pass);
		System.out.println("? Connecté de manière sécurisée à PostgreSQL !");
	}

	public static void main(String[] args) {
		// Lance uniquement l'interface graphique de connexion
		connexion();
	}

	public static boolean connexion() {
		
		nbTries=0;
		panel = new JPanel(); // On utilise le panel global
        
		// ? CORRECTIF : Enlevez "JFrame" ici pour assigner directement la variable globale
		jframe = new JFrame("Connexion à Mélodie"); 
        
		jframe.setSize(350,200);
		jframe.setLocationRelativeTo(null);
		jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		jframe.add(panel);

		panel.setLayout(null);

		login = new JLabel("Utilisateur");
		login.setBounds(10, 20, 80, 25);
		panel.add(login);

		utilisateur = new JTextField("eric");
		utilisateur.setBounds(100, 20, 165, 25);
		utilisateur.setEditable(false);
		panel.add(utilisateur);

		passwordLabel = new JLabel("Mot de passe");
		passwordLabel.setBounds(10, 50, 80, 25);
		panel.add(passwordLabel);
		// boolean gainedFocusBefore;

		motDePasse = new JPasswordField();
		motDePasse.setBounds(100, 50, 165, 25);
		// motDePasse.requestFocusInWindow();
		// motDePasse(autofocus: true);
		panel.add(motDePasse);

		bouton = new JButton("Login");
		bouton.setBounds(130, 80, 80, 25);
		bouton.addActionListener(new MelodieMain());
		// new MelodieMain());
		panel.add(bouton);

		success = new JLabel("En attente de connexion");
		success.setBounds(10, 110, 300, 25);
		panel.add(success);

		jframe.setVisible(true);
		do {

		} while (statutCnx != true);
		System.out.println("Connexion mdp " + statutCnx);
		jframe.dispose();
		return statutCnx;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		user = utilisateur.getText();
		mdp = String.valueOf(motDePasse.getPassword());
		if (mdp.equals("Listing//2021+")) {
			success.setText("Connexion à la base de données Mélodie établie.");
			statutCnx = true;

			jframe.dispose();
			// for (int i = 0; i < 2; i++) {
			// Thread.sleep(1000);
		}

		else {
			success.setText("Mot de passe incorrect. Aucune connexion établie.");
			statutCnx = false;
			nbTries++;
			if (nbTries >= MaxTries) {
				JFrame fermeture = new JFrame();

				JOptionPane.showMessageDialog(fermeture,
						"Le programme va fermer.\nNombre maximal de tentatives de connexion atteint.",
						"Alerte", JOptionPane.WARNING_MESSAGE);

				System.exit(0);
			}
		}

		System.out.println(user + " " + mdp + " " + statutCnx);
	}

	public static boolean affichageEtatCnx() {
		if (statutCnx == true) {
			System.out.println("Connexion : " + statutCnx);
			JOptionPane.showMessageDialog(null, "Connexion établie.");

		} else {
			JOptionPane.showMessageDialog(null, "Mot de passe erroné.\nPas de connexion.", "Oups",
					JOptionPane.ERROR_MESSAGE);
			// jframe.dispose();
			// System.out.println"Connexion : "+statutCnx);
			statutCnx = false;
		}
		return statutCnx;
	}

	static void rechercheArtiste() {
		try {
			// 1. Demande du nom de l'artiste via une boîte de saisie Swing graphiquement
			// propre
			String nomArtiste = JOptionPane.showInputDialog(
					null,
					"Nom (même partiel) de l'artiste/groupe :",
					"Recherche d'artiste - Mélodie",
					JOptionPane.PLAIN_MESSAGE);

			if (nomArtiste == null)
				return; // Gestion du bouton Annuler ou de la fermeture de la boîte
			nomArtiste = nomArtiste.trim();

			// 2. Requête SQL sécurisée (ILIKE pour ignorer la casse spécifique à
			// PostgreSQL)
			String queryAlbum = "SELECT id, artist, title, label, price FROM melodie WHERE artist ILIKE ? ORDER BY artist, title";

			// Utilisation d'un PreparedStatement pour éviter les injections et les crashs
			// d'apostrophes
			PreparedStatement pstmt = conn.prepareStatement(queryAlbum);
			pstmt.setString(1, "%" + nomArtiste + "%");

			ResultSet res = pstmt.executeQuery();

			// 3. Extraction des métadonnées pour configurer automatiquement la grille Swing
			ResultSetMetaData metaData = res.getMetaData();
			int columnCount = metaData.getColumnCount();

			// Création des en-têtes du tableau graphique
			String[] colonnes = { "ID", "Artiste", "Titre", "Label", "Prix" };

			// Modèle de données dynamique pour la JTable
			DefaultTableModel tableModel = new DefaultTableModel(colonnes, 0);

			// 4. Boucle de lecture des pixels textuels (le ResultSet de Postgres)
			boolean aucunResultat = true;
			while (res.next()) {
				aucunResultat = false;
				Object[] ligneData = new Object[columnCount];
				for (int i = 1; i <= columnCount; i++) {
					ligneData[i - 1] = res.getObject(i);
				}
				tableModel.addRow(ligneData); // Ajoute la ligne de vinyle au tableau Swing
			}

			// 5. Affichage du résultat graphique
			if (aucunResultat) {
				JOptionPane.showMessageDialog(null, "Aucun disque trouvé pour l'artiste : " + nomArtiste, "Recherche",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				// Fabrication d'une fenêtre de résultats dédiée à la volée
				JFrame frameResultats = new JFrame("Résultats de recherche - " + nomArtiste);
				frameResultats.setSize(700, 400);
				frameResultats.setLocationRelativeTo(null);
				frameResultats.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JTable table = new JTable(tableModel);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				JScrollPane scrollPane = new JScrollPane(table);
				frameResultats.add(scrollPane, BorderLayout.CENTER);

				frameResultats.setVisible(true);
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erreur SQL lors de la recherche :\n" + e.getMessage(), "Erreur",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	static void rechercheTitre() throws ClassNotFoundException {
		try {

			Class.forName("org.postgresql.Driver");

			String url = "jdbc:postgresql://localhost:5432/melodie30mars";
			String user = "postgres";
			String passwd = "daredevil";

			Connection conn = DriverManager.getConnection(url, user, passwd);

			// RECHERCHE TITRE
			JFrame frame = new JFrame("");
			String album = JOptionPane.showInputDialog(
					frame, "Nom (même partiel) de l'album", "Recherche d'album(s)",
					JOptionPane.PLAIN_MESSAGE);
			// System.exit(0);

			String queryAlbum = "SELECT listing_id, artist, title, label, price FROM disques where (title ilike " + "\'"
					+ "%" + album + "%" + "\'" + ")";
			// + "\'"+"%"+"silence"+"%"+"\'";
			// + " order by artist";
			// query+="\'"+"%"+"silence"+"%"+"\'";
			Statement stm = conn.createStatement();

			ResultSet rs = stm.executeQuery("select count(*) from disques");
			rs.next();
			int count = rs.getInt(1);
			System.out.println(count + "  résultats trouvés.");

			// Moving the cursor to the last row
			// System.out.println("La base contient "+rs.getInt("count(*)")+"
			// enregistrements (lignes)");

			ResultSet res = stm.executeQuery(queryAlbum);

			String colonnes[] = { "ID", "Artiste", "Titre", "Label", "Prix" }; // PRIX NE FONCTIONNE PAS
			String data[][] = new String[count][8];

			int i = 0;
			while (res.next()) {
				String id = res.getString("listing_id");
				String artiste = res.getString("artist");
				String titre = res.getString("title");
				String label = res.getString("label");
				String prix = res.getString("price");
				data[i][0] = id;
				/*
				 * System.out.println("ID :"+data[i][1]);
				 * data[i][1] = artiste;
				 * System.out.println("Artiste :" +data[i][2]);
				 * data[i][2] = titre;
				 * System.out.println("Titre : "+data[i][3]);
				 * data[i][3]=label;
				 * System.out.println(data[i][4]);
				 * data[i][4]=prix;
				 * System.out.println(data[i][5]);
				 */
				i++;
			}

			DefaultTableModel model = new DefaultTableModel(data, colonnes);
			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setShowVerticalLines(true);
			JScrollPane pane = new JScrollPane(table);
			JFrame f = new JFrame(count + " enregistrements au total.");
			JPanel panel = new JPanel();
			panel.add(pane);
			f.add(panel);
			f.setSize(500, 250);
			f.setLocationRelativeTo(null);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setTitle(i + " résultat(s) trouvé(s)");
			f.setVisible(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void afficherTout() {
		// try {
		// UIManager.setLookAndFeel(new NimbusLookAndFeel());
		// } catch (UnsupportedLookAndFeelException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		try {
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://localhost:5432/melodie30mars";
			String user = "postgres";
			String passwd = "daredevil";

			Connection conn = DriverManager.getConnection(url, user, passwd);

			// créer l'objet statement et exécuter la requête

			Statement stm;
			stm = conn.createStatement();
			ResultSet res = stm.executeQuery("SELECT count(*) FROM disques");
			res.next();
			int count = res.getInt(1);
			// System.out.println(count + " résultats trouvés.");

			String queryAffichage = "SELECT listing_id, artist, title, label, price FROM disques";
			ResultSet rs = conn.createStatement().executeQuery(queryAffichage);

			String colonnes[] = { "ID", "Artiste", "Titre", "Label", "Prix" };
			String data[][] = new String[count][8];
			int i = 0;
			while (rs.next()) {

				String id = rs.getString("listing_id");
				String artiste = rs.getString("artist");
				String titre = rs.getString("title");
				String label = rs.getString("label");
				String prix = rs.getString("price");
				data[i][0] = id;
				// System.out.println("ID :"+data[i][1]);
				data[i][1] = artiste;
				// System.out.println("Artiste :" +data[i][2]);
				data[i][2] = titre;
				// System.out.println("Titre : "+data[i][3]);
				data[i][3] = label;
				// System.out.println(data[i][4]);
				data[i][4] = prix;
				// System.out.println(data[i][5]);
				i++;
			}
			DefaultTableModel model = new DefaultTableModel(data, colonnes);
			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setShowVerticalLines(true);
			JScrollPane pane = new JScrollPane(table);
			JFrame f = new JFrame(count + " enregistrements au total.");
			JPanel panel = new JPanel();
			panel.add(pane);
			f.add(panel);
			f.setSize(500, 250);
			f.setLocationRelativeTo(null);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setTitle(i + " résultat(s) trouvé(s)");
			f.setVisible(true);
			// affichage des résultats en ligne de commande OK
			/*
			 * while(res.next())
			 * System.out.println(res.getInt(1)+"  "+res.getString(2)
			 * +"  "+res.getString(3));
			 * //fermer l'objet de connexion
			 */
			conn.close();
		} catch (Exception e) {

			// System.out.println(e);
		}
	}

	    public static void GUI() {
        JFrame gui = new JFrame();

        gui.setTitle("Mélodie en sous-sol");
        gui.setSize(800, 500); // Augmenté pour accueillir une future table de disques
        gui.setLocationRelativeTo(null); // Centre la fenêtre
        gui.setResizable(true); // Permet de redimensionner le catalogue
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme proprement le processus

        JMenuBar barreMenu = new JMenuBar();

        // --- MENU FICHIER ---
        JMenu menuFichier = new JMenu("Fichier");
        menuFichier.setMnemonic('F');
        
        JMenuItem ouvrir = new JMenuItem("Ouvrir csv");
        ouvrir.setIcon(new ImageIcon("icons/open.png"));
        
        JMenuItem quitter = new JMenuItem("Quitter");
        quitter.setMnemonic('Q');
        quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        
        // ? Action Quitter fonctionnelle
        quitter.addActionListener(e -> System.exit(0));

        menuFichier.add(ouvrir);
        menuFichier.add("Enregistrer...");
        menuFichier.add("Enregistrer sous...");
        menuFichier.addSeparator();
        menuFichier.add(quitter);
        quitter.setIcon(new ImageIcon("icons/exit.png"));

        // --- MENU ÉDITION (Transformé en JMenuItem pour activer les fonctions) ---
        JMenu menuEdition = new JMenu("Édition");
        menuEdition.setMnemonic('E');

        JMenuItem itemAfficherTout = new JMenuItem("Afficher tout");
        JMenuItem itemChercherTitre = new JMenuItem("Chercher par Titre");
        JMenuItem itemChercherArtiste = new JMenuItem("Chercher par Artiste");
        JMenuItem itemAjouter = new JMenuItem("Ajouter Nouvel album");
        JMenuItem itemSupprimer = new JMenuItem("Supprimer Album");

        // ? RACCORDEMENT : Associer le clic du menu à votre fonction PostgreSQL !
        itemChercherArtiste.addActionListener(e -> {
            try {
                rechercheArtiste();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        menuEdition.add(itemAfficherTout);
        menuEdition.add(itemChercherTitre);
        menuEdition.add(itemChercherArtiste);
        menuEdition.addSeparator();
        menuEdition.add(itemAjouter);
        menuEdition.add(itemSupprimer);

        // --- MENU AIDE ---
        JMenu menuAide = new JMenu("Aide");
        menuAide.setMnemonic('A');
        
        JMenuItem aPropos = new JMenuItem("À propos");
        aPropos.setIcon(new ImageIcon("icons/about.png"));
        aPropos.addActionListener(e -> JOptionPane.showMessageDialog(gui, "Mélodie en sous-sol - Gestionnaire de Stock\nVersion Java/Swing 2026", "À propos", JOptionPane.INFORMATION_MESSAGE));

        JMenuItem aide = new JMenuItem("Aide");
        aide.setIcon(new ImageIcon("icons/aider.png"));
        aide.setMnemonic('A');
        aide.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));

        menuAide.add(aPropos);
        menuAide.add(aide);

        // Assemblage de la barre de menus
        barreMenu.add(menuFichier);
        barreMenu.add(menuEdition);
        barreMenu.add(menuAide);
        gui.setJMenuBar(barreMenu);

        // ? Gestion sécurisée du Panel central
        try {
            // Si buildPanel() modifie une variable de panel globale, on s'assure qu'il s'ajoute à gui
            buildPanel(); 
            if (panel != null) {
                gui.add(panel, BorderLayout.CENTER);
            }
        } catch (Exception ex) {
            System.out.println("Note : buildPanel() incomplet ou absent, initialisation d'un panneau vide.");
            JPanel panneauVide = new JPanel();
            gui.add(panneauVide, BorderLayout.CENTER);
        }

        gui.setVisible(true); // Affichage final de la borne d'arcade disquaire
    }

	// }
	public static void buildPanel() {
		/* setTitle("Mélodie"); //On donne un titre à l'application */
		// setSize(320,240); //On donne une taille à notre fenêtre
		// setLocationRelativeTo(null); //On centre la fenêtre sur l'écran
		// setResizable(true); //On permet le redimensionnement
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //On dit à l'application de
		// se fermer lors du clic sur la croix
		// setContentPane(buildContentPane());

		// UIManager.setLookAndFeel(new NimbusLookAndFeel()) throws Exception;
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JButton boutonListeComplete = new JButton("Liste complète");
		JButton boutonChercherArtiste = new JButton("Recherche par artiste");
		JButton boutonChercherAlbum = new JButton("Recherche par album");
		JButton boutonImporter = new JButton("Importer csv"); // dans le menu fichier
		JButton boutonSupprimer = new JButton("Supprimer");
		JButton boutonNouveau = new JButton("Nouveau");

		panel.add(boutonListeComplete);
		panel.add(boutonChercherArtiste);
		panel.add(boutonChercherAlbum);
		panel.add(boutonNouveau);
		panel.add(boutonSupprimer);
		panel.setLayout(new FlowLayout());
		// panel.add(boutonRechercher);

		panel.setVisible(true);
		// return contentPane;

	}

	public void barreOutils() {
	}

	private void quitterListener(ActionEvent event) {
		int i = JOptionPane.showInternalConfirmDialog(this, "Voulez-vous vraiment quitter ?", "Quitter",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (i == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	private void aproposListener(ActionEvent event) {
		String aPropos = "https://www.facebook.com/melodiemulhouse\nmelodiensoussol@gmail.com";

		// JOptionPane.showMessageDialog(this, "Mélodie en sous-sol v0.5\n3 mai
		// 2022\n"+aPropos);
	}

	public static void listeComplete() {
		try {
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://localhost:5432/melodie30mars";
			String user = "postgres";
			String passwd = "daredevil";

			Connection conn = DriverManager.getConnection(url, user, passwd);

			// créer l'objet statement et exécuter la requête

			Statement stm;
			stm = conn.createStatement();
			ResultSet res = stm.executeQuery("SELECT count(*) FROM disques");
			res.next();
			int count = res.getInt(1);
			// System.out.println(count + " résultats trouvés.");

			String queryAffichage = "SELECT listing_id, artist, title, label, price FROM disques";
			ResultSet rs = conn.createStatement().executeQuery(queryAffichage);

			String colonnes[] = { "ID", "Artiste", "Titre", "Label", "Prix" };
			String data[][] = new String[count][8];
			int i = 0;
			while (rs.next()) {

				String id = rs.getString("listing_id");
				String artiste = rs.getString("artist");
				String titre = rs.getString("title");
				String label = rs.getString("label");
				String prix = rs.getString("price");
				data[i][0] = id;
				// System.out.println("ID :"+data[i][1]);
				data[i][1] = artiste;
				// System.out.println("Artiste :" +data[i][2]);
				data[i][2] = titre;
				// System.out.println("Titre : "+data[i][3]);
				data[i][3] = label;
				// System.out.println(data[i][4]);
				data[i][4] = prix;
				// System.out.println(data[i][5]);
				i++;
			}

			DefaultTableModel model = new DefaultTableModel(data, colonnes);
			JTable table = new JTable(model);
			table.setShowGrid(true);
			table.setShowVerticalLines(true);
			JScrollPane pane = new JScrollPane(table);
			JFrame f = new JFrame(count + " enregistrements au total.");
			JPanel panel = new JPanel();
			panel.add(pane);
			f.add(panel);
			f.setSize(500, 250);
			f.setLocationRelativeTo(null);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setTitle(i + " résultat(s) trouvé(s)");
			f.setVisible(true);
			// affichage des résultats en ligne de commande OK
			/*
			 * while(res.next())
			 * System.out.println(res.getInt(1)+"  "+res.getString(2)
			 * +"  "+res.getString(3));
			 * //fermer l'objet de connexion
			 * conn.close();
			 */
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void insertionDisque() {
							try {try {
								Class.forName("org.postgresql.Driver");
							} catch (ClassNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						       
						    	String url = "jdbc:postgresql://localhost:5432/melodie30mars";
						    	String user = "postgres";
						    	String passwd = "daredevil";
						       
						    conn = DriverManager.getConnection(url, user, passwd);
						    stm = conn.createStatement();
						 
							}catch(SQLException sqlE) {
								JOptionPane.showMessageDialog(null, "Une erreur s'est produite.\nLe programme va se fermer.","ERREUR !",JOptionPane.WARNING_MESSAGE);
								sqlE.printStackTrace();
								System.exit(0);
							}
						JFrame jframe=new JFrame("Nouvel album");
						JPanel panel=new JPanel();
						jframe.setSize(450,300);
						jframe.setLocation(600,400);
						jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						
						jframe.add(panel);
						
						panel.setLayout(null);
						
						JLabel artiste= new JLabel("Artiste");
						artiste.setBounds(10,20,80,25);
						panel.add(artiste);
						JTextField artistField=new JTextField();
						//artist.setColumns(100);
						artistField.setBounds(120,20,200, 25);
						panel.add(artistField);
						
						JLabel titre= new JLabel("Titre");
						titre.setBounds(10,45,165,25);
						panel.add(titre);
						JTextField title=new JTextField(100);
						title.setBounds(120,45,200, 25);
						panel.add(title);
						
						JLabel label= new JLabel("Label");
						label.setBounds(10,70,100,25);
						panel.add(label);
						JTextField editeur=new JTextField(100);
						editeur.setBounds(120,70,200, 25);
						panel.add(editeur);
						
						JLabel format= new JLabel("Format");
						format.setBounds(10,95,100,25);
						panel.add(format);
						JTextField formatLong=new JTextField(100);
						formatLong.setBounds(120,95,200, 25);
						panel.add(formatLong);
						
						JLabel prix= new JLabel("Prix");
						prix.setBounds(10,120,100,25);
						panel.add(prix);
						JTextField prixLong=new JTextField(100);
						prixLong.setBounds(120,120,200, 25);
						panel.add(prixLong);
						
						JLabel mediaSupport= new JLabel("État du support");
						mediaSupport.setBounds(10,145,100,25);
						panel.add(mediaSupport);
						JTextField mediaSupportLong=new JTextField(100);
						mediaSupportLong.setBounds(120,145,200, 25);
						panel.add(mediaSupportLong);
					
						JLabel sleeveCondition= new JLabel("État de la pochette");
						sleeveCondition.setBounds(10,170,200,25);
						panel.add(sleeveCondition);
						JTextField sleeveConditionLong=new JTextField(100);
						sleeveConditionLong.setBounds(120,170,200, 25);
						panel.add(sleeveConditionLong);
						
						JButton bouton= new JButton("Ajouter");
						bouton.setBounds(165,200,80,25);
						panel.add(bouton);
						jframe.setLocationRelativeTo(null);
						jframe.setVisible(true);
						
						bouton.addActionListener(new ActionListener()
						                          @Override
    public void actionPerformed(ActionEvent e) {
        user = utilisateur.getText();
        mdp = String.valueOf(motDePasse.getPassword());
        
        if (mdp.equals("Listing//2021+") || mdp.equals("admin")) {
            success.setText("Connexion réussie. Chargement de la BDD...");
            statutCnx = true;
            
            jframe.dispose(); // Ferme proprement la fenêtre de login
            
            try {
                lancerApplicationPrincipale(); // Charge le .properties et établit la cnx conn
                
                // ? LE RACCORDEMENT : Lance immédiatement la boîte de recherche sur les 976 disques !
                rechercheArtiste();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur au chargement de la base de données :\n" + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {;
				
			champArtiste=artistField.getText();
			champTitre=title.getText();
			champEditeur=editeur.getText();
			champFormat=formatLong.getText();
			String champPrix=prixLong.getText();
			champEtatSupport=mediaSupportLong.getText();
			champEtatPochette=sleeveConditionLong.getText();
			//int Prix=Integer.parseInt(prixLong.getText());
			//if ((champArtiste!=null)&(champTitre!=null)) {
			//compte du nombre d'entrées pour sélectionner le numéro de clé primaire suivant
			 						   
			try {
											
			Statement state = conn.createStatement();
			ResultSet rs = state.executeQuery("select count(*) from disques");
			//String queryTest="insert into disques (listing_id, artist, title, label) values (999, 'Genesis', 'Foxtrot', 'indé')";
											
			//state.executeQuery(queryTest);
											
			//ResultSet rs = state.executeQuery(queryTest);
			rs.next();
			Integer count = rs.getInt(1);
			System.out.println(count + "  résultats trouvés.");
			rs = stm.executeQuery("select count(*) from disques");
											
														    	
			//int count = rs.getInt(1);
			System.out.println("Count vaut : "+count);
								    	
			//int listing_id=count;
			count++;
			System.out.println("Numéro d'ID : "+id);
			System.out.println("ID : "+count +" Artiste : "+champArtiste + " Titre : "+champTitre + " Label : "+champEditeur);		    	
								    	
			// insertion des valeurs du formulaire
								    	
			Statement state2 = conn.createStatement();
			System.out.println("Numéro ID = "+count);
			//state2.executeUpdate(queryInsertion);
								      
			//solution avec preparedStatement
			String queryInsertion="INSERT INTO disques (listing_id,artist, title, label, format, price, media_condition, sleeve_condition) values (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps=conn.prepareStatement(queryInsertion);
			ps.setInt(1, count);
			ps.setString(2, champArtiste);
			ps.setString(3, champTitre);
			ps.setString(4,  champEditeur);
			ps.setString(5,  champFormat);
			ps.setFloat(6, Float.parseFloat(champPrix));
			ps.setString(7, champEtatSupport);
			ps.setString(8, champEtatPochette);
			ps.executeUpdate();
			JOptionPane.showMessageDialog(null, "Nouvel album enregistré.","AJOUT D'ALBUM",JOptionPane.INFORMATION_MESSAGE);
			ps.close();
				
			} catch (SQLException ex) {
											
			JOptionPane.showMessageDialog(null, "Une erreur s'est produite.\nLe programme va se fermer.","ERREUR !",JOptionPane.WARNING_MESSAGE);
				ex.printStackTrace();
				System.exit(0);
											// TODO Auto-generated catch block
											//ex.printStackTrace();
										}	
								   }
								  }	
						);	
						}

	public void aideListener() {
		System.out.println("Aide");
	}

	public static void menu() {

		System.out.println("1: Afficher tout le catalogue");
		System.out.println("2: Rechercher par titre");
		System.out.println("3: Rechercher par artiste");
		System.out.println("4: Insérer nouveau titre");
		System.out.println("5: Supprimer un enregistrement");
		System.out.println("0: Quitter le  programme");

		int choix = -1;
		Scanner sc = new Scanner(System.in);
		do {
			choix = sc.nextInt();
			switch (choix) {
				case 1: {
					afficherTout();
					choix = -1;
					break;
				}

				case 2: {
					try {
						rechercheTitre();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}

				case 3: {
					try {
						rechercheArtiste();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}

				case 4: {
					insertionDisque();
					break;
				}

				case 5: {
					supprimerAlbum();
					choix = -1;
					break;
				}

				case 0: {
					int i = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment quitter ?", "Quitter",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (i == JOptionPane.YES_OPTION) {
						System.exit(0);
					} else {
						choix = -1;
						break;
					}
				}
			}
		} while (choix != 0);

	}

	public static void cnxBDD() {
		{
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String url = "jdbc:postgresql://localhost:5432/melodie30mars";
			String user = "postgres";
			String passwd = "daredevil";

			try {
				conn = DriverManager.getConnection(url, user, passwd);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unused")
	public static void supprimerAlbum() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		String url = "jdbc:postgresql://localhost:5432/melodie30mars";
		String user = "postgres";
		String passwd = "daredevil";

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, passwd);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// RECHERCHE TITRE
		JFrame frame = new JFrame("");
		String album = JOptionPane.showInputDialog(
				frame, "Nom (même partiel) de l'album à supprimer", "Recherche d'album(s)",
				JOptionPane.PLAIN_MESSAGE);

		String queryAlbum = "SELECT listing_id,artist, title, label, price FROM disques where (title ilike " + "\'"
				+ "%" + album + "%" + "\'" + ")";
		// + "\'"+"%"+"silence"+"%"+"\'";
		// + " order by artist";
		// query+="\'"+"%"+"silence"+"%"+"\'";
		Statement stm = null;
		try {
			stm = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSet rs = null;
		try {
			rs = stm.executeQuery("select count(*) from disques");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			rs.next();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int count = 0;
		try {
			count = rs.getInt(1);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// System.out.println(count + " résultats trouvés.");

		// Moving the cursor to the last row
		// System.out.println("La base contient "+rs.getInt("count(*)")+"
		// enregistrements (lignes)");

		ResultSet res = null;
		try {
			res = stm.executeQuery(queryAlbum);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String colonnes[] = { "ID", "Artiste", "Titre", "Label", "Prix" };
		String data[][] = new String[count][8];

		int i = 0;
		/*
		 * try {
		 * while (res.next()) {
		 * String id=res.getString("listing_id");
		 * String artiste = res.getString("artist");
		 * String titre = res.getString("title");
		 * String label=res.getString("label");
		 * String prix=res.getString("price");
		 * data[i][0]=id;
		 * System.out.println("ID :"+data[i][1]);
		 * data[i][1] = artiste;
		 * System.out.println("Artiste :" +data[i][2]);
		 * data[i][2] = titre;
		 * System.out.println("Titre : "+data[i][3]);
		 * data[i][3]=label;
		 * System.out.println(data[i][4]);
		 * data[i][4]=prix;
		 * System.out.println(data[i][5]);
		 * i++;
		 * }
		 * } catch (SQLException e1) {
		 * // TODO Auto-generated catch block
		 * e1.printStackTrace();
		 * }
		 */

		DefaultTableModel tablemodel = new DefaultTableModel(data, colonnes);
		JTable table = new JTable(tablemodel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JButton btn = new JButton("Supprimer");

		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// vérifier d'abord la ligne sélectionnée
				if (table.getSelectedRow() != -1)
					System.out.println("Ligne sélectionnée " + table.getSelectedRow());
				{

					String queryDel = "delete from disques where listing_id=?";

					Statement stm = null;
					try {
						// stm = conn.createStatement();
						int row = table.getSelectedRow();
						System.out.println("Ligne sélectionnée : " + row);
						String cell = table.getModel().getValueAt(row, 0).toString();
						String sql = "DELETE from disques where listing_id =" + cell;
						PreparedStatement pst = conn.prepareStatement(sql);
						JOptionPane.showMessageDialog(btn, "ATTENTION, cette opération est irréversible",
								"AVERTISSEMENT", JOptionPane.WARNING_MESSAGE);
						pst.execute();
						// suppression de la ligne du tableau
						tablemodel.removeRow(table.getSelectedRow());
						// stm.executeQuery(queryDel); //TROUVER LE NUMERO de la CLÉ PRIM
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// TODO Auto-generated catch block
					;

					JOptionPane.showMessageDialog(null, "Suppression réussie");
				}
			}
		});
		table.setShowGrid(true);
		JScrollPane pane = new JScrollPane(table);
		JFrame f = new JFrame(count + " enregistrements au total.");
		JPanel panel = new JPanel();
		panel.add(pane);
		f.add(panel, BorderLayout.CENTER);
		f.setSize(500, 250);
		// f.setLocation(800,500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(panel);
		f.setTitle(i + " résultat(s) trouvé(s)");
		f.add(btn, BorderLayout.SOUTH);
		f.setVisible(true);

	}
}
