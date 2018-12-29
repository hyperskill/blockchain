package blockchain.data;

public class Message {

  private final String author;
  private final String text;

  public Message(String author, String text) {
    this.author = author;
    this.text = text;
  }

  public String getAuthor() {
    return author;
  }

  public String getText() {
    return text;
  }
}
