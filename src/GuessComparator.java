import java.util.*;
public class GuessComparator implements Comparator<String> {
    String end;
    public GuessComparator(String end) {
        this.end = end;
    }
    public int compare(String s1, String s2) {
        int score1 = -1 * Math.abs(s1.length() - end.length());
        for (int i = 0; i < s1.length() && i < end.length(); i++) {
            if (s1.charAt(i) == end.charAt(i)) {
                score1++;
            }
        }

        int score2 = -1 * Math.abs(s2.length() - end.length());
        for (int i = 0; i < s2.length() && i < end.length(); i++) {
            if (s2.charAt(i) == end.charAt(i)) {
                score2++;
            }
        }

        return score1 - score2;
    }
}
