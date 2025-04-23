package pl.edu.agh.cs.common;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestPair {

    @Test
    public void testElements() {
        Pair<Integer, Integer> pair = new Pair<>(1, 2);
        assertEquals(1, (int) pair.getFirst());
        assertEquals(2, (int) pair.getSecond());

        pair.setFirst(3);
        assertEquals(3, (int) pair.getFirst());
        pair.setSecond(4);
        assertEquals(4, (int) pair.getSecond());
    }

    @Test
    public void testEquality() {
        Pair<Integer, Integer> pair = new Pair<>(1, 2);
        assertTrue(pair.equals(pair));
        assertTrue(pair.equals(new Pair<>(1, 2)));
        assertFalse(pair.equals(new Pair<>(2,3)));
    }

    @Test
    public void testHashCode() {
        Pair<Integer, Integer> pair = new Pair<>(1, 2);
        assertEquals(pair.hashCode(), pair.hashCode());
        assertNotEquals(pair.hashCode(), new Pair<>(2, 1).hashCode());
        assertEquals(pair.hashCode(), new Pair<>(1, 2).hashCode());
    }

}
