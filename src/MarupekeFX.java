import Marupeke.Violations.Reason;
import ModelComponents.Type;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Marupeke application implemented with JavaFX
 * @author Cand# 215730
 * @version 1.0
 */

public class MarupekeFX extends Application implements MarupekeGUI, Serializable {
    private MarupekeModel gameGrid;
    private transient MarupekeController gameController;
    private transient String projectDir;
    private transient Image xImageEditable;
    private transient Image oImageEditable;
    private transient Image xImageUnedtbl;
    private transient Image oImageUnedtbl;
    private transient Image xImageGreen;
    private transient Image oImageGreen;
    private transient Image xImageRed;
    private transient Image oImageRed;
    private transient Image filledImage;
    private transient Image blankImage;
    private transient Image clearButtonImage;
    private transient Image markOButtonImage;
    private transient Image markXButtonImage;
    private transient Image hintButtonImage;
    private transient Image noHintImage;
    private transient Image quitButtonImage;
    private transient Image loadButtonImage;
    private transient Image saveButtonImage;
    private transient Image newButtonImage;
    private transient Image iconImage;
    private transient BorderPane root;
    private transient Button[][] gridButtons;
    private transient MediaPlayer musicMediaPlayer;
    private transient MediaPlayer buzzerMediaPlayer;
    private transient MediaPlayer choirMediaPlayer;
    private transient MediaPlayer fanfareMediaPlayer;
    private transient MediaPlayer clickMediaPlayer;
    private transient String musicFilePath;
    private transient String clickSoundFilePath;
    private transient String buzzerSoundFilePath;
    private transient String fanfareSoundFilePath;
    private transient String choirSoundFilePath;
    private boolean loadSuccess;
    private double imageSize;
    private int size;

    private final double    GRID_HEIGHT                 = 750;
    private final int       MAX_GRID_SIZE               = 10;
    private final int       MIN_GRID_SIZE               = 3;
    private final int       NUM_OF_TILE_TYPES           = 3;

    //Fixed maximum filled tiles for grids of size 10
    private final int       TEN_GRID_MAX_FILLED = 21;


    /**
     * Main method for MarupekeFX. Runs the game in a JavaFX window
     * @param args Takes four int parameters for the size pof the grid, the number of filled tiles, the number of X
     *             tiles, and the number of O tiles. Will also run with just size, or no parameters.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initialises the root node of the scene, as well as the media resources required
     * @throws Exception The possible exceptions thrown by the super.init method call
     */
    @Override
    public void init() throws Exception {
        super.init();
        loadSuccess = false;
        //initialises the current path for loading files
        setProjectDir();
        //Gets the grid parameters from the command line, or calculates them if some or all are not provided.
        List<String> rawParameters = getParameters().getRaw();
        try{
            size = Integer.parseInt(rawParameters.get(0));
        }
        catch (IndexOutOfBoundsException e){
            Random random = new Random();
            size = random.nextInt((MAX_GRID_SIZE - MIN_GRID_SIZE));
            size = size + MIN_GRID_SIZE;
        }
        System.out.println("Grid Size:\t" + size);
        int numFill, numX, numO;
        try {
            numFill = Integer.parseInt(rawParameters.get(1));
            System.out.println("NumFill:\t" + numFill);
            numX    = Integer.parseInt(rawParameters.get(2));
            System.out.println("NumX:\t" + numX);
            numO    = Integer.parseInt(rawParameters.get(3));
            System.out.println("NumO:\t" + numO);
        }
        catch (IndexOutOfBoundsException e){
            if(size < MAX_GRID_SIZE) {
                int halfTotalTiles = size * size / 2;
                numFill     = halfTotalTiles / NUM_OF_TILE_TYPES;
                numX        = halfTotalTiles / NUM_OF_TILE_TYPES;
                numO        = halfTotalTiles / NUM_OF_TILE_TYPES;
            }
            else{
                numFill     = TEN_GRID_MAX_FILLED / NUM_OF_TILE_TYPES;
                numX        = TEN_GRID_MAX_FILLED / NUM_OF_TILE_TYPES;
                numO        = TEN_GRID_MAX_FILLED / NUM_OF_TILE_TYPES;
            }
            System.out.println("NumFill:\t" + numFill);
            System.out.println("NumX:\t\t" + numX);
            System.out.println("NumO:\t\t" + numO);
        }
        System.out.println("Generating game grid...");
        //Checks that the parameters provided don't throw an exception, and that grid generation doesnt time out
        try {
            gameGrid = MarupekeModel.randomPuzzle(size, numFill, numX, numO);
            loadSuccess = true;
            System.out.println("Game grid generation successful");
        }
        //Creates error popup in the event of failure
        catch (RuntimeException | MarupekeModel.TooManyMarkedSquaresException e){
            System.out.println("Game grid generation failed");
            root = initFailure(e);
        }
        if(loadSuccess) {
            imageSize = GRID_HEIGHT/ (double) gameGrid.getGridSize();
            gameGrid.setGui(this);
        }
        gameController = new MarupekeController(gameGrid);
        //Initialise the media players and the sound files
        initSoundFiles();
        Media music         = new Media(new File(musicFilePath).toURI().toString());
        musicMediaPlayer    = new MediaPlayer(music);
        Media choir         = new Media(new File(choirSoundFilePath).toURI().toString());
        choirMediaPlayer    = new MediaPlayer(choir);
        Media buzzer        = new Media(new File(buzzerSoundFilePath).toURI().toString());
        buzzerMediaPlayer   = new MediaPlayer(buzzer);
        Media fanfare       = new Media(new File(fanfareSoundFilePath).toURI().toString());
        fanfareMediaPlayer  = new MediaPlayer(fanfare);
        Media click         = new Media(new File(clickSoundFilePath).toURI().toString());
        clickMediaPlayer    = new MediaPlayer(click);
        //Initialises the image files
        initImages();
        //Creates the grid of Buttons
        gridButtons = new Button[size][size];
        if (loadSuccess) {
            root = createRoot();
        }
    }
    //Corrects the path for finding the resources (images, music etc) if the program is run from the command line
    private void setProjectDir(){
        projectDir = System.getProperty("user.dir");
        if (projectDir.substring(projectDir.length() - 3).equals("src")){
            projectDir = projectDir.substring(0, projectDir.length() - 4);
        }
    }

    //Returns a scene root node to be used in the event of failed grid generation
    private BorderPane initFailure(Exception e){
        BorderPane sceneRoot = new BorderPane();
        Label failureHeaderLabel = new Label("Error: Game grid generation failed");
        int windowWidth     = 500;
        int windowHeight    = 300;
        sceneRoot.setPrefSize(windowWidth, windowHeight);
        failureHeaderLabel.setAlignment(Pos.CENTER);
        failureHeaderLabel.setFont(new Font("Arial", 24));
        Label failureSubLabel = new Label();
        if (e.getMessage().equals("Error: Puzzle generation has timed out")){
            failureSubLabel.setText("Puzzle Generation has timed out. Please retry with\n" +
                    "different parameters (A smaller grid is more likely to\n" +
                    "successfully generate.)");
        }
        else  if (e.getMessage().equals("invalid size parameters")){
            failureSubLabel.setText("Invalid size parameter, grid size must be between " + MIN_GRID_SIZE + "\n" +
                    "and " + MAX_GRID_SIZE);
        }
        else {
            failureSubLabel.setText("Illegal parameters used. parameters. Less than half \n" +
                    "the tiles in the grid must be marked, and more than 25 \n" +
                    "must be blank");
        }

        failureSubLabel.setFont(new Font("Arial", 18));
        sceneRoot.setTop(failureHeaderLabel);
        sceneRoot.setCenter(failureSubLabel);
        return sceneRoot;
    }

    //Initialises the images to be used in the application
    private void initImages(){
        try {
            xImageEditable   = new Image(new FileInputStream(projectDir + "/rsc/images/XTileEditable.png"));
            oImageEditable   = new Image(new FileInputStream(projectDir + "/rsc/images/OTileEditable.png"));
            xImageUnedtbl    = new Image(new FileInputStream(projectDir + "/rsc/images/XTileUneditable.png"));
            oImageUnedtbl    = new Image(new FileInputStream(projectDir + "/rsc/images/OTileUneditable.png"));
            xImageGreen      = new Image(new FileInputStream(projectDir + "/rsc/images/XTileGreen.png"));
            oImageGreen      = new Image(new FileInputStream(projectDir + "/rsc/images/OTileGreen.png"));
            xImageRed        = new Image(new FileInputStream(projectDir + "/rsc/images/XTileRed.png"));
            oImageRed        = new Image(new FileInputStream(projectDir + "/rsc/images/OTileRed.png"));
            filledImage      = new Image(new FileInputStream(projectDir + "/rsc/images/FilledTile.png"));
            blankImage       = new Image(new FileInputStream(projectDir + "/rsc/images/BlankTile.png"));
            clearButtonImage = new Image(new FileInputStream(projectDir + "/rsc/images/ClearDefault.jpg"));
            markOButtonImage = new Image(new FileInputStream(projectDir + "/rsc/images/MarkODefault.jpg"));
            markXButtonImage = new Image(new FileInputStream(projectDir + "/rsc/images/MarkXDefault.jpg"));
            hintButtonImage  = new Image(new FileInputStream(projectDir + "/rsc/images/HintButton.jpg"));
            noHintImage      = new Image(new FileInputStream(projectDir + "/rsc/images/HintButtonNoHint.jpg"));
            quitButtonImage  = new Image(new FileInputStream(projectDir + "/rsc/images/QuitButton.jpg"));
            loadButtonImage  = new Image(new FileInputStream(projectDir + "/rsc/images/LoadButton.jpg"));
            saveButtonImage  = new Image(new FileInputStream(projectDir + "/rsc/images/SaveButton.jpg"));
            newButtonImage   = new Image(new FileInputStream(projectDir + "/rsc/images/NewButton.jpg"));
            iconImage        = new Image(new FileInputStream(projectDir + "/rsc/images/icon.png"));
        }
        catch (FileNotFoundException e){
            System.out.println(e.toString());
            System.exit(-1);
        }
    }

    //Initialises the sound files to be used in the application
    private void initSoundFiles(){
        musicFilePath           = projectDir + "/rsc/music/GameMusic.mp3";
        clickSoundFilePath      = projectDir + "/rsc/sounds/Click.wav";
        buzzerSoundFilePath     = projectDir + "/rsc/sounds/Buzzer.wav";
        fanfareSoundFilePath    = projectDir + "/rsc/sounds/fanfare.wav";
        choirSoundFilePath      = projectDir + "/rsc/sounds/choir.wav";
    }

    /**
     * Start method for the application
     * @param primaryStage the primary stage for the application
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setResizable(false);
        primaryStage.setTitle("Marupeke");
        primaryStage.getIcons().add(iconImage);
        //Sets the music to repeat
        musicMediaPlayer.setOnRepeat(() -> musicMediaPlayer.seek(Duration.ZERO));
        musicMediaPlayer.setOnEndOfMedia(() -> musicMediaPlayer.seek(Duration.ZERO));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        //Code to be executed if grid generation failed
        if(!loadSuccess){
            PauseTransition p = new PauseTransition(Duration.seconds(10));
            p.setOnFinished(event -> Platform.exit());
            p.play();
        }

        //Plays the music
        else musicMediaPlayer.setAutoPlay(true);

        //Listens for a the underlying grid being changed as the result of loading a new file
        MarupekeController.gridChangeProperty().addListener((observable, oldValue, newValue) -> {
            //Sets the stage to reflect the newly loaded underlying grid
            root = createRoot();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        });

        //Listens for the controller detecting a win state in the underlying grid
        gameController.isCompleteProperty().addListener((observable, oldValue, newValue) -> {
            if(gameController.isCompleteProperty().get()) {
                playWinAnimation();
                Alert winAlert = createConfirmAlert("Game Complete",
                        "Congratulations!",
                        "In the face of adversity you have prevailed! Play a new game, or quit",
                        "New Game", "Quit, I've had enough");
                Optional<ButtonType> choice = winAlert.showAndWait();
                if ("New Game".equals(choice.get().getText())) newGame();
                else Platform.exit();
            }
        });

        //Listens for violations being detected in the underlying grid
        gameController.getViolationDetected().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                //Plays violation sound
                buzzerMediaPlayer.seek(Duration.ZERO);
                buzzerMediaPlayer.play();
                //Plays the violation animation for all violations detected
                for (Reason r : gameGrid.illegalitiesInGrid()) playViolationAnimation(  r.getStart(),
                                                                                        r.getEnd(),
                                                                                        r.getType());
            }
        });
    }

    //Plays the win animation for when the game is complete
    private void playWinAnimation(){
        musicMediaPlayer.stop();
        //Plays a victory fanfare sound
        fanfareMediaPlayer.seek(Duration.ZERO);
        fanfareMediaPlayer.play();
        setAllEditableGreen();
    }

    //Sets all editable tiles to their respective green versions
    private void setAllEditableGreen(){
        for (int i = 0; i < gameGrid.getGridSize(); i++){
            for(int j = 0; j < gameGrid.getGridSize(); j++){
                if(gameGrid.isEditable(i, j)){
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(imageSize);
                    imageView.setFitHeight(imageSize);
                    switch (gameGrid.getValueAt(i, j)){
                        case X:
                            imageView.setImage(xImageGreen);
                            gridButtons[i][j].setGraphic(imageView);
                            break;
                        case O:
                            imageView.setImage(oImageGreen);
                            gridButtons[i][j].setGraphic(imageView);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    //Plays the violation animation
    private void playViolationAnimation(Reason.Coordinate start, Reason.Coordinate end, Type violationType){
        Image animationImage;
        if(violationType.equals(Type.X)) animationImage = xImageRed;
        else animationImage = oImageRed;
        ImageView imageView1 = createImageView();
        ImageView imageView2 = createImageView();
        ImageView imageView3 = createImageView();
        imageView1.setImage(animationImage);
        imageView2.setImage(animationImage);
        imageView3.setImage(animationImage);
        gridButtons[start.getX()][start.getY()].setGraphic(imageView1);
        gridButtons[(start.getX() + end.getX()) / 2][(start.getY() + end.getY()) /2].setGraphic(imageView2);
        gridButtons[end.getX()][end.getY()].setGraphic(imageView3);
        PauseTransition p = new PauseTransition(Duration.seconds(1));
        p.setOnFinished(event -> defaultSetAllTiles());
        p.play();
    }

    //Initialises the image view with the required size
    private ImageView createImageView(){
        ImageView imageView = new ImageView();
        imageView.setFitWidth(imageSize);
        imageView.setFitHeight(imageSize);
        return imageView;
    }

    //Creates the root node
    private BorderPane createRoot(){
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(drawGrid());
        borderPane.setRight(drawRightPane());
        Label attribution = new Label("Music: \"Sidewalk Shade\" by Kevin Macleod / CC BY 3.0\n" +
                "Sound Effects (All available at www.freesound.org):\n\"Buzzer sounds (Wrong answer / Error)\" by " +
                "Breviceps / Creative Commons 0\n" +
                "\"Choir of Voices - Single Chord\" by bone666138 / Creative Commons 0\n" +
                "\"Click1.wav by EdgardEdition\" / Attribution\n" +
                "\"Short Brass Fanfare 1.wav\" by _MC5_ / Attribution");
        attribution.setFont(new Font("Arial", 15));
        borderPane.setBottom(attribution);
        return borderPane;
    }

    //Creates a grid of buttons representing the underlying game grid
    @Override
    public GridPane drawGrid(){
        GridPane gamePane = new GridPane();
        for (int i = 0; i < gameGrid.getGridSize(); i++) {
            for (int j = 0; j < gameGrid.getGridSize(); j++) {
                int x = j;
                int y = i;
                Button button = new Button("", getTileImage(j, i));
                gamePane.add(button, i, j);
                //Adds functionality to the buttons
                button.setOnAction(event -> {
                    if(gameController.markTile(x, y)) {
                        //Plays a click sound when buttons are pressed
                        clickMediaPlayer.seek(Duration.ZERO);
                        clickMediaPlayer.play();
                        if(!gameGrid.isPuzzleComplete()) {
                            //Sets the tile to its new graphic
                            button.setGraphic(getTileImage(x, y));
                        }
                    }
                });
                gridButtons[j][i] = button;
            }
        }
        return gamePane;
    }

    //Returns the appropriate image for a tile at the coordinates (x, y)
    private ImageView getTileImage(int x, int y){
        ImageView imageView = new ImageView();
        imageView.setFitWidth(imageSize);
        imageView.setFitHeight(imageSize);
        switch (gameGrid.getValueAt(x, y)){
            case X:
                if (gameGrid.isEditable(x, y)) imageView.setImage(xImageEditable);
                else imageView.setImage(xImageUnedtbl);
                break;
            case O:
                if (gameGrid.isEditable(x, y)) imageView.setImage(oImageEditable);
                else imageView.setImage(oImageUnedtbl);
                break;
            case FILLED:
                imageView.setImage(filledImage);
                break;
            case BLANK:
                imageView.setImage(blankImage);
                break;
        }
        return imageView;
    }

    //Initialises the right pane (where all the buttons are) of the application
    private GridPane drawRightPane(){
        GridPane rightPane = new GridPane();
        Label selectorLabel = drawSelectionLabel();
        rightPane.add(selectorLabel, 0, 3);
        rightPane.add(drawSelectorButton(markXButtonImage, Type.X,      selectorLabel), 0, 0);
        rightPane.add(drawSelectorButton(markOButtonImage, Type.O,      selectorLabel), 0, 1);
        rightPane.add(drawSelectorButton(clearButtonImage, Type.BLANK,  selectorLabel), 0, 2);
        rightPane.add(drawHintButton(),     0, 4);
        rightPane.add(drawQuitButton(),     0, 8);
        rightPane.add(drawLoadButton(),     0, 6);
        rightPane.add(drawSaveButton(),     0, 5);
        rightPane.add(drawNewGameButton(),  0, 7);
        return rightPane;
    }

    //Generates the label which shows the user which tile they have selected
    private Label drawSelectionLabel(){
        Label selectorLabel = new Label("  Current Selection:");
        selectorLabel.setPrefHeight(75);
        selectorLabel.setFont(new Font("Arial", 15));
        return selectorLabel;
    }

    //Generates the hint button
    private Button drawHintButton(){
        ImageView hintImageView = new ImageView();
        hintImageView.setImage(hintButtonImage);
        Button hintButton = new Button("", hintImageView);
        hintButton.setOnAction(event -> {
            MarupekeModel.Hint hint = gameController.getHint();
            ImageView imageView = new ImageView();
            //Code to execute if there is no possible hint
            if(hint.getX() == -1){
                buzzerMediaPlayer.seek(Duration.ZERO);
                buzzerMediaPlayer.play();
                ImageView prevImage = new ImageView();
                imageView.setImage(noHintImage);
                hintButton.setGraphic(imageView);
                prevImage.setImage(hintButtonImage);
                PauseTransition p = new PauseTransition(Duration.seconds(3));
                p.setOnFinished(hintEvent -> hintButton.setGraphic(prevImage));
                p.play();
            }
            //Code to execute when a hint is successfully generated
            else{
                imageView.setFitWidth(imageSize);
                imageView.setFitHeight(imageSize);
                if(hint.getType().equals(Type.X)) imageView.setImage(xImageGreen);
                else imageView.setImage(oImageGreen);
                choirMediaPlayer.seek(Duration.ZERO);
                choirMediaPlayer.play();
                gridButtons[hint.getX()][hint.getY()].setGraphic(imageView);
            }
            PauseTransition p = new PauseTransition(Duration.seconds(5));
            p.setOnFinished(hintEvent -> defaultSetAllTiles());
            p.play();
        });
        return hintButton;
    }

    //Generates the quit button
    private Button drawQuitButton(){
        ImageView quitImageView = new ImageView();
        quitImageView.setImage(quitButtonImage);
        Button quitButton = new Button("", quitImageView);
        quitButton.setOnAction(event -> {
            Alert quitAlert = new Alert(Alert.AlertType.CONFIRMATION);
            quitAlert.setTitle("Marupeke Alert");
            quitAlert.setHeaderText("Commiserations!");
            quitAlert.setContentText("They who fight and run away, shall live to run again!\n" +
                    "Are you sure you want to quit?");
            ButtonType alertNewGameButton = new ButtonType("Yes, I give up");
            ButtonType alertQuitGameButton = new ButtonType("No, I want to keep trying");
            quitAlert.getButtonTypes().setAll(alertNewGameButton, alertQuitGameButton);
            Optional<ButtonType> choice = quitAlert.showAndWait();
            if ("Yes, I give up".equals(choice.get().getText())) Platform.exit();
        });
        return quitButton;
    }

    //Generates the new game button
    private Button drawNewGameButton(){
        ImageView newGameImageView = new ImageView();
        newGameImageView.setImage(newButtonImage);
        Button newGameButton = new Button("", newGameImageView);
        newGameButton.setOnAction(event -> {
            clickMediaPlayer.seek(Duration.ZERO);
            clickMediaPlayer.play();
            newGame();
        });
        return newGameButton;
    }

    //Generates the load button
    private Button drawLoadButton(){
        ImageView loadImageView = new ImageView();
        loadImageView.setImage(loadButtonImage);
        Button loadButton = new Button("", loadImageView);
        loadButton.setOnAction(event -> {
            clickMediaPlayer.seek(Duration.ZERO);
            clickMediaPlayer.play();
            Alert loadAlert = createConfirmAlert("Load Game",
                    "Are you sure you want to load a saved game?",
                    "Progress in your current game will be lost forever", "Yes, load my saved game",
                    "No, I don't want to");
            Optional<ButtonType> choice = loadAlert.showAndWait();
            if ("Yes, load my saved game".equals(choice.get().getText())) {
                if (gameGrid.loadFromFile() == null){
                    Alert noSavedGameAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    noSavedGameAlert.setTitle("No Saved Game Found");
                    noSavedGameAlert.setHeaderText("In order to load a saved game, you must first save a game");
                    noSavedGameAlert.setContentText("Obviously...");
                    noSavedGameAlert.show();
                }
                else {
                    gameGrid = gameGrid.loadFromFile();
                    imageSize = GRID_HEIGHT / (double) gameGrid.getGridSize();
                    gridButtons = new Button[gameGrid.getGridSize()][gameGrid.getGridSize()];
                    gameGrid.setGui(this);
                    gameController.setGameGrid(gameGrid);
                    MarupekeController.setGridChange(MarupekeController.gridChangeProperty().getValue() + 1);
                }
            }
        });
        return loadButton;
    }
    //Generates the save button
    private Button drawSaveButton(){
        ImageView saveImageView = new ImageView();
        saveImageView.setImage(saveButtonImage);
        Button saveButton = new Button("", saveImageView);
        saveButton.setOnAction(event -> {
            clickMediaPlayer.seek(Duration.ZERO);
            clickMediaPlayer.play();
                Alert saveAlert = createConfirmAlert("Save Game",
                        "Are you sure you want to save this game?",
                        "Doing so will permanently delete any currently saved game",
                        "Yes, save this game",
                        "No, don't save");
                Optional<ButtonType> choice = saveAlert.showAndWait();
                if ("Yes, load my saved game".equals(choice.get().getText())) gameController.writeToFile();
        });
        return saveButton;
    }

    //Draws a standard alert dialog box with two buttons
    private Alert createConfirmAlert(String title, String header, String content, String btn1Text, String btn2Text){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        ButtonType buttonType1 = new ButtonType(btn1Text);
        ButtonType buttonType2 = new ButtonType(btn2Text);
        alert.getButtonTypes().setAll(buttonType1, buttonType2);
        return alert;
    }

    //Generates a selector button
    private Button drawSelectorButton(Image buttonImage, Type sType, Label selectorLabel){
        ImageView imageView = new ImageView();
        imageView.setImage(buttonImage);
        Button button = new Button("", imageView);
        button.setOnAction( event -> {
            clickMediaPlayer.seek(Duration.ZERO);
            clickMediaPlayer.play();
            gameController.setCurrentSetter(sType);
            if (sType.toString().equals("BLANK")) selectorLabel.setText("  Current Selection:\tClear");
            else selectorLabel.setText("  Current Selection:\tMark " + sType);
        });
        return button;
    }

    /*
     * Sets all the tiles to their default state based on the underlying grid. Used to 'flush' the grid after animations
     * Or when the grid has been reloaded
     */
    private void defaultSetAllTiles(){
        for (int i = 0; i < gameGrid.getGridSize(); i++){
            for(int j = 0; j < gameGrid.getGridSize(); j++) gridButtons[i][j].setGraphic(getTileImage(i, j));
        }
    }

    //New game method to be called when a new game needs to be setup
    private void newGame(){
        Dialog<Boolean> loadingDialog = new Dialog<>();
        loadingDialog.setTitle("LOADING");
        loadingDialog.setGraphic(new ProgressBar());
        loadingDialog.setHeaderText("Your new grid is being loaded for you");
        loadingDialog.setContentText("This shouldn't take too long........");
        loadingDialog.show();
        gameGrid.setGui(this);
        Platform.runLater(() -> {
            List<String> rawParameters = getParameters().getRaw();
            int numFill, numX, numO;
            try {
                numFill = Integer.parseInt(rawParameters.get(1));
                numX = Integer.parseInt(rawParameters.get(2));
                numO = Integer.parseInt(rawParameters.get(3));
            }
            catch (IndexOutOfBoundsException e){
                if(size < MAX_GRID_SIZE) {
                    int halfTotalTiles = size * size / 2;
                    numFill     = halfTotalTiles / NUM_OF_TILE_TYPES;
                    numX        = halfTotalTiles / NUM_OF_TILE_TYPES;
                    numO        = halfTotalTiles / NUM_OF_TILE_TYPES;
                }
                else{
                    numFill     = TEN_GRID_MAX_FILLED / NUM_OF_TILE_TYPES;
                    numX        = TEN_GRID_MAX_FILLED / NUM_OF_TILE_TYPES;
                    numO        = TEN_GRID_MAX_FILLED / NUM_OF_TILE_TYPES;
                }
            }
            try {
                gameGrid = MarupekeModel.randomPuzzle(size, numFill, numX, numO);
            } catch (MarupekeModel.TooManyMarkedSquaresException | RuntimeException e) {
                System.out.println(e.toString());
                Platform.exit();
            }
            gridButtons = new Button[gameGrid.getGridSize()][gameGrid.getGridSize()];
            gameController.setGameGrid(gameGrid);
            MarupekeController.setGridChange(MarupekeController.gridChangeProperty().getValue() + 1);
            musicMediaPlayer.play();
            gameController.setIsComplete(false);
            loadingDialog.setResult(true);
            loadingDialog.close();
        });
    }
}