package blockchain;


import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static Blockchain blockchain;
    public static List<Thread> users = new ArrayList<>();
    public static List<Thread> miners = new ArrayList<>();
    public static GenerateKeys gk;


    public static void main(String[] args) throws InterruptedException, NoSuchProviderException, NoSuchAlgorithmException {
        Scanner in = new Scanner(System.in);
        String filePath = in.nextLine();//"D:\\hyperskillGit\\blockchain\\MyBlockchain";
        blockchain = new Blockchain(filePath);
        gk = new GenerateKeys(1024);
        gk.createKeys();


        if (blockchain.isBlockchainValid()) {
            users.add(new User("Tom"));
            users.add(new User("Sara"));
            users.add(new User("John"));

            for (int i = 0; i < 7; i++) {
                miners.add(new Miner(i + 1));
            }
            miners.forEach(Thread::start);

            users.forEach(Thread::start);

            for (Thread miner : miners) {
                miner.join();
            }

            blockchain.writeBlockchainToFile(filePath);
            System.exit(0);

        } else {
            System.out.println("Blockchain loaded from file is not valid.");
        }

    }
}