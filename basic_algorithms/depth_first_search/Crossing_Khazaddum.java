import java.io.*;
import java.util.*;

// Data structures for representing a graph
class Edge {
    int succ;
    Edge next;

    Edge(int succ, Edge next) {
        this.succ = succ;        // Successor vertex
        this.next = next;        // Adjacency list (next vertex u has an edge to)
    }
}

class Graph {
    Edge[] A;
    // A[u] points to the head of a linked list;
    // p in the list corresponds to an edge u -> p.succ in the graph

    // Initialize a graph with n vertices and no edges
    Graph(int n) {
        A = new Edge[n];
    }

    // Add an edge u -> v to the graph
    void addEdge(int u, int v) {
        A[u] = new Edge(v, A[u]);
    }
}

public class Crossing_Khazaddum {

    static Graph graph;     // Global variable representing the graph
    static int[] parent;    // Global variable representing the parent of each node in the DFS forest
    static int[] color;     // Global variable storing the color of each node during DFS
                                // 0 = white (unvisited), 1 = gray (visited), 2 = black (finished)
    static boolean result = false;
    static int loopedAt = -1;

    // Performs a recursive DFS, starting at u
    static void recDFS(int u) {
        // Mark current node as visited
        color[u] = 1;
        Edge current = graph.A[u];

        while (current != null && result == false) {
            parent[current.succ] = u;
            // Check if current node is unvisited
            if (color[current.succ] == 0) {
                int succ = current.succ;
                // Recurse on successor
                recDFS(succ);
            }
            // If visited --> end loop
            else if (color[current.succ] == 1) {
                loopedAt = u;
                result = true;
            }
            else {
            	current = current.next;
            }
        }
        color[u] = 2;
    }

    // Performs a "full" DFS on graph
    static void DFS() {
        int u = 1;
        while (u < color.length && result == false) {
            // Check if current node is unvisited
            if (color[u] == 0) {
                // Perform recDFS on unvisited node
                recDFS(u);
            }
            u++;
        }
    }

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);
        int n = in.nextInt() + 1;    // Number of nodes in graph (+ 1)

        graph = new Graph(n);       // Initialize graph
        parent = new int[n];        // Initialize parent array
        color = new int[n];            // Initialize color array

        int m = in.nextInt();        // Number of edges
        // Add edges to graph
        while (m > 0) {
            int u = in.nextInt();
            int v = in.nextInt();
            graph.addEdge(u, v);
            m--;
        }

        // Produce output
        // If there's a loop --> print the loop
        DFS();

        if (result == true) {
            System.out.println(1);
            String output = Integer.toString(loopedAt);
            int current = parent[loopedAt];
            while (current != loopedAt) {
                output = current + " " + output;
                current = parent[current];
            }
            System.out.println(output);
        }
        else {
            System.out.println(0);
        }
        in.close();
    }
}
