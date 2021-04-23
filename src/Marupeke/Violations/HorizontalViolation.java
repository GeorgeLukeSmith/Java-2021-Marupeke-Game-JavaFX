package Marupeke.Violations;

import ModelComponents.Type;

/**
 * Class representing a horizontal violation in a MarupekeGrid
 * @author Cand#215730
 * @version 1.0
 */
public class HorizontalViolation extends Reason {
    /**
     * Constructor for HorizontalViolation
     * @param start Coordinates of the first tile of the violation
     * @param end Coordinates of the last tile of the violation
     * @param type The character in the grid causing the violation
     */
    public HorizontalViolation(Coordinate start, Coordinate end, Type type){
        super(start, end, type);
    }
    /**
     * returns a string detailing the violation
     * @return String detailing the violation
     */
    @Override
    public String toString(){
        return "Horizontal violation: 3 " + type.toString() + "s starting at " + start + " and ending at " + end;
    }
}