import Marupeke.Violations.DiagonalViolation;
import Marupeke.Violations.HorizontalViolation;
import Marupeke.Violations.Reason;
import Marupeke.Violations.VerticalViolation;
import ModelComponents.MarupekeTile;
import ModelComponents.Type;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.shuffle;

/**
 * The MarupekeGrid class creates a representation of a game board for the game Marupeke. It can create a blank board,
 * or a randomly generated board, and allows for the placing of the different relevant symbols on the board.
 *
 * @author Cand#215730
 * @version 3.0
 */
public class MarupekeModel implements Serializable {
    //stores the game state.
    private MarupekeTile[][] grid;
    //ui for sending information to the GUI
    private MarupekeGUI gui;
    //file location for saving and loading grids
    private final String fPath = getCurrDir() + "\\SavedGame\\Saved.dat";

    /**
     * Constructs a new MarupekeGrid of the specified size.
     *
     * @param size The desired size of the marupeke grid
     */
    public MarupekeModel(int size) {
        size = setToRange(size, 10, 3);
        grid = new MarupekeTile[size][size];
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                grid[i][j] = new MarupekeTile();
            }
        }
    }

    //Corrects the path for finding the resources (images, music etc) if the program is run from the command line
    private String getCurrDir(){
        String projectDir = System.getProperty("user.dir");
        if (projectDir.substring(projectDir.length() - 3).equals("src")){
            projectDir = projectDir.substring(0, projectDir.length() - 4);
        }
        return projectDir;
    }

    /**
     * Writes the grid to a file, saved in the location dictated by the value in fPath
     */
    public void writeToFile(){
        try (FileOutputStream modelFOut = new FileOutputStream(fPath);
                ObjectOutputStream modelOut = new ObjectOutputStream(modelFOut)){
            modelOut.writeObject(this);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Loads a grid from a file dictated by the path stored in fPath and returns it
     * @return a MarupekeGrid object read from a file, or null if no file is found
     */
    public MarupekeModel loadFromFile(){
        try (FileInputStream modelFIn = new FileInputStream(fPath);
             ObjectInputStream modelIn = new ObjectInputStream(modelFIn)){
            return (MarupekeModel) modelIn.readObject();
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Generates a randomly laid out MarupekeGrid based on the parameters provided. The grid will contain the specified
     * number of filled squares ('#'), Xs and Os arranged at random unless the specified numbers add up to more than the
     * available squares on the board, in which case a null MarupekeGrid will be returned. The board will be legal under
     * the rules of Marupeke
     *
     * @param size    The size of the board (generates a board with size^size squares).
     * @param numFill The number of squares to be filled ('#').
     * @param numX    The number of Xs in the grid.
     * @param numO    The number of Os in the grid.
     * @return A randomly generated MarupekeGrid to the specification of the parameters provided, that does not contain
     *         three Xs/Os in diagonal, vertical or horizontal sequence
     */
    public static MarupekeModel randomPuzzle(int size, int numFill, int numX, int numO)
            throws TooManyMarkedSquaresException {
        MarupekeModel randomGrid = null;
        if(size > 10 || size < 3) throw new RuntimeException("invalid size parameters");
        if ((numFill + numX + numO) > (size * size) / 2) {
            throw new TooManyMarkedSquaresException("Sum of marked squares must be lower than half the squares " +
                    "in the grid");
        }
        //Executes if the number of elements provided is LESS than the half number of squares in the grid
        else {
            ArrayList<Type> puzzleList = new ArrayList<>();
            //Adds the '#'s to puzzleList
            for (int i = 0; i < numFill; i++) {
                puzzleList.add(Type.FILLED);
            }
            //Adds the 'X's to puzzleList
            for (int i = 0; i < numX; i++) {
                puzzleList.add(Type.X);
            }
            //Adds the 'O's to puzzleList
            for (int i = 0; i < numO; i++) {
                puzzleList.add(Type.O);
            }
            //Adds the '_'s to puzzleList
            for (int i = 0; i < ((size * size) - (numFill + numX + numO)); i++) {
                puzzleList.add(Type.BLANK);
            }
            int loopCount = 0;
            while (randomGrid == null || randomGrid.solvePuzzle().getX() == -1) {
                //Randomly shuffles the elements in puzzleList
                randomGrid = new MarupekeModel(size);
                shuffle(puzzleList);
                Iterator<Type> listIterator = puzzleList.iterator();
                for (int i = 0; i < randomGrid.getGridSize(); i++) {
                    for (int j = 0; j < randomGrid.getGridSize(); j++) {
                        //Puts the next element in puzzleList into randomGrid
                        Type item = listIterator.next();
                        randomGrid.setGridType(i, j, item, item == Type.BLANK);
                    }
                }
                loopCount ++;
                if (loopCount > 1000000){
                    throw new RuntimeException("Error: Puzzle generation has timed out");
                }
            }
        }
        return randomGrid;
    }
    /**
     * Returns a hint for the user if the grid is solvable, otherwise returns a hint with x and y set to -1
     * @return true if solvable, false if not solvable
     */
    public Hint solvePuzzle(){
        Hint hint;
        if(!isLegalGrid()) return new Hint(-1, -1, null);
        else if(countInGrid(Type.BLANK) == 0 & isLegalGrid()){
            ArrayList<Hint> hintList = new ArrayList<>();
            for (int i = 0; i < getGridSize(); i++) {
                for (int j = 0; j < getGridSize(); j++) {
                    if (grid[i][j].getSearchSetEditable()) {
                        hintList.add(new Hint(i, j, grid[i][j].getData()));
                    }
                }
            }
            shuffle(hintList);
            return hintList.get(0);
        }
        else{
            int[] tileCoordinates = getNextBlank();
            grid[tileCoordinates[0]][tileCoordinates[1]].setSearchSetEditable(true);
            markX(tileCoordinates[0], tileCoordinates[1]);
            hint = solvePuzzle();
            grid[tileCoordinates[0]][tileCoordinates[1]].setSearchSetEditable(false);
            unmark(tileCoordinates[0], tileCoordinates[1]);
            if(hint.getX() == -1){
                markO(tileCoordinates[0], tileCoordinates[1]);
                grid[tileCoordinates[0]][tileCoordinates[1]].setSearchSetEditable(true);
                hint = solvePuzzle();
                grid[tileCoordinates[0]][tileCoordinates[1]].setSearchSetEditable(false);
                unmark(tileCoordinates[0], tileCoordinates[1]);
            }
        }
        return hint;
    }

    /**
     * Inner class representing hints
     */
    public static class Hint{
        private int x;
        private int y;
        private Type type;

        /**
         * constructor for hint objects
         * @param x x value for the hint
         * @param y y value for the hint
         * @param type type value for the hint
         */
        public Hint(int x, int y, Type type){
            this.x = x;
            this.y = y;
            this.type = type;
        }

        /**
         * Getter for the x value of the hint
         * @return x value of the hint
         */
        public int getX() {
            return x;
        }

        /**
         * setter for the x value of the hint
         * @param x new x value for the hint
         */
        public void setX(int x) {
            this.x = x;
        }
        /**
         * Getter for the y value of the hint
         * @return y value of the hint
         */
        public int getY() {
            return y;
        }
        /**
         * setter for the y value of the hint
         * @param y new y value for the hint
         */
        public void setY(int y) {
            this.y = y;
        }

        /**
         * getter for the type value of the hint
         * @return type value of the hint
         */
        public Type getType() {
            return type;
        }

        /**
         * setter for the type value of the hint
         * @param type new type value for the hint
         */
        public void setType(Type type) {
            this.type = type;
        }
    }

    //Used to find the next blank tile for solvePuzzle
    private int[] getNextBlank(){
        int[] coordinates = new int[] {-1, -1};
        for (int i = 0; i < getGridSize(); i++) {
            for (int j = 0; j < getGridSize(); j++) {
                if (grid[i][j].getData().equals(Type.BLANK)){
                    coordinates[0] = i;
                    coordinates[1] = j;
                    return coordinates;
                }
            }
        }
        return coordinates;
    }
    /**
     * returns true if the grid does not violate the rules of marupeke
     * @return true if the grid does not violate the rules of marupeke, otherwise false
     */
    public boolean isLegalGrid(){
        boolean legal;
        //check horizontal violations
        legal = checkLegalTiles(new int[]{0, getGridSize()}, new int[]{0, getGridSize() - 2},
                0, 1);

        //check vertical violations
        if (legal) {
            legal = checkLegalTiles(new int[]{0, getGridSize() - 2}, new int[]{0, getGridSize()},
                    1, 0);
        }

        //check diagonal decline
        if (legal) {
            legal = checkLegalTiles(new int[]{0, getGridSize() - 2}, new int[]{0, getGridSize() - 2},
                    1, 1);
        }

        //check diagonal incline
        if (legal) {
            legal = checkLegalTiles(new int[]{2, getGridSize()}, new int[]{0, (getGridSize() - 2)},
                    -1, 1);
        }
        return legal;
    }
    /*
     * Checks tiles for illegalities based on the parameters provided, returning false upon finding one, or returning
     * true if it fails to find one
     */
    private boolean checkLegalTiles(int[] rowBound, int[] colBound, int rowOffset, int colOffset) {
        for (int i = rowBound[0]; i < rowBound[1]; i++) {
            for (int j = colBound[0]; j < colBound[1]; j++) {
                if (!grid[i][j].getData().equals(Type.BLANK) && !grid[i][j].getData().equals(Type.FILLED)) {
                    if (grid[i][j].getData().equals(grid[i + rowOffset][j + colOffset].getData()) &&
                            grid[i][j].getData().equals(grid[i + (2 * rowOffset)][j + (2 * colOffset)].getData())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    /**
     * Creates and returns a list of all of the violations in a MarupekeGrid
     * @return A list of the violations in a MarupekeGrid
     */
    public List<Reason> illegalitiesInGrid(){
        List<Reason> illegalities = new ArrayList<>();
        //check horizontal violations
        illegalities.addAll(getIllegalities(new int[]{0, getGridSize()}, new int[]{0, getGridSize() - 2},
                0, 1, "horizontal"));
        //check vertical violations
        illegalities.addAll(getIllegalities(new int[]{0, getGridSize() - 2}, new int[]{0, getGridSize()},
                1, 0, "vertical"));
        //check diagonal decline
        illegalities.addAll(getIllegalities(new int[]{0, getGridSize() - 2}, new int[]{0, getGridSize() - 2},
                1, 1, "diagonalIncline"));
        //check diagonal incline
        illegalities.addAll(getIllegalities(new int[]{2, getGridSize()}, new int[]{0, (getGridSize() - 2)},
                -1, 1, "diagonalDecline"));
        return illegalities;
    }
    //Checks tiles for illegalities based on the parameters provided, returning a list of the illegalities found
    private List<Reason> getIllegalities(int[] rowBound, int[] colBound, int rowOffset, int colOffset, String vType){
        List<Reason> illegalities = new ArrayList<>();
        for (int i = rowBound[0]; i < rowBound[1]; i++) {
            for (int j = colBound[0]; j < colBound[1]; j++) {
                if (!grid[i][j].getData().equals(Type.BLANK) && !grid[i][j].getData().equals(Type.FILLED)) {
                    if (grid[i][j].getData().equals(grid[i + rowOffset][j + colOffset].getData()) &&
                            grid[i][j].getData().equals(grid[i + (2 * rowOffset)][j + (2 * colOffset)].getData())) {
                        switch (vType){
                            case "horizontal":
                                illegalities.add(new HorizontalViolation(new Reason.Coordinate(i, j), new Reason.Coordinate(i, j + 2), grid[i][j].getData()));
                                break;
                            case "vertical":
                                illegalities.add(new VerticalViolation(new Reason.Coordinate(i, j), new Reason.Coordinate(i + 2, j), grid[i][j].getData()));
                                break;
                            case "diagonalIncline":
                                illegalities.add(new DiagonalViolation(new Reason.Coordinate(i, j), new Reason.Coordinate(i + 2, j + 2), grid[i][j].getData()));
                                break;
                            default:
                                illegalities.add(new DiagonalViolation(new Reason.Coordinate(i, j), new Reason.Coordinate(i - 2, j + 2), grid[i][j].getData()));
                                break;
                        }
                    }
                }
            }
        }
        return illegalities;
    }
    /**
     * returns true if the puzzle is legal and all tiles are marked
     * @return true if the puzzle is legal and all tiles are marked, otherwise false
     */
    public boolean isPuzzleComplete(){
        if (!isLegalGrid()){return false;}
        else {
            for (MarupekeTile[] tiles : grid) {
                for (MarupekeTile aTile : tiles) {
                    if (aTile.getData() == Type.BLANK) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    /**
     * Creates a string representation of the MarupekeGrid that can be printed to the console.
     *
     * @return String representation of the MarupekeGrid.
     */
    @Override
    public String toString() {
        int rowCount = 0;
        StringBuilder boardPrint = new StringBuilder();
        boardPrint.append("R\n");
        for (MarupekeTile[] tiles : grid) {
            boardPrint.append(getGridSize() - rowCount - 1).append(" ");
            rowCount++;
            for (MarupekeTile aTile : tiles) {
                boardPrint.append(aTile.toString()).append(" ");
            }
            //Adds new-line character (\n) to the string at the end of every row in the board
            boardPrint.append("\n");
        }
        boardPrint.append("  ");
        for (int i = 0; i < getGridSize(); i++) {
            boardPrint.append(i).append(" ");
        }
        boardPrint.append("C\n");
        return boardPrint.toString();
    }
    /**
     * Marks a tile in the grid with an X if it is editable
     * @param x x Coordinate of the tile to be marked
     * @param y y coordinate of the tile to be marked
     */
    public void markX(int x, int y){ setX(x, y, true); }
    /**
     * Marks a tile in the grid with an O if it is editable
     * @param x x Coordinate of the tile to be marked
     * @param y y coordinate of the tile to be marked
     */
    public void markO(int x, int y){ setO(x, y, true); }
    /**
     * Sets a square in the MarupekeGrid to solid (represented by the character '#') and returns whether the 'square'
     * was originally editable or not.
     *
     * @param x The x coordinate of the square to be changed.
     * @param y The Y coordinate of the square to be changed.
     */
    public void setSolid(int x, int y) {
        setGridType(x, y, Type.FILLED, false);
    }
    /**
     * Sets a square in the MarupekeGrid to X and returns whether the square was originally editable or not. Sets the
     * square as editable or non-editable.
     *
     * @param x       The x coordinate of the square to be changed.
     * @param y       The Y coordinate of the square to be changed.
     * @param canEdit True to set the square as editable, false to set the square as non-editable.
     * @return Whether the square was originally editable.
     */
    public boolean setX(int x, int y, boolean canEdit) {return setGridType(x, y, Type.X, canEdit);
    }
    /**
     * Sets a square in the MarupekeGrid to O and returns whether the 'square' was originally editable or not. Sets the
     * square as editable or non-editable.
     *
     * @param x       The x coordinate of the square to be changed.
     * @param y       The Y coordinate of the square to be changed.
     * @param canEdit True to set the square as editable, false to set the square as non-editable.
     * @return Whether the square was originally editable.
     */
    public boolean setO(int x, int y, boolean canEdit) { return setGridType(x, y, Type.O, canEdit); }
    /**
     * Unmarks a tile in the grid if it is editable, setting it to blank
     * @param x x coordinate of the tile to be unmarked
     * @param y y coordinate of the tile to be unmarked
     * @return true if tile was originally editable, false otherwise
     */
    public Boolean unmark(int x, int y){ return setGridType(x, y, Type.BLANK, true); }
    /**
     * Checks if the selected square in the MarupekeGrid is solid.
     *
     * @param x x coordinate of square.
     * @param y y coordinate of square.
     * @return True if the square is solid, otherwise false.
     */
    public boolean isSolid(int x, int y) {return (grid[x][y].getData() == Type.FILLED);}
    /**
     * Checks if the selected square in the MarupekeGrid is editable
     *
     * @param x x coordinate of square.
     * @param y y coordinate of square.
     * @return true if the square is editable, otherwise false.
     */
    public boolean isEditable(int x, int y) {return (grid[x][y].getEditable());}
    /**
     * Gets the character stored in the selected square in the MarupekeGrid.
     *
     * @param x x coordinate of square.
     * @param y y coordinate of square.
     * @return The character stored in the selected square.
     */
    public Type getValueAt(int x, int y) {return grid[x][y].getData();}
    /**
     * Returns the number of filled squares ('#') in the MarupekeGrid.
     *
     * @return the number of filled squares in the MarupekeGrid.
     */
    public int numOfFilled() {return countInGrid(Type.FILLED);}
    /**
     * Returns the number of squares containing X in the MarupekeGrid.
     *
     * @return The number of squares containing X.
     */
    public int numOfX() {return countInGrid(Type.X);}
    /**
     * Returns the number of squares containing O in the MarupekeGrid.
     *
     * @return The number of squares containing O.
     */
    public int numOfO() {return countInGrid(Type.O);}

    /**
     * Returns the number of a specified element in the MarupekeGrid.
     *
     * @param element The element to be counted.
     * @return The number of the specified element.
     */
    public int countInGrid(Type element) {
        int total = 0;
        for (MarupekeTile[] tiles : grid) {
            for (MarupekeTile aTile : tiles) {
                if (aTile.getData().equals(element)) {
                    total++;
                }
            }
        }
        return total;
    }
    /**
     * Ensures the provided value is within a specified range. If the value is above the range, it is converted to the
     * upper limit, if it is bellow, it is converted to the lower limit.
     *
     * @param value The value to be checked.
     * @param upper The upper of the range.
     * @param lower The lower limit of the range
     * @return The number, set to the range bounds if necessary, otherwise the same.
     */
    private int setToRange(int value, int upper, int lower) {
        if (value > upper) {
            value = upper;
        } else if (value < lower) {
            value = lower;
        }
        return value;
    }
    //Sets a selected square in the grid to a chosen value, and changes it's equivalent square in the editable table.
    private boolean setGridType(int x, int y, Type T, boolean changeEdit) {
        boolean formerState = grid[x][y].getEditable();
        //if grid x, y in editable contains the value 'true'
        if (formerState) {
            grid[x][y].setData(T);
            grid[x][y].setEditable(changeEdit);
        }
        return formerState;
    }
    /**
     * Returns the size of the MarupekeGrid.
     *
     * @return The size of the MarupekeGrid.
     */
    public int getGridSize() {
        if(grid[0].length == grid.length){
            return grid.length;
        }
        else{
            throw new RuntimeException("Grid length and grid with do not match");
        }
    }
    /**
     * setter for the gui
     * @return the gui
     */
    public MarupekeGUI getGui() {
        return gui;
    }
    /**
     * getter for the gui
     * @param gui the models gui
     */
    public void setGui(MarupekeGUI gui) {
        this.gui = gui;
    }

    /**
     * Inner class for an exception that will be thrown if there are too many marked squares in randomPuzzle
     */
    public static class TooManyMarkedSquaresException extends Exception{
        /**
         * Constructor for TooManyMarkedSquaresException
         * @param s String passed to detail the error
         */
        TooManyMarkedSquaresException(String s){
            super(s);
        }
    }
}