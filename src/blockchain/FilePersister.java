package blockchain;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class FilePersister implements Persister {


  private final String filename;

  public FilePersister(String filename) {
    this.filename = filename;
  }

  @Override
  public void save(List<Block> blockchain) {
    try (FileOutputStream fileOutputStream = new FileOutputStream(filename);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
      objectOutputStream.writeObject(blockchain);
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

  @Override
  public List<Block> load() {
    if (fileExists()) {
      try (FileInputStream fileInputStream = new FileInputStream(filename);
          ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
        return (List<Block>) objectInputStream.readObject();
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
