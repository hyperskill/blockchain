package blockchain;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

public class Blockchain implements Iterable<Block> {

  private final LinkedList<Block> blocks;

  public Blockchain() {
    blocks = new LinkedList<>(Collections.singleton(
        new Block(1,
            "0",
            StringUtil.applySha256("0"),
            new Date().getTime()))
    );
  }

  public void generate() {
    Long timestamp = new Date().getTime();
    Block tailBlock = blocks.getLast();
    String hash = StringUtil.applySha256(tailBlock.getHash());
    blocks.add(new Block(tailBlock.getId() + 1, tailBlock.getHash(), hash, timestamp));
  }

  public boolean validate() {
    for (int i = 1; i < blocks.size(); i++) {
      Block prev = blocks.get(i - 1);
      Block cur = blocks.get(i);
      if (!cur.getHashPreviousBlock().equals(prev.getHash())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Iterator<Block> iterator() {
    return blocks.iterator();
  }
}
