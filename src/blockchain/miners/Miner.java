package blockchain.miners;

import blockchain.Block;
import blockchain.Blockchain;
import blockchain.utils.StringUtil;
import java.util.Random;

public class Miner implements Runnable {

  private final Blockchain<?> blockchain;
  private final String id;
  private final Random random;

  public Miner(Blockchain<?> blockchain, String id) {
    this.blockchain = blockchain;
    this.id = id;
    this.random = new Random();
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      long timestamp = System.currentTimeMillis();

      Block prev;
      String prefix;
      int magicNumber;
      String hash;

      do {
        prev = blockchain.tail();
        prefix = blockchain.getPrefix();
        magicNumber = random.nextInt();
        String dataUsedForHash = prev.getHash() + magicNumber;
        hash = StringUtil.applySha256(dataUsedForHash);
      } while (!hash.startsWith(prefix));

      Block newBlock = new Block(prev.getId() + 1, id, prev.getHash(),
          hash, timestamp, magicNumber, System.currentTimeMillis() - timestamp);
      blockchain.accept(newBlock);
    }
  }
}
