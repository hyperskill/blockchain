package blockchain.data;

public class Transaction {

  private final String from;
  private final String to;
  private final Double amount;

  public Transaction(String from, String to, Double amount) {
    this.from = from;
    this.to = to;
    this.amount = amount;
  }

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  public Double getAmount() {
    return amount;
  }
}
