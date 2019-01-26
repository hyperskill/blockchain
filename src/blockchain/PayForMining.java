package blockchain;

import blockchain.crypto.Sign;
import blockchain.data.Transaction;
import java.util.Optional;

public class PayForMining implements SystemFeedback<Transaction> {

  private Sign sign;

  public PayForMining(Sign sign) {

    this.sign = sign;
  }

  @Override
  public Optional<Transaction> apply(Integer id, String x) {
    try {
      return Optional.of(new
          Transaction(id, "System", x, 100.0,
          sign.sign(String.format("%s,%s,%s", "System", x, 100.0) + id),
          sign.getPublicKeyBytes()));
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
