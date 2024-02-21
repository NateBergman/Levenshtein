// Make it work with monkey to business
// need to record paths I've already taken
public class Main {
    public static void main(String[] args) {

    }
    public static boolean isAdjacent (String s1, String s2) {
        if (s1.length() == s2.length()) {
            boolean mismatch = false;
            for (int i = 0; i < s1.length(); i++) {
                if (s1.charAt(i) != s2.charAt(i)) {
                    if (mismatch) {
                        return false;
                    } else {
                        mismatch = true;
                    }
                }
            }
        } else {
            if (s1.length() < s2.length()) { //make sure 1 is always longer
                String s3 = s2;
                s2 = s1;
                s1 = s3;
            }
            int i = 0;
            int j = 0;
            boolean mismatch = false;
            while (j < s2.length()) {
                if (s1.charAt(i) != s2.charAt(j)) {
                    if (mismatch) {
                        return false;
                    } else {
                        mismatch = true;
                        j--;
                    }
                }
                i++;
                j++;
            }
        }
        return true;
    }
}