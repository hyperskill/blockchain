package blockchain.currency;

import blockchain.Blockchain;
import blockchain.data.Transaction;
import java.util.List;

public class AccountTransactions {

  public static final int INITIAL_SIGNIN_SUM = 100;
  private final Blockchain<Transaction> transactionBlockchain;

  public AccountTransactions(
      Blockchain<Transaction> transactionBlockchain) {
    this.transactionBlockchain = transactionBlockchain;
  }

  public Double walletBallance(String userId) {
    List<Transaction> transactions = transactionBlockchain.dataFromBlockchain();
    double userSent = transactions.stream().filter(t -> t.getFrom().equals(userId))
        .map(x -> -x.getAmount())
        .mapToDouble(Double::doubleValue)
        .sum();
    double userRecieved = transactions.stream().filter(t -> t.getTo().equals(userId))
        .map(Transaction::getAmount)
        .mapToDouble(Double::doubleValue)
        .sum();

    return INITIAL_SIGNIN_SUM + userSent + userRecieved;
  }
}
