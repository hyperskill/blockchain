package blockchain;

import java.io.Serializable;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;


class Message implements Serializable{
    private List<byte[]> list;
    private long id;

    //The constructor of Message class builds the list that will be written to the file.
    //The list consists of the message and the signature.
    public Message(String data, long id) throws Exception {
        list = new ArrayList<>();
        list.add(data.getBytes());
        list.add(sign(data));
        this.id = id;
    }

    //The method that signs the data using the private key that is stored in keyFile path
    public byte[] sign(String data) throws Exception{
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(Main.gk.getPrivateKey());
        rsa.update(data.getBytes());
        return rsa.sign();
    }

    //Method for signature verification that initializes with the Public Key,
    //updates the data to be verified and then verifies them using the signature
    public boolean verifySignature(byte[] data, byte[] signature) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(Main.gk.getPublicKey());
        sig.update(data);
        return sig.verify(signature);
    }

    public List<byte[]> getList() {
        return list;
    }

    public long getId(){
        return this.id;
    }
}
