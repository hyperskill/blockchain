package blockchain.currency;

import blockchain.Blockchain;
import blockchain.data.Transaction;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccountTransactions {

  private final Blockchain<Transaction> transactionBlockchain;

  public AccountTransactions(
      Blockchain<Transaction> transactionBlockchain) {
    this.transactionBlockchain = transactionBlockchain;
  }

  public Double walletBallance(String userId) {
    double total = 0.0;
    List<Transaction> transactions = transactionBlockchain.dataFromBlockchain();
    List<Transaction> userSent = transactions.stream().filter(t -> t.getFrom().equals(userId))
        .collect(Collectors.toList());
    List<Transaction> userRecieved = transactions.stream().filter(t -> t.getTo().equals(userId))
        .collect(Collectors.toList());
    return total;
  }
}
