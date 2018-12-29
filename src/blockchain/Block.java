package blockchain;

import java.io.Serializable;

public class Block implements Serializable {

  private final Integer id;
  private final Long minerId;
  private final String hashPreviousBlock;
  private final String hash;
  private final Long timestamp;
  private final Integer magicNumber;
  private String data;


  public Block(Integer id, Long minerId, String hashPreviousBlock, String hash, Long timestamp,
      Integer magicNumber, String data) {
    this.id = id;
    this.minerId = minerId;
    this.hashPreviousBlock = hashPreviousBlock;
    this.hash = hash;
    this.timestamp = timestamp;
    this.magicNumber = magicNumber;
    this.data = data;
  }

  public Integer getId() {
    return id;
  }

  public String getHashPreviousBlock() {
    return hashPreviousBlock;
  }

  public String getHash() {
    return hash;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Integer getMagicNumber() {
    return magicNumber;
  }

  public Long getMinerId() {
    return minerId;
  }
}
