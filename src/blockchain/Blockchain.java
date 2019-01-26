package blockchain;

import blockchain.crypto.SignValidator;
import blockchain.data.SignedData;
import blockchain.data.format.DataFormatter;
import blockchain.io.Persister;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Blockchain<T extends SignedData & Serializable> {

  private static final Long ACCEPTABLE_TIME = TimeUnit.SECONDS.toMillis(5);

  private final ReentrantReadWriteLock readWritelock;

  private final List<Block<T>> blocks;
  private final List<T> pendingMessages;
  private final Persister persister;

  private int prefixLength;
  private DataFormatter<T> dataFormatter;

  private AtomicInteger idGenerator;
  private BiFunction<Integer, String, Optional<T>> systemFeedback;

  public Blockchain(Persister<T> persister, DataFormatter<T> dataFormatter,
      SystemFeedback<T> systemFeedback) {
    this.systemFeedback = systemFeedback;
    this.readWritelock = new ReentrantReadWriteLock();
    this.persister = persister;
    this.dataFormatter = dataFormatter;
    this.prefixLength = 0;
    List<Block<T>> fileBlocks = persister.load();
    if (!fileBlocks.isEmpty() && validateAllChain(fileBlocks)) {
      this.blocks = fileBlocks;
    } else {
      this.blocks = new LinkedList<>(Collections.singleton(
          new BlockBuilder<T>()
              .withId(1)
              .withMinerId(0L)
              .withHashPreviousBlock("")
              .withHash("0")
              .withMagicNumber(0)
              .withTimestamp(new Date().getTime()).build()));
      persister.save(fileBlocks);
    }
    pendingMessages = new LinkedList<>();
    this.idGenerator = new AtomicInteger(blocks.get(blocks.size() - 1).getId() + 1);
  }

  public void accept(Block<T> newBlock) {
    WriteLock writeLock = readWritelock.writeLock();
    try {
      writeLock.lock();
      if (isValid(newBlock)) {
        applyFeedback(String.format("miner%d", newBlock.getMinerId()));
        newBlock.setData(new ArrayList<>(pendingMessages));
        outputAndAdjust(newBlock);
        blocks.add(newBlock);
        pendingMessages.clear();
        persister.save(blocks);
      }
    } finally {
      writeLock.unlock();
    }
  }

  public boolean appendData(T data) {
    WriteLock writeLock = readWritelock.writeLock();
    try {
      writeLock.lock();
      if (isSigned(data) && dataIdentityValid(data)) {
        return pendingMessages.add(data);
      }
    } finally {
      writeLock.unlock();
    }
    return false;
  }

  public String getPrefix() {
    ReadLock readLock = readWritelock.readLock();
    try {
      readLock.lock();
      return Stream.iterate("0", x -> "0")
          .limit(prefixLength)
          .reduce("", (x, y) -> x + y);
    } finally {
      readLock.unlock();
    }
  }

  public Block<T> tail() {
    ReadLock readLock = readWritelock.readLock();
    try {
      readLock.lock();
      return blocks.get(blocks.size() - 1);
    } finally {
      readLock.unlock();
    }
  }

  public Integer uniqueIdentity() {
    return idGenerator.incrementAndGet();
  }

  public Stream<T> data() {
      return new ArrayList<>(blocks).stream()
          .map(Block::getData)
          .filter(List::isEmpty)
          .flatMap(Collection::stream);
  }

  private boolean isSigned(T data) {
    try {
      SignValidator signValidator = new SignValidator(data.publicKey());
      return signValidator.verifySignature(data.raw(), data.dataSignature());
    } catch (Exception e) {
      return false;
    }
  }

  private boolean isValid(Block newBlock) {
    Block tailBlock = blocks.get(blocks.size() - 1);
    if (!newBlock.getHash().startsWith(getPrefix()) || !Objects
        .equals(newBlock.getHashPreviousBlock(), tailBlock.getHash())) {
      return false;
    }
    return true;
  }

  private void outputAndAdjust(Block<T> newBlock) {
    outputStats(newBlock);
    adjustComplexity(newBlock);
  }

  private void outputStats(Block<T> newBlock) {
    System.out.printf("Block:\n");
    System.out.printf("Id: %s\n", newBlock.getId());
    System.out.printf("Created by miner # %s\n", newBlock.getMinerId());
    System.out.printf("Timestamp: %s\n", newBlock.getTimestamp());
    System.out.printf("Magic number: %s\n", newBlock.getMagicNumber());
    System.out.printf("Hash of the previous block:\n%s\n", newBlock.getHashPreviousBlock());
    System.out.printf("Block data: \n%s\n", newBlock.getData().stream().map(
        x -> dataFormatter.format(x)
    ).collect(Collectors.joining("\n")));
    System.out.printf("Magic number: %s\n", newBlock.getMagicNumber());
    System.out
        .printf("Block was generating for: %s seconds\n", getGenerationTime(newBlock) / 1000);
  }

  private void adjustComplexity(Block<T> newBlock) {
    if (!withinAcceptable(newBlock)) {
      if (ACCEPTABLE_TIME < getGenerationTime(newBlock)) {
        if (prefixLength > 0) {
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

  private Long getGenerationTime(Block<?> newBlock) {
    return tail() == null ? 0 : (newBlock.getTimestamp() - tail().getTimestamp());
  }

  private boolean withinAcceptable(Block<?> newBlock) {
    return Math.abs(getGenerationTime(newBlock) - ACCEPTABLE_TIME) <= 1000;
  }

  private boolean validateAllChain(List<Block<T>> blocks) {
    for (int i = 1; i < blocks.size(); i++) {
      Block<T> prev = blocks.get(i - 1);
      Block<T> cur = blocks.get(i);
      Stream<T> concatData = Stream.concat(
          Objects.requireNonNullElse(prev.getData(), new ArrayList<T>()).stream(),
          Objects.requireNonNullElse(cur.getData(), new ArrayList<T>()).stream());
      if (!Objects.equals(prev.getHash(), cur.getHashPreviousBlock())
          && dataIdentityValid(concatData.collect(Collectors.toList()))) {
        return false;
      }
    }
    return true;
  }

  private boolean dataIdentityValid(List<T> jointBlocks) {
    for (int i = 1; i < jointBlocks.size(); i++) {
      if (Objects.compare(jointBlocks.get(i - 1), jointBlocks.get(i),
          Comparator.comparing(T::id)) > 0) {
        return false;
      }
    }
    return true;
  }

  private boolean dataIdentityValid(T data) {
    Optional<T> lastEntry = getLastEntry();
    return lastEntry
        .map(t -> t.id() < data.id())
        .orElse(true);
  }

  private Optional<T> getLastEntry() {
    return blocks.stream()
        .map(Block::getData)
        .filter(List::isEmpty)
        .flatMap(Collection::stream)
        .limit(1)
        .findFirst();
  }

  private void applyFeedback(String miner) {
    Optional<T> feedback = systemFeedback.apply(uniqueIdentity(), miner);
    feedback.ifPresent(
        this::appendData
    );
  }
}
