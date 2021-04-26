import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class LCS {

    static String[] tiles;
    static String target;
    static int infinity = 50001;
    static int[][] T;       // 2D table where T[i][j] = opt(i,j)
    static int[] solution;
    static int solIndx = 0;

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);

        target = in.nextLine();      // Proposed DNA Sequence
        int n = target.length();
        int k = Integer.parseInt(in.nextLine());     // Number of tiles

        T = new int[k + 1][n + 1];                  // Initialize T
        tiles = new String[k];

        for (int s = 0; s < k; s++) {
            tiles[s] = in.nextLine();
        }

        // Fill table T
        for (int i = 0; i <= k; i++) { // k = number of tiles
            for (int j = 0; j <= n; j++){
                // If there are no tiles (i == 0) & target string is empty (j == 0)
                if ((i == 0 && j == 0) || (j == 0 && i > 0)) {
                    T[i][j] = 0;
                }  
                // If there are no tiles (i == 0) & target is NOT empty (j > 0)
                else if (i == 0 && j > 0) {
                    T[i][j] = infinity;
                }
                else {
                    T[i][j] = opt(i, j);
                }
            }
        }


        if (T[k][n] == infinity) {
            System.out.println(0);
        }
        else {
            sol(T.length - 1, T[0].length - 1);   
            System.out.print(solIndx + " ");
            while (solIndx > 0) {
                System.out.print(solution[solIndx - 1] + " ");
                solIndx--;
            }
        }
        in.close();
    }

    // Optimal # of tiles b/w S1 ... Si that cover the first n characters of target: t[0...j)
    // where n = length of Si
    public static int opt(int i, int j) {

        // If there are tiles (i > 0) & Si DOES match the tail of target[0...j)
        if (tiles[i - 1].length() <= j && tiles[i - 1].regionMatches(0, target, j-tiles[i-1].length(), tiles[i - 1].length())) {
            return Math.min(T[i - 1][j], 1 + T[i - 1][j - tiles[i - 1].length()]);
        }
        // If there are tiles (i > 0) & Si does NOT match the tail of target[0...j)
        return T[i - 1][j];
    }
    
    public static void sol(int i, int j) {
        
        solution = new int[i];
        
        while (i > 0 && j > 0) {
            // Find solution
            if (T[i][j] != T[i - 1][j]){
                solution[solIndx] = i;
                solIndx++;
                j -= tiles[i - 1].length();
            }
            i--;
        }
    }
}

