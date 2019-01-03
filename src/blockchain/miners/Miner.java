package blockchain.miners;

import blockchain.Block;
import blockchain.BlockBuilder;
import blockchain.Blockchain;
import blockchain.data.SignedData;
import blockchain.utils.StringUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;

public class Miner<T extends SignedData & Serializable> implements Runnable {

  private final Blockchain<T> blockchain;
  private final Long id;
  private final Random random;

  public Miner(Blockchain<T> blockchain, Long id) {
    this.blockchain = blockchain;
    this.id = id;
    this.random = new Random();
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      Block<T> prev = blockchain.tail();
      blockchain.accept(
          generateNextBlock(prev.getId() + 1, id, prev.getHash())
      );
    }
  }

  private Block<T> generateNextBlock(int blockId, long minerId, String previousHash) {
    BlockBuilder<T> blockBuilder = new BlockBuilder<T>()
        .withId(blockId)
        .withMinerId(minerId)
        .withHashPreviousBlock(previousHash);

    Integer magicNumber;
    String hash;
    String prefix;
    do {
      prefix = blockchain.getPrefix();
      magicNumber = random.nextInt();
      hash = StringUtil.applySha256(previousHash + magicNumber);
      blockBuilder = blockBuilder.withHash(hash)
          .withMagicNumber(magicNumber)
          .withTimestamp(new Date().getTime());
    } while (!hash.startsWith(prefix));

    return blockBuilder.build();
  }
}
