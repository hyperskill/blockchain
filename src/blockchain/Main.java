package blockchain;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main {

    public static void main(String[] args) {
      int miners_count = 4;

       for (int i = 1; i <=miners_count;  i ++){
           Thread thread = new Blockchain(i);
           thread.start();
       }


    }
    }
