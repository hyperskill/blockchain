package blockchain;


import java.util.Scanner;


class User extends Thread {
    private String name;
    private Message message;
    Scanner in = new Scanner(System.in);
    private int VC = 100;

    public User(String name){
        this.name = name;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            newMaessage();
            try {
                Main.blockchain.addMessagesToTheBlockchain(getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void newMaessage(){
        if(!this.isInterrupted()) {
            synchronized (User.class) {
                System.out.print(this.name + ": ");
                String data = in.nextLine();
                data = this.name + ": " + data;
                try {
                    this.message = new Message(data,Main.blockchain.setMsgID());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Message getMessage(){
        return this.message;
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
