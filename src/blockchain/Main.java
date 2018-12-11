package blockchain;

import blockchain.crypto.Sign;
import blockchain.crypto.SignValidator;
import blockchain.currency.AccountTransactions;
import blockchain.data.Transaction;
import blockchain.data.format.CSVTransactionFormat;
import blockchain.io.FilePersister;
import blockchain.io.Persister;
import blockchain.miners.Miner;

public class Main {

  private static final String OUTPUT_FILE_NAME = "blockchain.ser";
  private static final int NUMBER_OF_MINERS = 10;

  public static void main(String[] args) throws Exception {

    Sign sign = new Sign("keys/privateKey");
    SignValidator signValidator = new SignValidator("keys/publicKey");

    Persister persister = new FilePersister(OUTPUT_FILE_NAME);
    Blockchain<Transaction> blockchain = new Blockchain<>(persister,
        new CSVTransactionFormat(), sign, id -> new Transaction("System", id, 100.0),
        signValidator);

    Thread[] miners = new Thread[NUMBER_OF_MINERS];

    for (int i = 0; i < NUMBER_OF_MINERS; i++) {
      miners[i] = new Thread(
          new Miner(blockchain, String.format("miner%s", String.valueOf(i))));
    }
    for (Thread miner : miners) {
      miner.start();
    }

    Thread.sleep(3000);
    AccountTransactions transactions = new AccountTransactions(blockchain);

    if (transactions.walletBallance("miner9") > 30.0) {
      blockchain.appendData(new Transaction("miner9", "miner1", 30.0));
    }
    if (transactions.walletBallance("miner9") > 0) {
      blockchain.appendData(new Transaction("miner9", "miner2", 30.0));
    }
    if (transactions.walletBallance("miner9") > 0) {
      blockchain.appendData(new Transaction("miner9", "Nick", 30.0));
    }
    if (transactions.walletBallance("miner9") > 30.0) {
      blockchain.appendData(new Transaction("miner9", "Other", 30.0));
    }

    Thread.sleep(10000);

    for (int i = 0; i < NUMBER_OF_MINERS; i++) {
      String minerId = String.format("miner%s", String.valueOf(i));
      Double b = transactions.walletBallance(minerId);
      System.out.println(minerId + " has " + b + " VC");
    }

    for (Thread miner : miners) {
      miner.interrupt();
    }
  }
}