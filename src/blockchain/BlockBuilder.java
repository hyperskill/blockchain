package blockchain;

public class BlockBuilder {

  private Integer id;
  private Long minerId;
  private String hashPreviousBlock;
  private String hash;
  private Long timestamp;
  private Integer magicNumber;
  private String data;

  public BlockBuilder withId(Integer id) {
    this.id = id;
    return this;
  }

  public BlockBuilder withMinerId(Long minerId) {
    this.minerId = minerId;
    return this;
  }

  public BlockBuilder withHashPreviousBlock(String hashPreviousBlock) {
    this.hashPreviousBlock = hashPreviousBlock;
    return this;
  }

  public BlockBuilder withHash(String hash) {
    this.hash = hash;
    return this;
  }

  public BlockBuilder withTimestamp(Long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public BlockBuilder withMagicNumber(Integer magicNumber) {
    this.magicNumber = magicNumber;
    return this;
  }

  public BlockBuilder withData(String data) {
    this.data = data;
    return this;
  }

  public Block build() {
    return new Block(id, minerId, hashPreviousBlock, hash, timestamp, magicNumber, data);
  }
}
