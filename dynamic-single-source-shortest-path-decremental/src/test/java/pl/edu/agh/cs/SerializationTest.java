package pl.edu.agh.cs;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SerializationTest {

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        EvenShiloachTree obj = new EvenShiloachTree();
        try {
            obj.addEdge(0, 1);
        } catch (Exception e) {

        }

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("obj.ser"));
        out.writeObject(obj);
        out.close();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream("obj.ser"));
        EvenShiloachTree obj1 = (EvenShiloachTree) in.readObject();
        in.close();

        assertEquals(obj,obj1);


    }
}
