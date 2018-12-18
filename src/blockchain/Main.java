package blockchain;

public class Main {

  public static void main(String[] args) {
    Blockchain blockchain = new Blockchain();
    for (int i = 0; i < 10; i++) {
      blockchain.generate();
    }

    for (Block block : blockchain) {
      System.out.printf("Block:\n");
      System.out.printf("Id: %s\n", block.getId());
      System.out.printf("Timestamp: %s\n", block.getTimestamp());
      System.out.printf("Hash of the previous block:\n%s\n", block.getHashPreviousBlock());
      System.out.printf("Hash of the block: \n%s\n\n", block.getHash());
    }
  }
}