import Marupeke.Violations.Reason;
import ModelComponents.Type;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
/**
 * Test class for MarupekeGrid.
 *
 * @author Cand#215730
 * @version 1.0
 */
public class MarupekeGridTest {
    //Constants for the string representing bold characters
    String BOLD_X = "[0;1mX[0;0m";
    String BOLD_O = "[0;1mO[0;0m";
    //Four MarupekeGrids of various sizes for testing purposes
    MarupekeModel myMarupekeGridSize10;
    MarupekeModel myMarupekeGridSize9;
    MarupekeModel myMarupekeGridSize6;
    MarupekeModel myMarupekeGridSize3;

    //Initialises the four test grids
    @Before
    public void setUp() {
        myMarupekeGridSize10 = new MarupekeModel(10);
        myMarupekeGridSize9 = new MarupekeModel(9);
        myMarupekeGridSize6 = new MarupekeModel(6);
        myMarupekeGridSize3 = new MarupekeModel(3);
    }

    //TESTS FOR ASSIGNMENT 1
    //Tests for part two begin on line 306
    //SIZE AND INITIALISATION TESTS
    //tests for various MarupekeGrid sizes
    @Test
    public void testMarupekeGrid9() {
        assertEquals(9, myMarupekeGridSize9.getGridSize());
    }

    @Test
    public void testMarupekeGrid3() {
        assertEquals(3, myMarupekeGridSize3.getGridSize());
    }

    //Tests that a grid with a length or width smaller than 3 is corrected
    @Test
    public void testMarupekeGridSmallerThan3() {
        MarupekeModel smallerThan3Grid = new MarupekeModel(2);
        assertEquals(3, smallerThan3Grid.getGridSize());
    }

    //Tests that a grid with a length or width bigger than 10 is corrected
    @Test
    public void testMarupekeGridBiggerThan10() {
        MarupekeModel biggerThan10Grid = new MarupekeModel(11);
        assertEquals(10, biggerThan10Grid.getGridSize());
    }

    //Tests that squares in the grid are editable by default
    @Test
    public void testEditableByDefault() {
        assertTrue(myMarupekeGridSize9.isEditable(3, 4));
    }

    //TESTS FOR setSolid()
    //Tests that a normal input fills squares correctly
    @Test
    public void testSetSolidNormalInput() {
        myMarupekeGridSize9.setSolid(2, 3);
        assertTrue(myMarupekeGridSize9.isSolid(2, 3));
        assertFalse(myMarupekeGridSize9.isEditable(2, 3));
    }

    //Tests all edge and corner squares of a grid are filled correctly
    @Test
    public void testSetSolidEdgeCases() {
        myMarupekeGridSize3.setSolid(0, 0);
        myMarupekeGridSize3.setSolid(0, 1);
        myMarupekeGridSize3.setSolid(0, 2);
        myMarupekeGridSize3.setSolid(1, 0);
        myMarupekeGridSize3.setSolid(1, 2);
        myMarupekeGridSize3.setSolid(2, 0);
        myMarupekeGridSize3.setSolid(2, 1);
        myMarupekeGridSize3.setSolid(2, 2);
        assertEquals("R\n2 # # # \n1 # _ # \n0 # # # \n  0 1 2 C\n", myMarupekeGridSize3.toString());
    }

    //Tests that every square in the grid can be correctly set
    @Test
    public void testSetSolidFullGrid() {
        for (int i = 0; i < myMarupekeGridSize3.getGridSize(); i++) {
            for (int j = 0; j < myMarupekeGridSize3.getGridSize(); j++) {
                myMarupekeGridSize3.setSolid(i, j);
            }
        }
        assertEquals("R\n2 # # # \n1 # # # \n0 # # # \n  0 1 2 C\n", myMarupekeGridSize3.toString());
    }

    //Tests that providing a coordinate that is out of bounds returns an error
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetSolidOutOfBounds() {
        myMarupekeGridSize9.setSolid(14, 11);
    }

    //TESTS FOR setX()
    //Tests that a normal input fills squares correctly if square is originally editable and a false parameter is provided
    @Test
    public void testSetXNormalInputFalseValue() {
        myMarupekeGridSize9.setX(5, 2, false);
        assertFalse(myMarupekeGridSize9.isEditable(5, 2));
        assertEquals(Type.X, myMarupekeGridSize9.getValueAt(5, 2));
    }

    //Tests that the method returns a true value when expected
    @Test
    public void testSetXNormalInputOriginallyEditable() {
        assertTrue(myMarupekeGridSize9.setX(5, 2, false));
    }

    //Tests that the method returns a false value when expected
    @Test
    public void testSetXNormalInputOriginallyUneditable() {
        myMarupekeGridSize9.setX(5, 2, false);
        assertFalse(myMarupekeGridSize9.setX(5, 2, false));
    }

    //Tests all edge and corner squares of a grid are filled correctly
    @Test
    public void testSetXEdgeCases() {
        myMarupekeGridSize3.setX(0, 0, false);
        myMarupekeGridSize3.setX(0, 1, false);
        myMarupekeGridSize3.setX(0, 2, false);
        myMarupekeGridSize3.setX(1, 0, false);
        myMarupekeGridSize3.setX(1, 2, false);
        myMarupekeGridSize3.setX(2, 0, false);
        myMarupekeGridSize3.setX(2, 1, false);
        myMarupekeGridSize3.setX(2, 2, false);
        assertEquals("R\n2 X X X \n1 X _ X \n0 X X X \n  0 1 2 C\n", myMarupekeGridSize3.toString());
    }

    //Tests that every square in the grid can be correctly set
    @Test
    public void testSetXFullGrid() {
        for (int i = 0; i < myMarupekeGridSize3.getGridSize(); i++) {
            for (int j = 0; j < myMarupekeGridSize3.getGridSize(); j++) {
                myMarupekeGridSize3.setX(i, j, false);
            }
        }
        assertEquals("R\n2 X X X \n1 X X X \n0 X X X \n  0 1 2 C\n", myMarupekeGridSize3.toString());
    }

    //Tests that providing a coordinate that is out of bounds returns an error
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetXOutOfBounds() {
        myMarupekeGridSize9.setX(14, 36, false);
    }

    //TESTS FOR setO()
    //Tests that a normal input fills squares correctly if square is originally editable and a false parameter is provided
    @Test
    public void testSetONormalInputFalseValue() {
        myMarupekeGridSize9.setO(5, 2, false);
        assertFalse(myMarupekeGridSize9.isEditable(5, 2));
        assertEquals(Type.O, myMarupekeGridSize9.getValueAt(5, 2));
    }

    //Tests that the method returns a true value when expected
    @Test
    public void testSetONormalInputOriginallyEditable() {
        assertTrue(myMarupekeGridSize9.setO(5, 2, false));
    }

    //Tests that the method returns a false value when expected
    @Test
    public void testSetONormalInputOriginallyUneditable() {
        myMarupekeGridSize9.setO(5, 2, false);
        assertFalse(myMarupekeGridSize9.setO(5, 2, false));
    }

    //Tests all edge and corner squares of a grid are filled correctly
    @Test
    public void testSetOEdgeCases() {
        myMarupekeGridSize3.setO(0, 0, false);
        myMarupekeGridSize3.setO(0, 1, false);
        myMarupekeGridSize3.setO(0, 2, false);
        myMarupekeGridSize3.setO(1, 0, false);
        myMarupekeGridSize3.setO(1, 2, false);
        myMarupekeGridSize3.setO(2, 0, false);
        myMarupekeGridSize3.setO(2, 1, false);
        myMarupekeGridSize3.setO(2, 2, false);
        assertEquals("R\n2 O O O \n1 O _ O \n0 O O O \n  0 1 2 C\n", myMarupekeGridSize3.toString());
    }

    //Tests that every square in the grid can be correctly set
    @Test
    public void testSetOFullGrid() {
        for (int i = 0; i < myMarupekeGridSize3.getGridSize(); i++) {
            for (int j = 0; j < myMarupekeGridSize3.getGridSize(); j++) {
                myMarupekeGridSize3.setO(i, j, false);
            }
        }
        assertEquals("R\n2 O O O \n1 O O O \n0 O O O \n  0 1 2 C\n", myMarupekeGridSize3.toString());
    }

    //Tests that providing a coordinate that is out of bounds returns an error
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetOOutOfBounds() {
        myMarupekeGridSize9.setO(14, 36, false);
    }

    //TESTS FOR randomPuzzle()
    //Tests for a randomly generated puzzle of size 10, with all relevant chars tested
    /* Test takes too long due to the complexity class of randomPuzzle
    @Test
    public void testRandomPuzzleSize10() throws MarupekeModel.TooManyMarkedSquaresException {
        MarupekeModel randomMarupekeGrid = MarupekeModel.randomPuzzle(10, 5, 8, 11);
        assertEquals(10, randomMarupekeGrid.getGridSize());
        assertEquals(5, randomMarupekeGrid.numOfFilled());
        assertEquals(8, randomMarupekeGrid.numOfX());
        assertEquals(11, randomMarupekeGrid.numOfO());
    }*/

    //Tests for a randomly generated puzzle of size 6, with all relevant chars tested
    @Test
    public void testRandomPuzzleSize6() throws MarupekeModel.TooManyMarkedSquaresException {
        MarupekeModel randomMarupekeGrid = MarupekeModel.randomPuzzle(6, 4, 9, 3);
        assertEquals(6, randomMarupekeGrid.getGridSize());
        assertEquals(4, randomMarupekeGrid.numOfFilled());
        assertEquals(9, randomMarupekeGrid.numOfX());
        assertEquals(3, randomMarupekeGrid.numOfO());
    }

    //Tests for a randomly generated puzzle of size 3, with all relevant chars tested
    @Test
    public void testRandomPuzzleSize3() throws MarupekeModel.TooManyMarkedSquaresException {
        MarupekeModel randomMarupekeGrid = MarupekeModel.randomPuzzle(3, 1, 2, 1);
        assertEquals(3, randomMarupekeGrid.getGridSize());
        assertEquals(1, randomMarupekeGrid.numOfFilled());
        assertEquals(2, randomMarupekeGrid.numOfX());
        assertEquals(1, randomMarupekeGrid.numOfO());
    }
    //Note: Test for returns null removed as TooManyMarkedSquaresException prevents it from happening

    //TESTS FOR toString()
    //Tests printing a 3 by 3 grid with all relevant chars
    @Test
    public void testToStringSize3() {
        MarupekeModel testPrintGrid = new MarupekeModel(3);
        testPrintGrid.setSolid(0, 1);
        testPrintGrid.setO(1, 0, false);
        testPrintGrid.setX(2, 2, false);
        String expectedOutput = "R\n2 _ # _ \n1 O _ _ \n0 _ _ X \n  0 1 2 C\n";
        assertEquals(expectedOutput, testPrintGrid.toString());

    }

    //Tests printing a 5 by 5 grid with all relevant chars
    @Test
    public void testTtoStringSize5() {
        MarupekeModel testPrintGrid = new MarupekeModel(5);
        testPrintGrid.setX(0, 2, false);
        testPrintGrid.setO(1, 3, false);
        testPrintGrid.setSolid(2, 2);
        testPrintGrid.setO(3, 1, false);
        testPrintGrid.setX(4, 0, false);
        String expectedOutput = "R\n4 _ _ X _ _ \n3 _ _ _ O _ \n2 _ _ # _ _ \n1 _ O _ _ _ \n0 X _ _ _ _ \n  0 1 2 3 4 C\n";
        assertEquals(expectedOutput, testPrintGrid.toString());
    }

    //Test specifically focussing on edge and corner squares
    @Test
    public void toStringEdgeCases() {
        MarupekeModel testPrintGrid = new MarupekeModel(4);
        testPrintGrid.setSolid(0, 0);
        testPrintGrid.setX(0, 1, false);
        testPrintGrid.setO(0, 2, false);
        testPrintGrid.setSolid(0, 3);
        testPrintGrid.setX(1, 0, false);
        testPrintGrid.setO(1, 3, false);
        testPrintGrid.setSolid(2, 0);
        testPrintGrid.setX(2, 3, false);
        testPrintGrid.setO(3, 0, false);
        testPrintGrid.setSolid(3, 1);
        testPrintGrid.setX(3, 2, false);
        testPrintGrid.setO(3, 3, false);
        String expectedOutput = "R\n3 # X O # \n2 X _ _ O \n1 # _ _ X \n0 O # X O \n  0 1 2 3 C\n";
        assertEquals(expectedOutput, testPrintGrid.toString());
    }

    //TESTS FOR EXTRA METHODS
    //tests for countInGrid();
    @Test
    public void testCountInGrid() {
        MarupekeModel localGrid = new MarupekeModel(5);
        localGrid.setX(0, 2, false);
        localGrid.setO(1, 3, false);
        localGrid.setSolid(2, 2);
        localGrid.setX(3, 1, false);
        localGrid.setX(4, 0, false);
        assertEquals(3, localGrid.countInGrid(Type.X));
    }

    //TESTS FOR ASSIGNMENT 2
    //TESTS FOR isLegalGrid()
    @Test
    public void testDetectsLegalSmallGrid() {
        myMarupekeGridSize3.setO(0, 0, true);
        myMarupekeGridSize3.setX(1, 2, true);
        myMarupekeGridSize3.setSolid(0, 2);
        myMarupekeGridSize3.setX(2, 1, true);
        assertTrue(myMarupekeGridSize3.isLegalGrid());
    }

    @Test
    public void testDetectsIllegalSmallGrid() {
        myMarupekeGridSize3.setO(1, 0, true);
        myMarupekeGridSize3.setO(1, 1, true);
        myMarupekeGridSize3.setO(1, 2, true);
        assertFalse(myMarupekeGridSize3.isLegalGrid());
    }

    @Test
    public void testDetectsLegalMediumGrid() {
        myMarupekeGridSize6.setX(0, 0, true);
        myMarupekeGridSize6.setX(0, 1, true);
        myMarupekeGridSize6.setO(0, 2, true);
        myMarupekeGridSize6.setO(0, 3, true);
        myMarupekeGridSize6.setSolid(3, 2);
        myMarupekeGridSize6.setX(5, 4, true);
        myMarupekeGridSize6.setO(5, 5, true);
        assertTrue(myMarupekeGridSize6.isLegalGrid());
    }

    @Test
    public void testDetectsIllegalMediumGrid() {
        myMarupekeGridSize6.setX(0, 0, true);
        myMarupekeGridSize6.setX(0, 3, true);
        myMarupekeGridSize6.setO(0, 2, true);
        myMarupekeGridSize6.setO(0, 3, true);
        myMarupekeGridSize6.setSolid(3, 2);
        myMarupekeGridSize6.setX(5, 4, true);
        myMarupekeGridSize6.setO(5, 5, true);
        myMarupekeGridSize6.setO(4, 4, true);
        myMarupekeGridSize6.setO(3, 3, true);
        assertFalse(myMarupekeGridSize6.isLegalGrid());
    }

    @Test
    public void testDetectsLegalLargeGrid() {
        myMarupekeGridSize10.setX(2, 3, true);
        myMarupekeGridSize10.setX(2, 5, true);
        myMarupekeGridSize10.setX(1, 8, true);
        myMarupekeGridSize10.setX(9, 3, true);
        myMarupekeGridSize10.setX(0, 4, true);
        myMarupekeGridSize10.setO(8, 4, true);
        myMarupekeGridSize10.setO(3, 9, true);
        myMarupekeGridSize10.setO(2, 2, true);
        myMarupekeGridSize10.setO(7, 6, true);
        myMarupekeGridSize10.setO(2, 0, true);
        assertTrue(myMarupekeGridSize6.isLegalGrid());
    }

    @Test
    public void testDetectsIllegalLargeGrid() {
        myMarupekeGridSize10.setX(2, 3, true);
        myMarupekeGridSize10.setX(2, 5, true);
        myMarupekeGridSize10.setX(1, 8, true);
        myMarupekeGridSize10.setX(9, 3, true);
        myMarupekeGridSize10.setX(0, 4, true);
        myMarupekeGridSize10.setO(8, 4, true);
        myMarupekeGridSize10.setO(3, 9, true);
        myMarupekeGridSize10.setO(3, 8, true);
        myMarupekeGridSize10.setO(3, 7, true);
        myMarupekeGridSize10.setO(2, 0, true);
        assertFalse(myMarupekeGridSize10.isLegalGrid());
    }

    //TESTS FOR illegalitiesInGrid()
    @Test
    public void testIllegalitiesInGridVHorizontalX1() {
        myMarupekeGridSize6.setX(2, 0, true);
        myMarupekeGridSize6.setX(2, 1, true);
        myMarupekeGridSize6.setX(2, 2, true);
        List<Reason> localReasons = myMarupekeGridSize6.illegalitiesInGrid();
        assertEquals("Horizontal violation: 3 Xs starting at index (0, 3)", localReasons.get(0).toString());
    }

    @Test
    public void testIllegalitiesInGridHorizontalX2() {
        myMarupekeGridSize6.setO(5, 3, true);
        myMarupekeGridSize6.setO(5, 4, true);
        myMarupekeGridSize6.setO(5, 5, true);
        List<Reason> localReasons = myMarupekeGridSize6.illegalitiesInGrid();
        assertEquals("Horizontal violation: 3 Os starting at index (3, 0)", localReasons.get(0).toString());
    }

    @Test
    public void testIllegalitiesInGridVerticalX1() {
        myMarupekeGridSize6.setX(2, 4, true);
        myMarupekeGridSize6.setX(3, 4, true);
        myMarupekeGridSize6.setX(4, 4, true);
        List<Reason> localReasons = myMarupekeGridSize6.illegalitiesInGrid();
        assertEquals("Vertical violation: 3 Xs starting at index (4, 3)", localReasons.get(0).toString());

    }

    @Test
    public void testIllegalitiesInGridVerticalX2() {
        myMarupekeGridSize6.setO(1, 1, true);
        myMarupekeGridSize6.setO(1, 2, true);
        myMarupekeGridSize6.setO(1, 3, true);
        List<Reason> localReasons = myMarupekeGridSize6.illegalitiesInGrid();
        assertEquals("Horizontal violation: 3 Os starting at index (1, 4)", localReasons.get(0).toString());
    }

    @Test
    public void testIllegalitiesInGridDiagonalX1() {
        myMarupekeGridSize6.setX(2, 2, true);
        myMarupekeGridSize6.setX(3, 3, true);
        myMarupekeGridSize6.setX(4, 4, true);
        List<Reason> localReasons = myMarupekeGridSize6.illegalitiesInGrid();
        assertEquals("Diagonal violation: 3 Xs starting at index (2, 3)", localReasons.get(0).toString());
    }

    @Test
    public void testIllegalitiesInGridDiagonalX2() {
        myMarupekeGridSize6.setO(1, 5, true);
        myMarupekeGridSize6.setO(2, 4, true);
        myMarupekeGridSize6.setO(3, 3, true);
        List<Reason> localReasons = myMarupekeGridSize6.illegalitiesInGrid();
        assertEquals("Diagonal violation: 3 Os starting at index (3, 2)", localReasons.get(0).toString());
    }

    @Test
    public void testTwoViolations1() {
        myMarupekeGridSize6.setX(0, 0, true);
        myMarupekeGridSize6.setX(1, 0, true);
        myMarupekeGridSize6.setX(2, 0, true);
        myMarupekeGridSize6.setX(2, 1, true);
        myMarupekeGridSize6.setX(2, 2, true);
        List<Reason> localReasons = myMarupekeGridSize6.illegalitiesInGrid();
        String testString = localReasons.get(0).toString() + "\n" + localReasons.get(1).toString();
        assertEquals("Horizontal violation: 3 Xs starting at index (0, 3)\n" +
        "Vertical violation: 3 Xs starting at index (0, 5)", testString);
    }

    @Test
    public void testTwoViolations2() {
        myMarupekeGridSize6.setO(5, 5, true);
        myMarupekeGridSize6.setO(4, 4, true);
        myMarupekeGridSize6.setO(3, 3, true);
        myMarupekeGridSize6.setO(3, 4, true);
        myMarupekeGridSize6.setO(3, 5, true);
        List<Reason> localReasons = myMarupekeGridSize6.illegalitiesInGrid();
        String testString = localReasons.get(0).toString() + "\n" + localReasons.get(1).toString();
        assertEquals("Horizontal violation: 3 Os starting at index (3, 2)\n" +
                "Diagonal violation: 3 Os starting at index (3, 2)", testString);
    }

    //TESTS FOR MODIFIED randomPuzzle()
    @Test
    public void testReturnsLegalGridSmall() throws MarupekeModel.TooManyMarkedSquaresException {
        MarupekeModel randomTest = MarupekeModel.randomPuzzle(3, 2, 1, 1);
        assertTrue(randomTest.isLegalGrid());
    }

    @Test
    public void testReturnsLegalGridMedium() throws MarupekeModel.TooManyMarkedSquaresException {
        MarupekeModel randomTest = MarupekeModel.randomPuzzle(6, 3, 6, 4);
        assertTrue(randomTest.isLegalGrid());
    }
    /* Test takes too long due to the complexity class of randomPuzzle
    @Test
    public void testReturnsLegalGridLarge() throws MarupekeModel.TooManyMarkedSquaresException {
        MarupekeModel randomTest = MarupekeModel.randomPuzzle(10, 10, 20, 18);
        assertTrue(randomTest.isLegalGrid());
    }*/

    @Test(expected = MarupekeModel.TooManyMarkedSquaresException.class)
    public void testRandomGridTooManyMarkedSquaresExceptionThrownSmallGrid() throws MarupekeModel.TooManyMarkedSquaresException {
        MarupekeModel randomTest = MarupekeModel.randomPuzzle(3, 4, 5, 6);
    }

    @Test(expected = MarupekeModel.TooManyMarkedSquaresException.class)
    public void testRandomGridTooManyMarkedSquaresExceptionThrownMediumGrid() throws MarupekeModel.TooManyMarkedSquaresException {
        MarupekeModel randomTest = MarupekeModel.randomPuzzle(6, 10, 12, 8);
    }

    @Test(expected = MarupekeModel.TooManyMarkedSquaresException.class)
    public void testRandomGridTooManyMarkedSquaresExceptionThrownLargeGrid() throws MarupekeModel.TooManyMarkedSquaresException {
        MarupekeModel randomTest = MarupekeModel.randomPuzzle(10, 25, 16, 11);
    }

    //TESTS FOR markX()
    @Test
    public void testMarkXNormal() {
        myMarupekeGridSize6.markX(3, 3);
        myMarupekeGridSize6.markX(2, 1);
        assertEquals("R\n5 _ _ _ _ _ _ \n4 _ _ _ _ _ _ \n3 _ " + BOLD_X + " _ _ _ _ \n2 _ _ _ " + BOLD_X +
                " _ _ \n1 _ _ _ _ _ _ \n0 _ _ _ _ _ _ \n  0 1 2 3 4 5 C\n", myMarupekeGridSize6.toString());
    }

    @Test
    public void testMarkXCorners() {
        myMarupekeGridSize6.markX(0, 0);
        myMarupekeGridSize6.markX(0, 5);
        myMarupekeGridSize6.markX(5, 0);
        myMarupekeGridSize6.markX(5, 5);
        assertEquals("R\n5 " + BOLD_X + " _ _ _ _ " + BOLD_X + " \n4 _ _ _ _ _ _ \n3 _ _ _ _ _ _ \n" +
                "2 _ _ _ _ _ _ \n1 _ _ _ _ _ _ \n0 " + BOLD_X + " _ _ _ _ " + BOLD_X + " \n  0 1 2 3 4 5 C\n",
                myMarupekeGridSize6.toString());
    }

    @Test
    public void testMarkXEdges() {
        myMarupekeGridSize6.markX(0, 0);
        myMarupekeGridSize6.markX(0, 1);
        myMarupekeGridSize6.markX(0, 2);
        myMarupekeGridSize6.markX(0, 3);
        myMarupekeGridSize6.markX(0, 4);
        myMarupekeGridSize6.markX(0, 5);
        myMarupekeGridSize6.markX(1, 0);
        myMarupekeGridSize6.markX(1, 5);
        myMarupekeGridSize6.markX(2, 0);
        myMarupekeGridSize6.markX(2, 5);
        myMarupekeGridSize6.markX(3, 0);
        myMarupekeGridSize6.markX(3, 5);
        myMarupekeGridSize6.markX(4, 0);
        myMarupekeGridSize6.markX(4, 5);
        myMarupekeGridSize6.markX(5, 0);
        myMarupekeGridSize6.markX(5, 1);
        myMarupekeGridSize6.markX(5, 2);
        myMarupekeGridSize6.markX(5, 3);
        myMarupekeGridSize6.markX(5, 4);
        myMarupekeGridSize6.markX(5, 5);
        assertEquals("R\n5 " + BOLD_X + " " + BOLD_X + " " + BOLD_X + " " + BOLD_X + " " + BOLD_X + " " +
                BOLD_X + " \n4 " + BOLD_X + " _ _ _ _ " + BOLD_X + " \n3 " + BOLD_X + " _ _ _ _ " + BOLD_X + " \n2 " +
                BOLD_X + " _ _ _ _ " + BOLD_X + " \n1 " + BOLD_X + " _ _ _ _ " + BOLD_X + " \n0 " + BOLD_X + " " +
                BOLD_X + " " + BOLD_X + " " + BOLD_X + " " + BOLD_X + " " + BOLD_X + " \n  0 1 2 3 4 5 C\n",
                myMarupekeGridSize6.toString());
    }

    @Test
    public void testMarkXOnUneditable() {
        myMarupekeGridSize3.setSolid(1, 1);
        myMarupekeGridSize3.markX(1, 1);
        assertEquals("R\n2 _ _ _ \n1 _ # _ \n0 _ _ _ \n  0 1 2 C\n", myMarupekeGridSize3.toString());
    }

    //TESTS FOR markO()
    @Test
    public void testMarkONormal() {
        myMarupekeGridSize6.markO(3, 3);
        myMarupekeGridSize6.markO(2, 1);
        assertEquals("R\n5 _ _ _ _ _ _ \n4 _ _ _ _ _ _ \n3 _ " + BOLD_O + " _ _ _ _ \n2 _ _ _ " + BOLD_O +
                " _ _ \n1 _ _ _ _ _ _ \n0 _ _ _ _ _ _ \n  0 1 2 3 4 5 C\n", myMarupekeGridSize6.toString());
    }

    @Test
    public void testMarkOCorners() {
        myMarupekeGridSize6.markO(0, 0);
        myMarupekeGridSize6.markO(0, 5);
        myMarupekeGridSize6.markO(5, 0);
        myMarupekeGridSize6.markO(5, 5);
        assertEquals("R\n5 " + BOLD_O + " _ _ _ _ " + BOLD_O + " \n4 _ _ _ _ _ _ \n3 _ _ _ _ _ _ " +
                "\n2 _ _ _ _ _ _ \n1 _ _ _ _ _ _ \n0 " + BOLD_O + " _ _ _ _ " + BOLD_O + " \n  0 1 2 3 4 5 C\n",
                myMarupekeGridSize6.toString());
    }

    @Test
    public void testMarkOEdges() {
        myMarupekeGridSize6.markO(0, 0);
        myMarupekeGridSize6.markO(0, 1);
        myMarupekeGridSize6.markO(0, 2);
        myMarupekeGridSize6.markO(0, 3);
        myMarupekeGridSize6.markO(0, 4);
        myMarupekeGridSize6.markO(0, 5);
        myMarupekeGridSize6.markO(1, 0);
        myMarupekeGridSize6.markO(1, 5);
        myMarupekeGridSize6.markO(2, 0);
        myMarupekeGridSize6.markO(2, 5);
        myMarupekeGridSize6.markO(3, 0);
        myMarupekeGridSize6.markO(3, 5);
        myMarupekeGridSize6.markO(4, 0);
        myMarupekeGridSize6.markO(4, 5);
        myMarupekeGridSize6.markO(5, 0);
        myMarupekeGridSize6.markO(5, 1);
        myMarupekeGridSize6.markO(5, 2);
        myMarupekeGridSize6.markO(5, 3);
        myMarupekeGridSize6.markO(5, 4);
        myMarupekeGridSize6.markO(5, 5);
        assertEquals("R\n5 " + BOLD_O + " " + BOLD_O + " " + BOLD_O + " " + BOLD_O + " " + BOLD_O + " " +
                BOLD_O + " \n4 " + BOLD_O + " _ _ _ _ " + BOLD_O + " \n3 " + BOLD_O + " _ _ _ _ " + BOLD_O + " \n2 " +
                BOLD_O + " _ _ _ _ " + BOLD_O + " \n1 " + BOLD_O + " _ _ _ _ " + BOLD_O + " \n0 " + BOLD_O + " " +
                BOLD_O + " " + BOLD_O + " " + BOLD_O + " " + BOLD_O + " " + BOLD_O + " \n  0 1 2 3 4 5 C\n",
                myMarupekeGridSize6.toString());
    }

    @Test
    public void testMarkOOnUneditable() {
        myMarupekeGridSize3.setSolid(1, 1);
        myMarupekeGridSize3.markO(1, 1);
        assertEquals("R\n2 _ _ _ \n1 _ # _ \n0 _ _ _ \n  0 1 2 C\n", myMarupekeGridSize3.toString());
    }

    //TESTS FOR unmark()
    @Test
    public void testUnmarkNormal() {
        myMarupekeGridSize6.markO(3, 3);
        myMarupekeGridSize6.markO(2, 1);
        myMarupekeGridSize6.unmark(3, 3);
        myMarupekeGridSize6.unmark(2, 1);
        assertEquals("R\n5 _ _ _ _ _ _ \n4 _ _ _ _ _ _ \n3 _ _ _ _ _ _ \n2 _ _ _ _ _ _ \n1 _ _ _ _ _ _ \n" +
                "0 _ _ _ _ _ _ \n  0 1 2 3 4 5 C\n", myMarupekeGridSize6.toString());
    }

    @Test
    public void testUnmarkCorners() {
        myMarupekeGridSize6.markX(0, 0);
        myMarupekeGridSize6.markX(0, 5);
        myMarupekeGridSize6.markX(5, 0);
        myMarupekeGridSize6.markX(5, 5);
        myMarupekeGridSize6.unmark(0, 0);
        myMarupekeGridSize6.unmark(0, 5);
        myMarupekeGridSize6.unmark(5, 0);
        myMarupekeGridSize6.unmark(5, 5);
        assertEquals("R\n5 _ _ _ _ _ _ \n4 _ _ _ _ _ _ \n3 _ _ _ _ _ _ \n2 _ _ _ _ _ _ \n1 _ _ _ _ _ _ \n" +
                "0 _ _ _ _ _ _ \n  0 1 2 3 4 5 C\n", myMarupekeGridSize6.toString());
    }

    @Test
    public void testUnmarkEdges() {
        myMarupekeGridSize6.markO(0, 0);
        myMarupekeGridSize6.markX(0, 1);
        myMarupekeGridSize6.markO(0, 2);
        myMarupekeGridSize6.markX(0, 3);
        myMarupekeGridSize6.markO(0, 4);
        myMarupekeGridSize6.markX(0, 5);
        myMarupekeGridSize6.markO(1, 0);
        myMarupekeGridSize6.markX(1, 5);
        myMarupekeGridSize6.markO(2, 0);
        myMarupekeGridSize6.markX(2, 5);
        myMarupekeGridSize6.markO(3, 0);
        myMarupekeGridSize6.markX(3, 5);
        myMarupekeGridSize6.markO(4, 0);
        myMarupekeGridSize6.markX(4, 5);
        myMarupekeGridSize6.markO(5, 0);
        myMarupekeGridSize6.markX(5, 1);
        myMarupekeGridSize6.markO(5, 2);
        myMarupekeGridSize6.markX(5, 3);
        myMarupekeGridSize6.markO(5, 4);
        myMarupekeGridSize6.markX(5, 5);
        myMarupekeGridSize6.unmark(0, 0);
        myMarupekeGridSize6.unmark(0, 1);
        myMarupekeGridSize6.unmark(0, 2);
        myMarupekeGridSize6.unmark(0, 3);
        myMarupekeGridSize6.unmark(0, 4);
        myMarupekeGridSize6.unmark(0, 5);
        myMarupekeGridSize6.unmark(1, 0);
        myMarupekeGridSize6.unmark(1, 5);
        myMarupekeGridSize6.unmark(2, 0);
        myMarupekeGridSize6.unmark(2, 5);
        myMarupekeGridSize6.unmark(3, 0);
        myMarupekeGridSize6.unmark(3, 5);
        myMarupekeGridSize6.unmark(4, 0);
        myMarupekeGridSize6.unmark(4, 5);
        myMarupekeGridSize6.unmark(5, 0);
        myMarupekeGridSize6.unmark(5, 1);
        myMarupekeGridSize6.unmark(5, 2);
        myMarupekeGridSize6.unmark(5, 3);
        myMarupekeGridSize6.unmark(5, 4);
        myMarupekeGridSize6.unmark(5, 5);
        assertEquals("R\n5 _ _ _ _ _ _ \n4 _ _ _ _ _ _ \n3 _ _ _ _ _ _ \n2 _ _ _ _ _ _ \n1 _ _ _ _ _ _ \n" +
                "0 _ _ _ _ _ _ \n  0 1 2 3 4 5 C\n", myMarupekeGridSize6.toString());
    }

    @Test
    public void testUnmarkOnUneditable() {
        myMarupekeGridSize3.setX(1, 1, false);
        assertFalse(myMarupekeGridSize3.unmark(1, 1));
        assertEquals("R\n2 _ _ _ \n1 _ X _ \n0 _ _ _ \n  0 1 2 C\n", myMarupekeGridSize3.toString());
    }

    @Test
    public void testUnmarkAlreadyUnmarked() {
        myMarupekeGridSize3.unmark(1, 1);
        assertEquals("R\n2 _ _ _ \n1 _ _ _ \n0 _ _ _ \n  0 1 2 C\n", myMarupekeGridSize3.toString());
    }

    //TESTS FOR isPuzzleComplete()
    @Test
    public void testSmallGridComplete() {
        myMarupekeGridSize3.markX(0, 0);
        myMarupekeGridSize3.markO(0, 1);
        myMarupekeGridSize3.setSolid(0, 2);
        myMarupekeGridSize3.setSolid(1, 0);
        myMarupekeGridSize3.markX(1, 1);
        myMarupekeGridSize3.setSolid(1, 2);
        myMarupekeGridSize3.markX(2, 0);
        myMarupekeGridSize3.markO(2, 1);
        myMarupekeGridSize3.markO(2, 2);
        assertTrue(myMarupekeGridSize3.isPuzzleComplete());
    }

    @Test
    public void testGridIncomplete() {
        myMarupekeGridSize3.markO(0, 1);
        myMarupekeGridSize3.setSolid(0, 2);
        myMarupekeGridSize3.markO(1, 1);
        myMarupekeGridSize3.setSolid(1, 2);
        myMarupekeGridSize3.markX(2, 0);
        myMarupekeGridSize3.setSolid(2, 2);
        assertFalse(myMarupekeGridSize3.isPuzzleComplete());
    }
}