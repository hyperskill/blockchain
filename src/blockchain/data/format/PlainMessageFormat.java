package blockchain.data.format;

import blockchain.data.Message;

public class PlainMessageFormat implements DataFormatter<Message> {

  @Override
  public String format(Message message) {
    return String.format("%s: %s", message.getAuthor(), message.getText());
  }
}
