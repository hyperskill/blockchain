package blockchain;

import blockchain.crypto.Sign;
import blockchain.crypto.SignValidator;
import blockchain.data.format.DataFormatter;
import blockchain.io.Persister;
import blockchain.utils.StringUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Blockchain<T> {

  private static final Long ACCEPTABLE_TIME = TimeUnit.SECONDS.toMillis(3);

  private final List<Block> blocks;
  private final List<T> pendingData;
  private final Persister persister;
  private final DataFormatter<T> dataFormatter;
  private final Sign sign;
  private Function<String, T> extraData;
  private SignValidator signValidator;

  private int prefixLength;

  public Blockchain(Persister persister,
      DataFormatter<T> dataFormatter, Sign sign, Function<String, T> extraData,
      SignValidator signValidator) {
    this.persister = persister;
    this.dataFormatter = dataFormatter;
    this.sign = sign;
    this.extraData = extraData;
    this.signValidator = signValidator;
    this.prefixLength = 0;
    List<Block> blocks = persister.load();
    if (!blocks.isEmpty() && validateAllChain(blocks)) {
      this.blocks = blocks;
    } else {
      this.blocks = new LinkedList<>(Collections.singleton(
          new Block(0, "0", "0", StringUtil.applySha256("0" + 0),
              "",
              null, System.currentTimeMillis(), 0, 0L)));
      persister.save(blocks);
    }
    pendingData = new LinkedList<>();
  }

  public synchronized void accept(Block newBlock) {
    if (isValid(newBlock)) {
      outputAndAdjust(newBlock);
      blocks.add(newBlock);
      pendingData.clear();
      T earning = extraData.apply(newBlock.getMinerId());
      pendingData.add(earning);
      persister.save(blocks);
    }
  }

  public synchronized void appendData(T data) {
    pendingData.add(data);
  }

  public synchronized String getPrefix() {
    return Stream.iterate("0", x -> "0")
        .limit(prefixLength)
        .reduce("", (x, y) -> x + y);
  }

  public synchronized String pendingData() {
    StringJoiner stringJoiner = new StringJoiner("\n");
    if (pendingData.isEmpty()) {
      return "";
    }
    pendingData.stream()
        .map(dataFormatter::format)
        .forEach(stringJoiner::add);
    return stringJoiner.toString();
  }

  public synchronized Block tail() {
    return blocks.get(blocks.size() - 1);
  }

  public synchronized List<T> dataFromBlockchain() {
    return Stream.concat(pendingData.stream(), blocks.stream()
        .filter(b -> {
          try {
            return signValidator.verifySignature(b.getData().getBytes(), b.getDataSignature());
          } catch (Exception e) {
            return false;
          }
        })
        .map(Block::getData)
        .flatMap(x -> Arrays.stream(x.split("\n")))
        .filter(x -> !x.isEmpty())
        .map(dataFormatter::parse))
        .collect(Collectors.toList());
  }

  private boolean isValid(Block newBlock) {
    try {
      Block tailBlock = blocks.get(blocks.size() - 1);
      if (isBlocksNotRelatedOrLowProof(newBlock, tailBlock)) {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  private boolean isBlocksNotRelatedOrLowProof(Block newBlock, Block tailBlock) {
    return !newBlock.getHash().startsWith(getPrefix()) || !newBlock.getHashPreviousBlock()
        .equals(tailBlock.getHash());
  }

  private void outputAndAdjust(Block newBlock) {
    addPendingData(newBlock);
    outputStats(newBlock);
    adjustComplexity(newBlock);
  }

  private void addPendingData(Block newBlock) {
    String data = pendingData();
    newBlock.setData(data);
    try {
      newBlock.setDataSignature(sign.sign(data));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void outputStats(Block newBlock) {
    System.out.printf("Block:\n");
    System.out.printf("Id: %s\n", newBlock.getId());
    System.out.printf("Created by %s\n", newBlock.getMinerId());
    System.out.printf("%s gets 100 VC\n", newBlock.getMinerId());
    System.out.printf("Timestamp: %s\n", newBlock.getTimestamp());
    System.out.printf("Magic number: %s\n", newBlock.getMagicNumber());
    System.out.printf("Hash of the previous block:\n%s\n", newBlock.getHashPreviousBlock());
    System.out.printf("Block data: \n%s\n", newBlock.getData());
    System.out.printf("Magic number: %s\n", newBlock.getMagicNumber());
    System.out
        .printf("Block was generating for: %s milliseconds\n", newBlock.getGenerationTime());
  }

  private void adjustComplexity(Block newBlock) {
    if (!withinAcceptable(newBlock.getGenerationTime())) {
      if (ACCEPTABLE_TIME < newBlock.getGenerationTime()) {
        if (prefixLength - 1 >= 0) {
          prefixLength--;
        }
        System.out.printf("N was decreased to %s\n\n", prefixLength);
      } else {
        prefixLength++;
        System.out.printf("N was increased to %s\n\n", prefixLength);
      }
    } else {
      System.out.printf("N stays the same\n\n");
    }
  }

  private boolean withinAcceptable(Long generationTime) {
    return Math.abs(generationTime - ACCEPTABLE_TIME) <= 500;
  }

  private boolean validateAllChain(List<Block> blocks) {
    for (int i = 1; i < blocks.size(); i++) {
      Block prev = blocks.get(i - 1);
      Block cur = blocks.get(i);
      if (!cur.getHashPreviousBlock().equals(prev.getHash())) {
        return false;
      }
    }
    return true;
  }

}
