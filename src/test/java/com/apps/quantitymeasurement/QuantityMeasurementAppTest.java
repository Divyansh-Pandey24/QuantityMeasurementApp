package com.apps.quantitymeasurement;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {
	
	@Test
	public void testFeetEquality_SameValue() {
		Feet f1 = new Feet(1.0);
		Feet f2 = new Feet(1.0);
		
		assertTrue(f1.equals(f2));
	}

	 @Test
	    void testFeetEquality_DifferentValue() {
	        Feet f1 = new Feet(1.0);
	        Feet f2 = new Feet(2.0);
	        assertFalse(f1.equals(f2));
	    }

	    @Test
	    void testFeetEquality_NullComparison() {
	        Feet f1 = new Feet(1.0);
	        assertFalse(f1.equals(null));
	    }

	    @Test
	    void testFeetEquality_DifferentClass() {
	        Feet f1 = new Feet(1.0);
	        String str = "1.0";
	        assertFalse(f1.equals(str));
	    }

	    @Test
	    void testFeetEquality_SameReference() {
	        Feet f1 = new Feet(1.0);
	        assertTrue(f1.equals(f1));
	    }
	    
	    @Test
	    public void testInchesEquality_SameValue() {
	        Inch i1 = new Inch(1.0);
	        Inch i2 = new Inch(1.0);

	        assertTrue(i1.equals(i2), "1.0 inch should be equal to 1.0 inch");
	    }
	    
	    @Test
	    public void testInchesEquality_DifferentValue() {
	        Inch i1 = new Inch(1.0);
	        Inch i2 = new Inch(2.0);

	        assertFalse(i1.equals(i2), "1.0 inch should not be equal to 2.0 inch");
	    }

	    @Test
	    public void testInchesEquality_NullComparison() {
	        Inch i1 = new Inch(1.0);

	        assertFalse(i1.equals(null), "Inch object should not be equal to null");
	    }

	    @Test
	    public void testInchesEquality_DifferentClass() {
	        Inch i1 = new Inch(1.0);
	        String other = "1.0";

	        assertFalse(i1.equals(other), "Inch object should not be equal to different type");
	    }

	    @Test
	    public void testInchesEquality_SameReference() {
	        Inch i1 = new Inch(1.0);

	        assertTrue(i1.equals(i1), "Object should be equal to itself");
	    }
}
