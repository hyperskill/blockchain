package blockchain;

import blockchain.crypto.Sign;
import blockchain.data.Transaction;
import blockchain.data.format.TransactionPrintFormat;
import blockchain.io.FilePersister;
import blockchain.io.Persister;
import blockchain.io.Wallet;
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

    Persister<Transaction> persister = new FilePersister<>(OUTPUT_FILE_NAME);
    Blockchain<Transaction> blockchain = new Blockchain<>(persister, new TransactionPrintFormat(),
        new PayForMining(sign));

    List<Thread> miners = Stream
        .generate(() -> new Thread(new Miner<>(blockchain, minersIdGenerator.getAndIncrement())))
        .limit(NUMBER_OF_MINERS)
        .collect(Collectors.toList());

    miners.forEach(Thread::start);

    Wallet wallet = new Wallet(blockchain, sign);

    wallet.sendMoney("miner9", "miner1", 30);
    wallet.sendMoney("miner9", "miner2", 30);
    wallet.sendMoney("miner9", "Nick", 30);
    Thread.sleep(2000);
    wallet.sendMoney("miner9", "Bob", 10);
    wallet.sendMoney("miner7", "Alice", 10);
    wallet.sendMoney("Nick", "ShoesShop", 1);
    wallet.sendMoney("Nick", "FastFood", 2);
    wallet.sendMoney("Nick", "CarShop", 15);
    wallet.sendMoney("miner7", "CarShop", 90);
    Thread.sleep(3000);
    wallet.sendMoney("CarShop", "Worker1", 10);
    wallet.sendMoney("CarShop", "Worker2", 10);
    wallet.sendMoney("CarShop", "Worker3", 10);
    wallet.sendMoney("CarShop", "Director1", 30);
    wallet.sendMoney("CarShop", "CarPartsShop", 45);
    wallet.sendMoney("Bob", "GamingShop", 5);
    wallet.sendMoney("Alice", "BeautyShop", 5);

    miners.forEach(Thread::interrupt);
  }
}