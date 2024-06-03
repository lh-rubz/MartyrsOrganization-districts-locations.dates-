package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main2 extends Application {
    DistrictBST Districts = new DistrictBST();
    CustomError error = new CustomError();
    ComboBox<String> AgeCB = new ComboBox<String>();
    MenuItem addLocation = new MenuItem("Add Location"), updateL = new MenuItem("Update Location"),
            deletel = new MenuItem("Delete Location"), showL = new MenuItem("Show Locations"),
            searchL = new MenuItem("Search for Location"), MartyrFunctions = new MenuItem("Martyr Functions");
    javafx.scene.control.Menu LocationM = new javafx.scene.control.Menu("Location");
    ConfirmationDialog confirm = new ConfirmationDialog();

    void ErrorMessage(String errorM) {
        // Set the error message in the error dialog
        error.setErrorMessage(errorM);
        // Start the error dialog in a new window (Stage)
        error.start(new Stage());
    }

    boolean ConfirmMessage(String confirmM) {
        // Set the confirmation message in the confirmation dialog
        confirm.setConfirmationMessage(confirmM);
        boolean result = false;
        // Start the confirmation dialog in a new window (Stage)
        confirm.start(new Stage());
        // Check if the user confirmed (assuming getDone() returns 1 on confirmation)
        if (confirm.getDone() == 1) {
            return true;
        } else {
            return false;
        }
    }


    public boolean readFromFile() throws FileNotFoundException {
        // Create a new stage for file chooser
        Stage stage = new Stage();

        // Create a file chooser dialog
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Choose file to read your Data.");
        // Show the file chooser dialog and wait for user input
        File file = chooseFile.showOpenDialog(stage);
        // Check if the user canceled the dialog
        if (file == null) {
            ErrorMessage("You have not chosen a file.");
            return false;
        }
        // Check if the chosen file is not a CSV file
        if (!file.getAbsolutePath().endsWith(".csv")) {
            ErrorMessage("File chosen is not a CSV file");
            return false;
        }
        // Try to read the file
        try (Scanner scan = new Scanner(file)) {
            // Skip the first line as it contains headers
            scan.nextLine(); // we don't need the first line
            // Read each line of the file
            while (scan.hasNext()) {
                try {
                    // Split the line by comma to get individual fields
                    String[] line = scan.nextLine().split(",");
                    // Check if the line doesn't have exactly 6 fields
                    if (line.length != 6) {
                        ErrorMessage("The file chosen is invalid");
                        continue; // Skip to the next line
                    }
                    // Split the date field to extract day, month, and year
                    String[] date = line[1].split("/");
                    // Check if the date format is valid
                    if (date.length != 3) {
                        continue; // Skip this line and proceed to the next one
                    }

                    // Parse day, month, and year
                    int month = Integer.parseInt(date[0]);
                    int day = Integer.parseInt(date[1]);
                    int year = Integer.parseInt(date[2]);
                    int age = 0;
                    // Parse age field, handle NumberFormatException if invalid
                    try {
                        age = Integer.parseInt(line[2]);
                    } catch (NumberFormatException e) {
                        age = -1;
                    }

                    // Convert gender to uppercase
                    char gender = Character.toUpperCase(line[5].charAt(0));
                    // Create a Martyr object with parsed data
                    Martyr martyr = new Martyr(line[0], (year + "/" + month + "/" + day), age, line[3], line[4], gender);

                    // Create a new District object
                    District newDistrict = new District(line[4]);
                    // Check if the district does not exist in the list of districts
                    if (!Districts.contains(newDistrict)) {
                        // Add the martyr to the district
                        newDistrict.addMartyr(martyr, line[3]);
                        // Add the district to the list of districts
                        Districts.add(newDistrict);

                    } else {
                        // Get the existing district from the list
                        District tempDistrict = Districts.getDistrict(newDistrict.getDistrictName());
                        // Add the martyr to the existing district
                        tempDistrict.addMartyr(martyr, line[3]);
                    }
                } catch (Exception ex) {
                    // Handle any exceptions during line processing
                    ex.printStackTrace();
                }
            }
        } catch (FileNotFoundException ex) {
            // Handle file not found exception
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {//starts the program

        try {
            FirstScene(primaryStage);
            LocationM.getItems().setAll(addLocation, updateL, deletel, showL, searchL, MartyrFunctions);
            primaryStage.show();
        } catch (Exception e) {

        }

    }

    private void FirstScene(Stage stage) { // this is the first scene to show
        ImageView Dome = new ImageView("file:dome_drawing.jpg");

        // Introduction label
        Label intro = new Label(
                "\n\n\t\t\tThis project shares numbers about the martyrs in different areas in Palestine.\n\t\t\tIt's a way to honor their sacrifice and show how tough the occupation is.\n\t\tBy remembering our martyrs, we hope to raise awareness and support for our cause.");
        intro.setAlignment(Pos.CENTER);
        intro.setStyle(
                "-fx-font-weight: bold; -fx-font-family:Times-Roman; -fx-font-size: 12px; -fx-text-fill:black;");

        // Start label
        Label startL = new Label("\t\t\tPlease choose:\n read from file or continue without reading");
        startL.setStyle(
                "-fx-font-weight: bold;-fx-font-family:Times-Roman; -fx-font-size: 12px; -fx-text-fill:black;");

        VBox readFromFileVB = new VBox(10);
        Button readB = new Button("Read From File"), continueB = new Button("Continue without Reading");
        readB.setStyle(
                "-fx-font-weight: bold;-fx-font-family:Times-Roman; -fx-font-size: 12px; -fx-text-fill:black; -fx-background-color: #FFFFF0");

        // Buttons container
        HBox buttonsHB = new HBox(12);
        buttonsHB.setAlignment(Pos.CENTER);
        buttonsHB.getChildren().addAll(readB, continueB);
        readFromFileVB.getChildren().addAll(startL, buttonsHB);

        BorderPane bpp = new BorderPane();
        continueB.setStyle(
                "-fx-font-weight: bold;-fx-font-family:Times-Roman; -fx-font-size: 12px; -fx-text-fill:black; -fx-background-color: #FFFFF0;");

        // Continue button action
        continueB.setOnAction(e -> {
            secondStage(stage);
        });

        readFromFileVB.getChildren().add(new Label("\n\n"));
        bpp.setBottom(readFromFileVB);
        readFromFileVB.setAlignment(Pos.CENTER);

        // Read button action

        readB.setOnAction(e -> {
            try {
                if (readFromFile()) {
                    secondStage(stage);
                }
            } catch (Exception r) {
                r.printStackTrace();
            }
        });


        bpp.setTop(intro);
        bpp.setCenter(Dome);
        bpp.setStyle("-fx-background-color:#D2B48C");
        bpp.setAlignment(readFromFileVB, Pos.TOP_CENTER);
        Scene scene = new Scene(bpp, 600, 600);
        stage.setScene(scene);
    }


    private void secondStage(Stage stage) {
        // Create the menu bar for the second stage
        MenuBar menu1 = Menu1(stage);

        // Create and configure the main BorderPane layout
        BorderPane Mainbp = new BorderPane();
        Mainbp.setTop(menu1);
        Mainbp.setStyle("-fx-background-color:#F0DFD0;");

        // Create the scene and set it on the stage
        Scene Mainscene = new Scene(Mainbp, 500, 500);
        stage.setScene(Mainscene);
    }

    MenuBar Menu1(Stage stage) {
        MenuBar MB = new MenuBar();
        javafx.scene.control.Menu DistrictM = new javafx.scene.control.Menu("District");

        // Create menu items
        MenuItem InsertD = new MenuItem("Insert a new District"),
                updateD = new MenuItem("Update district Record"),
                deleteD = new MenuItem("Delete a district"),
                navigateD = new MenuItem("Show districts"),
                SaveToFile = new MenuItem("Save to File"),
                PrintToFileD = new MenuItem("Print District data"),
                readFromFile = new MenuItem("Read From File"),
                MoveToLocation = new MenuItem("Location Screen"),
                SearchMartyr = new MenuItem("Search For Martyr");

        // Add menu items to the District menu
        DistrictM.getItems().setAll(InsertD, updateD, deleteD, navigateD, PrintToFileD, readFromFile, SearchMartyr,
                SaveToFile, MoveToLocation);

        // Set actions for each menu item
        MoveToLocation.setOnAction(e -> getDistrictScene(stage));
        InsertD.setOnAction(e -> insertDistrictPane(stage));
        updateD.setOnAction(e -> updateDistrictScreen(stage));
        PrintToFileD.setOnAction(e -> printToFile(stage));
        deleteD.setOnAction(e -> deleteDistrictScreen(stage));
        readFromFile.setOnAction(e -> inSceneRead());
        navigateD.setOnAction(e -> goThroughDistrictsScreen(stage));
        SaveToFile.setOnAction(e -> saveToCSVFileScreen());
        SearchMartyr.setOnAction(e -> SearchinDistrictsScreen(stage));

        // Set styles for the menu bar and menu
        MB.setStyle("-fx-background-color:#B9D1BF;");
        DistrictM.setStyle(
                "-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-border-color:#F0DFD0;");

        // Add the District menu to the menu bar
        MB.getMenus().setAll(DistrictM);
        return MB;
    }


    private TableView<Martyr> AllmartyrsD = new TableView<>();

    private void SearchinDistrictsScreen(Stage stage) {
        Scene scene = null;
        BorderPane districtPane = new BorderPane();
        VBox DataVB = new VBox(10);

        // Setup the start label
        Label StartL = new Label("Search for martyrs in All Districts");
        StartL.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
        StartL.setAlignment(Pos.CENTER);

        // Setup the search text field and button
        TextField MartyrNameTF = new TextField();
        MartyrNameTF.setPromptText("EXAMPLE: Heba jamal");
        Button searchB = new Button("Search");
        searchB.setStyle(
                "-fx-background-color: #add0b3; -fx-font-weight: bold; -fx-font-family: Times-Roman; -fx-text-fill: white");
        HBox searchingHB = new HBox(5);

        // Align search components
        searchingHB.setAlignment(Pos.CENTER);
        searchingHB.getChildren().addAll(MartyrNameTF, searchB);

        // Initialize martyr data
        ObservableList<Martyr> AllMartyrs = InitialListMartyrsinDistrictAll();

        // Setup the table view for displaying martyrs
        TableView<Martyr> martyrsTable = new TableView<>();
        TableColumn<Martyr, String> nameL = new TableColumn<>("name");
        nameL.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Martyr, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        TableColumn<Martyr, Integer> age = new TableColumn<>("age");
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        TableColumn<Martyr, String> locationC = new TableColumn<>("location");
        locationC.setCellValueFactory(new PropertyValueFactory<>("location"));
        TableColumn<Martyr, String> districtC = new TableColumn<>("District");
        districtC.setCellValueFactory(new PropertyValueFactory<>("District"));
        TableColumn<Martyr, Character> gender = new TableColumn<>("gender");
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        martyrsTable.getColumns().setAll(nameL, date, age, locationC, districtC, gender);
        martyrsTable.setPrefSize(400, 400);

        // Populate the table with data
        ObservableList<Martyr> listData = FXCollections.observableArrayList();
        martyrsTable.setItems(listData);
        Stack DistrictsS = Districts.inOrder();
        while (!DistrictsS.isEmpty()) {
            District district = (District) DistrictsS.pop();
            Stack locationS = district.getLocations().inOrder();
            while (!locationS.isEmpty()) {
                Location templocation = (Location) locationS.pop();
                Stack dateS = templocation.getDateBST().inOrder();
                while (!dateS.isEmpty()) {
                    Date dateTemp = (Date) dateS.pop();
                    for (int i = 0; i < dateTemp.getMartyrs().getSize(); i++) {
                        listData.add(dateTemp.getMartyrs().getElement(i));
                    }
                }
            }
        }

        // Set action for the search button
        searchB.setOnAction(e -> {
            if (MartyrNameTF.getText().isEmpty())
                ErrorMessage("Please enter the name you are searching for and then press search.");
            else {
                listData.setAll(searchList(MartyrNameTF.getText(), AllMartyrs));
            }
        });

        // Arrange components in the layout
        DataVB.getChildren().addAll(StartL, searchingHB, martyrsTable);
        DataVB.setAlignment(Pos.CENTER);
        districtPane.setTop(Menu1(stage));
        districtPane.setCenter(DataVB);
        districtPane.setAlignment(DataVB, Pos.CENTER);
        districtPane.setStyle("-fx-background-color:#F0DFD0;");

        // Create and set the scene
        scene = new Scene(districtPane, 500, 500);
        stage.setScene(scene);
        stage.show();
    }


    private ObservableList<Martyr> searchList(String text, ObservableList<Martyr> allMartyrs) {
        // Create a new list to hold the search results
        ObservableList<Martyr> newListMartyrs = FXCollections.observableArrayList();

        // Iterate through all martyrs
        for (int i = 0; i < allMartyrs.size(); i++) {
            // Check if the martyr's name contains the search text (case insensitive)
            if (allMartyrs.get(i).getName().toLowerCase().contains(text.toLowerCase())) {
                // Add matching martyr to the new list
                newListMartyrs.add(allMartyrs.get(i));
            }
        }

        // Return the list of matching martyrs
        return newListMartyrs;
    }


    public ObservableList<Martyr> InitialListMartyrsinDistrictAll() {
        // Create a list to hold all martyrs
        ObservableList<Martyr> list = FXCollections.observableArrayList();

        // Define table columns for displaying martyr data
        TableColumn<Martyr, String> nameL = new TableColumn<>("name");
        nameL.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Martyr, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        TableColumn<Martyr, String> age = new TableColumn<>("age");
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        TableColumn<Martyr, String> location = new TableColumn<>("location");
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        TableColumn<Martyr, String> districtN = new TableColumn<>("District");
        districtN.setCellValueFactory(new PropertyValueFactory<>("District"));
        TableColumn<Martyr, String> gender = new TableColumn<>("gender");
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Add the columns to the table view
        AllmartyrsD.getColumns().setAll(nameL, date, age, location, districtN, gender);
        AllmartyrsD.setPrefSize(400, 400);

        // Populate the list with martyrs from all districts
        Stack DistrictsS = Districts.inOrder();
        while (!DistrictsS.isEmpty()) {
            District district = (District) DistrictsS.pop();
            Stack locationS = district.getLocations().inOrder();
            while (!locationS.isEmpty()) {
                Location templocation = (Location) locationS.pop();
                Stack dateS = templocation.getDateBST().inOrder();
                while (!dateS.isEmpty()) {
                    Date dateTemp = (Date) dateS.pop();
                    for (int i = 0; i < dateTemp.getMartyrs().getSize(); i++) {
                        list.add(dateTemp.getMartyrs().getElement(i));
                    }
                }
            }
        }

        // Set the list of martyrs to the table view
        AllmartyrsD.setItems(list);
        return list;
    }


    private void saveToCSVFileScreen() {
        // Create a new stage for file saving options
        Stage newStage = new Stage();

        // Create buttons for saving and saving as
        HBox buttonsHBox = new HBox(5);
        Button saveB = new Button("Save"), SaveAsB = new Button("Save As");
        saveB.setStyle("-fx-background-color: #add0b3; -fx-font-weight: bold; -fx-font-family: Times-Roman; -fx-text-fill: white");
        SaveAsB.setStyle("-fx-background-color: #add0b3; -fx-font-weight: bold; -fx-font-family: Times-Roman; -fx-text-fill: white");
        buttonsHBox.getChildren().addAll(saveB, SaveAsB);

        // Create a message label
        VBox stageVB = new VBox(10);
        Label messageL = new Label("Choose \"Save\" to save to the default file.\nChoose \"Save As\" to save to a file you choose.");
        messageL.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
        stageVB.getChildren().addAll(messageL, buttonsHBox);

        // Align components
        buttonsHBox.setAlignment(Pos.CENTER);
        stageVB.setAlignment(Pos.CENTER);
        messageL.setAlignment(Pos.CENTER);

        // Set actions for save and save as buttons
        saveB.setOnAction(e -> {
            try {
                saveToFile(new File("Data.csv")); // Save to default file
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            newStage.hide(); // Hide the stage after saving
        });
        SaveAsB.setOnAction(e -> {
            // Create a new stage for file chooser
            Stage stage = new Stage();

            // Create a file chooser dialog
            FileChooser chooseFile = new FileChooser();
            chooseFile.setTitle("Choose file to read your Data.");
            // Show the file chooser dialog and wait for user input
            File file = chooseFile.showOpenDialog(stage);
            // Check if the user canceled the dialog
            if (file == null) {
                ErrorMessage("You have not chosen a file.");
                return;
            }

            try {
                saveToFile(file); // Save to chosen file
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            newStage.hide(); // Hide the stage after saving
        });

        // Set style for the stage
        stageVB.setStyle("-fx-background-color:#F0DFD0;");
        stageVB.setAlignment(Pos.CENTER);

        // Create and set the scene for the stage
        Scene scene = new Scene(stageVB, 300, 300);
        newStage.setScene(scene);
        newStage.show(); // Show the stage
    }

    private void saveToFile(File file) throws FileNotFoundException {
        PrintWriter pR = new PrintWriter(file);
        pR.println("Name,Date,Age,Location,District,gender");
        Stack DistrictS = Districts.inOrder();
        District DD;
        while (!DistrictS.isEmpty()) {
            DD = (District) DistrictS.pop();
            Stack locationS = DD.getLocations().inOrder();
            while (!locationS.isEmpty()) {
                Location templocation = (Location) locationS.pop();
                Stack dateS = templocation.getDateBST().inOrder();
                while (!dateS.isEmpty()) {
                    Date dateTemp = (Date) dateS.pop();
                    for (int i = 0; i < dateTemp.getMartyrs().getSize(); i++) {
                        pR.println(dateTemp.getMartyrs().getElement(i).toString());
                    }
                }
            }
        }
        pR.close();
    }

    static Label DistrictName = new Label(), NumOfMartyrsL = new Label();

    private void resetDistrictData(District tempDistrict) {
        // Set district name and total number of martyrs
        DistrictName.setText("District Name: " + tempDistrict.getDistrictName());
        NumOfMartyrsL.setText("Total Number OF Martyrs is: " + tempDistrict.getTotalNumMartyrs());

        // Apply styles
        DistrictName.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
        NumOfMartyrsL.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
    }

    public void TableViewMartyrsinDistrict(District district) {
        // Create a new stage for displaying martyrs in the district
        Stage newStage = new Stage();

        // Create a table view to display martyrs
        TableView<Martyr> martyrs = new TableView<>();
        ObservableList<Martyr> list = FXCollections.observableArrayList();

        // Define table columns
        TableColumn<Martyr, String> nameL = new TableColumn<Martyr, String>("name");
        nameL.setCellValueFactory(new PropertyValueFactory<Martyr, String>("name"));
        TableColumn<Martyr, String> date = new TableColumn<Martyr, String>("Date");
        date.setCellValueFactory(new PropertyValueFactory<Martyr, String>("Date"));
        TableColumn<Martyr, Integer> age = new TableColumn("age");
        age.setCellValueFactory(new PropertyValueFactory<Martyr, Integer>("age"));
        TableColumn<Martyr, String> location = new TableColumn<Martyr, String>("location");
        location.setCellValueFactory(new PropertyValueFactory<Martyr, String>("location"));
        TableColumn<Martyr, String> districtN = new TableColumn<Martyr, String>("District");
        districtN.setCellValueFactory(new PropertyValueFactory<Martyr, String>("District"));
        TableColumn<Martyr, Character> gender = new TableColumn<Martyr, Character>("gender");
        gender.setCellValueFactory(new PropertyValueFactory<Martyr, Character>("gender"));
        martyrs.getColumns().addAll(nameL, date, age, location, districtN, gender);
        martyrs.setPrefSize(400, 400);

        // Populate martyrs list from district
        Stack locationS = district.getLocations().inOrder();
        while (!locationS.isEmpty()) {
            Location templocation = (Location) locationS.pop();
            Stack dateS = templocation.getDateBST().inOrder();
            while (!dateS.isEmpty()) {
                Date dateTemp = (Date) dateS.pop();
                for (int i = 0; i < dateTemp.getMartyrs().getSize(); i++) {
                    list.add(dateTemp.getMartyrs().getElement(i));
                }
            }
        }

        // Set martyrs list to the table view
        martyrs.setItems(list);

        // Create label for displaying district name
        Label DL = new Label("Martyrs in " + district.getDistrictName());
        DL.setStyle("-fx-font-weight:bold;-fx-font-size: 18px; -fx-font-family: 'Times New Roman'; -fx-text-fill: black;");

        // Create VBox to hold label and table view
        VBox martyrsVB = new VBox(30);
        martyrsVB.setAlignment(Pos.CENTER);
        martyrsVB.setStyle("-fx-background-color:#F0DFD0;");
        martyrsVB.getChildren().addAll(DL, martyrs);

        // Create scene and set it to the stage
        Scene scene = new Scene(martyrsVB, 600, 500);
        newStage.setScene(scene);

        // Show the stage
        newStage.show();
    }

    District tempDistrict = null;
    Stack Firststack = new Stack(), secStack = new Stack();

    private void goThroughDistrictsScreen(Stage stage) {
        // Initialize variables
        Scene scene = null;
        secStack = new Stack();
        Firststack = Districts.inOrder();
        BorderPane districtPane = new BorderPane();
        VBox DataVB = new VBox(20);
        Button showMartyrB = new Button("Show Martyrs");

        // Check if there are no districts
        if (Districts.isEmpty()) {
            // Display message if no districts are present
            Label noMartyrs = new Label("No districts were added to the system.");
            noMartyrs.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
            DataVB.getChildren().setAll(noMartyrs);
            DataVB.setAlignment(Pos.CENTER);
        } else {
            // If districts exist, display district statistics
            tempDistrict = (District) Firststack.pop();
            resetDistrictData(tempDistrict);
            Label titleL = new Label("District statistics\n\n\n\n");
            titleL.setStyle("-fx-font-weight:bold;-fx-font-size: 18px; -fx-font-family: 'Times New Roman'; -fx-text-fill: black;");
            VBox AllDataVB = new VBox(15);
            showMartyrB.setStyle("-fx-background-color: #add0b3; -fx-font-weight: bold; -fx-font-family: Times-Roman; -fx-text-fill: white");
            AllDataVB.getChildren().setAll(DistrictName, NumOfMartyrsL, showMartyrB);
            AllDataVB.setAlignment(Pos.CENTER);
            DataVB.getChildren().setAll(titleL, AllDataVB);
            DataVB.setAlignment(Pos.CENTER);
            districtPane.setTop(Menu1(stage));
            districtPane.setCenter(DataVB);
            districtPane.setStyle("-fx-background-color:#F0DFD0;");
            ImageView nextB = new ImageView("file:right-arrow.png"), prevB = new ImageView("file:left-arrow.png");
            HBox nextPrevHB = new HBox(80);
            nextPrevHB.setAlignment(Pos.CENTER);
            DataVB.getChildren().add(nextPrevHB);
            nextPrevHB.getChildren().setAll(prevB, nextB);
            // Set actions for navigation buttons
            nextB.setOnMouseClicked(e -> {
                if (!Firststack.isEmpty()) {
                    if (tempDistrict != null)
                        secStack.push(tempDistrict);
                    tempDistrict = (District) Firststack.pop();
                    resetDistrictData(tempDistrict);
                }
            });
            prevB.setOnMouseClicked(e -> {
                if (!secStack.isEmpty()) {
                    if (tempDistrict != null)
                        Firststack.push(tempDistrict);
                    tempDistrict = (District) secStack.pop();
                    resetDistrictData(tempDistrict);
                }
            });

            DataVB.setAlignment(Pos.CENTER);
        }

        // Set scene and actions for show martyrs button
        districtPane.setTop(Menu1(stage));
        districtPane.setCenter(DataVB);
        districtPane.setAlignment(DataVB, Pos.CENTER);
        districtPane.setStyle("-fx-background-color:#F0DFD0;");
        scene = new Scene(districtPane, 500, 500);
        showMartyrB.setOnAction(e -> {
            showMartyrsD(tempDistrict);
        });

        // Key event handling
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case RIGHT:
                    if (!Firststack.isEmpty()) {
                        if (tempDistrict != null)
                            secStack.push(tempDistrict);
                        tempDistrict = (District) Firststack.pop();
                        resetDistrictData(tempDistrict);
                    }
                    break;
                case LEFT:
                    if (!secStack.isEmpty()) {
                        if (tempDistrict != null)
                            Firststack.push(tempDistrict);
                        tempDistrict = (District) secStack.pop();
                        resetDistrictData(tempDistrict);
                    }
                    break;
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showMartyrsD(District temDistrict) {
        // Call method to display martyrs in the district
        TableViewMartyrsinDistrict(tempDistrict);
    }

    private void inSceneRead() {
        // Create a new stage for reading confirmation
        Stage stage = new Stage();
        VBox questionVB = new VBox(20);
        Label label = new Label("Do you want to replace all data?");
        label.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton yesRadioButton = new RadioButton("Yes");
        yesRadioButton.setToggleGroup(toggleGroup);
        yesRadioButton.setSelected(true);
        RadioButton noRadioButton = new RadioButton("No");
        noRadioButton.setToggleGroup(toggleGroup);
        Button ensureButton = new Button("READ");
        ensureButton.setStyle("-fx-background-color: #add0b3; -fx-font-weight: bold; -fx-font-family: Times-Roman; -fx-text-fill: white");
        ensureButton.setOnAction(e -> {
            try {
                if (yesRadioButton.isSelected()) {
                    // Clear existing data and read from file
                    Districts.clear();
                    readFromFile();
                } else {
                    // Read from file without replacing existing data
                    readFromFile();
                }
                stage.hide();
            } catch (FileNotFoundException l) {
                // Handle file not found exception
            }
        });

        questionVB.getChildren().addAll(label, yesRadioButton, noRadioButton, ensureButton);
        questionVB.setAlignment(Pos.CENTER);
        questionVB.setStyle("-fx-background-color: #F0DFD0;");

        Scene scene = new Scene(questionVB, 400, 200);
        stage.setScene(scene);

        stage.show();
    }

    private void deleteDistrictScreen(Stage stage) {
        // Initialize UI elements for deleting a district
        BorderPane districtPane = new BorderPane();
        HBox DeleteHB = new HBox(20);
        Label enterNameL = new Label("District to delete:");
        enterNameL.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");

        ComboBox<String> districtCB = getDistrictCb();
        districtCB.setStyle("-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
        districtCB.setPromptText("choose your district");
        Button deleteDB = new Button("DELETE");
        deleteDB.setOnAction(e -> {
            // Handle district deletion
            if (districtCB.getValue().isEmpty()) {
                ErrorMessage("Enter the name of the district then press Delete.");
            } else {
                District district = Districts.getDistrict(districtCB.getValue());
                if (district == null) {
                    ErrorMessage("There's no District with the entered name.");
                    return;
                } else {
                    if (ConfirmMessage("Are You sure You want to delete " + district.getDistrictName() + " District?")) {
                        Districts.delete(district);
                        deleteDistrictScreen(stage);
                    } else {
                        ErrorMessage("District " + district.getDistrictName() + " was not deleted.");
                    }
                }
            }
        });
        deleteDB.setStyle("-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
        DeleteHB.getChildren().addAll(enterNameL, districtCB, deleteDB);
        districtPane.setTop(Menu1(stage));
        VBox mainvb = new VBox();
        Label titleL = new Label("Delete District\n\n\n\n");
        titleL.setStyle("-fx-font-weight:bold;-fx-font-size: 18px; -fx-font-family: 'Times New Roman'; -fx-text-fill: black;");
        mainvb.getChildren().setAll(titleL, DeleteHB);
        mainvb.setAlignment(Pos.CENTER);
        districtPane.setCenter(mainvb);
        districtPane.setStyle("-fx-background-color:#F0DFD0;");
        DeleteHB.setAlignment(Pos.CENTER);
        Scene scene = new Scene(districtPane, 500, 500);
        stage.setScene(scene);
        stage.show();
    }


    private void printToFile(Stage stage) {
        // Initialize UI elements for printing to file
        BorderPane MainPane = new BorderPane();
        HBox searchHB = new HBox(20);
        Label enterNameLabel = new Label("Choose a District :");
        enterNameLabel.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
        ComboBox<String> districtsCB = getDistrictCb();
        districtsCB.setPromptText("choose your district");
        districtsCB.setStyle("-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
        Button searchButton = new Button("Print");
        searchButton.setStyle("-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
        searchHB.getChildren().setAll(enterNameLabel, districtsCB, searchButton);
        searchHB.setAlignment(Pos.CENTER);
        searchButton.setOnAction(e -> {
            // Handle printing to file
            if (districtsCB.getValue().isEmpty()) {
                ErrorMessage("Please enter the name of the district then press enter.");
            } else {
                District district = Districts.getDistrict(districtsCB.getValue());
                if (district == null) {
                    ErrorMessage("No Districts with this name in the system.");
                } else {
                    writeToFileDistrict(district);
                }
            }
        });

        // Set up the main pane
        MainPane.setTop(Menu1(stage));
        MainPane.setCenter(searchHB);
        MainPane.setAlignment(searchHB, Pos.CENTER);
        MainPane.setStyle("-fx-background-color:#F0DFD0;");

        // Set up the scene and stage
        Scene scene = new Scene(MainPane, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void writeToFileDistrict(District district) {
        // Create a new stage for file chooser
        Stage stage = new Stage();
        FileChooser chooseFile = new FileChooser();
        chooseFile.setTitle("Choose file to read your Data.");
        File file = chooseFile.showOpenDialog(stage);
        if (file == null) {
            ErrorMessage("You have not chosen a file.");
            return;
        }
        if (!file.canWrite()) {
            ErrorMessage("File can't be written on.");
            return;
        }
        try {
            // Write district data to the selected file
            PrintWriter write = new PrintWriter(file);
            String text = district.toString();
            write.print(text);
            write.close();
        } catch (FileNotFoundException e) {
            // Handle file not found exception
        }
    }

    ComboBox<String> DistrictsCb;

    private void updateDistrictScreen(Stage stage) {
        // Initialize UI elements for updating district
        BorderPane districtPane = new BorderPane();
        HBox updateHB = new HBox(20);
        DistrictsCb = getDistrictCb();
        DistrictsCb.setStyle("-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
        DistrictsCb.setPromptText("choose your district");
        TextField newname = new TextField();
        newname.setPromptText("Type new Name here");
        Button updateB = new Button("Update");
        updateB.setStyle("-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
        newname.setStyle("-fx-font-family:Times-Roman;-fx-background-color:#FFFFF0;");
        updateB.setOnAction(e -> {
            // Handle update action
            if (DistrictsCb == null || DistrictsCb.getValue() == null || DistrictsCb.getValue().isEmpty()
                    || newname.getText().isEmpty() || DistrictsCb.getValue().isBlank()) {
                ErrorMessage("You can't keep any textField Empty.");
            } else if (DistrictsCb.getValue().equalsIgnoreCase(newname.getText())) {
                ErrorMessage("You Entered The same name.");
                newname.setStyle("-fx-background-color:#B9D1BF;");
            } else {
                // Call updateDistrict method
                if (updateDistrict(DistrictsCb.getValue(), newname.getText())) {
                    newname.setStyle("-fx-background-color:#B9D1BF;");
                    updateDistrictScreen(stage);
                } else {
                    errorTF(newname);
                }
            }
        });

        // Add UI elements to the updateHB HBox
        updateHB.getChildren().addAll(DistrictsCb, newname, updateB);

        // Set up the main pane
        districtPane.setTop(Menu1(stage));
        VBox mainvb = new VBox();
        Label titleL = new Label("Update District\n\n\n\n");
        titleL.setStyle("-fx-font-weight:bold;-fx-font-size: 18px; -fx-font-family: 'Times New Roman'; -fx-text-fill: black;");
        mainvb.getChildren().setAll(titleL, updateHB);
        mainvb.setAlignment(Pos.CENTER);
        districtPane.setCenter(mainvb);
        districtPane.setStyle("-fx-background-color:#F0DFD0;");

        // Set alignment for the updateHB HBox
        updateHB.setAlignment(Pos.CENTER);

        // Set up the scene and stage
        Scene scene = new Scene(districtPane, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    public boolean updateDistrict(String oldName, String newName) {
        // Search for the district with the old name
        District oldDistrict = Districts.getDistrict(oldName);

        // Check if the new name contains only numbers
        if (containsOnlyNumbers(newName)) {
            ErrorMessage("The new name can't consist of only numbers.");
            return false;
        }

        // If the district with the old name doesn't exist
        if (oldDistrict == null) {
            ErrorMessage("No District was found with this name.");
            return false;
        }

        // Remove the district with the old name from the BST
        Districts.delete(oldDistrict);

        // Update the name of the old district
        oldDistrict.setDistrictName(newName);

        // Insert the updated district back into the BST based on its new name
        Districts.add(oldDistrict);

        return true;
    }


    public boolean addDistrict(District district) {// worked
        // Check if the district already exists
        District tempDistrict = Districts.getDistrict(district.getDistrictName());
        if (tempDistrict != null) {
            ErrorMessage("There is already a district with this name.");
            return false;
        }

        // Add the district to the BST
        Districts.add(district);
        return true;
    }

    private void insertDistrictPane(Stage stage) {
        // Initialize UI elements for adding a new district
        BorderPane districtPane = new BorderPane();
        HBox insertHB = new HBox(10);
        Label DistrictNameL = new Label("Please enter the name of the District:");
        DistrictNameL.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
        TextField DistrictNameTF = new TextField();
        DistrictNameTF.setPromptText("EXAMPLE: RAMALLAH");
        DistrictNameTF.setStyle("-fx-font-family:Times-Roman;-fx-background-color:#FFFFF0;");
        Button insertB = new Button("ADD District");
        insertB.setStyle("-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");

        // Handle button action
        insertB.setOnAction(e -> {
            // Check for empty text field
            if (DistrictNameTF.getText().isEmpty()) {
                errorTF(DistrictNameTF);
                error.setErrorMessage("Please type in the name then press the button.");
                error.start(new Stage());
            } else {
                // Check if the name contains only numbers
                if (containsOnlyNumbers(DistrictNameTF.getText())) {
                    ErrorMessage("District Name Can't be numbers only.");
                    errorTF(DistrictNameTF);
                } else {
                    // Add the new district
                    if (addDistrict(new District(DistrictNameTF.getText()))) {
                        DistrictNameTF.setStyle("-fx-background-color:#B9D1BF;");
                    } else {
                        errorTF(DistrictNameTF);
                    }
                }
            }
        });

        // Add UI elements to the insertHB HBox
        insertHB.getChildren().addAll(new Label("\n\n\n"), DistrictNameL, DistrictNameTF, insertB);

        // Set up the main pane
        VBox mainvb = new VBox();
        Label titleL = new Label("Add New District\n\n\n\n");
        titleL.setStyle("-fx-font-weight:bold;-fx-font-size: 18px; -fx-font-family: 'Times New Roman'; -fx-text-fill: black;");
        mainvb.getChildren().setAll(titleL, insertHB);
        mainvb.setAlignment(Pos.CENTER);
        districtPane.setCenter(mainvb);
        districtPane.setTop(Menu1(stage));
        districtPane.setStyle("-fx-background-color:#F0DFD0;");
        insertHB.setAlignment(Pos.CENTER);

        // Set up the scene and stage
        Scene districtScene = new Scene(districtPane, 500, 500);
        stage.setScene(districtScene);
    }

    private boolean containsOnlyNumbers(String input) {
        return input.matches("[0-9]+");// this means that the text applies this contidion one or more times.
    }

    private void errorTF(TextField districtNameTF) {
        districtNameTF.setStyle("-fx-background-color: coral;");
    }

    ComboBox<String> getDistrictCb() {
        Stack districtsStack = Districts.inOrder();
        ComboBox<String> districtsCB = new ComboBox<String>();
        while (!districtsStack.isEmpty()) {
            District tempDistrict = (District) districtsStack.pop();
            districtsCB.getItems().add(tempDistrict.getDistrictName());
        }
        return districtsCB;
    }

    private void getDistrictScene(Stage stage) {
        // Set up the border pane
        BorderPane bp = new BorderPane();
        bp.setTop(Menu1(stage));

        // Set up UI elements for selecting a district
        HBox DistrictDataHB = new HBox(20);
        Label DistrictL = new Label("District Name:");
        DistrictL.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
        ComboBox<String> DistrictsCB = getDistrictCb();
        DistrictsCB.setPromptText("choose your district");
        DistrictsCB.setStyle("-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
        Button enterB = new Button("ENTER");
        enterB.setStyle("-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
        DistrictDataHB.getChildren().addAll(DistrictL, DistrictsCB, enterB);
        DistrictDataHB.setAlignment(Pos.CENTER);

        // Handle button action
        enterB.setOnAction(e -> {
            if (DistrictsCB.getValue() == null || DistrictsCB.getValue().trim().isEmpty()) {
                ErrorMessage("Please enter District name then press enter.");
            } else {
                // Get the selected district
                District districtToReturn = Districts.getDistrict(DistrictsCB.getValue());
                if (districtToReturn == null) {
                    ErrorMessage("No Districts with this name in the system.");
                } else {
                    // Proceed to the location screen
                    LocationScreen(stage, districtToReturn);
                }
            }
        });

        // Set the style for the district data HBox
        DistrictDataHB.setStyle("-fx-background-color: #F0DFD0;");

        // Set up the border pane
        bp.setCenter(DistrictDataHB);

        // Set up the scene and stage
        Scene scene = new Scene(bp, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void LocationScreen(Stage stage, District district) {
        // Set up the border pane
        BorderPane Mainbp = new BorderPane();
        ImageView BackB = new ImageView("file:back.png");
        Mainbp.setTop(Menu2());
        Mainbp.setStyle("-fx-background-color: #F0DFD0;");
        BorderPane innerBp = new BorderPane();
        Mainbp.setBottom(BackB);
        Mainbp.setAlignment(BackB, Pos.BOTTOM_LEFT);

        // Handle back button action
        BackB.setOnMouseClicked(e -> {
            secondStage(stage);
        });

        // Set up actions for various buttons
        MartyrFunctions.setOnAction(e -> {
            innerBp.setBottom(null);
            getMartyrFunctions(district, innerBp);
        });
        updateL.setOnAction(e -> {
            innerBp.setBottom(null);
            updateLocationS(district, innerBp);
        });
        deletel.setOnAction(e -> {
            innerBp.setBottom(null);
            deleteLocationS(district, innerBp);
        });
        showL.setOnAction(e -> {
            innerBp.setBottom(null);
            navigateL(district, innerBp);
        });
        searchL.setOnAction(e -> {
            innerBp.setBottom(null);
            SearchForMartyrsInLoaction(district, innerBp);
        });
        addLocation.setOnAction(e -> {
            innerBp.setBottom(null);
            addLocation(district, innerBp);
        });

        // Set up the center of the border pane
        Mainbp.setCenter(innerBp);
        Mainbp.setAlignment(innerBp, Pos.CENTER);

        // Set up the scene and stage
        Scene scene = new Scene(Mainbp, 500, 500);
        stage.setScene(scene);
    }

    private void SearchForMartyrsInLoaction(District district, BorderPane innerBp) {
        // Set up the border pane
        BorderPane districtPane = new BorderPane();
        VBox DataVB = new VBox(10);
        Label StartL = new Label("Search for martyrs in All Location in " + district.getDistrictName());
        StartL.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
        StartL.setAlignment(Pos.CENTER);
        TextField MartyrNameTF = new TextField();
        MartyrNameTF.setPromptText("EXAMPLE: Heba jamal");
        Button searchB = new Button("Search");
        searchB.setStyle("-fx-background-color: #add0b3; -fx-font-weight: bold; -fx-font-family: Times-Roman; -fx-text-fill: white");
        HBox searchingHB = new HBox(5);
        searchingHB.setAlignment(Pos.CENTER);
        searchingHB.getChildren().addAll(MartyrNameTF, searchB);
        ObservableList<Martyr> AllMartyrs = InitialListMartyrsinLocationAll(district);
        TableView<Martyr> martyrsTable = AllmartyrsD;

        // Set up search button action
        searchB.setOnAction(e -> {
            if (MartyrNameTF.getText().isEmpty())
                ErrorMessage("Please enter the name you are searching for and then press search.");
            else {


                MartyrLinkedList martyrs = new MartyrLinkedList();
                Stack locations= district.getLocations().inOrder();
                while(!locations.isEmpty()) {
                    Location location = (Location) locations.pop();
                    Stack DateS = new Stack();
                    DateS = location.getDateBST().inOrder();
                    while (!DateS.isEmpty()) {
                        Date date = (Date) DateS.pop();
                        for (int i = 0; i < date.getMartyrs().getSize(); i++) {
                            Martyr tempMartyr = date.getMartyrs().getElement(i);
                            if (tempMartyr.getName().toLowerCase().contains(MartyrNameTF.getText().toLowerCase())) {
                                martyrs.addMartyr(tempMartyr);
                            }
                        }
                    }
                }
                if (martyrs.getSize() != 0) {

                    TableColumn nameL = new TableColumn("name");
                    nameL.setCellValueFactory(new PropertyValueFactory<Martyr, String>("name"));
                    TableColumn date = new TableColumn("Date");
                    date.setCellValueFactory(new PropertyValueFactory<Martyr, String>("Date"));
                    TableColumn age = new TableColumn("age");
                    age.setCellValueFactory(new PropertyValueFactory<Martyr, Integer>("age"));
                    TableColumn locationC = new TableColumn("location");
                    locationC.setCellValueFactory(new PropertyValueFactory<Martyr, String>("location"));
                    TableColumn districtC = new TableColumn("District");
                    districtC.setCellValueFactory(new PropertyValueFactory<Martyr, String>("District"));
                    TableColumn gender = new TableColumn("gender");
                    gender.setCellValueFactory(new PropertyValueFactory<Martyr, String>("gender"));
                    martyrsTable.getColumns().setAll(nameL, date, age, locationC, districtC, gender);
                    martyrsTable.setPrefSize(400, 400);
                    ObservableList<Martyr> listData = FXCollections.observableArrayList();
                    martyrsTable.setItems(listData);
                    for (int i = 0; i < martyrs.getSize(); i++) {
                        listData.add(martyrs.getElement(i));
                    }
                    DataVB.getChildren().setAll(StartL, searchingHB, martyrsTable);
                } else {
                    ErrorMessage("No martyrs found with this name.");
                }
            }
        });

        // Add UI elements to the data VBox
        DataVB.getChildren().addAll(StartL, searchingHB, martyrsTable);
        DataVB.setAlignment(Pos.CENTER);

        // Set up the district pane
        districtPane.setCenter(DataVB);
        districtPane.setAlignment(DataVB, Pos.CENTER);
        districtPane.setStyle("-fx-background-color:#F0DFD0;");

        // Set up the inner border pane
        innerBp.setCenter(districtPane);
    }


    private ObservableList<Martyr> InitialListMartyrsinLocationAll(District district) {
        ObservableList<Martyr> list = FXCollections.observableArrayList();

        TableColumn<Martyr, String> nameL = new TableColumn<>("name");
        nameL.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Martyr, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        TableColumn<Martyr, String> age = new TableColumn<>("age");
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        TableColumn<Martyr, String> location = new TableColumn<>("location");
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        TableColumn<Martyr, String> districtN = new TableColumn<>("District");

        districtN.setCellValueFactory(new PropertyValueFactory<>("District"));
        TableColumn<Martyr, String> gender = new TableColumn<>("gender");
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        AllmartyrsD.getColumns().setAll(nameL, date, age, location, districtN, gender);
        AllmartyrsD.setPrefSize(400, 400);

        Stack locationS = district.getLocations().inOrder();
        while (!locationS.isEmpty()) {
            Location templocation = (Location) locationS.pop();
            Stack dateS = templocation.getDateBST().inOrder();
            while (!dateS.isEmpty()) {
                Date dateTemp = (Date) dateS.pop();
                for (int i = 0; i < dateTemp.getMartyrs().getSize(); i++) {
                    list.add(dateTemp.getMartyrs().getElement(i));
                }
            }
        }

        AllmartyrsD.setItems(list);
        return list;
    }

    private void addLocation(District district, BorderPane innerBp) {
        // Create an HBox to hold the location input components and set alignment
        HBox LocationDataHB = new HBox(20);
        LocationDataHB.setAlignment(Pos.CENTER);

        // Create a label for the location name
        Label locationName = new Label("Location name:");
        locationName.setStyle("-fx-font-weight: bold; -fx-font-family: Times-Roman;");

        // Create a text field for entering the location name
        TextField lnameTF = new TextField();
        lnameTF.setPromptText("EXAMPLE: al-bireh");
        lnameTF.setStyle("-fx-background-color: white");

        // Create a button for adding the location
        Button addLB = new Button("ADD");
        addLB.setStyle("-fx-background-color: #add0b3; -fx-font-weight: bold; -fx-font-family: Times-Roman; -fx-text-fill: white");

        // Add components to the location input HBox
        LocationDataHB.getChildren().setAll(locationName, lnameTF, addLB);

        // Set the location input HBox as the center of the BorderPane
        innerBp.setCenter(LocationDataHB);

        // Set action for the add button
        addLB.setOnAction(e -> {
            // Check if the location name field is empty
            if (lnameTF.getText().isEmpty()) {
                ErrorMessage("Please enter the name of the location then press add.");
            } else {
                // Check if the location name contains only numbers
                if (containsOnlyNumbers(lnameTF.getText())) {
                    ErrorMessage("Location Name can't be Numbers only");
                    return;
                } else {
                    // Try to add the location to the district
                    if (!district.addLocation(new Location(lnameTF.getText()))) {
                        // Show error message if location already exists
                        ErrorMessage("This location already exists.");
                        lnameTF.setStyle("-fx-background-color: coral");
                    } else {
                        // If location added successfully, set text field background color to green
                        lnameTF.setStyle("-fx-background-color: #B9D1BF");
                    }
                }
            }
        });
    }

    Location TempLocation;
    Stack firstStackL = new Stack(), seconedStackL = new Stack();

    private void navigateL(District district, BorderPane innerBp) {
        // Initialize stacks for navigation
        firstStackL = district.getLocations().levelOrder();
        seconedStackL = new Stack();

        // Set up VBox to display location data and show martyrs button
        VBox DataLVB = new VBox(20);
        Button showMartyrsB = new Button("Show Martyrs");

        // Check if there are any locations in the district
        if (!district.getLocations().isEmpty()) {
            // Get the first location from the stack
            if (!firstStackL.isEmpty())
                TempLocation = (Location) firstStackL.pop();

            // Set up button style and action
            showMartyrsB.setStyle("-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
            showMartyrsB.setOnAction(e -> {
                if (TempLocation != null)
                    ShowMartyrsinLoc(TempLocation);
            });

            // Set up VBox alignment and add location data
            DataLVB.setAlignment(Pos.CENTER);
            resetLData(TempLocation);
            DataLVB.getChildren().setAll(lName, EarliestDate, LatestDate, MaxDate);

            // Set up navigation arrows
            ImageView nextB = new ImageView("file:right-arrow.png"), prevB = new ImageView("file:left-arrow.png");
            HBox nextPrevHB = new HBox(80);
            nextPrevHB.setAlignment(Pos.CENTER);
            nextPrevHB.getChildren().setAll(prevB, nextB);

            // Set up VBox to contain all UI elements
            VBox allVB = new VBox(5);
            allVB.getChildren().addAll(DataLVB, showMartyrsB, nextPrevHB);
            allVB.setAlignment(Pos.CENTER);

            // Set up actions for navigation arrows
            nextB.setOnMouseClicked(e -> {
                if (!firstStackL.isEmpty()) {
                    if (TempLocation != null)
                        seconedStackL.push(TempLocation);
                    TempLocation = (Location) firstStackL.pop();
                    resetLData(TempLocation);
                }
            });

            prevB.setOnMouseClicked(e -> {
                if (!seconedStackL.isEmpty()) {
                    if (TempLocation != null)
                        firstStackL.push(TempLocation); // Push the current location onto firstStackL
                    TempLocation = (Location) seconedStackL.pop(); // Pop from seconedStackL
                    resetLData(TempLocation); // Update UI with the new TempLocation data
                }
            });

            // Set center of inner border pane to contain all UI elements
            innerBp.setCenter(allVB);
        } else {
            // Display error message if there are no locations in the district
            ErrorMessage("There's no location in this District");
        }
    }


    private void ShowMartyrsinLoc(Location tempLocation2) {
        Stage newStage = new Stage();
        /*
         * private String name, Date, location, District; private int age; private char
         * gender;
         */
        TableView<Martyr> martyrs = new TableView<>();
        ObservableList<Martyr> list = FXCollections.observableArrayList();

        TableColumn<Martyr, String> nameL = new TableColumn<Martyr, String>("name");
        nameL.setCellValueFactory(new PropertyValueFactory<Martyr, String>("name"));
        TableColumn<Martyr, String> date = new TableColumn<Martyr, String>("Date");
        date.setCellValueFactory(new PropertyValueFactory<Martyr, String>("Date"));
        TableColumn<Martyr, Integer> age = new TableColumn("age");
        age.setCellValueFactory(new PropertyValueFactory<Martyr, Integer>("age"));
        TableColumn<Martyr, String> location = new TableColumn<Martyr, String>("location");
        location.setCellValueFactory(new PropertyValueFactory<Martyr, String>("location"));
        TableColumn<Martyr, String> districtN = new TableColumn<Martyr, String>("District");
        districtN.setCellValueFactory(new PropertyValueFactory<Martyr, String>("District"));
        TableColumn<Martyr, Character> gender = new TableColumn<Martyr, Character>("gender");
        gender.setCellValueFactory(new PropertyValueFactory<Martyr, Character>("gender"));
        martyrs.getColumns().addAll(nameL, date, age, location, districtN, gender);
        martyrs.setPrefSize(400, 400);

        Stack dateS = tempLocation2.getDateBST().inOrder();
        while (!dateS.isEmpty()) {
            Date dateTemp = (Date) dateS.pop();
            for (int i = 0; i < dateTemp.getMartyrs().getSize(); i++) {
                list.add(dateTemp.getMartyrs().getElement(i));

            }
        }

        martyrs.setItems(list);
        Label DL = new Label("Martyrs in " + tempLocation2.getLocationName());
        DL.setStyle(
                "-fx-font-weight:bold;-fx-font-size: 18px; -fx-font-family: 'Times New Roman'; -fx-text-fill: black;");
        VBox martyrsVB = new VBox(30);
        martyrsVB.setAlignment(Pos.CENTER);
        martyrsVB.setStyle("-fx-background-color:#F0DFD0;");
        martyrsVB.getChildren().addAll(DL, martyrs);
        Scene scene = new Scene(martyrsVB, 600, 500);
        newStage.setScene(scene);

        newStage.show();

    }

    Label lName = new Label(), EarliestDate = new Label(), LatestDate = new Label(), MaxDate = new Label();

    private void resetLData(Location location) {
        lName.setText("Location Name: " + location.getLocationName());
        EarliestDate.setText("Earilest Date is: " + location.getEarliestDate());
        LatestDate.setText("Latest Date is: " + location.getLatestDate());
        MaxDate.setText("The Date that has Max num of Martyrs is: " + location.getMaxDate());

        SetStyleLabel(lName);
        SetStyleLabel(EarliestDate);
        SetStyleLabel(LatestDate);
        SetStyleLabel(MaxDate);

    }

    private void SetStyleLabel(Label label) {
        label.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
    }

    //deletes location
    private void deleteLocationS(District district, BorderPane innerBp) {
        HBox DeleteHB = new HBox(20);
        DeleteHB.setAlignment(Pos.CENTER);//set center
        Label enterNameL = new Label("Location to delete:");
        enterNameL.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
        ComboBox<String> LocationName = district.getLocationsCB();
        LocationName.setPromptText("EXAMPLE: al-Bireh");//example prompt
        LocationName.setStyle(
                "-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
        Button deletelB = new Button("DELETE");
        deletelB.setOnAction(e -> {
            if (LocationName != null && LocationName.getValue().isEmpty()) {
                ErrorMessage("Choose the name of the location, then press Delete.");
            } else {

                Location Location = district.getLocations().getLocation(LocationName.getValue());
                if (Location == null) {
                    ErrorMessage("There's no location with the entered name.");
                } else {
                    if (ConfirmMessage("Are you sure about delteting " + Location.getLocationName() + " location?")) {
                        district.getLocations().delete(Location);
                        deleteLocationS(district, innerBp);
                    } else
                        ErrorMessage("Location " + Location.getLocationName() + " was not deleted.");
                }
            }
        });
        deletelB.setStyle(
                "-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
        DeleteHB.getChildren().addAll(enterNameL, LocationName, deletelB);

        VBox mainvb = new VBox();
        mainvb.setAlignment(Pos.CENTER);
        Label titleL = new Label("Delete Location\n\n\n\n");
        titleL.setStyle(
                "-fx-font-weight:bold;-fx-font-size: 18px; -fx-font-family: 'Times New Roman'; -fx-text-fill: black;");
        mainvb.getChildren().setAll(titleL, DeleteHB);
        mainvb.setAlignment(Pos.CENTER);
        innerBp.setCenter(mainvb);
    }

    private void updateLocationS(District district, BorderPane innerBp) {
        HBox updateHB = new HBox(20);
        updateHB.setAlignment(Pos.CENTER);
        ComboBox<String> oldName = district.getLocationsCB();
        TextField newname = new TextField();
        oldName.setPromptText("choose old name here");
        newname.setPromptText("Type new Name here");
        Button updateB = new Button("Update");
        updateB.setStyle(
                "-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
        oldName.setStyle(
                "-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");
        newname.setStyle("-fx-font-family:Times-Roman;-fx-background-color:#FFFFF0;");
        updateB.setOnAction(e -> {

            newname.setStyle("-fx-background-color:#FFFFF0;");
            if (oldName.getValue() == null || oldName.getValue().isEmpty() || newname.getText().isEmpty()) {
                errorTF(newname);
                ErrorMessage("You can't keep any textField Empty.");
            } else if (oldName.getValue().equalsIgnoreCase(newname.getText())) {

                ErrorMessage("You entered the same name");
                errorTF(newname);

            } else {
                if (updateLocation(oldName.getValue(), newname.getText(), district)) {

                    newname.setStyle("-fx-background-color:#B9D1BF;");
                    updateLocationS(district, innerBp);
                } else {

                    errorTF(newname);
                }
            }
        });

        updateHB.getChildren().addAll(oldName, newname, updateB);
        innerBp.setCenter(updateHB);

    }

    private boolean updateLocation(String oldName, String newName, District district) {
        if (containsOnlyNumbers(newName)) {
            ErrorMessage("Location name can't me only numbers.");
            return false;
        }
        Location oldLocation = district.getLocations().getLocation(oldName);
        if (oldLocation == null) {
            ErrorMessage("No location was found with this name.");
            return false;
        }

        district.getLocations().delete(oldLocation);

        oldLocation.setLocationName(newName);
        oldLocation.changeName(newName);

        district.getLocations().add(oldLocation);

        return true;
    }

    private void getMartyrFunctions(District district, BorderPane innerBp) {
        // Create an HBox to hold search components and set alignment
        HBox searchHB = new HBox(20);
        searchHB.setAlignment(Pos.CENTER);

        // Create a label for entering location name
        Label enterNameL = new Label("Location Name:");
        enterNameL.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");

        // Create a ComboBox for selecting locations
        ComboBox<String> locationsCB = district.getLocationsCB();
        locationsCB.setPromptText("choose your location");
        locationsCB.setStyle("-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");

        // Create a button for initiating the search
        Button searchB = new Button("SHOW Dates SCENE");
        searchB.setStyle("-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");

        // Add components to the search HBox
        searchHB.getChildren().setAll(enterNameL, locationsCB, searchB);

        // Set action for the search button
        searchB.setOnAction(e -> {
            // Check if a location is selected
            if (locationsCB.getValue() == null || locationsCB.getValue().isEmpty()) {
                // Display error message if no location is selected
                ErrorMessage("Enter Location name then press search");
            } else {
                // Get the selected location from the ComboBox
                Location location = district.getLocations().getLocation(locationsCB.getValue());
                // Check if the location is found
                if (location == null) {
                    // Display error message if location is not found
                    ErrorMessage("Location entered not Found");
                } else {
                    // Show functions for the selected location
                    ShowFunctions(district, location, innerBp);
                }
            }
        });

        // Set the search HBox as the center of the BorderPane
        innerBp.setCenter(searchHB);
    }

    private void ButtonSetStyle(Button button) {
        button.setStyle(
                "-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-text-fill:white");

    }

    private void ShowFunctions(District district, Location location, BorderPane innerBp) {
        VBox buttonsVB = new VBox(10);
        Label intro = new Label("MARTYR FUNCTIONS");
        SetStyleLabel(intro);
        Button AddM = new Button("Add Martyr"), EditM = new Button("Edit martyr"),
                DeleteM = new Button("Delete Martyr"), SearchM = new Button("Search For Martyr");
        Button NavigateDate = new Button("Navigate Through Dates");
        ButtonSetStyle(NavigateDate);
        ButtonSetStyle(SearchM);
        GridPane buttonsGP = new GridPane();

        ImageView AddI = new ImageView("file:addPerson.png"), EditI = new ImageView("file:editUser.png"),
                DeleteI = new ImageView("file:deleteUser.png"), searchI = new ImageView("file:searchUser.png"),
                NavigateI = new ImageView("file:time.png");
        buttonsGP.add(AddI, 0, 0);
        buttonsGP.add(AddM, 1, 0);
        buttonsGP.add(EditI, 0, 1);
        buttonsGP.add(EditM, 1, 1);
        buttonsGP.add(DeleteI, 0, 2);
        buttonsGP.add(DeleteM, 1, 2);
        buttonsGP.add(searchI, 0, 3);
        buttonsGP.add(SearchM, 1, 3);
        buttonsGP.add(NavigateI, 0, 4);
        buttonsGP.add(NavigateDate, 1, 4);
        NavigateDate.setOnAction(e -> {
            NavigateTDates(district, location, innerBp);
        });
        buttonsGP.setVgap(10);
        buttonsGP.setAlignment(Pos.CENTER);
        buttonsVB.setAlignment(Pos.CENTER);
        ButtonSetStyle(AddM);
        ButtonSetStyle(EditM);
        ButtonSetStyle(DeleteM);
        buttonsVB.getChildren().setAll(intro, buttonsGP);
        innerBp.setCenter(buttonsVB);
        AddM.setOnAction(e -> {
            AddMartyr(district, location, innerBp);
        });
        EditM.setOnAction(e -> {
            EditMartyr(district, location, innerBp);
        });
        DeleteM.setOnAction(e -> {
            DeleteMartyr(district, location, innerBp);
        });
        SearchM.setOnAction(e -> {
            seachForMartyrs(district, location, innerBp);
        });

    }

    Label DateName = new Label(), AverageAges = new Label(), youngest = new Label(), oldest = new Label();
    Stack DateStack1 = new Stack(), DateStack2 = new Stack();
    Date tempDate = null;

    private void NavigateTDates(District district, Location location, BorderPane innerBp) {
        ButtonSetStyle(backTof);
        backTof.setAlignment(Pos.TOP_LEFT);
        backTof.setOnAction(d -> {
            ShowFunctions(district, location, innerBp);
        });
        HBox BackHB = new HBox(10);
        BackHB.getChildren().setAll(userfunctionsI, backTof);
        DateStack2 = new Stack();
        DateStack1 = location.getDateBST().inOrder();
        VBox DataVB = new VBox();
        Button showMartyrsB = new Button("Show Martyrs");

        if (location.getDateBST().isEmpty()) {
            Label noMartyrs = new Label("No Martyrs in this Date");
            noMartyrs.setStyle("-fx-font-weight: bold;-fx-font-family:Times-Roman;");
            BorderPane bp = new BorderPane();
            bp.setCenter(noMartyrs);
            bp.setTop(BackHB);
            innerBp.setCenter(bp);
        } else {
            tempDate = (Date) DateStack1.pop();
            resetDateDate(tempDate);
            Label titleL = new Label("Date Statistics\n\n\n\n");
            titleL.setStyle(
                    "-fx-font-weight:bold;-fx-font-size: 18px; -fx-font-family: 'Times New Roman'; -fx-text-fill: black;");
            VBox dataVB = new VBox(15);
            showMartyrsB.setStyle(
                    "-fx-background-color: #add0b3; -fx-font-weight: bold; -fx-font-family: Times-Roman; -fx-text-fill: white");

            dataVB.getChildren().setAll(DateName, AverageAges, youngest, oldest, showMartyrsB);

            dataVB.setAlignment(Pos.CENTER);

            ImageView nextB = new ImageView("file:right-arrow.png"), prevB = new ImageView("file:left-arrow.png");
            HBox nextPrevHB = new HBox(80);
            nextPrevHB.setAlignment(Pos.CENTER);

            DataVB.getChildren().setAll(dataVB, nextPrevHB);
            BorderPane bp = new BorderPane();
            bp.setCenter(DataVB);
            bp.setTop(BackHB);
            innerBp.setCenter(bp);
            nextPrevHB.getChildren().setAll(prevB, nextB);
            nextB.setOnMouseClicked(e -> {
                if (!DateStack1.isEmpty()) {
                    if (tempDate != null)
                        DateStack2.push(tempDate);
                    tempDate = (Date) DateStack1.pop();
                    resetDateDate(tempDate);
                }
            });
            prevB.setOnMouseClicked(e -> {
                if (!DateStack2.isEmpty()) {
                    if (tempDate != null)
                        DateStack1.push(tempDate);
                    tempDate = (Date) DateStack2.pop();
                    resetDateDate(tempDate);
                }
            });
            showMartyrsB.setOnAction(e -> {
                showMartyrsDate(tempDate);
            });
            DataVB.setAlignment(Pos.CENTER);

        }


    }

    private void showMartyrsDate(Date tempDate) {
        Stage newStage = new Stage();
        Label titleLabel = new Label("Martyrs in Date:" + tempDate.getDate());
        SetStyleLabel(titleLabel);

        // Create TableView and ObservableList for martyrs
        TableView<Martyr> martyrs = new TableView<>();
        ObservableList<Martyr> list = FXCollections.observableArrayList();

        // Define table columns
        TableColumn<Martyr, String> nameL = new TableColumn<>("Name");
        nameL.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Martyr, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        TableColumn<Martyr, Integer> age = new TableColumn<>("Age");
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        TableColumn<Martyr, String> location = new TableColumn<>("Location");
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        TableColumn<Martyr, String> districtN = new TableColumn<>("District");
        districtN.setCellValueFactory(new PropertyValueFactory<>("District"));
        TableColumn<Martyr, Character> gender = new TableColumn<>("Gender");
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Add columns to TableView
        martyrs.getColumns().addAll(nameL, date, age, location, districtN, gender);
        martyrs.setPrefSize(400, 400);

        // Add martyrs to list
        for (int i = 0; i < tempDate.getMartyrs().getSize(); i++) {
            list.add(tempDate.getMartyrs().getElement(i));
        }

        // Sort the list of martyrs
        Collections.sort(list);

        // Set the sorted list to the TableView
        martyrs.setItems(list);

        // Create VBox to hold title label and TableView
        VBox tempVB = new VBox(10);
        tempVB.getChildren().addAll(titleLabel, martyrs);

        // Create scene and set it to the stage
        Scene scene = new Scene(tempVB);
        newStage.setScene(scene);
        newStage.show();
    }

    //DateName,AverageAges,youngest,oldest
    private void resetDateDate(Date tempDate) {
        DateName.setText("Date: " + tempDate.getDate());
        AverageAges.setText("Average age: " + tempDate.getAverageAge());
        youngest.setText("youngest Martyr age: " + tempDate.getYoungest());
        oldest.setText("oldest Martyr age: " + tempDate.getOldest());
        SetStyleLabel(AverageAges);
        SetStyleLabel(youngest);
        SetStyleLabel(DateName);
        SetStyleLabel(oldest);
    }

    private void seachForMartyrs(District district, Location location, BorderPane innerBp) {
        VBox mainvb = new VBox(10);
        HBox searchForMartyr = new HBox(10);
        HBox backToFunctionsB = new HBox(10);
        backToFunctionsB.getChildren().setAll(userfunctionsI, backTof);
        backTof.setOnAction(e -> {
            ShowFunctions(district, location, innerBp);
        });
        mainvb.getChildren().setAll(backToFunctionsB, searchForMartyr);
        Label Mname = new Label();
        TextField nametf = new TextField();
        nametf.setStyle("-fx-background-color: #add0b3;");
        nametf.setPromptText("EXAMPLE: heba Abu elrub");
        Button searchB = new Button("SEARCH");
        searchForMartyr.getChildren().setAll(Mname, nametf, searchB);
        ButtonSetStyle(searchB);
        searchB.setOnAction(e -> {
            if (nametf.getText().isEmpty()) {
                mainvb.getChildren().setAll(backToFunctionsB, searchForMartyr);
                ErrorMessage("Enter name then press SEARCH.");
            } else {

                MartyrLinkedList martyrs = new MartyrLinkedList();
                Stack DateS = new Stack();
                DateS = location.getDateBST().inOrder();
                while (!DateS.isEmpty()) {
                    Date date = (Date) DateS.pop();
                    for (int i = 0; i < date.getMartyrs().getSize(); i++) {
                        Martyr tempMartyr = date.getMartyrs().getElement(i);
                        if (tempMartyr.getName().contains(nametf.getText())) {
                            martyrs.addMartyr(tempMartyr);
                        }
                    }
                }

                if (martyrs.getSize() != 0) {
                    TableView<Martyr> martyrsTable = new TableView<>();
                    TableColumn nameL = new TableColumn("name");
                    nameL.setCellValueFactory(new PropertyValueFactory<Martyr, String>("name"));
                    TableColumn date = new TableColumn("Date");
                    date.setCellValueFactory(new PropertyValueFactory<Martyr, String>("Date"));
                    TableColumn age = new TableColumn("age");
                    age.setCellValueFactory(new PropertyValueFactory<Martyr, Integer>("age"));
                    TableColumn locationC = new TableColumn("location");
                    locationC.setCellValueFactory(new PropertyValueFactory<Martyr, String>("location"));
                    TableColumn districtC = new TableColumn("District");
                    districtC.setCellValueFactory(new PropertyValueFactory<Martyr, String>("District"));
                    TableColumn gender = new TableColumn("gender");
                    gender.setCellValueFactory(new PropertyValueFactory<Martyr, String>("gender"));
                    martyrsTable.getColumns().setAll(nameL, date, age, locationC, districtC, gender);
                    martyrsTable.setPrefSize(400, 400);
                    ObservableList<Martyr> listData = FXCollections.observableArrayList();
                    martyrsTable.setItems(listData);
                    for (int i = 0; i < martyrs.getSize(); i++) {
                        listData.add(martyrs.getElement(i));
                    }
                    mainvb.getChildren().setAll(backToFunctionsB, searchForMartyr, martyrsTable);
                } else {
                    ErrorMessage("No martyrs found with this name.");
                }

            }
        });
        innerBp.setCenter(mainvb);
    }

    private void DeleteMartyr(District district, Location location, BorderPane innerBp) {
        VBox mainvb = new VBox(10);
        HBox searchForMartyr = new HBox(10);
        HBox backToFunctionsB = new HBox(10);
        backToFunctionsB.getChildren().setAll(userfunctionsI, backTof);
        backTof.setOnAction(e -> {
            ShowFunctions(district, location, innerBp);
        });
        mainvb.getChildren().setAll(backToFunctionsB, searchForMartyr);
        Label Mname = new Label();
        TextField nametf = new TextField();
        nametf.setStyle("-fx-background-color: #add0b3;");
        nametf.setPromptText("EXAMPLE: heba Abu elrub");
        Button searchB = new Button("SEARCH");
        searchForMartyr.getChildren().setAll(Mname, nametf, searchB);
        ButtonSetStyle(searchB);
        searchB.setOnAction(e -> {
            if (nametf.getText().isEmpty()) {
                ErrorMessage("Enter name then press SEARCH.");
            } else {
                Stack Dates = new Stack();
                Dates = location.getDateBST().inOrder();
                MartyrLinkedList martyrsFound = new MartyrLinkedList();
                while (!Dates.isEmpty()) {
                    Date date = (Date) Dates.pop();
                    for (int i = 0; i < date.getMartyrs().getSize(); i++) {
                        Martyr tempMartyr = date.getMartyrs().getElement(i);
                        if (tempMartyr.getName().equalsIgnoreCase(nametf.getText())) {
                            martyrsFound.addMartyr(tempMartyr);
                        }
                    }
                }
                if (martyrsFound.isEmpty()) {
                    ErrorMessage("No Martyrs Found With this Name.");
                } else {
                    NewMartyr = null;

                    showMartyrs(martyrsFound);
                    if (NewMartyr == null)
                        ErrorMessage("You haven't chosen a martyr.");
                    else {
                        if (ConfirmMessage("Are you sure about deleteing " + NewMartyr.getName() + " ?")) {
                            location.deleteMartyr(NewMartyr);
                            ShowFunctions(district, location, innerBp);
                        } else {
                            ErrorMessage("Martyr " + NewMartyr.getName() + " was not deleted.");
                        }
                    }
                }
            }
        });
        innerBp.setCenter(mainvb);
    }

    Martyr NewMartyr = null, oldMartyr = null;

    private void EditMartyr(District district, Location location, BorderPane innerBp) {
        VBox mainVB = new VBox(10);
        HBox backToFunctionsB = new HBox(10);
        backToFunctionsB.getChildren().setAll(userfunctionsI, backTof);
        backTof.setOnAction(e -> {
            ShowFunctions(district, location, innerBp);
        });

        HBox searchForMartyr = new HBox(10);
        mainVB.getChildren().setAll(backToFunctionsB, searchForMartyr);
        Label Mname = new Label();
        TextField nametf = new TextField();
        nametf.setStyle("-fx-background-color: #add0b3;");
        nametf.setPromptText("EXAMPLE: heba Abu elrub");
        Button searchB = new Button("SEARCH");
        searchForMartyr.getChildren().setAll(Mname, nametf, searchB);
        ButtonSetStyle(searchB);

        searchB.setOnAction(e -> {
            if (nametf.getText().isEmpty()) {
                mainVB.getChildren().setAll(backToFunctionsB, searchForMartyr);
                ErrorMessage("Type in the name then press on Search");
            } else {
                MartyrLinkedList martyrs = new MartyrLinkedList();
                Stack DatesS = location.getDateBST().inOrder();
                while (!DatesS.isEmpty()) {
                    Date date = (Date) DatesS.pop();
                    for (int i = 0; i < date.getMartyrs().getSize(); i++) {
                        Martyr checkMartyr = date.getMartyrs().getElement(i);
                        if (checkMartyr.getName().equalsIgnoreCase(nametf.getText())) {
                            martyrs.Add(checkMartyr);
                        }
                    }
                }
                if (martyrs.isEmpty()) {
                    ErrorMessage("Martyr was Not Found in this location.");
                } else {

                    if (martyrs.getSize() > 1) {
                        showMartyrs(martyrs);
                        if (NewMartyr == null) {
                            ErrorMessage("You haven't chosen a Martyr ");
                        }

                    } else {
                        NewMartyr = martyrs.getFirst();
                        oldMartyr = martyrs.getFirst();
                    }
                    VBox vb = new VBox(10);
                    Button ChangeNameB = new Button("CHANGE NAME"), CAgeB = new Button("CHANGE AGE"),
                            CGender = new Button("CHANGE GENDER"), CDate = new Button("CHANGE DATE OF MARTYRDOM"),
                            CDistrict = new Button("CHANGE DISTRICT"), CLocation = new Button("CHANGE LOCATION");

                    ButtonSetStyle(ChangeNameB);
                    ButtonSetStyle(CAgeB);
                    ButtonSetStyle(CGender);
                    ButtonSetStyle(CDate);
                    ButtonSetStyle(CDistrict);
                    ButtonSetStyle(CLocation);
                    CDistrict.setOnAction(k -> {
                        ChangeDistrict();
                    });
                    CLocation.setOnAction(k -> {
                        ChangeLocation();
                    });
                    vb.getChildren().setAll(ChangeNameB, CAgeB, CGender, CDate, CDistrict, CLocation);
                    ChangeNameB.setOnAction(f -> {
                        changeName();

                    });
                    CAgeB.setOnAction(k -> {
                        ChangeAge();
                    });
                    CGender.setOnAction(k -> {
                        if (NewMartyr.getGender() == 'F') {
                            NewMartyr.setGender('M');
                        } else
                            NewMartyr.setGender('F');
                        CGender.setStyle("-fx-backgrounf-color: green");
                    });
                    CDate.setOnAction(k -> {
                        ChangeDate();
                    });
                    vb.setAlignment(Pos.CENTER);
                    mainVB.getChildren().setAll(backToFunctionsB, searchForMartyr, vb);
                    Button DoneEditing = new Button("DONE EDITING");
                    vb.getChildren().add(DoneEditing);
                    ButtonSetStyle(DoneEditing);
                    DoneEditing.setOnAction(l -> {

                        district.DeleteMartyr(oldMartyr, location);
                        ShowFunctions(district, location, innerBp);
                        District wantedDistrict = getWantedDistrict(NewMartyr.getDistrict());
                        wantedDistrict.addMartyr(NewMartyr, NewMartyr.getLocation());
                    });

                }
            }

        });
        innerBp.setCenter(mainVB);

    }

    private void ChangeLocation() {
        Stage stage = new Stage();
        HBox AgeHb = new HBox(10);
        Label AgeL = new Label("Martyr's Location:");
        SetStyleLabel(AgeL);
        District district = getWantedDistrict(NewMartyr.getDistrict());
        ComboBox<String> LocationCB = district.getLocationsCB();
        LocationCB.setStyle("-fx-background-color:#add0b3;");
        LocationCB.setPromptText("choose a location");
        Button editB = new Button("EDIT");
        ButtonSetStyle(editB);
        editB.setOnAction(e -> {
            if (!containsOnlyNumbers(LocationCB.getValue())) {
                NewMartyr.setDistrict(LocationCB.getValue());
                stage.hide();
            } else
                ErrorMessage("Choose a District then press edit.");

        });
        AgeHb.setStyle("-fx-background-color:#F0DFD0;");
        AgeHb.getChildren().setAll(AgeL, LocationCB, editB);
        Scene scene = new Scene(AgeHb, 300, 100);
        stage.setScene(scene);
        stage.show();
    }

    private District getWantedDistrict(String district) {
        Stack Disticts = Districts.inOrder();
        District wantedDistrict = null;
        while (!Districts.isEmpty()) {
            District tempD = (District) Disticts.pop();
            if (tempD != null)
                if (tempD.getDistrictName().equalsIgnoreCase(NewMartyr.getDistrict())) {
                    wantedDistrict = tempD;
                    return wantedDistrict;
                }
        }
        return null;
    }

    private void ChangeDistrict() {
        Stage stage = new Stage();
        HBox AgeHb = new HBox(10);
        Label AgeL = new Label("Martyr's District:");
        SetStyleLabel(AgeL);
        ComboBox<String> DistrictCB = getDistrictCb();
        DistrictCB.setStyle("-fx-background-color:#add0b3;");
        DistrictCB.setPromptText("choose a district");
        Button editB = new Button("EDIT");
        ButtonSetStyle(editB);
        editB.setOnAction(e -> {
            if (!containsOnlyNumbers(DistrictCB.getValue())) {
                NewMartyr.setDistrict(DistrictCB.getValue());
                stage.hide();
            } else
                ErrorMessage("Choose a District then press edit.");

        });
        AgeHb.setStyle("-fx-background-color:#F0DFD0;");
        AgeHb.getChildren().setAll(AgeL, DistrictCB, editB);
        Scene scene = new Scene(AgeHb, 300, 100);
        stage.setScene(scene);
        stage.show();
    }

    private void ChangeAge() {
        Stage stage = new Stage();
        HBox AgeHb = new HBox(10);
        Label AgeL = new Label("Martyr's Age:");
        SetStyleLabel(AgeL);
        ComboBox<String> agecb = new ComboBox<String>();
        agecb.setStyle("-fx-background-color:#add0b3;");
        agecb.getItems().setAll("Unknown");
        for (int i = 0; i < 101; i++) {
            agecb.getItems().add(i + "");
        }
        agecb.setPromptText("choose Age");
        agecb.setValue("1");
        Button editB = new Button("EDIT");
        ButtonSetStyle(editB);
        editB.setOnAction(e -> {
            try {
                NewMartyr.setAge(Integer.parseInt(agecb.getValue()));
            } catch (Exception k) {
                NewMartyr.setAge(-1);
            }
            stage.hide();
        });
        AgeHb.setStyle("-fx-background-color:#F0DFD0;");
        AgeHb.getChildren().setAll(AgeL, agecb, editB);
        Scene scene = new Scene(AgeHb, 300, 100);
        stage.setScene(scene);
        stage.show();
    }

    private void ChangeDate() {
        Stage stage = new Stage();
        HBox DateHb = new HBox(10);
        Label DateL = new Label("Martyrdom Date::");
        SetStyleLabel(DateL);
        DatePicker dateP = new DatePicker();
        dateP.setPromptText("choose Date");
        Button editB = new Button("EDIT");
        ButtonSetStyle(editB);
        editB.setOnAction(e -> {
            LocalDate newdate = dateP.getValue();
            if (newdate == null) {
                ErrorMessage("Choose Date then press on EDIT");
            } else {
                int month = newdate.getMonthValue();
                int day = newdate.getDayOfMonth();
                int year = newdate.getYear();
                String newDate = month + "/" + day + "/" + year;
                NewMartyr.setDate(newDate);
                stage.hide();
            }
        });

        DateHb.getChildren().setAll(DateL, dateP, editB);
        Scene scene = new Scene(DateHb, 300, 100);
        stage.setScene(scene);
        stage.show();

    }

    private void changeName() {
        Stage stage = new Stage();
        HBox nameHb = new HBox(10);
        Label nameL = new Label("Martyr's Name:");
        SetStyleLabel(nameL);
        TextField nametf = new TextField();
        nametf.setPromptText("EXAMPLE: heba abuelrub");
        Button editB = new Button("EDIT");
        ButtonSetStyle(editB);
        editB.setOnAction(e -> {
            if (nametf.getText().isEmpty()) {
                ErrorMessage("Enter the new name then press on EDIT");

            } else {
                NewMartyr.setName(nametf.getText());
                stage.hide();
            }
        });
        nameHb.setStyle("-fx-background-color:#F0DFD0;");
        nameHb.getChildren().setAll(nameL, nametf, editB);
        Scene scene = new Scene(nameHb, 300, 100);
        stage.setScene(scene);
        stage.show();

    }

    private void showMartyrs(MartyrLinkedList martyrsLi) {
        Stage newStage = new Stage();

        TableView<Martyr> martyrs = new TableView<>();
        ObservableList<Martyr> list = FXCollections.observableArrayList();

        TableColumn<Martyr, String> nameL = new TableColumn<Martyr, String>("name");
        nameL.setCellValueFactory(new PropertyValueFactory<Martyr, String>("name"));
        TableColumn<Martyr, String> date = new TableColumn<Martyr, String>("Date");
        date.setCellValueFactory(new PropertyValueFactory<Martyr, String>("Date"));
        TableColumn<Martyr, Integer> age = new TableColumn("age");
        age.setCellValueFactory(new PropertyValueFactory<Martyr, Integer>("age"));
        TableColumn<Martyr, String> location = new TableColumn<Martyr, String>("location");
        location.setCellValueFactory(new PropertyValueFactory<Martyr, String>("location"));
        TableColumn<Martyr, String> districtN = new TableColumn<Martyr, String>("District");
        districtN.setCellValueFactory(new PropertyValueFactory<Martyr, String>("District"));
        TableColumn<Martyr, Character> gender = new TableColumn<Martyr, Character>("gender");
        gender.setCellValueFactory(new PropertyValueFactory<Martyr, Character>("gender"));
        martyrs.getColumns().addAll(nameL, date, age, location, districtN, gender);
        martyrs.setPrefSize(400, 400);

        for (int i = 0; i < martyrsLi.getSize(); i++) {
            list.add(martyrsLi.getElement(i));

        }

        martyrs.setItems(list);

        Label DL = new Label("please choose a martyr");
        DL.setStyle(
                "-fx-font-weight:bold;-fx-font-size: 18px; -fx-font-family: 'Times New Roman'; -fx-text-fill: black;");
        VBox tempVB = new VBox(10);
        martyrs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                NewMartyr = newValue;
                oldMartyr = newValue;

                newStage.hide();
            }
        });
        tempVB.getChildren().addAll(DL, martyrs);

        Scene scene = new Scene(tempVB);
        newStage.setScene(scene);
        newStage.showAndWait();
    }

    char gender = 'F';
    int age = 0;

    ImageView userfunctionsI = new ImageView("file:account-settings.png");
    Button backTof = new Button("Back to user Functions");

    private void AddMartyr(District district, Location location, BorderPane innerBp) {
        ButtonSetStyle(backTof);
        GridPane MartyrInfoGp = new GridPane();
        MartyrInfoGp.setVgap(7);
        MartyrInfoGp.setHgap(7);
        Label titleL = new Label("ADD NEW MARTYR"), Mname = new Label("Martyr's Name:"),
                MDate = new Label("Date Of Martyrdom:"), MAge = new Label("Martyr's Age:"),
                Mgender = new Label("Martyr's Gender");
        SetStyleLabel(titleL);
        SetStyleLabel(Mname);
        SetStyleLabel(MDate);
        SetStyleLabel(MAge);
        SetStyleLabel(Mgender);
        TextField nameTf = new TextField();
        DatePicker DateM = new DatePicker();
        CheckBox femaleCB = new CheckBox("Female"), MaleCB = new CheckBox("Male");
        femaleCB.setSelected(true);
        femaleCB.setOnAction(e -> {
            if (femaleCB.isSelected()) {
                MaleCB.setSelected(false);
                gender = 'F';
            }
        });
        MaleCB.setOnAction(e -> {

            if (MaleCB.isSelected()) {
                gender = 'M';
                femaleCB.setSelected(false);

            }
        });

        AgeCB.setOnAction(e -> {
            try {
                age = Integer.parseInt(AgeCB.getValue());
            } catch (Exception e1) {
                age = -1;
            }
        });
        HBox BackHB = new HBox(10);
        BackHB.getChildren().setAll(userfunctionsI, backTof);
        backTof.setOnAction(d -> {
            ShowFunctions(district, location, innerBp);
        });
        MartyrInfoGp.add(Mname, 0, 0);
        MartyrInfoGp.add(nameTf, 1, 0);
        MartyrInfoGp.add(MDate, 0, 1);
        MartyrInfoGp.add(DateM, 1, 1);
        MartyrInfoGp.add(MAge, 0, 2);
        MartyrInfoGp.add(AgeCB, 1, 2);
        MartyrInfoGp.add(Mgender, 0, 3);
        HBox gendersHB = new HBox(3);
        gendersHB.getChildren().setAll(femaleCB, MaleCB);
        MartyrInfoGp.add(gendersHB, 1, 3);
        MartyrInfoGp.setAlignment(Pos.CENTER);
        VBox MainVb = new VBox(10);
        MainVb.setAlignment(Pos.CENTER);
        Button AddMartyrB = new Button("ADD");
        ButtonSetStyle(AddMartyrB);
        AgeCB.setPromptText("Choose Age");
        AgeCB.getItems().setAll("Unknown");
        for (int i = 1; i < 101; i++) {
            AgeCB.getItems().add(i + "");
        }

        AddMartyrB.setOnAction(e -> {
            if (nameTf.getText().isEmpty() || DateM.getValue() == null || AgeCB.getValue().isEmpty()) {
                ErrorMessage("Please Fill empty Blanks with the new MartyrInfo then press add.");
            } else {
                if (containsOnlyNumbers(nameTf.getText())) {
                    ErrorMessage("Martyr Name Can't contain only numbers.");
                } else {
                    LocalDate selectedDate = DateM.getValue();
                    int month = selectedDate.getMonthValue();
                    int day = selectedDate.getDayOfMonth();
                    int year = selectedDate.getYear();
                    String DateS = year + "/" + month + "/" + day;
                    Martyr newMartyr = new Martyr(nameTf.getText(), DateS, age, location.getLocationName(),
                            district.getDistrictName(), gender);
                    if (district.addMartyr(newMartyr, location.getLocationName())) {
                        nameTf.setText("");
                        DateM.setValue(null);
                        AgeCB.setValue(null);
                        femaleCB.setSelected(true);
                        MaleCB.setSelected(false);
                    } else
                        ErrorMessage("This Martyr already exists");

                }
            }
        });
        AgeCB.setStyle("-fx-background-color:#add0b3");
        MainVb.getChildren().setAll(BackHB, titleL, MartyrInfoGp, AddMartyrB);
        innerBp.setCenter(MainVb);
    }

    MenuBar Menu2() {
        MenuBar MB = new MenuBar();
        MB.setStyle("-fx-background-color:#B9D1BF;");

        LocationM.setStyle(
                "-fx-background-color: #add0b3;-fx-font-weight: bold;-fx-font-family:Times-Roman;-fx-border-color:#F0DFD0;");
        MB.getMenus().setAll(LocationM);
        return MB;
    }
}
