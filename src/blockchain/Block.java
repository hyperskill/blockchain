package blockchain;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;



class Block implements Serializable{

    private List<Message> messagesStack = new LinkedList<>();
    private String prevHash;
    private String hash;
    private int id;
    private long timeStamp;
    private int magicNumber;
    private long maxMsgID;

    public Block(Block prevBlock, int difficulty){

        if(prevBlock == null) {
            this.prevHash = "0";
            this.id = 1;
        }
        else {
            this.prevHash = prevBlock.getHash();
            this.id = prevBlock.getId() + 1;
        }
        String zeros = "";
        for (int i = 0; i < difficulty; i++){
            zeros += "0";
        }

        do {
            setMagicNumber();
            this.hash = applySha256(this.prevHash + this.magicNumber);
        }while(!this.hash.startsWith(zeros));
        setMaxMsgID();
        this.timeStamp = new Date().getTime();
    }

    public void printOutResults() throws UnsupportedEncodingException {
        System.out.println("Id: " + this.getId());
        System.out.println("Timestamp: " + this.getTimeStamp());
        System.out.println("Magic number: " + this.getMagicNumber());
        System.out.println("Hash of the previous block:");
        System.out.println(this.getPrevHash());
        System.out.println("Hash of the block:");
        System.out.println(this.getHash());
        if (this.id == 1 || messagesStack.isEmpty()){
            System.out.println("Block data: no messages");
        }else{
            System.out.println("Block data: ");
            for(Message s: messagesStack) {
                String str = new String(s.getList().get(0), "UTF-8");

                System.out.println(str);
            }
        }
    }


    private void setMagicNumber() {
        this.magicNumber = new Random().nextInt(Integer.MAX_VALUE);
    }

    public int getId() {
        return id;
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public long getTimeStamp(){
        return timeStamp;
    }


    public String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte i : hash) {
                String hex = Integer.toHexString(0xff & i);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getMagicNumber(){
        return this.magicNumber;
    }

    public void writeMessagesStack(LinkedList<Message> messagesStack){
        this.messagesStack.addAll(messagesStack);
    }

    public LinkedList<Message> getMessageStack(){
        return (LinkedList<Message>) this.messagesStack;
    }

    private void setMaxMsgID(){
        long max = 0;
        for (Message aMessagesStack : this.messagesStack) {
            if (max < aMessagesStack.getId()) {
                max = aMessagesStack.getId();
            }
        }
        this.maxMsgID = max;
    }

    public long getMaxMsgID(){
        return this.maxMsgID;
    }

}
