package dbproject;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dbproject.tableModelsGUI.FilmsOfActorTableModel;
import dbproject.tableModelsGUI.GenreRecordTableModel;
import dbproject.tableModelsGUI.GenreTableModel;
import dbproject.tableModelsGUI.PersonTableModel;
import dbproject.tableModelsGUI.RolesOfActorTableModel;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.border.MatteBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;

public class AppGUI extends JFrame {
	
	private AddDAO mainAdder;
	private GetDAO mainGetter;	
	
	private JPanel contentPane;
	
	//==========================================
	
	//Tab 1
	private JPanel searchPanel;
	private JTable searchTable;

	//Tab 2
	//addScreenplayGUI();
	private JPanel panelAdd;
	private List<String> seriesNames;
	private JComboBox seasonComboBox;
	private JComboBox serieComboBox;
	private List<String> seasonNames;
	private JComboBox companyComboBox;
	private List<String> companyNames;
	//addPersonToScreenplayGUI();
	private JPanel personPanel;
	private JComboBox nameComboBox;
	private ArrayList<ArrayList<String>> personArray;
	private List<String> roles;
	private List<Integer> actors;
	private List<Integer> writers;
	private List<Integer> directors;
	private JTable table1;
	private List<String> personNames;	
	//addGenreToScreenplayGUI();
	private JComboBox genreComboBox;
	private JScrollPane genreScrollPane;
	private List<String> genreNames;
	private JTable genreTable;
	private ArrayList<String> chosenGenreArray;
	private ArrayList<Integer> chosenGenreID;

	//Tab 3
	private JPanel panelAddSeriesSeasons;
	// addSeasonsSeriesGUI
	private JPanel SeasonPanel;
	// addCompanyGUI()
	private JPanel CompanyPanel;	
	// addPersonGUI()
	private JPanel newPersonPanel;
	// addGenreGUI()
	private JPanel newGenrePanel;
	
	//Tab 4
	// addUserGUI()
	private JPanel panelAddReview;
	private List<Integer> listOfReviewObjectIDs;
	
	//==========================================
	
	public AppGUI(AddDAO adder, GetDAO getter) throws SQLException {
		
		//myIMDB = myDB;
		mainAdder = adder;
		mainGetter = getter;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					mainGetter.closeConnection();
					mainAdder.closeConnection();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			    System.exit(0);
			}
		});
		
		setBounds(100, 100, 939, 632);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(6, 6, 910, 602);
		contentPane.add(tabbedPane);
		
		
		//  ==============================================================================
		//  ==============================================================================

		searchPanel = new JPanel();
		tabbedPane.addTab("Databasesøk", null, searchPanel, null);
		searchPanel.setLayout(null);
		
		searchGUI();
		
		//  ==============================================================================
		//  ==============================================================================
		
		panelAdd = new JPanel();
		panelAdd.setBorder(null);
		tabbedPane.addTab("Legg til film/episode", null, panelAdd, null);
		
		//addScreenplayBackgroundGUI();
		addScreenplayGUI();
		addPersonToScreenplayGUI();		
		addGenreToScreenplayGUI();		
		
		//  ==============================================================================
		//  ==============================================================================

		
		panelAddSeriesSeasons = new JPanel();
		tabbedPane.addTab("Legg til serie, sesong, sjanger, person og selskap", null, panelAddSeriesSeasons, null);
		panelAddSeriesSeasons.setLayout(null);
		
		addSeasonsSeriesGUI();
		addPersonGUI();
		addGenreGUI();
		addCompanyGUI();
		
		
		//  ==============================================================================
		//  ==============================================================================

		panelAddReview = new JPanel();
		tabbedPane.addTab("Legg til anmeldelse og bruker", null, panelAddReview, null);
		panelAddReview.setLayout(null);
		listOfReviewObjectIDs = new ArrayList<Integer>();
		addUserGUI();
		addReviewGUI();
		

	}
	
	
// METODER ========================================

	//Tab 1: retrieve relevant information
	protected void searchGUI() { 
		JLabel labelSearch = new JLabel("Velg informasjon");
		labelSearch.setBounds(48, 103, 114, 16);
		searchPanel.add(labelSearch);
		
		JLabel labelSearch1 = new JLabel("Spesifiser søk");
		labelSearch1.setBounds(48, 135, 103, 16);
		searchPanel.add(labelSearch1);
		
		
		
		JScrollPane searchScrollPane = new JScrollPane();
		searchScrollPane.setBounds(48, 217, 500, 188);
		searchPanel.add(searchScrollPane);
		
		searchTable = new JTable();
		searchScrollPane.setViewportView(searchTable);
		
		
		JComboBox specifySearchComboBox = new JComboBox();
		specifySearchComboBox.setBounds(164, 131, 160, 27);
		searchPanel.add(specifySearchComboBox);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(164, 99, 374, 27);
		comboBox.addItem("Alle roller for gitt skuespiller");
		comboBox.addItem("Alle filmer/episode med gitt skuespiller");
		comboBox.addItem("Filmselskap med flest filmer innenfor hver sjanger");
		comboBox.setSelectedItem(null);
		searchPanel.add(comboBox);
		
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				specifySearchComboBox.removeAllItems();
				try {
					personNames = mainGetter.getPersons();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				switch (comboBox.getSelectedIndex()) {
				case 0:
					specifySearchComboBox.setEnabled(true);
					for (int i = 0; i < personNames.size(); i++) {
						specifySearchComboBox.addItem(personNames.get(i));
					}
					specifySearchComboBox.setSelectedItem(null);
					break;
				case 1:
					
					specifySearchComboBox.setEnabled(true);
					for (int i = 0; i < personNames.size(); i++) {
						specifySearchComboBox.addItem(personNames.get(i));
					}
					specifySearchComboBox.setSelectedItem(null);
					break;
				case 2:
					
					specifySearchComboBox.setEnabled(false);
					break;
				case 3:
					specifySearchComboBox.setEnabled(false);
					break;
				case 4:
					specifySearchComboBox.setEnabled(true);
					specifySearchComboBox.addItem("case5");
					specifySearchComboBox.setSelectedItem(null);
					break;
				default: 
					break;
				}
			}
		});
		
		JButton searchButton = new JButton("Søk");
		searchButton.setBounds(164, 163, 123, 42);
		searchPanel.add(searchButton);
		
		ArrayList<String> listOfRolesForActor = new ArrayList<String>();
		
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				switch (comboBox.getSelectedIndex()) {
				case 0:
					ArrayList<String> roleArray = null;
					try {
						roleArray = mainGetter.getRolesForActor((String) specifySearchComboBox.getSelectedItem());
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					RolesOfActorTableModel rolesOfActorModel = new RolesOfActorTableModel(roleArray);
					searchTable.setModel(rolesOfActorModel);
					break;
				case 1:
					ArrayList<String> filmArray = null;
					try {
						filmArray = mainGetter.getFilmsForActor((String) specifySearchComboBox.getSelectedItem());
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					FilmsOfActorTableModel filmsOfActorModel = new FilmsOfActorTableModel(filmArray);
					searchTable.setModel(filmsOfActorModel);
					break;
				case 2:
					ArrayList<ArrayList<String>> genreCompanyArray = null;
					try {
						genreCompanyArray = mainGetter.getCompanyGenreRecords();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					GenreRecordTableModel genreRecordModel = new GenreRecordTableModel(genreCompanyArray);
					searchTable.setModel(genreRecordModel);
					break;
				default: 
					break;
				}
				
			}
		});
		
		
		
		JButton trunctateButton = new JButton("Nullstill database");
		trunctateButton.setBounds(578, 351, 181, 54);
		searchPanel.add(trunctateButton);
		
		JLabel lblNewLabel_5 = new JLabel("Hent ut informasjon fra databasen");
		lblNewLabel_5.setFont(new Font("Lucida Grande", Font.BOLD, 17));
		lblNewLabel_5.setBounds(48, 39, 354, 42);
		searchPanel.add(lblNewLabel_5);
		
		trunctateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					mainGetter.trunctateDB();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				setComboBoxBlank();
			}
		});
		
		
	}
	
	//Tab 2: add screenplay (episode or film)
	protected void addPersonToScreenplayGUI() {
	
		personPanel = new JPanel();
		personPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		personPanel.setBounds(6, 279, 375, 271);
		panelAdd.add(personPanel);
		personPanel.setLayout(null);
		
		// Navn
		JLabel nameLabel = new JLabel("Velg person");
		nameLabel.setBounds(6, 73, 94, 16);
		personPanel.add(nameLabel);
		
		nameComboBox = new JComboBox();
		nameComboBox.setBounds(112, 69, 130, 27);
		try {
			personNames = mainGetter.getPersons();
			for (int i = 0; i < personNames.size(); i++) {
				nameComboBox.addItem(personNames.get(i));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		nameComboBox.setSelectedItem(null);
		personPanel.add(nameComboBox);
		
		// Rolle 
		JTextField roleTextField = new JTextField();
		roleTextField.setBounds(112, 96, 130, 26);
		roleTextField.setColumns(10);
		personPanel.add(roleTextField);
		JLabel roleLabel = new JLabel("Rolle");
		roleLabel.setBounds(6, 101, 61, 16);
		personPanel.add(roleLabel);
		
		// Funksjon
		JLabel functionLabel = new JLabel("Velg funksjon");
		functionLabel.setBounds(6, 45, 94, 16);
		personPanel.add(functionLabel);
		JComboBox functionComboBox = new JComboBox();
		functionComboBox.setBounds(112, 41, 130, 27);
		functionComboBox.addItem("Skuespiller");
		functionComboBox.addItem("Regissør");
		functionComboBox.addItem("Manusforfatter");
		functionComboBox.setSelectedItem(null);
		functionComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (functionComboBox.getSelectedIndex()==0) {
					roleTextField.setEnabled(true);
				} else {
					roleTextField.setText("");
					roleTextField.setEnabled(false);
				}
			}
		});
		personPanel.add(functionComboBox);
		
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(6, 134, 357, 131);
		personPanel.add(scrollPane2);
		table1 = new JTable();
		scrollPane2.setViewportView(table1);
	
		JButton btnNewButton = new JButton("Legg til person");
		btnNewButton.setBounds(254, 37, 109, 52);
		personPanel.add(btnNewButton);
		
		JButton resetButton = new JButton("Nullstill");
		resetButton.setBounds(254, 92, 109, 37);
		personPanel.add(resetButton);
		
		JLabel lblNewLabel_1 = new JLabel("Knytt personer til film/episode");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblNewLabel_1.setBounds(6, 10, 209, 16);
		personPanel.add(lblNewLabel_1);
		
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				personArray = new ArrayList<ArrayList<String>>();
				writers = new ArrayList<Integer>(); 
				actors = new ArrayList<Integer>(); 
				directors = new ArrayList<Integer>();
				roles = new ArrayList<String>(); 
				PersonTableModel model1 = new PersonTableModel(personArray);
				table1.setModel(model1);
			}
		});
		personArray = new ArrayList<ArrayList<String>>();
		writers = new ArrayList<Integer>(); 
		actors = new ArrayList<Integer>(); 
		directors = new ArrayList<Integer>(); 
		roles = new ArrayList<String>(); 
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> person = new ArrayList<String>(); 
				person.add((String) functionComboBox.getSelectedItem());
				person.add((String) nameComboBox.getSelectedItem());
				
				if (functionComboBox.getSelectedIndex() == 0) {
					person.add(roleTextField.getText());
					actors.add(nameComboBox.getSelectedIndex()+1);
					roles.add(roleTextField.getText());
				} if (functionComboBox.getSelectedIndex() == 1) {
					person.add("");
					directors.add(nameComboBox.getSelectedIndex()+1);
				} if (functionComboBox.getSelectedIndex() == 2) {
					person.add("");
					writers.add(nameComboBox.getSelectedIndex()+1);
				}    
				personArray.add(person);
				PersonTableModel model1 = new PersonTableModel(personArray);
				table1.setModel(model1);
				nameComboBox.setSelectedItem(null);
				functionComboBox.setSelectedItem(null);
			}
		});
	}
	protected void addGenreToScreenplayGUI() {
		JPanel genrePanel = new JPanel();
		genrePanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		genrePanel.setBounds(393, 279, 286, 271);
		panelAdd.add(genrePanel);
		genrePanel.setLayout(null);
		
		genreComboBox = new JComboBox();
		genreComboBox.setBounds(135, 65, 139, 27);
		genrePanel.add(genreComboBox);
		try {
			genreNames = mainGetter.getGenres();
			for (int i = 0; i < genreNames.size(); i++) {
				genreComboBox.addItem(genreNames.get(i));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		genreComboBox.setSelectedItem(null);
		
		JLabel genreLabel = new JLabel("Knytt sjangre til film/episode");
		genreLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		genreLabel.setBounds(16, 6, 244, 37);
		genrePanel.add(genreLabel);
		
		chosenGenreArray = new ArrayList<String>();
		chosenGenreID = new ArrayList<Integer>();
		JButton newGenreButton = new JButton("Legg til sjanger");
		newGenreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chosenGenreArray.add((String) genreComboBox.getSelectedItem());
				chosenGenreID.add(genreComboBox.getSelectedIndex()+1); 
				GenreTableModel genreModel = new GenreTableModel(chosenGenreArray);
				genreTable.setModel(genreModel);
			}
		});
		newGenreButton.setBounds(162, 138, 112, 74);
		genrePanel.add(newGenreButton);
		
		JButton resetGenreButton = new JButton("Nullstill");
		resetGenreButton.setBounds(162, 223, 112, 37);
		genrePanel.add(resetGenreButton);
		resetGenreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chosenGenreArray = new ArrayList<String>();
				chosenGenreID = new ArrayList<Integer>();
				GenreTableModel genreModel = new GenreTableModel(chosenGenreArray);
				genreTable.setModel(genreModel);
			}
		});
		genreScrollPane = new JScrollPane();
		genreScrollPane.setBounds(16, 138, 134, 122);
		genrePanel.add(genreScrollPane);
		
		genreTable = new JTable();
		genreScrollPane.setViewportView(genreTable);
		
		JLabel lblNewLabel_2 = new JLabel("Velg sjanger");
		lblNewLabel_2.setBounds(16, 69, 77, 16);
		genrePanel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_4 = new JLabel("Legg til film/episode");
		lblNewLabel_4.setFont(new Font("Lucida Grande", Font.BOLD, 17));
		lblNewLabel_4.setBounds(6, 6, 202, 22);
		panelAdd.add(lblNewLabel_4);
		
		
		
	}
	protected void addScreenplayGUI() {
		
		JRadioButton filmRadioButton = new JRadioButton("Film");
		filmRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seasonComboBox.setSelectedItem(null);
				seasonComboBox.setEnabled(false);
				serieComboBox.setSelectedItem(null);
				serieComboBox.setEnabled(false);
			}
		});
		filmRadioButton.setBounds(107, 51, 130, 27);
		panelAdd.add(filmRadioButton);
		
		
		JRadioButton episodeRadioButton = new JRadioButton("Episode");
		episodeRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				seasonComboBox.setEnabled(true);
				serieComboBox.setEnabled(true);
			}
		});
		episodeRadioButton.setBounds(107, 76, 130, 27);
		panelAdd.add(episodeRadioButton);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(episodeRadioButton);
	    group.add(filmRadioButton);
		
		// Tittel
		JLabel titleLabel = new JLabel("Tittel");
		titleLabel.setBounds(6, 115, 51, 26);
		panelAdd.add(titleLabel);
		JTextField titleTextField = new JTextField();
		titleTextField.setBounds(107, 115, 130, 26);
		titleTextField.setColumns(10);
		panelAdd.add(titleTextField);
		
		// Utgivelsesår
		JLabel yearLabel = new JLabel("Utgivelsesår");
		yearLabel.setBounds(6, 140, 89, 26);
		panelAdd.add(yearLabel);
		JTextField yearTextField = new JTextField();
		yearTextField.setBounds(107, 140, 130, 26);
		yearTextField.setColumns(10);
		panelAdd.add(yearTextField);
		
		
		// Filmdetaljer
		JLabel dateLabel = new JLabel("Lanseringsdato");
		dateLabel.setBounds(6, 178, 103, 16);
		panelAdd.add(dateLabel);
		JTextField dateTextField = new JTextField();
		dateTextField.setBounds(107, 173, 130, 26);
		panelAdd.add(dateTextField);
		dateTextField.setColumns(10);
		
		JLabel lengthLabel = new JLabel("Lengde");
		lengthLabel.setBounds(269, 178, 61, 16);
		panelAdd.add(lengthLabel);
		JTextField lengthTextField = new JTextField();
		lengthTextField.setBounds(366, 173, 125, 26);
		panelAdd.add(lengthTextField);
		lengthTextField.setColumns(10);
		
		JLabel mediumLabel = new JLabel("Velg medium");
		mediumLabel.setBounds(269, 120, 85, 16);
		panelAdd.add(mediumLabel);
		JComboBox mediumComboBox = new JComboBox();
		mediumComboBox.addItem("Kino");
		mediumComboBox.addItem("TV");
		mediumComboBox.addItem("Streaming");
		mediumComboBox.setSelectedItem(null);
		mediumComboBox.setBounds(366, 119, 125, 27);
		panelAdd.add(mediumComboBox);
		
		JLabel videoLabel= new JLabel("Videoutgivelse");
		videoLabel.setBounds(269, 145, 103, 16);
		panelAdd.add(videoLabel);
		JComboBox videoComboBox = new JComboBox();
		videoComboBox.setBounds(366, 144, 125, 27);
		videoComboBox.addItem("Nei");
		videoComboBox.addItem("Ja");
		videoComboBox.setSelectedItem(null);
		panelAdd.add(videoComboBox);
		
		// ==================================================================================
		// Filmselskap
		JLabel companyLabel = new JLabel("Velg selskap");
		companyLabel.setBounds(522, 119, 103, 16);
		panelAdd.add(companyLabel);
		companyComboBox = new JComboBox();
		companyComboBox.setBounds(614, 115, 130, 27);
		try {
			companyNames = mainGetter.getCompanies();
			for (int i = 0; i < companyNames.size(); i++) {
				companyComboBox.addItem(companyNames.get(i));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		companyComboBox.setSelectedItem(null);
		panelAdd.add(companyComboBox);
		
		// ==================================================================================
		
		// Musikk
		JLabel composerLabel = new JLabel("Komponist");
		composerLabel.setBounds(522, 145, 77, 16);
		panelAdd.add(composerLabel);
		JTextField composerTextField = new JTextField();
		composerTextField.setBounds(614, 145, 130, 26);
		panelAdd.add(composerTextField);
		composerTextField.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Fremført av");
		lblNewLabel_3.setBounds(522, 178, 83, 16);
		panelAdd.add(lblNewLabel_3);
		JTextField performerTextField = new JTextField();
		performerTextField.setBounds(614, 173, 130, 26);
		panelAdd.add(performerTextField);
		performerTextField.setColumns(10);
		
		// ==================================================================================
		
		// Storyline
		JLabel storyLabel = new JLabel("Storyline");
		storyLabel.setBounds(6, 206, 61, 16);
		panelAdd.add(storyLabel);
		
		JScrollPane storylineScrollPane = new JScrollPane();
		storylineScrollPane.setBounds(107, 211, 384, 48);
		panelAdd.add(storylineScrollPane);
		
		JTextArea storylineTextArea = new JTextArea();
		storylineScrollPane.setViewportView(storylineTextArea);
		
		// ==================================================================================
		
		JButton buttonAddFilm = new JButton("Legg til film/episode");
		buttonAddFilm.setBounds(713, 481, 158, 59);
		panelAdd.setLayout(null);
		panelAdd.add(buttonAddFilm);
		
		buttonAddFilm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String tittel = titleTextField.getText();
				int medium = mediumComboBox.getSelectedIndex()+1;
				int company = companyComboBox.getSelectedIndex()+1;
				int year = Integer.parseInt(yearTextField.getText());
				int date = Integer.parseInt(dateTextField.getText());
				int length = Integer.parseInt(lengthTextField.getText());;
				int videoutg = videoComboBox.getSelectedIndex();
				String storyline = storylineTextArea.getText();
				String komponist = composerTextField.getText();
				String fremfører = performerTextField.getText();
				
				int sesongID = 0;
				if (episodeRadioButton.isSelected()) {
					sesongID = mainGetter.getSeasonIDs().get(seasonComboBox.getSelectedIndex());					
				}
				try {
					mainAdder.addScreenplay(tittel, medium, company, year, date, 
							length, videoutg, storyline, actors, writers, directors, komponist, fremfører, sesongID, chosenGenreID, roles);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				titleTextField.setText("");
				yearTextField.setText("");
				dateTextField.setText("");
				lengthTextField.setText("");
				composerTextField.setText("");
				performerTextField.setText("");
			}
		});
		
		
		
		seasonComboBox = new JComboBox();
		seasonComboBox.setBounds(614, 53, 130, 27);
		panelAdd.add(seasonComboBox);
		
		serieComboBox = new JComboBox();
		serieComboBox.setBounds(361, 53, 130, 27);
		try {
			seriesNames = mainGetter.getSeries();
			for (int i = 0; i < seriesNames.size(); i++) {
				serieComboBox.addItem(seriesNames.get(i));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		serieComboBox.setSelectedItem(null);
		serieComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					seasonNames = mainGetter.getSeasons(serieComboBox.getSelectedIndex()+1);
					seasonComboBox.removeAllItems();
					for (int i = 0; i < seasonNames.size(); i++) {
						seasonComboBox.addItem(seasonNames.get(i));
					}
					seasonComboBox.setSelectedItem(null);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		panelAdd.add(serieComboBox);
		
		
		JLabel serieNewLabel = new JLabel("Velg serie");
		serieNewLabel.setBounds(269, 57, 61, 16);
		panelAdd.add(serieNewLabel);
		
		JLabel seasonNewLabel = new JLabel("Velg sesong");
		seasonNewLabel.setBounds(522, 57, 80, 16);
		panelAdd.add(seasonNewLabel);
		
	}
	
	//Tab 3: add persons, genres, companies, series and seasons
	protected void addSeasonsSeriesGUI() {
		
		SeasonPanel = new JPanel();
		SeasonPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		SeasonPanel.setBounds(446, 23, 271, 314);
		panelAddSeriesSeasons.add(SeasonPanel);
		SeasonPanel.setLayout(null);
		
		JLabel seasonLabel1 = new JLabel("Velg serie");
		seasonLabel1.setBounds(24, 39, 84, 16);
		SeasonPanel.add(seasonLabel1);
				
		JComboBox seriesComboBox = new JComboBox();
		seriesComboBox.setBounds(120, 35, 130, 27);
		SeasonPanel.add(seriesComboBox);
		seriesComboBox.removeAllItems();
		seriesComboBox.setSelectedItem(null);
		
		JLabel seasonLabel2 = new JLabel("Sesongnavn");
		seasonLabel2.setBounds(24, 69, 84, 16);
		SeasonPanel.add(seasonLabel2);
		
		JTextField seasonTextField = new JTextField();
		seasonTextField.setBounds(120, 64, 130, 26);
		SeasonPanel.add(seasonTextField);
		seasonTextField.setColumns(10);
		
		JButton addSeasonButton = new JButton("Legg til sesong");
		addSeasonButton.setBounds(120, 94, 130, 44);
		SeasonPanel.add(addSeasonButton);
		
		addSeasonButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					mainAdder.addSeason(seasonTextField.getText(), seriesComboBox.getSelectedIndex()+1);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				seasonTextField.setText("");
				seriesComboBox.setSelectedItem(null);
			}
		});
		// ------------------------------------------------------------

		
		JLabel seriesLabel = new JLabel("Serienavn");
		seriesLabel.setBounds(24, 195, 71, 16);
		SeasonPanel.add(seriesLabel);
		
		JTextField seriesTextField = new JTextField();
		seriesTextField.setBounds(120, 190, 130, 26);
		SeasonPanel.add(seriesTextField);
		seriesTextField.setColumns(10);
		
		JButton addSeriesButton = new JButton("Legg til serie");
		addSeriesButton.setBounds(120, 216, 130, 44);
		SeasonPanel.add(addSeriesButton);
		try {
			seriesNames = mainGetter.getSeries();
			for (int i = 0; i < seriesNames.size(); i++) {
				seriesComboBox.addItem(seriesNames.get(i));
			}
			seriesTextField.setText("");
			seriesComboBox.setSelectedItem(null);
			
			JLabel addSeasonToSerieLabel = new JLabel("Legg til sesong i serie");
			addSeasonToSerieLabel.setFont(new Font("Lucida Grande", Font.BOLD, 17));
			addSeasonToSerieLabel.setBounds(24, 6, 207, 25);
			SeasonPanel.add(addSeasonToSerieLabel);
			
			JLabel lblNewLabel = new JLabel("Legg til serie");
			lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 17));
			lblNewLabel.setBounds(24, 163, 150, 27);
			SeasonPanel.add(lblNewLabel);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		addSeriesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String serienavn = seriesTextField.getText();
				try {
					mainAdder.addSeries(serienavn);
					seriesNames = mainGetter.getSeries();			
					seriesComboBox.removeAllItems();
					serieComboBox.removeAllItems();
					for (int i = 0; i < seriesNames.size(); i++) {
						seriesComboBox.addItem(seriesNames.get(i));
						serieComboBox.addItem(seriesNames.get(i));
					}
					seriesComboBox.setSelectedItem(null);
					serieComboBox.setSelectedItem(null);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				setComboBoxBlank();
			}
		});
	}
	protected void addPersonGUI() {
		newPersonPanel = new JPanel();
		newPersonPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		newPersonPanel.setBounds(83, 23, 255, 217);
		panelAddSeriesSeasons.add(newPersonPanel);
		newPersonPanel.setLayout(null);
		
		JLabel namePersonLabel = new JLabel("Navn");
		namePersonLabel.setBounds(16, 71, 32, 16);
		newPersonPanel.add(namePersonLabel);
		JTextField nameTextField2 = new JTextField();
		nameTextField2.setBounds(109, 66, 127, 26);
		newPersonPanel.add(nameTextField2);
		nameTextField2.setColumns(10);
		
		// Fødselsår
		JLabel birthPersonLabel = new JLabel("Fødselsår");
		birthPersonLabel.setBounds(16, 99, 60, 16);
		newPersonPanel.add(birthPersonLabel);
		JTextField birthYearTextField2 = new JTextField();
		birthYearTextField2.setBounds(109, 94, 127, 26);
		newPersonPanel.add(birthYearTextField2);
		birthYearTextField2.setColumns(10);
		
		// Fødselsland
		JLabel countryPersonLabel = new JLabel("Fødselsland");
		countryPersonLabel.setBounds(16, 128, 75, 16);
		newPersonPanel.add(countryPersonLabel);
		JTextField countryTextField2 = new JTextField();
		countryTextField2.setBounds(109, 123, 127, 26);
		newPersonPanel.add(countryTextField2);
		countryTextField2.setColumns(10);
		
		JButton btnNewButtonPerson = new JButton("Legg til person");
		btnNewButtonPerson.setBounds(109, 155, 127, 45);
		newPersonPanel.add(btnNewButtonPerson);
		
		JLabel addNewPersonLabel = new JLabel("Legg til person");
		addNewPersonLabel.setFont(new Font("Lucida Grande", Font.BOLD, 17));
		addNewPersonLabel.setBounds(16, 24, 194, 26);
		newPersonPanel.add(addNewPersonLabel);
	
		btnNewButtonPerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					mainAdder.addPerson(nameTextField2.getText(), Integer.parseInt(birthYearTextField2.getText()), countryTextField2.getText());
					personNames = mainGetter.getPersons();
					nameComboBox.removeAllItems();
					for (int i = 0; i < personNames.size(); i++) {
						nameComboBox.addItem(personNames.get(i));
					}
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				setComboBoxBlank();
			}
		});
	}
	protected void addGenreGUI() {
		newGenrePanel = new JPanel();
		newGenrePanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		newGenrePanel.setBounds(446, 368, 271, 155);
		panelAddSeriesSeasons.add(newGenrePanel);
		newGenrePanel.setLayout(null);
		
		JLabel addGenreLabel = new JLabel("Sjanger");
		addGenreLabel.setBounds(16, 44, 79, 16);
		newGenrePanel.add(addGenreLabel);
		
		JTextField genreTextField = new JTextField();
		genreTextField.setBounds(107, 39, 130, 26);
		newGenrePanel.add(genreTextField);
		genreTextField.setColumns(10);
		
		JButton addGenreButton = new JButton("Legg til sjanger");
		addGenreButton.setBounds(107, 68, 130, 45);
		newGenrePanel.add(addGenreButton);
		
		JLabel addNewGenreLabel = new JLabel("Legg til sjanger");
		addNewGenreLabel.setFont(new Font("Lucida Grande", Font.BOLD, 17));
		addNewGenreLabel.setBounds(16, 6, 147, 26);
		newGenrePanel.add(addNewGenreLabel);
		addGenreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					mainAdder.addGenre(genreTextField.getText());
					genreNames = mainGetter.getGenres();
					genreComboBox.removeAllItems();
					for (int i = 0; i < genreNames.size(); i++) {
						genreComboBox.addItem(genreNames.get(i));
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				genreTextField.setText("");
			}
		});
	}
	protected void addCompanyGUI() {		
		CompanyPanel = new JPanel();
		CompanyPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		CompanyPanel.setBounds(83, 268, 255, 255);
		panelAddSeriesSeasons.add(CompanyPanel);
		CompanyPanel.setLayout(null);
		
		JLabel companyLabel2 = new JLabel("Selskapsnavn");
		companyLabel2.setBounds(19, 61, 85, 16);
		CompanyPanel.add(companyLabel2);
		JTextField companyNameTextField2 = new JTextField();
		companyNameTextField2.setBounds(106, 56, 130, 27);
		CompanyPanel.add(companyNameTextField2);
		companyNameTextField2.setColumns(10);
		
		JLabel adressLabel2 = new JLabel("Adresse");
		adressLabel2.setBounds(19, 89, 61, 16);
		CompanyPanel.add(adressLabel2);
		JTextField adressTextField3 = new JTextField();
		adressTextField3.setBounds(106, 84, 130, 26);
		CompanyPanel.add(adressTextField3);
		adressTextField3.setColumns(10);
		JTextField companyFromTextField2 = new JTextField();
		companyFromTextField2.setBounds(106, 113, 130, 26);
		CompanyPanel.add(companyFromTextField2);
		companyFromTextField2.setColumns(10);
		
		JLabel companyCountryLabel2 = new JLabel("Land");
		companyCountryLabel2.setBounds(19, 118, 61, 16);
		CompanyPanel.add(companyCountryLabel2);
		
		JLabel urlLabel2 = new JLabel("URL");
		urlLabel2.setBounds(19, 152, 61, 16);
		CompanyPanel.add(urlLabel2);
		JTextField urlTextField2 = new JTextField();
		urlTextField2.setBounds(106, 147, 130, 26);
		CompanyPanel.add(urlTextField2);
		urlTextField2.setColumns(10);
		
		JButton btnNewButtonCompany = new JButton("Legg til selskap");
		btnNewButtonCompany.setBounds(106, 180, 130, 52);
		CompanyPanel.add(btnNewButtonCompany);
		
		JLabel addCompanyLabel = new JLabel("Legg til selskap");
		addCompanyLabel.setFont(new Font("Lucida Grande", Font.BOLD, 17));
		addCompanyLabel.setBounds(19, 17, 180, 32);
		CompanyPanel.add(addCompanyLabel);
		
		btnNewButtonCompany.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					mainAdder.addCompany(companyNameTextField2.getText(), adressTextField3.getText(), companyFromTextField2.getText(), urlTextField2.getText());
					companyNames = mainGetter.getCompanies();
					companyComboBox.removeAllItems();
					for (int i = 0; i < companyNames.size(); i++) {
						companyComboBox.addItem(companyNames.get(i));
					}
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				companyNameTextField2.setText("");
				adressTextField3.setText("");
				companyFromTextField2.setText("");
				urlTextField2.setText("");
			}
		});
	
	}
	
	//Tab 4: add user and review
	protected void addUserGUI() {

		JPanel addUserPanel = new JPanel();
		addUserPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		addUserPanel.setBounds(31, 28, 350, 158);
		panelAddReview.add(addUserPanel);
		addUserPanel.setLayout(null);
		
		JLabel userLabel = new JLabel("Skriv inn brukernavn");
		userLabel.setBounds(21, 68, 167, 16);
		addUserPanel.add(userLabel);
		
		JTextField userTextField = new JTextField();
		userTextField.setBounds(200, 63, 130, 26);
		addUserPanel.add(userTextField);
		userTextField.setColumns(10);
		
		JButton addUserButton = new JButton("Legg til bruker");
		addUserButton.setBounds(200, 100, 130, 39);
		addUserPanel.add(addUserButton);
		
		JLabel addNewUserLabel = new JLabel("Legg til ny bruker");
		addNewUserLabel.setFont(new Font("Lucida Grande", Font.BOLD, 17));
		addNewUserLabel.setBounds(21, 16, 177, 26);
		addUserPanel.add(addNewUserLabel);
		
		addUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					mainAdder.addUser(userTextField.getText());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				userTextField.setText("");
			}
		});
		
		
	}
	protected void addReviewGUI() {
		
		JPanel addReviewPanel = new JPanel();
		addReviewPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		addReviewPanel.setBounds(31, 228, 702, 276);
		panelAddReview.add(addReviewPanel);
		addReviewPanel.setLayout(null);
		

		JComboBox reviewObjectComboBox = new JComboBox();
		reviewObjectComboBox.setBounds(147, 198, 125, 27);
		addReviewPanel.add(reviewObjectComboBox);
		
		JComboBox chooseSeasonComboBox = new JComboBox();
		chooseSeasonComboBox.setBounds(148, 159, 124, 27);
		addReviewPanel.add(chooseSeasonComboBox);
		
		
		JComboBox chooseSerieComboBox = new JComboBox();
		chooseSerieComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					seasonNames = mainGetter.getSeasons(chooseSerieComboBox.getSelectedIndex()+1);
					chooseSeasonComboBox.removeAllItems();
					for (int i = 0; i < seasonNames.size(); i++) {
						chooseSeasonComboBox.addItem(seasonNames.get(i));
					}
					chooseSeasonComboBox.setSelectedItem(null);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		chooseSerieComboBox.setBounds(148, 124, 124, 27);
		addReviewPanel.add(chooseSerieComboBox);
		
		chooseSeasonComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ArrayList<String> listOfEpisodeNames = new ArrayList<String>();
			
				try {
					listOfEpisodeNames = mainGetter.getEpisodeNames((String) chooseSeasonComboBox.getSelectedItem(), (String) chooseSerieComboBox.getSelectedItem());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				reviewObjectComboBox.removeAllItems();
				for (int i = 0; i < listOfEpisodeNames.size(); i++) {
					reviewObjectComboBox.addItem(listOfEpisodeNames.get(i));
				}
				reviewObjectComboBox.setSelectedItem(null);
				//listOfReviewObjectIDs.clear();
				//listOfReviewObjectIDs = myIMDB.getReviewObjectIDs();
				//System.out.println("1 lengde på liste er "+listOfReviewObjectIDs.size());
			}
		});
		
		
		
		JRadioButton filmRadioButton = new JRadioButton("Film");
		filmRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> listOfFilmNames = new ArrayList<String>();
				chooseSerieComboBox.setSelectedItem(null);
				chooseSeasonComboBox.setSelectedItem(null);
				chooseSerieComboBox.setEnabled(false);
				chooseSeasonComboBox.setEnabled(false);
				reviewObjectComboBox.removeAllItems();
				try {
					listOfFilmNames = mainGetter.getFilmNames();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				for (int i = 0; i < listOfFilmNames.size(); i++) {
					reviewObjectComboBox.addItem(listOfFilmNames.get(i));
				}
				reviewObjectComboBox.setSelectedItem(null);
			}
		});
		filmRadioButton.setBounds(16, 55, 141, 23);
		addReviewPanel.add(filmRadioButton);
		
		JRadioButton episodeRadioButton = new JRadioButton("Episode");
		episodeRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseSerieComboBox.setEnabled(true);
				chooseSeasonComboBox.setEnabled(true);
				try {
					seriesNames = mainGetter.getSeries();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				chooseSerieComboBox.removeAllItems();
				for (int i = 0; i < seriesNames.size(); i++) {
					chooseSerieComboBox.addItem(seriesNames.get(i));
				}
				chooseSerieComboBox.setSelectedItem(null);
			}
		});
		episodeRadioButton.setBounds(16, 79, 141, 23);
		addReviewPanel.add(episodeRadioButton);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(episodeRadioButton);
	    group.add(filmRadioButton);
		
		
		
		JComboBox chooseUserComboBox = new JComboBox();
		List<String> listOfUserNames = new ArrayList<String>();
		try {
			listOfUserNames = mainGetter.getUserNames();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		chooseUserComboBox.removeAllItems();
		for (int i = 0; i < listOfUserNames.size(); i++) {
			chooseUserComboBox.addItem(listOfUserNames.get(i));
		}
		chooseUserComboBox.setSelectedItem(null);
		chooseUserComboBox.setBounds(147, 237, 125, 27);
		addReviewPanel.add(chooseUserComboBox);
		
		
		JComboBox chooseRatingComboBox = new JComboBox();
		chooseRatingComboBox.setBounds(404, 110, 72, 27);
		chooseRatingComboBox.addItem("1");
		chooseRatingComboBox.addItem("2");
		chooseRatingComboBox.addItem("3");
		chooseRatingComboBox.addItem("4");
		chooseRatingComboBox.addItem("5");
		chooseRatingComboBox.addItem("6");
		chooseRatingComboBox.addItem("7");
		chooseRatingComboBox.addItem("8");
		chooseRatingComboBox.addItem("9");
		chooseRatingComboBox.addItem("10");
		addReviewPanel.add(chooseRatingComboBox);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(310, 181, 169, 83);
		addReviewPanel.add(scrollPane);
		
		JTextArea reviewTextArea = new JTextArea();
		scrollPane.setViewportView(reviewTextArea);
		
		JButton addReviewButton = new JButton("Legg til anmeldelse");
		addReviewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listOfReviewObjectIDs = mainGetter.getReviewObjectIDs();
				try {
					mainAdder.addReview(
							chooseUserComboBox.getSelectedIndex()+1, 
							listOfReviewObjectIDs.get(reviewObjectComboBox.getSelectedIndex()), 
							reviewTextArea.getText(),
							Integer.parseInt((String) chooseRatingComboBox.getSelectedItem()));
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				chooseRatingComboBox.setSelectedItem(null);
				chooseUserComboBox.setSelectedItem(null);
				chooseSerieComboBox.setSelectedItem(null);
				chooseSeasonComboBox.setSelectedItem(null);
			}
		});
		addReviewButton.setBounds(512, 214, 169, 50);
		addReviewPanel.add(addReviewButton);
		
		JLabel writeReviewLabel = new JLabel("Skriv anmeldelse");
		writeReviewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 17));
		writeReviewLabel.setBounds(16, 10, 229, 22);
		addReviewPanel.add(writeReviewLabel);
		
		JLabel chooseSerieLabel = new JLabel("Velg serie");
		chooseSerieLabel.setBounds(16, 128, 61, 16);
		addReviewPanel.add(chooseSerieLabel);
		
		JLabel chooseSeasonLabel = new JLabel("Velg sesong");
		chooseSeasonLabel.setBounds(16, 163, 81, 16);
		addReviewPanel.add(chooseSeasonLabel);
		
		JLabel chooseFilmEpisodeLabel = new JLabel("Velg film/episode");
		chooseFilmEpisodeLabel.setBounds(16, 202, 125, 16);
		addReviewPanel.add(chooseFilmEpisodeLabel);
		
		JLabel chooseUserLabel = new JLabel("Velg brukernavn");
		chooseUserLabel.setBounds(16, 241, 119, 16);
		addReviewPanel.add(chooseUserLabel);
		
		JLabel chooseRatingLabel = new JLabel("Velg rating");
		chooseRatingLabel.setBounds(310, 114, 86, 16);
		addReviewPanel.add(chooseRatingLabel);
		
		JLabel writeOpinionLabel = new JLabel("Skriv anmeldelse");
		writeOpinionLabel.setBounds(310, 151, 129, 16);
		addReviewPanel.add(writeOpinionLabel);
	
		
		
	}
	
	protected void setComboBoxBlank() {
		seasonComboBox.setSelectedItem(null);
		serieComboBox.setSelectedItem(null);
		companyComboBox.setSelectedItem(null);
		genreComboBox.setSelectedItem(null);
	}
}






