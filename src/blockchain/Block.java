package blockchain;

import blockchain.data.SignedData;
import java.io.Serializable;
import java.util.List;

public class Block<T extends SignedData & Serializable> implements Serializable {

  private final Integer id;
  private final Long minerId;
  private final String hashPreviousBlock;
  private final String hash;
  private final Long timestamp;
  private final Integer magicNumber;
  private List<T> data = List.of();


  public Block(Integer id, Long minerId, String hashPreviousBlock, String hash,
      Long timestamp, Integer magicNumber) {
    this.id = id;
    this.minerId = minerId;
    this.hashPreviousBlock = hashPreviousBlock;
    this.hash = hash;
    this.timestamp = timestamp;
    this.magicNumber = magicNumber;
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

  public Integer getMagicNumber() {
    return magicNumber;
  }

  public Long getMinerId() {
    return minerId;
  }

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }
}
