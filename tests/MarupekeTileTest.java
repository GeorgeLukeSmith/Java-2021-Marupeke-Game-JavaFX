import ModelComponents.MarupekeTile;
import ModelComponents.Type;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MarupekeTileTest {
    MarupekeTile myTile;

    @Before
    public void testSetUp(){
        myTile = new MarupekeTile();
    }
    //Test that the constructor makes the tile BLANK and true
    @Test
    public void testInitialises(){
        assertEquals(Type.BLANK, myTile.getData());
        assertEquals(true, myTile.getEditable());
    }
    //Tests that setData works correctly
    @Test
    public void testSetData(){
        myTile.setData(Type.X);
        assertEquals(Type.X, myTile.getData());
    }
    //Tests for toString()
    @Test
    public void testSetEditable(){
        myTile.setEditable(false);
        assertEquals(false, myTile.getEditable());
    }
    @Test
    public void testToStringX(){
        myTile.setData(Type.X);
        assertEquals("X", myTile.toString());
    }
    @Test
    public void testToStringO(){
        myTile.setData(Type.O);
        assertEquals("O", myTile.toString());
    }
    @Test
    public void testToStringFilled(){
        myTile.setData(Type.FILLED);
        assertEquals("#", myTile.toString());
    }
    @Test
    public void testToStringBlank(){
        myTile.setData(Type.BLANK);
        assertEquals("_", myTile.toString());
    }
}
