package blockchain;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main {

    public static void main(String[] args) {
        new Blockchain();
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream("temp.out");
            oos = new ObjectOutputStream(fos);
        } catch (IOException e) {
            System.out.println("Exit");
        }
        Blockchain b = new Blockchain();

        try {
            oos.writeObject(b);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            System.out.println("Smth wrong");
        }

    }
    }
