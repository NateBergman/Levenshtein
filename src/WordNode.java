import java.util.*;
public class WordNode {
    String word;
    int depth;
    List<String> path;
    public WordNode (String word, int depth, List<String> path) {
        this.word = word;
        this.depth = depth;
        this.path = new LinkedList<>(path);
        this.path.add(word);
    }
    public String getWord() {
        return word;
    }
    public int getDepth() {
        return depth;
    }
    public List<String> getPath() {
        return path;
    }
}
