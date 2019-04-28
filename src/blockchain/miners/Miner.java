package blockchain.miners;

import blockchain.Block;
import blockchain.Blockchain;
import blockchain.StringUtil;
import java.util.Random;

public class Miner implements Runnable {

  private final Blockchain blockchain;
  private final Random random;
  private final Long id;

  public Miner(Blockchain blockchain, Long id) {
    this.blockchain = blockchain;
    this.id = id;
    this.random = new Random();
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      Block prev = blockchain.tail();
      blockchain.accept(generateNextBlock(prev.getId() + 1, id, prev.getHash()));
    }
  }

  private Block generateNextBlock(int id, long minerId, String previousHash) {
    Integer magicNumber;
    String hash;
    String prefix;
    do {
      prefix = blockchain.getPrefix();
      magicNumber = random.nextInt();
      hash = StringUtil.applySha256(previousHash + magicNumber);
    } while (!hash.startsWith(prefix));
    return new Block(id, minerId, previousHash, hash, System.currentTimeMillis(), magicNumber);
  }
}
