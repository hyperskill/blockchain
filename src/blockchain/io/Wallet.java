package blockchain.io;

import blockchain.Blockchain;
import blockchain.crypto.Sign;
import blockchain.data.Transaction;
import java.util.Objects;

public class Wallet {

  private static final double INIT_SUM = 100.0;

  private Blockchain<Transaction> blockchain;
  private Sign sign;

  public Wallet(Blockchain<Transaction> blockchain, Sign sign) {
    this.blockchain = blockchain;
    this.sign = sign;
  }

  public boolean sendMoney(String from, String to, double amount) {
    try {
      if (!isEnoughMoney(from, amount)) {
        return false;
      }
      Integer id = blockchain.uniqueIdentity();
      Transaction message = new Transaction(id, from, to, amount,
          sign.sign(String.format("%s,%s,%s",
              from, to, amount) + id),
          sign.getPublicKeyBytes());
      return blockchain.appendData(message);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Message was not sent");
    }
    return false;
  }

  private boolean isEnoughMoney(String from, double amount) {
    double sentSum = -blockchain.data().filter(
        d -> Objects.equals(d.getFrom(), from))
        .mapToDouble(Transaction::getAmount)
        .sum();
    double recvSum = blockchain.data().filter(
        d -> Objects.equals(d.getTo(), from))
        .mapToDouble(Transaction::getAmount)
        .sum();
    return INIT_SUM + sentSum + recvSum >= amount;
  }
}
