package blockchain.data.format;

import blockchain.data.Transaction;

public class TransactionPrintFormat implements DataFormatter<Transaction> {

  @Override
  public String format(Transaction transaction) {
    return String
        .format("%s sent %s VC to %s", transaction.getFrom(), transaction.getAmount(),
            transaction.getTo());
  }
}
