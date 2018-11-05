import java.security.MessageDigest;
import java.time.LocalTime;
import java.util.ArrayList;

public class Blockchain extends  Thread {

    volatile static ArrayList<String> block = new ArrayList<>();

    private String nonce;
    volatile static int starthash = 0;
    volatile static int zeros = 0;
    private int no;
    volatile static int id = 1;
    volatile static boolean blockCLOSE;
    volatile  static int winner = 0;

    String previoushash = Integer.toString(starthash);
    String currenthash = "";
    private int tiktok;
    private long timestamp;

    volatile static int check = 0;


    Blockchain(int no) {
        this.no = no;
    }

    @Override
public void run(){

        if(!block.contains("0")){
            block.add("0");
        }

        for (int i = 0;  ; i++){
            nonce = "";
            for(int k = 0; k  < zeros; k ++){
                this.nonce+="0";

            }
            currenthash = proofOfWork(block.get(i));
            if (currenthash.equals("null")) {
                while (block.size()==id){
                    try {
                        Thread.sleep(2_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {

                getTimeStamp();


                System.out.println("Block:");
                System.out.println("Created by miner # " + no);
                System.out.println("id: " + id);
                System.out.println("Timestamp: " + timestamp);
                System.out.println("Hash of the previous block: " + block.get(i));
                System.out.println("Hash of the block: " + currenthash);
                System.out.println("Block was generating for " + tiktok + " seconds");
                id++;
                block.add(i+1, currenthash);

                if (tiktok > 59) {
                    zeros--;
                    System.out.println("N was decreased by 1");
                } else if(tiktok>10) {
                    //the same
                    System.out.println("N stays the same");
                }else {
                    zeros++;
                    System.out.println("N was increased to "+zeros);
                }
                System.out.println();

            }

        }


    }

    private void getTimeStamp() {
        this.timestamp = System.currentTimeMillis();
    }


    private String proofOfWork(String block) {

        String nonceKey = nonce;
        long nonce = 0;
        boolean nonceFound = false;
        blockCLOSE = false;
        String nonceHash = "";


        LocalTime time_before = LocalTime.now();

        while (!nonceFound) {

    nonceHash = GenerateBlock(block + nonce);

    nonceFound = nonceHash.substring(0, nonceKey.length()).equals(nonceKey);
    synchronized (Thread.currentThread()) {
    if(check == 0) {

        if (nonceFound == true) {
            this.winner = no;
            check++;
            break;
        }
    }


    nonce++;
}
        }

        LocalTime time_after = LocalTime.now();
        //считаем разницу во времени
        synchronized(this) {
            if (no == winner) {
                this.tiktok = Math.abs(time_after.getMinute() - time_before.getMinute()) * 60 + Math.abs(time_after.getSecond() - time_before.getSecond());

                return nonceHash;
            }else{
                return "null";
            }
        }


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
