package blockchain.data;

import java.io.Serializable;

public class Message implements SignedData, Serializable {

  private final Integer id;
  private final String author;
  private final String text;
  private final byte[] dataSignature;
  private final byte[] publicKey;

  public Message(Integer id, String author, String text, byte[] dataSignature, byte[] publicKey) {
    this.id = id;
    this.author = author;
    this.text = text;
    this.dataSignature = dataSignature;
    this.publicKey = publicKey;
  }

  public String getAuthor() {
    return author;
  }

  public String getText() {
    return text;
  }

  @Override
  public Integer id() {
    return id;
  }

  @Override
  public byte[] raw() {
    return (getText() + id()).getBytes();
  }

  @Override
  public byte[] dataSignature() {
    return dataSignature;
  }

  @Override
  public byte[] publicKey() {
    return publicKey;
  }
}
