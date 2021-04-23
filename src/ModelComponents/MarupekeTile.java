package ModelComponents;

import java.io.Serializable;

/**
 * Class representing a tile in a Marupeke game grid.
 * @author Cand#215730
 * @version 2.0
 */
public class MarupekeTile implements Serializable {
    private Boolean editable;
    private Type data;
    private Boolean searchSetEditable;

    /**
     * Constructor for MarupekeTile Objects
     */
    public MarupekeTile(){
        editable = true;
        data = Type.BLANK;
        searchSetEditable = false;
    }

    /**
     * Returns a string representation of the data in the tile. For X and O, a bold character will be returned if they
     * are editable
     * @return string representation of the data in the tile
     */
    @Override
    public String toString(){
        String returnString;
        switch (data){
            case X:
                returnString = editable ? "\033[0;1mX\033[0;0m" : "X";
                break;
            case O:
                returnString = editable ? "\033[0;1mO\033[0;0m" : "O";
                break;
            case FILLED:
                returnString = "#";
                break;
            default:
                returnString = "_";
        }
        return returnString;
    }
    /**
     * the data stored in the tile
     * @return data stored in the tile
     */
    public Type getData() {
        return data;
    }

    /**
     * Setter for data
     * @param data Type to be stored in data
     */
    public void setData(Type data) {
        this.data = data;
    }
    /**
     * gets the editable state of the tile
     * @return editable state of the tile
     */
    public Boolean getEditable() {
        return editable;
    }
    /**
     * setter for editable
     * @param editable boolean to be stored in editable
     */
    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    /**
     * get the searchSet editable value, used by the DFS
     * @return searchSet value
     */
    public Boolean getSearchSetEditable() {
        return searchSetEditable;
    }
    /**
     * set the searchSet editable value, used by the DFS
     * @param searchSetEditable new searchSet value
     */
    public void setSearchSetEditable(Boolean searchSetEditable) {
        this.searchSetEditable = searchSetEditable;
    }
}