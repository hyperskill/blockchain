package blockchain.data.format;

public interface DataFormatter<T> {

  String format(T message);
}
