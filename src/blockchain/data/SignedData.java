package blockchain.data;

public interface SignedData {

  Integer id();

  byte [] raw();

  byte[] dataSignature();

  byte[] publicKey();
}
