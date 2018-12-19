package blockchain;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Blockchain implements Iterable<Block> {

  private final List<Block> blocks;
  private final Random random;
  private final String prefix;
  private final Persister persister;

  public Blockchain(int N, Persister persister) {
    this.persister = persister;
    this.random = new Random();
    this.prefix = Stream.iterate("0", x -> "0")
        .limit(N)
        .reduce("", (x, y) -> x + y);

    List<Block> blocks = persister.load();
    if (!blocks.isEmpty() && validate(blocks)) {
      this.blocks = blocks;
    } else {
      this.blocks = new LinkedList<>(Collections.singleton(
          generateNextBlock(1, "0")));
      persister.save(blocks);
    }
  }


  public void generate() {
    Block tailBlock = blocks.get(blocks.size() - 1);
    Block newBlock = generateNextBlock(tailBlock.getId() + 1, tailBlock.getHash());
    blocks.add(newBlock);
    persister.save(blocks);
  }

  public boolean validate() {
    return validate(blocks);
  }

  @Override
  public Iterator<Block> iterator() {
    return blocks.iterator();
  }

  private boolean validate(List<Block> blocks) {
    for (int i = 1; i < blocks.size(); i++) {
      Block prev = blocks.get(i - 1);
      Block cur = blocks.get(i);
      if (!cur.getHashPreviousBlock().equals(prev.getHash())) {
        return false;
      }
    }
    return true;
  }

  private Block generateNextBlock(int id, String hashPreviousBlock) {
    long timestamp = System.currentTimeMillis();
    Integer magicNumber;
    String hash;
    do {
      magicNumber = random.nextInt();
      hash = StringUtil.applySha256(hashPreviousBlock + magicNumber);
    } while (!hash.startsWith(prefix));
    return new Block(id, hashPreviousBlock, hash, timestamp, magicNumber,
        System.currentTimeMillis() - timestamp);
  }
}
