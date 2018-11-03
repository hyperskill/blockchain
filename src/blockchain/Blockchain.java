package blockchain;
import java.io.Serializable;
import java.security.MessageDigest;
import java.time.LocalTime;
import java.util.Scanner;

public class Blockchain implements Serializable {

    private String nonce = "";
    int starthash = 0;
    String previoushash = Integer.toString(starthash);
    String currenthash = "";
    private int tiktok;


    Blockchain() {


        int id = 1;
        int time;
        Boolean sec;
        int zeros;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter how many zeros the hash must starts with: ");
        zeros = scanner.nextInt();

        for (int i = 0;  i < zeros; i ++){
            nonce+="0";
        }
        System.out.print("Enter how many block will created: ");

        time = scanner.nextInt();



        for (int i = 0; i < time; i++) {
            System.out.println("Block:");
            System.out.println("id: " + id);

            currenthash = proofOfWork(previoushash); 
            getTimeStamp();

            System.out.println("Hash of the previous block: " + previoushash);
            System.out.println("Hash of the block: " + currenthash);
            System.out.println("Block was generating for " + tiktok + " seconds");

            sec = validate(previoushash, currenthash);

            if (sec) {
                previoushash = currenthash;
                id++;
                System.out.println();
            } else {
                System.out.println("Hash not valid!!!");

                break;
            }
        }
    }

    private void getTimeStamp() {
        System.out.println("Timestamp: " + System.currentTimeMillis());
    }


    private String proofOfWork(String block) {

        String nonceKey = nonce;
        long nonce = 0;
        boolean nonceFound = false;
        String nonceHash = "";


        LocalTime time_before = LocalTime.now();

        while (!nonceFound) {

            nonceHash = GenerateBlock(block + nonce);

            nonceFound = nonceHash.substring(0, nonceKey.length()).equals(nonceKey);
            nonce++;

        }

        LocalTime time_after = LocalTime.now();
        //считаем разницу во времени
        this.tiktok = (time_after.getMinute()-time_before.getMinute())*60 + (time_after.getSecond()-time_before.getSecond());
        return nonceHash;

    }

    private Boolean validate(String previousblock, String currentblock) {
        String check = proofOfWork(previousblock);
        if(check.equals(currentblock)){
            return true;
        }else {
            return false;
        }
    }

    private String GenerateBlock(String currentid) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(currentid.getBytes("UTF-8"));
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

}
