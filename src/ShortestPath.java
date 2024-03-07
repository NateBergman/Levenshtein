import java.io.*; //Levenshtein by Nate Bergman
import java.util.*;
public class ShortestPath {
    public static void main (String[] args) throws FileNotFoundException {
        Map moveMap = new TreeMap<String,List<String>>(); //builds map of valid moves for each word from file
        Scanner lineScanner = new Scanner(new File("src/Moves"));
        while (lineScanner.hasNext()) {
            Scanner wordScanner = new Scanner(lineScanner.nextLine());
            String key = wordScanner.next();
            List<String> adjacents = new ArrayList<String>();
            wordScanner.useDelimiter(", |]| "); //I can't figure out how to use [ in the delimeter
            adjacents.add(wordScanner.next().substring(1)); //so I had to do this :(
            while (wordScanner.hasNext()) {
                adjacents.add(wordScanner.next());
            }
            moveMap.put(key,adjacents);
        }

        List<String> paths = new ArrayList<>();
        Queue<WordNode> queue = new LinkedList<>();
        Map<String,Integer> depths = new TreeMap<>();

        Scanner console = new Scanner(System.in); //gets starting and ending words
        System.out.print("Starting word : ");
        String first = console.next();
        queue.add(new WordNode(first,null));
        depths.put(first,0);
        System.out.print("Ending word : ");
        String end = console.next();

        int finalDepth = 20;
        while (!queue.isEmpty()) { //evaluation function - goes one guess at a time until we run out of moves/hit depth limit
            WordNode word = queue.element();
            int depth = depths.get(word.getWord());
            if (depth > finalDepth) {
                queue.clear();
                break;
            }
            String w = word.getWord();
            if (w.equals(end)) { //if this is an end condition we stop adding more guesses
                finalDepth = depth;
                paths.add(word.getPath());
            } else if (depth < finalDepth){ //otherwise, add all possible/good moves to the queue to be evaluated next
                List<String> moves = (List<String>) moveMap.get(w);
                for (String s : moves) {
                    if (!depths.keySet().contains(s) || depths.get(s) > depth) { //there is a list of all words we've previously visited at a lower depth - don't want longer paths to same words
                        queue.add(new WordNode(s,word)); //stores path and depth in the node
                        depths.put(s,depth + 1);
                    }
                }
            }
            queue.remove();
        }
        System.out.println("Depth: " + finalDepth); //displays results
        if (paths.isEmpty()) {
            System.out.println("No paths!");
        } else {
            System.out.println("Paths(" + paths.size() + "):\ndigraph something{concentrate=true;");
            for (String s : paths) {
                System.out.println(s);
            }
            System.out.print('}');
        }
    }
}