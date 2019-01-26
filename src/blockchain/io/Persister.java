package blockchain.io;

import blockchain.Block;
import blockchain.data.SignedData;
import java.io.Serializable;
import java.util.List;

public interface Persister<T extends SignedData & Serializable> {

  void save(List<Block<T>> blockchain);

  List<Block<T>> load();
}

