package blockchain.io;

import blockchain.Block;
import java.util.List;

public interface Persister {

  void save(List<Block> blockchain);

  List<Block> load();
}

