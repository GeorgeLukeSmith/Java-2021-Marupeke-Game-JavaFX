package Marupeke.Violations;

import ModelComponents.Type;

/**
 * Class representing a diagonal violation in a MarupekeGrid
 * @author Cand#215730
 * @version 2.0
 */

public class DiagonalViolation extends Reason {
    /**
     * Constructor for DiagonalViolation
     * @param start Coordinates of the first tile of the violation
     * @param end Coordinates of the last tile of the violation
     * @param type The character in the grid causing the violation
     */
    public DiagonalViolation(Coordinate start, Coordinate end, Type type){
        super(start, end, type);
    }
    /**
     * returns a string detailing the violation
     * @return String detailing the violation
     */
    @Override
    public String toString(){
        //correcting the xIndex to match the printed game board
        return "Diagonal violation: 3 " + type.toString() + "s starting at " + start + " and ending at " + end;
    }
}