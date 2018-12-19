package blockchain;

import java.io.Serializable;

public class Block implements Serializable {

  private final Integer id;
  private final String hashPreviousBlock;
  private final String hash;
  private final Long timestamp;
  private final Integer magicNumber;
  private final Long generationTime;


  public Block(Integer id, String hashPreviousBlock, String hash,
      Long timestamp, Integer magicNumber, Long generationTime) {
    this.id = id;
    this.hashPreviousBlock = hashPreviousBlock;
    this.hash = hash;
    this.timestamp = timestamp;
    this.magicNumber = magicNumber;
    this.generationTime = generationTime;
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

  public Long getGenerationTime() {
    return generationTime;
  }
}
