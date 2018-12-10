package blockchain.data.format;

import blockchain.data.Transaction;

public class CSVTransactionFormat implements DataFormatter<Transaction> {

  @Override
  public String format(Transaction message) {
    return String.format("%s,%s,%f", message.getFrom(), message.getTo(), message.getAmount());
  }

  @Override
  public Transaction parse(String data) {
    String[] dat = data.split(",");
    return new Transaction(dat[0], dat[1], Double.parseDouble(dat[2]));
  }
}
