package blockchain;

public class Block {

  private final Integer id;
  private final String hashPreviousBlock;
  private final String hash;
  private final Long timestamp;


  public Block(Integer id, String hashPreviousBlock, String hash, Long timestamp) {
    this.id = id;
    this.hashPreviousBlock = hashPreviousBlock;
    this.hash = hash;
    this.timestamp = timestamp;
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
}
