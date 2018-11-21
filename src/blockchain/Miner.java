package blockchain;


import java.io.UnsupportedEncodingException;
import java.time.LocalTime;

class Miner extends Thread {
    private int minerNumber;
    private int VC = 0;


    public Miner(int minerNumber) {
        this.minerNumber = minerNumber;
        this.VC = 0;
    }


    @Override
    public void run() {
        LocalTime start = LocalTime.now();
        while (!Main.blockchain.addBlock(new Block(Main.blockchain.getBlockchain().peekLast(), Main.blockchain.getDifficulty()))) { }
        Main.blockchain.addMessagesToTheBlock();
        LocalTime finish = LocalTime.now();
        Main.blockchain.setTimeSpent(finish.toSecondOfDay() - start.toSecondOfDay());
        System.out.println("");
        System.out.println("Block:");
        System.out.println("Created by miner # " + this.minerNumber);
        addVC(100);
        System.out.println(this.minerNumber + " gets " + 100 + "VC");
        try {
            Main.blockchain.getBlockchain().peekLast().printOutResults();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Block was generating for " + Main.blockchain.getTimeSpent() + " seconds");
        Main.blockchain.setDifficulty();
    }

    public void addVC(int add){
        this.VC += add;
    }

    public void sendVC(int sub){
        if(this.VC < sub){
            System.out.println("Not enough VC, send transaction suspend");
        }else{
            this.VC -= sub;
        }
    }

    public int getVC() {
        return this.VC;
    }

}


