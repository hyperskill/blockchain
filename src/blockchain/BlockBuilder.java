package blockchain;

import blockchain.data.SignedData;
import java.io.Serializable;

public class BlockBuilder<T extends SignedData & Serializable> {

  private Integer id;
  private Long minerId;
  private String hashPreviousBlock;
  private String hash;
  private Long timestamp;
  private Integer magicNumber;

  public BlockBuilder<T> withId(Integer id) {
    this.id = id;
    return this;
  }

  public BlockBuilder<T> withMinerId(Long minerId) {
    this.minerId = minerId;
    return this;
  }

  public BlockBuilder<T> withHashPreviousBlock(String hashPreviousBlock) {
    this.hashPreviousBlock = hashPreviousBlock;
    return this;
  }

  public BlockBuilder<T> withHash(String hash) {
    this.hash = hash;
    return this;
  }

  public BlockBuilder<T> withTimestamp(Long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public BlockBuilder<T> withMagicNumber(Integer magicNumber) {
    this.magicNumber = magicNumber;
    return this;
  }

  public Block<T> build() {
    return new Block<>(id, minerId, hashPreviousBlock, hash, timestamp, magicNumber);
  }
}