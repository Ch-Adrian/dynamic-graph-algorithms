package pl.edu.agh.cs;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SerializationTest {

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        DynamicConnectivity dynamicConnectivity = new DynamicConnectivity(10);
        dynamicConnectivity.addEdge(0, 1);

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dynCon.ser"));
        out.writeObject(dynamicConnectivity);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream("dynCon.ser"));
        DynamicConnectivity dynamicConnectivity1 = (DynamicConnectivity) in.readObject();
        in.close();

        assertEquals(dynamicConnectivity,dynamicConnectivity1);
        assertTrue(dynamicConnectivity1.isConnected(0, 1));


    }
}
