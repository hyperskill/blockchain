package blockchain.crypto;

import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class SignValidator {

  private PublicKey publicKey;

  public SignValidator(String publicKeyFilePath) throws Exception {
    this(Files.readAllBytes(new File(publicKeyFilePath).toPath()));
  }

  public SignValidator(byte[] publicKeyBytes) throws Exception {
    X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    publicKey = kf.generatePublic(spec);
  }

  public boolean verifySignature(byte[] data, byte[] signature) throws Exception {
    Signature sig = Signature.getInstance("SHA1withRSA");
    sig.initVerify(publicKey);
    sig.update(data);
    return sig.verify(signature);
  }
}
