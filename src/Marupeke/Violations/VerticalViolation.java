package Marupeke.Violations;

import ModelComponents.Type;

/**
 * Class representing a vertical violation in a MarupekeGrid
 * @author Cand#215730
 * @version 1.0
 */
public class VerticalViolation extends Reason {
    /**
     * Constructor for VerticalViolation
     * @param start Coordinates of the first tile of the violation
     * @param end Coordinates of the last tile of the violation
     * @param type The character in the grid causing the violation
     */
    public VerticalViolation(Coordinate start, Coordinate end, Type type){
        super(start, end, type);}
    /**
     * returns a string detailing the violation
     * @return String detailing the violation
     */
    @Override
    public String toString(){
        return "Vertical violation: 3 " + type.toString() + "s starting at " + start + " and ending at " + end;
    }
}