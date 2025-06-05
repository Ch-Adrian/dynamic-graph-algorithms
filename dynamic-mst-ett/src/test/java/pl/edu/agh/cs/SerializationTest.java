package pl.edu.agh.cs;

import org.junit.jupiter.api.Test;
import pl.edu.agh.cs.common.OperatingMode;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SerializationTest {

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        DynamicMST obj = new DynamicMST(10);
        obj.addEdge(0, 1, 1);
        obj.changeOperatinMode(OperatingMode.DECREMENTAL);
        obj.calculateMST();

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("obj.ser"));
        out.writeObject(obj);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream("obj.ser"));
        DynamicMST obj1 = (DynamicMST) in.readObject();
        in.close();

        assertEquals(obj, obj1);
        assertTrue(obj1.isConnected(0, 1));


    }
}
