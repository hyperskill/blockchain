package blockchain.data;

import java.io.Serializable;

public class Transaction implements SignedData, Serializable {

  private Integer id;
  private String from;
  private String to;
  private Double amount;
  private byte[] dataSignature;
  private byte[] publicKey;

  public Transaction(Integer id, String from, String to, Double amount, byte[] dataSignature,
      byte[] publicKey) {
    this.id = id;
    this.from = from;
    this.to = to;
    this.amount = amount;
    this.dataSignature = dataSignature;
    this.publicKey = publicKey;
  }

  @Override
  public Integer id() {
    return id;
  }

  @Override
  public byte[] raw() {
    return (String.format("%s,%s,%s", from, to, amount) + id()).getBytes();
  }

  @Override
  public byte[] dataSignature() {
    return dataSignature;
  }

  @Override
  public byte[] publicKey() {
    return publicKey;
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
