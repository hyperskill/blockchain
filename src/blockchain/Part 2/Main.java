package jetbrains;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;

class Block
{
    public static final int DIFFICULTY = 5;

    private long date;
    private int id;
    private long  nonce;
    private String prev;
    private String hash;

    private Long duration;

    public Block(int id, String prev) {
        this.date = new Date().getTime();
        this.prev = prev;
        this.id = id;

        StringBuilder sb = new StringBuilder("");
        for(int i = 0; i < DIFFICULTY; i++) {
            sb.append('0');
        }
        String starter = sb.toString();
        Long start = System.nanoTime();

        nonce = 0;
        this.hash = generateHash(nonce + (id + prev + date));
        while(!this.hash.startsWith(starter)) {
            nonce++;
            this.hash = generateHash(nonce + (id + prev + date));
        }
        this.duration = System.nanoTime() - start;
    }

    private String generateHash(String message) {
        return Blockchain.applySha256(message);
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return  "\nBlock:\nId: " + this.id +
                "\nTimestamp: " + this.date +
                "\nMagic number: " + nonce +
                "\nHash of the previous block:\n" + this.prev +
                "\nHash of the block:\n" + this.hash +
                "\nBlock was generating for " + this.duration/1_000_000_000 + " seconds";
    }
}

class Blockchain
{
    private static int id = 1;
    private ArrayList<Block> chain;

    public Blockchain()
    {
        chain = new ArrayList<>();
        chain.add(new Block(id++, "0"));
    }

    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addBlock()
    {
        chain.add(new Block(id++, chain.get(chain.size() - 1).getHash()));
    }

    public void displayChain()
    {
        System.out.println("Enter how many zeros the hash must starts with: " + Block.DIFFICULTY);
        for(Block block : chain) {
            System.out.println(block);
        }
    }
}

public class Main {

    public static void main(String[] args) {

        Blockchain chain = new Blockchain();

        for(int i = 0; i < 2; i++) {
            chain.addBlock();
        }
        chain.displayChain();
    }
}
