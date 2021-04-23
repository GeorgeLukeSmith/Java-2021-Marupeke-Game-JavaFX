package Marupeke.Violations;

import ModelComponents.Type;

/**
 * Abstract Class representing a violation in a MarupekeGrid
 * @author Cand#215730
 * @version 2.0
 */

public abstract class Reason {
    protected Coordinate start;
    protected Coordinate end;
    protected Type type;

    /**
     * Constructor for Reason
     * @param start Coordinates of the first tile of the violation
     * @param end Coordinates of the last tile of the violation
     * @param type The character in the grid causing the violation
     */
    public Reason(Coordinate start, Coordinate end, Type type){
        this.start = start;
        this.end = end;
        this.type = type;
    }

    /**
     * returns a string detailing the violation
     * @return String detailing the violation
     */
    @Override
    public abstract String toString();

    /**
     * Getter for the first tile in the violation
     * @return the coordinate value of the first tile in the violation
     */
    public Coordinate getStart() {
        return start;
    }

    /**
     * Sets the start value of the violation
     * @param start A coordinate value for the first tile in the violation
     */
    public void setStart(Coordinate start) {
        this.start = start;
    }

    public Coordinate getEnd() {
        return end;
    }

    public void setEnd(Coordinate end) {
        this.end = end;
    }

    public Type getType() {
        return type;
    }
    //Inner class representing violation coordinates
    public static class Coordinate {
        private int x;
        private int y;

        /**
         * Constructor for Coordinate objects
         * @param x x value of the coordinate
         * @param y y value of the coordinate
         */
        public Coordinate(int x, int y){
            this.x = x;
            this.y = y;
        }

        /**
         * toString method for Coordinate objects
         * @return a String representation of the coordinate
         */
        public String toString(){
            return "[" + x + ", " + y + "]";
        }

        /**
         * getter for the x value of the coordinate
         * @return the x value of the coordinate
         */
        public int getX() {
            return x;
        }

        /**
         * setter for the x value of the coordinate
         * @param x new x value for the coordinate
         */
        public void setX(int x) {
            this.x = x;
        }

        /**
         * getter for the y value of the coordinate
         * @return the y value of the coordinate
         */
        public int getY() {
            return y;
        }

        /**
         * setter for the y value of the coordinate
         * @param y new y value for the coordinate
         */
        public void setY(int y) {
            this.y = y;
        }

    }
}