package blockchain;

import blockchain.crypto.Sign;
import blockchain.data.Message;
import blockchain.data.format.PlainMessageFormat;
import blockchain.io.FilePersister;
import blockchain.io.Messenger;
import blockchain.io.Persister;
import blockchain.miners.Miner;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

  private static final String OUTPUT_FILE_NAME = "blockchain.ser";
  private static final int NUMBER_OF_MINERS = 10;
  private final static AtomicLong minersIdGenerator = new AtomicLong(1);

  public static void main(String[] args) throws Exception {

    Sign sign = new Sign("keys/privateKey", "keys/publicKey");

    Persister<Message> persister = new FilePersister<>(OUTPUT_FILE_NAME);
    Blockchain<Message> blockchain = new Blockchain<>(persister,
        new PlainMessageFormat());

    List<Thread> miners = Stream
        .generate(() -> new Thread(new Miner<>(blockchain, minersIdGenerator.getAndIncrement())))
        .limit(NUMBER_OF_MINERS)
        .collect(Collectors.toList());

    miners.forEach(Thread::start);

    Messenger messenger = new Messenger(blockchain, sign);

    Thread.sleep(1000);
    messenger.sendMessage("Tom", "Hey, I'm first!");
    messenger.sendMessage("Sarah", "It's not fair!");
    Thread.sleep(2000);
    messenger.sendMessage("Sarah", "You always will be first because it is your blockchain!");
    Thread.sleep(1000);
    messenger.sendMessage("Sarah", "Anyway, thank you for this amazing chat.");
    Thread.sleep(2000);
    messenger.sendMessage("Tom", "You're welcome :)");
    messenger.sendMessage("Nick", "Hey Tom, nice chat");

    miners.forEach(Thread::interrupt);
  }
}