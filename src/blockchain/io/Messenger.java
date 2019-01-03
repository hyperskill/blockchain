package blockchain.io;

import blockchain.Blockchain;
import blockchain.crypto.Sign;
import blockchain.data.Message;

public class Messenger {

  private Blockchain<Message> blockchain;
  private Sign sign;

  public Messenger(Blockchain<Message> blockchain, Sign sign) {
    this.blockchain = blockchain;
    this.sign = sign;
  }

  public boolean sendMessage(String user, String text) {
    try {
      Integer id = blockchain.uniqueIdentity();
      Message message = new Message(id, user, text, sign.sign(text + id),
          sign.getPublicKeyBytes());
      return blockchain.appendData(message);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Message was not sent");
    }
    return false;
  }
}
