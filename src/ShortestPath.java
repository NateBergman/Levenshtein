import java.io.*;
import java.util.*;
public class ShortestPath {
    public static void main (String[] args) throws FileNotFoundException {
        Map moveMap = new TreeMap<String,List<String>>();
        Scanner lineScanner = new Scanner(new File("src/Moves"));
        while (lineScanner.hasNext()) { //builds move map
            Scanner wordScanner = new Scanner(lineScanner.nextLine());
            String key = wordScanner.next();
            List<String> adjacents = new ArrayList<String>();
            wordScanner.useDelimiter(", |]| ");
            adjacents.add(wordScanner.next().substring(1));
            while (wordScanner.hasNext()) {
                adjacents.add(wordScanner.next());
            }
            moveMap.put(key,adjacents);
        }

        Set<String> previous = new HashSet<>();
        List<List<String>> finalPaths = new ArrayList<>();
        Queue<WordNode> queue = new LinkedList<>();

        Scanner console = new Scanner(System.in);
        System.out.print("Starting word : ");
        queue.add(new WordNode(console.next(),0,new LinkedList<String>()));
        System.out.print("Ending word : ");
        String end = console.next();

        boolean found = false;
        while (!queue.isEmpty()) { //evaluation function
            WordNode word = queue.element();
            if (word.getWord().equals(end)) {
                found = true;
                finalPaths.add(word.getPath());
            } else if (!found){
                //add all legal moves to queue
            }
            queue.remove();
        }
        System.out.println("Paths : " + finalPaths);
    }
}
