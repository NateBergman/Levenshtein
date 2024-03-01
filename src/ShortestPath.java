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
        int depth = 0;
        Set<String> tempPrevious = new HashSet<>();
        while (!queue.isEmpty() && depth < 20) { //evaluation function
            WordNode word = queue.element();
            if (word.getDepth() > depth) {
                depth = word.getDepth();
                previous.addAll(tempPrevious);
                tempPrevious.clear();
            }
            if (word.getWord().equals(end)) {
                found = true;
                finalPaths.add(word.getPath());
            } else if (!found){
                List<String> moves = (List<String>) moveMap.get(word.getWord());
                for (String s : moves) {
                    if (!previous.contains(s)) {
                        queue.add(new WordNode(s,depth + 1,word.getPath()));
                        tempPrevious.add(s);
                    }
                }
            }
            queue.remove();
        }
        System.out.println("Depth (inclusive): " + depth);
        if (finalPaths.isEmpty()) {
            System.out.println("No paths!");
        } else {
            System.out.println("Paths : " + finalPaths);
        }
    }
}
