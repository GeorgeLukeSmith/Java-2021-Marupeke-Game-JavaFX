import Marupeke.Violations.Reason;
import ModelComponents.Type;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

/**
 * Controller for Marupeke application
 * @author Cand#215730
 * @version 1.0
 */

public class MarupekeController {
    private MarupekeModel gameGrid;
    private Type currentSetter;
    private BooleanProperty isComplete;
    private List<Reason> currentViolations;
    private BooleanProperty violationDetected;
    private static IntegerProperty gridChange;

    /**
     * Constructor for MarupekeController
     * @param gameGrid The gameGrid the MarupekeController will control
     */
    public MarupekeController(MarupekeModel gameGrid){
        this.gameGrid = gameGrid;
        currentSetter = Type.BLANK;
        isComplete = new SimpleBooleanProperty(false);
        currentViolations = null;
        violationDetected = new SimpleBooleanProperty(false);
        gridChange = new SimpleIntegerProperty();
        gridChange.setValue(0);
    }

    /**
     * Marks a tile in the MarupekeGrid
     * @param x x coordinate of the tile to mark
     * @param y y coordinate of the tile to mark
     * @return  True if the tile was marked, otherwise false
     */
    public boolean markTile(int x, int y){
        boolean hasSet = true;
        switch (currentSetter){
            case X:
                gameGrid.markX(x, y);
                if(!gameGrid.isLegalGrid()){
                    currentViolations = gameGrid.illegalitiesInGrid();
                    violationDetected.setValue(true);
                    gameGrid.unmark(x, y);
                    hasSet = false;
                }
                break;
            case O:
                gameGrid.markO(x, y);
                if(!gameGrid.isLegalGrid()) {
                    currentViolations = gameGrid.illegalitiesInGrid();
                    violationDetected.setValue(true);
                    gameGrid.unmark(x, y);
                    hasSet = false;
                }
                break;
            default:
                gameGrid.unmark(x, y);
                break;
        }
        if (gameGrid.isPuzzleComplete()){
            isComplete.setValue(true);
        }
        violationDetected.setValue(false);
        return hasSet;
    }

    public void writeToFile(){
        gameGrid.writeToFile();
    }

    /**
     * Get a hint as to how to complete the board
     * @return A hint value
     */
    public MarupekeModel.Hint getHint(){
        return gameGrid.solvePuzzle();
    }

    /**
     * returns the current setter for the board
     * @param currentSetter the current setter
     */
    public void setCurrentSetter(Type currentSetter) {
        this.currentSetter = currentSetter;
    }

    /**
     * returns a true boolean property if the board is complete.
     * @return true boolean property if the board is complete, otherwise false boolean property
     */
    public BooleanProperty isCompleteProperty() {
        return isComplete;
    }

    /**
     * setter for the complete property
     * @param isComplete complete property
     */
    public void setIsComplete(boolean isComplete) {
        this.isComplete.set(isComplete);
    }

    /**
     * getter for violationDetected
     * @return violationDetected
     */
    public BooleanProperty getViolationDetected() {
        return violationDetected;
    }

    /**
     * Sets the gameGrid of the MarupekeController
     * @param gameGrid a gameGrid object
     */
    public void setGameGrid(MarupekeModel gameGrid) {
        this.gameGrid = gameGrid;
    }

    /**
     * Gets an integer property which increments when the gameGrid is changed
     * @return an integerProperty
     */
    public static IntegerProperty gridChangeProperty() {
        return gridChange;
    }

    /**
     * sets the integer property
     * @param gridChange the new integer property value
     */
    public static void setGridChange(int gridChange) {
        MarupekeController.gridChange.set(gridChange);
    }
}
