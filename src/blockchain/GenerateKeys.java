package blockchain;

import java.security.*;


class GenerateKeys {

    private KeyPairGenerator keyGen;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public GenerateKeys(int keylenth) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keylenth);
    }

    public void createKeys(){
        KeyPair pair;
        pair = this.keyGen.generateKeyPair();
        this.privateKey  = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey(){
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

}
