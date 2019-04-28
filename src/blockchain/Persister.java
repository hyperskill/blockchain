package blockchain;

import java.util.List;

public interface Persister {

  void save(List<Block> blockchain);

  List<Block> load();
}

