package blockchain;

import java.io.Serializable;

public class Block implements Serializable {

  private final Integer id;
  private final String minerId;
  private final String hashPreviousBlock;
  private final String hash;
  private final Long timestamp;
  private final Integer magicNumber;
  private final Long generationTime;

  private String data;
  private byte[] dataSignature;


  public Block(Integer id, String minerId, String hashPreviousBlock, String hash,
      String data, byte[] dataSignature, Long timestamp, Integer magicNumber, Long generationTime) {
    this.id = id;
    this.minerId = minerId;
    this.hashPreviousBlock = hashPreviousBlock;
    this.hash = hash;
    this.data = data;
    this.dataSignature = dataSignature;
    this.timestamp = timestamp;
    this.magicNumber = magicNumber;
    this.generationTime = generationTime;
  }

  public Block(int id, String minerId, String hashPreviousBlock, String hash, long timestamp,
      int magicNumber,
      long generationTime) {
    this(id, minerId, hashPreviousBlock, hash, null, null, timestamp, magicNumber, generationTime);
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

  public Long getGenerationTime() {
    return generationTime;
  }

  public String getMinerId() {
    return minerId;
  }

  public byte[] getDataSignature() {
    return dataSignature;
  }

  public void setDataSignature(byte[] dataSignature) {
    this.dataSignature = dataSignature;
  }
}
