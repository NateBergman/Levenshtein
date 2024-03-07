import java.util.*;
public class WordNode { //really just a compact way of storing everything important for a guess in a queue
    String word;
    WordNode previous;
    public WordNode (String word, WordNode previous) {
        this.word = word;
        this.previous = previous;
    }
    public String getWord() {
        return word;
    }
    public String getPath() {
        String path = "";
        if (previous == null) {
            path += word;
        } else {
            path += previous.getPath();
            path += " -> " + word;
        }
        return path;
    }
}