package blockchain.data.format;

import blockchain.data.Message;

public class PlanMessageFormat implements DataFormatter<Message> {

  @Override
  public String format(Message message) {
    return String.format("%s: %s", message.getAuthor(), message.getText());
  }

  @Override
  public Message parse(String data) {
    String[] dat = data.split(":");
    return new Message(dat[0], dat[1].trim());
  }
}
