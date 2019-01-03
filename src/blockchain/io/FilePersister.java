package blockchain.io;

import blockchain.Block;
import blockchain.data.SignedData;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FilePersister<T extends SignedData & Serializable> implements Persister<T> {


  private final String filename;

  public FilePersister(String filename) {
    this.filename = filename;
  }

  @Override
  public void save(List<Block<T>> blockchain) {
    try (FileOutputStream fileOutputStream = new FileOutputStream(filename);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
      objectOutputStream.writeObject(blockchain);
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

  @Override
  public List<Block<T>> load() {
    if (fileExists()) {
      try (FileInputStream fileInputStream = new FileInputStream(filename);
          ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
        return (List<Block<T>>) objectInputStream.readObject();
      } catch (IOException | ClassNotFoundException e) {
        return List.of();
      }
    }
    return List.of();
  }

  private boolean fileExists() {
    return Files.exists(Paths.get(filename));
  }
}
