package blockchain;

import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    try (Scanner scanner = new Scanner(System.in)) {
      System.out.print("Enter how many zeros the hash must starts with: ");

      Persister persister = new FilePersister("blockchain.ser");
      Blockchain blockchain = new Blockchain(scanner.nextInt(), persister);

      for (int i = 0; i < 10; i++) {
        blockchain.generate();
      }

      for (Block block : blockchain) {
        System.out.printf("Block:\n");
        System.out.printf("Id: %s\n", block.getId());
        System.out.printf("Timestamp: %s\n", block.getTimestamp());
        System.out.printf("Magic number: %s\n", block.getMagicNumber());
        System.out.printf("Hash of the previous block:\n%s\n", block.getHashPreviousBlock());
        System.out.printf("Hash of the block: \n%s\n", block.getHash());
        System.out
            .printf("Block was generating for: %s seconds\n\n", block.getGenerationTime() / 1000);
      }
    }
  }
}