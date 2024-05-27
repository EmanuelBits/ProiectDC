import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import javafx.util.Duration;

public class App extends Application {
    private static final String basePath = "";

    private Pane cpuPage;
    private Pane ssdPage;
    private Pane resultsPage;
    private StackPane root;

    private Stack<Pane> pageHistory;

    private VBox buttonBox;
    private VBox buttonBoxCPU;
    private VBox buttonBoxSSD;

    private Button buttonCPU;
    private Button buttonSSD;
    private Button buttonResults;

    private Button buttonCPUisPrime;
    private Button buttonCPUfindPrime;
    private Button buttonCPUfindIndex;

    private Button buttonSSDrun;

    private Button backButtonCPU;
    private Button backButtonSSD;
    private Button backButtonResults;

    private double buttonRootWidth = 400;
    private double buttonRootHeight = 140;

    private double buttonCPUWidth = 600;
    private double buttonCPUHeight = 200;

    private double buttonSSDWidth = 700;
    private double buttonSSDHeight = 400;

    private TextField inputCPUField;
    private TextField inputSSDField;
    private Label resultCPULabel;
    private Label resultSSDLabel;

    private Label resultLabel;

    @Override
    public void start(Stage primaryStage) {
        // Load the image file
        Image image = new Image("BackgroundRoot.jpg");
        
        // Load the image file for each pages : CPU SSD Results
        Image imageCPU = new Image("BackgroundCPU.jpg");
        Image imageSSD = new Image("BackgroundSSD.jpg");
        Image imageResults = new Image("BackgroundResults.jpg");

        // Create the background image
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false) // Cover the entire scene
        );

        // Create the background images for each page
        BackgroundImage backgroundImageCPU = new BackgroundImage(
            imageCPU,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(1.0, 1.0, true, true, false, false)
        );

        BackgroundImage backgroundImageSSD = new BackgroundImage(
                imageSSD,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );

        BackgroundImage backgroundImageResults = new BackgroundImage(
                imageResults,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );

        // Create a background object
        Background background = new Background(backgroundImage);

        // Create backgrounds for each page : CPU SSD Results
        Background backgroundCPU = new Background(backgroundImageCPU);
        Background backgroundSSD = new Background(backgroundImageSSD);
        Background backgroundResults = new Background(backgroundImageResults);

        // Initialize the page history stack
        pageHistory = new Stack<>();

        // Create pages
        createPages();

        // Set backgrounds for each page
        cpuPage.setBackground(backgroundCPU);
        ssdPage.setBackground(backgroundSSD);
        resultsPage.setBackground(backgroundResults);


        // Create a VBox to hold the buttons
        buttonBox = new VBox(15); // Set spacing between buttons
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT); // Center the buttons
        buttonBox.setPadding(new Insets(50, 200, 64, 100)); // Add padding around the VBox

        // Create the buttons
        buttonCPU = new Button("CPU");
        buttonSSD = new Button("SSD");
        buttonResults = new Button("Results");

        // Set actions for buttons
        buttonCPU.setOnAction(e -> showPage(cpuPage));
        buttonSSD.setOnAction(e -> showPage(ssdPage));
        buttonResults.setOnAction(e -> showPage(resultsPage));

        // Add buttons to the VBox
        buttonBox.getChildren().addAll(buttonCPU, buttonSSD, buttonResults);


        // Create a VBox for CPU to hold the buttons
        buttonBoxCPU = new VBox(32); // Set spacing between buttons
        buttonBoxCPU.setAlignment(Pos.CENTER); // Center the buttons
        buttonBoxCPU.setPadding(new Insets(50, 140, 30, 100)); // Add padding around the VBox

        // Create the buttons
        buttonCPUisPrime = new Button("Is Prime?");
        buttonCPUfindPrime = new Button("Get Prime");
        buttonCPUfindIndex = new Button("Find Prime Index");
        backButtonCPU = new Button("Go Back");

        backButtonCPU.setOnAction(e -> showPage(root));

        // Add buttons to the VBox
        buttonBoxCPU.getChildren().addAll(buttonCPUisPrime, buttonCPUfindPrime, buttonCPUfindIndex, backButtonCPU);


        // Create a VBox to hold the buttons
        buttonBoxSSD = new VBox(15); // Set spacing between buttons
        buttonBoxSSD.setAlignment(Pos.CENTER); // Center the buttons
        buttonBoxSSD.setPadding(new Insets(50, 140, 30, 100)); // Add padding around the VBox

        // Create the buttons
        buttonSSDrun = new Button("Run");
        backButtonSSD = new Button("Go Back");

        backButtonSSD.setOnAction(e -> showPage(root));

        // Add buttons to the VBox
        buttonBoxSSD.getChildren().addAll(buttonSSDrun, backButtonSSD);

        backButtonResults = new Button("Go Back");
        backButtonResults.setOnAction(e -> showPage(root));


        // Create a root node with a StackPane
        root = new StackPane();
        root.setBackground(background); // Set the background to the root node
        root.getChildren().addAll(buttonBox, cpuPage, ssdPage, resultsPage); // Add the pages and buttons to the root node

        // Set the root node size and alignment
        root.setPrefSize(1600, 1300);
        StackPane.setAlignment(buttonBox, Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(cpuPage, Pos.CENTER);
        StackPane.setAlignment(ssdPage, Pos.CENTER);
        StackPane.setAlignment(resultsPage, Pos.CENTER);

        // Set fixed width and height for all buttons
        buttonCPU.setPrefSize(buttonRootWidth, buttonRootHeight);
        buttonSSD.setPrefSize(buttonRootWidth, buttonRootHeight);
        buttonResults.setPrefSize(buttonRootWidth, buttonRootHeight);

        buttonCPUisPrime.setPrefSize(buttonCPUWidth, buttonCPUHeight);
        buttonCPUfindPrime.setPrefSize(buttonCPUWidth, buttonCPUHeight);
        buttonCPUfindIndex.setPrefSize(buttonCPUWidth, buttonCPUHeight);

        buttonSSDrun.setPrefSize(buttonSSDWidth, buttonSSDHeight);


        // Initially hide pages
        cpuPage.setVisible(false);
        ssdPage.setVisible(false);
        resultsPage.setVisible(false);

        // Apply CSS styles to the buttons
        buttonCPU.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #5DB6ED, #FFB3C4); -fx-font-family: Elephant; -fx-font-size: 64px; -fx-font-weight: bold;");
        buttonSSD.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #5DB6ED, #FFB3C4); -fx-font-family: Elephant; -fx-font-size: 64px; -fx-font-weight: bold;");
        buttonResults.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #5DB6ED, #FFB3C4); -fx-font-family: Elephant; -fx-font-size: 64px; -fx-font-weight: bold;");

        buttonCPUisPrime.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1F6E74, #499591, #BFB389); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-border-width: 4px; -fx-border-color: #25312D; -fx-border-radius: 10px; -fx-padding: 10px;");
        buttonCPUfindPrime.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1F6E74, #499591, #BFB389); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-border-width: 4px; -fx-border-color: #25312D; -fx-border-radius: 10px; -fx-padding: 10px;");
        buttonCPUfindIndex.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1F6E74, #499591, #BFB389); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-border-width: 4px; -fx-border-color: #25312D; -fx-border-radius: 10px; -fx-padding: 10px;");
        backButtonCPU.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #493F0B, #CDA34F, #F9E79F, #D35400); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold;");

        buttonSSDrun.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #17435D, #287D99 , #A76D33); -fx-font-family: Elephant; -fx-font-size: 128px; -fx-font-weight: bold;");
        backButtonSSD.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #493F0B, #CDA34F, #F9E79F, #D35400); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold;");

        backButtonResults.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #6E90F7, #CD9FF9, #FCCFFC, #9EFA9E); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold;");

        // Add hover effects
        buttonCPU.setOnMouseEntered(e ->buttonCPU.setStyle("-fx-text-fill: white; -fx-font-family: Elephant; -fx-font-size: 64px; -fx-font-weight: bold; -fx-background-image: url('ButtonCPU.jpg'); -fx-background-size: 70% auto; -fx-background-position: center;"));
        buttonCPU.setOnMouseExited(e -> buttonCPU.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #5DB6ED, #FFB3C4); -fx-font-family: Elephant; -fx-font-size: 64px; -fx-font-weight: bold;"));

        buttonSSD.setOnMouseEntered(e -> buttonSSD.setStyle("-fx-text-fill: transparent; -fx-background-image: url('ButtonSSD.jpg'); -fx-background-size: 70% auto; -fx-background-position: center;"));
        buttonSSD.setOnMouseExited(e -> buttonSSD.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #5DB6ED, #FFB3C4); -fx-font-family: Elephant; -fx-font-size: 64px; -fx-font-weight: bold;"));

        buttonResults.setOnMouseEntered(e -> buttonResults.setStyle("-fx-text-fill: black; -fx-font-family: Elephant; -fx-font-size: 64px; -fx-font-weight: bold; -fx-background-image: url('ButtonResults.jpg'); -fx-background-size: 100% auto; -fx-background-position: center;"));
        buttonResults.setOnMouseExited(e -> buttonResults.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #5DB6ED, #FFB3C4); -fx-font-family: Elephant; -fx-font-size: 64px; -fx-font-weight: bold;"));


        buttonCPUisPrime.setOnMouseEntered(e -> buttonCPUisPrime.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1F6E74, #499591, #BFB389); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);"));
        buttonCPUisPrime.setOnMouseExited(e -> buttonCPUisPrime.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1F6E74, #499591, #BFB389); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-border-width: 5px; -fx-border-color: #25312D; -fx-border-radius: 10px; -fx-padding: 10px;"));

        buttonCPUfindPrime.setOnMouseEntered(e -> buttonCPUfindPrime.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1F6E74, #499591, #BFB389); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);"));
        buttonCPUfindPrime.setOnMouseExited(e -> buttonCPUfindPrime.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1F6E74, #499591, #BFB389); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-border-width: 5px; -fx-border-color: #25312D; -fx-border-radius: 10px; -fx-padding: 10px;"));

        buttonCPUfindIndex.setOnMouseEntered(e -> buttonCPUfindIndex.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1F6E74, #499591, #BFB389); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);"));
        buttonCPUfindIndex.setOnMouseExited(e -> buttonCPUfindIndex.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #1F6E74, #499591, #BFB389); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-border-width: 5px; -fx-border-color: #25312D; -fx-border-radius: 10px; -fx-padding: 10px;"));
        

        buttonSSDrun.setOnMouseEntered(e -> buttonSSDrun.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #17435D, #287D99 , #A76D33); -fx-font-family: Elephant; -fx-font-size: 128px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);"));
        buttonSSDrun.setOnMouseExited(e -> buttonSSDrun.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #17435D, #287D99 , #A76D33); -fx-font-family: Elephant; -fx-font-size: 128px; -fx-font-weight: bold;"));
        

        backButtonCPU.setOnMouseEntered(e -> backButtonCPU.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #493F0B, #CDA34F, #F9E79F, #D35400); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);"));
        backButtonCPU.setOnMouseExited(e -> backButtonCPU.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #493F0B, #CDA34F, #F9E79F, #D35400); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold;"));

        backButtonSSD.setOnMouseEntered(e -> backButtonSSD.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #493F0B, #CDA34F, #F9E79F, #D35400); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);"));
        backButtonSSD.setOnMouseExited(e -> backButtonSSD.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #493F0B, #CDA34F, #F9E79F, #D35400); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold;"));

        backButtonResults.setOnMouseEntered(e -> backButtonResults.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #6E90F7, #CD9FF9, #FCCFFC, #9EFA9E); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);"));
        backButtonResults.setOnMouseExited(e -> backButtonResults.setStyle("-fx-text-fill: black; -fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #6E90F7, #CD9FF9, #FCCFFC, #9EFA9E); -fx-font-family: Elephant; -fx-font-size: 55px; -fx-font-weight: bold;"));
        
        // Set up the UI components
        setupUIforCPU();
        setupUIforSSD();

        // Create the result Label
        resultLabel = new Label();
        resultLabel.setWrapText(true);
        resultLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #5B2A47; -fx-font-weight: bold; -fx-background-color: linear-gradient(to bottom right, #C3F9FB, #F1E8D9); -fx-background-radius: 15px; -fx-font-family: 'Segoe UI'; -fx-border-width: 5px; -fx-border-color: #0D2742; -fx-border-radius: 10px; -fx-padding: 10px;");
        resultLabel.setMaxWidth(1800); // Set maximum width for the result Label
        resultLabel.setMaxHeight(1100); // Set maximum height for the result Label

        // Wrap the Label inside a StackPane to center it
        StackPane resultPane = new StackPane();
        resultPane.getChildren().add(resultLabel);
        StackPane.setAlignment(resultLabel, Pos.CENTER); // Center the Label

        StackPane.setAlignment(backButtonResults, Pos.BOTTOM_RIGHT);

        // Add the resultPane to the resultsPage
        resultsPage.getChildren().addAll(resultPane, backButtonResults);

        // Load text from file and display it in the Label
        loadResultTextFromFile();
                

        // Create the scene
        Scene scene = new Scene(root, 1900, 1300);
        
        primaryStage.setTitle("Elektro - Beats");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createPages() {
        cpuPage = createPage("CPU Page");
        ssdPage = createPage("SSD Page");
        resultsPage = createPage("Results Page");
    }    

    private Pane createPage(String title) {
        Pane page = new StackPane();
        page.setPrefSize(1900, 1300);
        return page;
    }   

    private void showPage(Pane page) {
        // Push the current page to the history stack
        if (!pageHistory.isEmpty() && pageHistory.peek() != page) {
            pageHistory.push(getCurrentVisiblePage());
        }

        // Hide all pages
        cpuPage.setVisible(false);
        ssdPage.setVisible(false);
        resultsPage.setVisible(false);

        // Show the selected page with fade transition
        page.setVisible(true);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), page);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

        // Fade in the result Label after a delay
        if (page == resultsPage) {
            FadeTransition resultFadeTransition = new FadeTransition(Duration.millis(5000), resultLabel);
            resultFadeTransition.setFromValue(0.0);
            resultFadeTransition.setToValue(1.0);
            // resultFadeTransition.setDelay(Duration.millis(500)); // Delay before fading in
            resultFadeTransition.play();
        }
    }

    private Pane getCurrentVisiblePage() {
        if (cpuPage.isVisible()) {
            return cpuPage;
        } else if (ssdPage.isVisible()) {
            return ssdPage;
        } else if (resultsPage.isVisible()) {
            return resultsPage;
        }
        return null;
    }    

    private void setupUIforCPU() {
        // Create a GridPane to hold the components
        GridPane container = new GridPane();
        container.setAlignment(Pos.CENTER); // Align the grid to the center
        container.setHgap(10); // Horizontal gap between components
        container.setVgap(10); // Vertical gap between components
        container.setPadding(new Insets(70, 10, 10, 20)); // Padding around the grid
    
        // Create the input field
        inputCPUField = new TextField();
        inputCPUField.setPromptText("Enter a Prime number");
        inputCPUField.setStyle("-fx-prompt-text-fill: gray; -fx-font-family: Elephant; -fx-font-size: 32px;");
        GridPane.setConstraints(inputCPUField, 0, 0); // Place input field at column 0, row 0
    
        // Create the result label
        resultCPULabel = new Label();
        resultCPULabel.setStyle("-fx-font-size: 32px; -fx-text-fill: #2F4F4F; -fx-font-weight: bold; -fx-background-color: linear-gradient(to bottom right, #CDB498, #A3EFF5); -fx-background-radius: 15px; -fx-font-family: 'Segoe UI'; -fx-border-width: 5px; -fx-border-color: #5C543D; -fx-border-radius: 10px; -fx-padding: 10px;");
        resultCPULabel.setText("Results will be displayed here.");

        // Create a fade transition for the result label
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1500), resultCPULabel);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(false);

        // Start the fade transition
        fadeTransition.play();

        GridPane.setConstraints(resultCPULabel, 1, 1); // Place result label at column 1, row 1

        GridPane.setConstraints(backButtonCPU, 2, 3);  // Place back button at column 2, row 3
    
        // Create the buttons
        buttonBoxCPU.setAlignment(Pos.CENTER_LEFT); // Align buttons to the center left
        GridPane.setConstraints(buttonBoxCPU, 0, 1); // Place button box at column 0, row 1
    
        // Set actions for existing buttons
        buttonCPUisPrime.setOnAction(e -> checkPrime(inputCPUField.getText(), resultCPULabel, "isPrime"));
        buttonCPUfindPrime.setOnAction(e -> checkPrime(inputCPUField.getText(), resultCPULabel, "findPrime"));
        buttonCPUfindIndex.setOnAction(e -> checkPrime(inputCPUField.getText(), resultCPULabel, "findIndex"));
    
        // Add components to the container
        container.getChildren().addAll(inputCPUField, resultCPULabel, buttonBoxCPU, backButtonCPU);
    
        // Add the container to the root
        cpuPage.getChildren().add(container);
    }    
    
    private void checkPrime(String inputText, Label resultLabel, String action) {
        // Convert the input to an integer
        try {
            long number = Long.parseLong(inputText);
            Prime prime;
    
            // Perform action based on the identifier
            switch (action) {
                case "isPrime":
                    prime = new Prime(number);
                    break;
                case "findPrime":
                    prime = new Prime(number, true);
                    break;
                case "findIndex":
                    prime = new Prime(number, false);
                    break;
                default:
                    resultLabel.setText("Invalid action!");
                    return; // Return early if action is invalid
            }
            
            // Build the result string
            StringBuilder resultText = new StringBuilder();
            resultText.append("Value: ").append(prime.getValue()).append("\n");
            resultText.append("Index: ").append(prime.getIndex()).append("\n");
            resultText.append("Computation Time: ").append(prime.getComputationTime()).append(" ms\n\n");
            resultText.append("Score: ").append(prime.getScore()).append("\n");

            // Set the result label text
            resultLabel.setText(resultText.toString());
        } catch (IllegalArgumentException e) {
            resultLabel.setText("Invalid input.\nEnter a Prime number!");
        }
    }

    private void setupUIforSSD() {
        // Define the preferred width and height
        double preferredWidth = 800;
        double preferredHeight = 80;
    
        // Create a VBox to hold the components
        VBox container = new VBox(20); // Adjust spacing as needed
        container.setAlignment(Pos.CENTER); // Center the components within the VBox
        container.setPadding(new Insets(10)); // Add some padding around the VBox
    
        // Create the input field
        inputSSDField = new TextField();
        inputSSDField.setPromptText("Enter a valid number: 4, 8, 16, 32, 64, 128, 256");
        inputSSDField.setStyle("-fx-prompt-text-fill: gray; -fx-font-family: Elephant; -fx-font-size: 32px;");
    
        // Set the preferred width and height for the input field
        inputSSDField.setPrefWidth(preferredWidth);
        inputSSDField.setPrefHeight(preferredHeight);
    
        // Adjust padding and margins for the input field to match the button
        inputSSDField.setPadding(new Insets(10)); // Ensure no additional padding
        inputSSDField.setMaxWidth(preferredWidth);
        inputSSDField.setMaxHeight(preferredHeight);
    
        // Create the result label
        resultSSDLabel = new Label();
        resultSSDLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: #2F4F4F; -fx-font-weight: bold; -fx-background-color: linear-gradient(to bottom right, #FFEBCC, #A3F3F5); -fx-background-radius: 15px; -fx-font-family: 'Segoe UI'; -fx-border-width: 5px; -fx-border-color: #176582; -fx-border-radius: 10px; -fx-padding: 10px;");
        resultSSDLabel.setText("Results will be displayed here.");
    
        // Set action for the button
        buttonSSDrun.setOnAction(e -> {
            try {
                runMaze3dArray(inputSSDField.getText(), resultSSDLabel);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    
        // Set the preferred width and height for the button
        buttonSSDrun.setPrefWidth(preferredWidth);
        buttonSSDrun.setPrefHeight(preferredHeight);
    
        // Adjust padding and margins for the button to match the input field
        buttonSSDrun.setPadding(new Insets(10)); // Ensure no additional padding
        buttonSSDrun.setMaxWidth(preferredWidth);
        buttonSSDrun.setMaxHeight(preferredHeight);
    
        // Add components to the container
        container.getChildren().addAll(inputSSDField, buttonSSDrun, resultSSDLabel);
    
        // Create a StackPane to center the container
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(container);
        StackPane.setAlignment(container, Pos.CENTER); // Ensure the VBox is centered

        StackPane.setAlignment(backButtonSSD, Pos.BOTTOM_RIGHT);
    
        // Add the StackPane to the SSD page
        ssdPage.getChildren().addAll(stackPane, backButtonSSD);
    }    

    private void runMaze3dArray(String inputText, Label resultLabel) throws IOException {
        try {
            int number = Integer.parseInt(inputText);
            if (isValidNumber(number)) {
                Maze maze = new Maze(number);
        
                StringBuilder resultText = new StringBuilder();
        
                resultText.append("Dimension : ").append(maze.getDimension()).append("\n");
                resultText.append("Maze File Length : ").append(maze.getMazeFileLength()).append(" bytes").append("\n\n");
                resultText.append("Minimum reading time : ").append(maze.getMinReadTime() / 1_000_000.0).append(" ms").append("\n");
                resultText.append("Maximum reading time : ").append(maze.getMaxReadTime() / 1_000_000.0).append(" ms").append("\n");
                resultText.append("Average reading time : ").append(maze.getAvgReadTime() / 1_000_000.0).append(" ms").append("\n\n");
                resultText.append("Minimum writing time : ").append(maze.getMinWriteTime() / 1_000_000.0).append(" ms").append("\n");
                resultText.append("Maximum writing time : ").append(maze.getMaxWriteTime() / 1_000_000.0).append(" ms").append("\n");
                resultText.append("Average writing time : ").append(maze.getAvgWriteTime() / 1_000_000.0).append(" ms").append("\n\n");
                resultText.append("Benchmark Read Score : ").append(maze.getReadScore()).append("\n");
                resultText.append("Benchmark Write Score : ").append(maze.getWriteScore()).append("\n");
                resultText.append("Benchmark Combined Score : ").append(maze.getOverallScore()).append("\n");    
        
                resultLabel.setText(resultText.toString());
            } else {
                resultLabel.setText("Invalid input. Please enter a valid number from the provided list.");
            }
        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid input. Please enter a valid number.");
        }
    }

    private boolean isValidNumber(int number) {
        // Define a list of valid numbers
        List<Integer> validNumbers = Arrays.asList(4, 8, 16, 32, 64, 128, 256);
        return validNumbers.contains(number);
    }

    // Method to update the resultTextArea with new text
    private void updateResultText(String text) {
        resultLabel.setText(text);
    }

    // Method to load text from file and display it in the result Label
    private void loadResultTextFromFile() {
        try {
            File file = new File(basePath + "Results.txt");
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                StringBuilder text = new StringBuilder();
                while (scanner.hasNextLine()) {
                    text.append(scanner.nextLine()).append("\n");
                }
                scanner.close();
                updateResultText(text.toString());
            } else {
                updateResultText("Results.txt file not found.");
            }
        } catch (FileNotFoundException e) {
            updateResultText("Error reading Results.txt file.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}