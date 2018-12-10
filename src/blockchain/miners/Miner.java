package blockchain.miners;

import blockchain.Block;
import blockchain.Blockchain;
import blockchain.crypto.Sign;
import blockchain.utils.StringUtil;
import java.util.Random;

public class Miner implements Runnable {

  private final Blockchain<?> blockchain;
  private final String id;
  private Sign sign;
  private final Random random;

  public Miner(Blockchain<?> blockchain, String id, Sign sign) {
    this.blockchain = blockchain;
    this.id = id;
    this.sign = sign;
    this.random = new Random();
  }

  @Override
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      long timestamp = System.currentTimeMillis();
      Block prev;
      String prefix;
      String data;
      int magicNumber;
      String hash;
      do {
        prev = blockchain.tail();
        prefix = blockchain.getPrefix();
        data = blockchain.pendingData();
        magicNumber = random.nextInt();
        String dataUsedForHash = prev.getHash() + magicNumber + data;
        hash = StringUtil.applySha256(dataUsedForHash);
      } while (!hash.startsWith(prefix));
      Block newBlock = null;
      try {
        newBlock = new Block(prev.getId() + 1, id, prev.getHash(), hash, data,
            sign.sign(data), timestamp, magicNumber, System.currentTimeMillis() - timestamp);
      } catch (Exception e) {
        Thread.currentThread().interrupt();
      }
      blockchain.accept(newBlock);
    }
  }
}
