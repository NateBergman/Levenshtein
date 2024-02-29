import java.io.*;
import java.util.*;
public class ShortestPath {
    public static void main (String[] args) throws FileNotFoundException {
        Map moveMap = new TreeMap<String,List<String>>();
        Scanner lineScanner = new Scanner(new File("src/Moves"));
        while (lineScanner.hasNext()) {
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
        List<List<String>> paths = new ArrayList<>();
        paths.add(new ArrayList<>());
        List<List<String>> finalPaths = new ArrayList<>();

        Scanner console = new Scanner(System.in);
        System.out.print("Starting word : ");
        paths.get(0).add(console.next());
        System.out.print("Ending word : ");
        String end = console.next();

        int depth = 0;
        boolean found = false;
        while (depth < 20 && !found) {
            List<List<String>> newPaths = new ArrayList<>();
            Set<String> newPrevious = new HashSet<>();
            for (int i = 0; i < paths.size(); i++) {
                List<String> currentPath = paths.get(i);
                String word = currentPath.get(currentPath.size() - 1);

                List<String> neighbors = (List<String>) moveMap.get(word);
                for (String s : neighbors) {
                    newPrevious.add(s);
                    List<String> l = new ArrayList<String>(currentPath);
                    l.add(s);
                    newPaths.add(l);
                    if (s.equals(end)) {
                        found = true;
                        finalPaths.add(l);
                    }
                }
            }
            paths = newPaths;
            previous.addAll(newPrevious);
            depth++;
        }

        System.out.println("Distance : " + depth);
        System.out.println("Paths : " + finalPaths);
    }
}
