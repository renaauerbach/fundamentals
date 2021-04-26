import java.io.*;
import java.util.*;

public class Spartan {

    static BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out), 4096);
    // static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static HashMap<String, Soldier> soldiers = new HashMap<>();

    public static void main(String[] args) throws IOException {

        long start = System.nanoTime();

        File file = new File(args[0]);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        //Number of soldiers
        int n = Integer.parseInt(reader.readLine());
        MinHeap minHeap = new MinHeap(n);

        //Fill the HashMap
        for (int i = 0; i < n; i++) {
            String[] line = reader.readLine().split(" ");

            Soldier s = new Soldier(line[0], Long.parseLong(line[1]));
            soldiers.put(line[0], s);
            minHeap.insert(s);
        }

        int m = Integer.parseInt(reader.readLine());
        for (int i = 0; i < m; i++) {
            String[] line = reader.readLine().split(" ");
            //Case 1: Improve score of name by difference
            if (line[0].equals("1")) {

                Soldier s = soldiers.get(line[1]);
                long difference = Long.parseLong(line[2]);
                //Change score of Soldier
                s.improveScore(difference);
                minHeap.minHeapify(s.getPos());
            }
            //Case 2: Return number of eligible soldiers below k
            else if (line[0].equals("2")) {

                long k = Long.parseLong(line[1]);
                output.write(Integer.toString(numEligible(k, minHeap)) + "\n");
                output.flush();
            }
        }
        long end = System.nanoTime();
        double totalTime = (end - start) / 1000000000;

        System.out.println("Time:" + totalTime);
        output.close();
        reader.close();
    }

    public static int numEligible(long k, MinHeap minHeap) {


        while(minHeap.heap[0].getScore() < k) {
            Soldier min = minHeap.deleteMin();
            soldiers.remove(min.getName());
        }
        return soldiers.size();
    }
}

class Soldier {

    String name;
    long score;
    int pos;

    Soldier(String name, long score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public long getScore() {
        return this.score;
    }

    public void improveScore(long difference) {
        this.score += difference;
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}

class MinHeap {

    Soldier[] heap;

    int maxSize;
    int size;       // Current size of heap

    MinHeap(int n) {

        size = 0;
        maxSize = n;
        heap = new Soldier[n + 1];
    }

    int parent(int pos) {
        if (pos % 2 == 1) {
            return pos / 2;
        }
        return (pos - 1) / 2;
    }

    // Insert new score into heap
    public void insert(Soldier s) {

        if (this.size >= this.maxSize) {
            return;
        }

        s.setPos(this.size);
        this.heap[this.size] = s;
        int current = this.size;
        this.size++;

        while(current != 0 && this.heap[current].getScore() < this.heap[parent(current)].getScore()) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    // Delete min value from heap
    public Soldier deleteMin() {

        Soldier deleted = this.heap[0];
        this.heap[0] = this.heap[--this.size];
        this.heap[0].setPos(0);
        minHeapify(0);
        return deleted;
    }

    // Helper function to swap 2 elements in the heap
    private void swap(int pos, int parent) {

        Soldier temp = this.heap[parent];
        this.heap[parent] = this.heap[pos];
        this.heap[pos] = temp;

        //Change positions
        this.heap[parent].setPos(parent);
        this.heap[pos].setPos(pos);
    }

    public void minHeapify(int pos) {

        while (pos <= this.size) {
            // Check if node at pos is a leaf
            if (!(pos >= (this.size / 2) && pos <= this.size)) {
                int left = (pos * 2) + 1;
                int right = (pos * 2) + 2;
                int smallest = -1;

                long current = this.heap[pos].getScore();
                long leftChild = this.heap[left].getScore();
                Soldier rightChild = this.heap[right];

                // Check if node at pos is greater than any of its children
                smallest = (left <= this.size - 1 && leftChild < current) ? left : pos;
                if (rightChild != null) {
                    smallest = (right <= this.size - 1 && rightChild.getScore() < this.heap[smallest].getScore()) ? right : smallest;
                }
                if (smallest != pos) {
                    swap(pos, smallest);
                    pos = smallest;
                }
                else {
                    break;
                }
            }
            else {
                break;
            }
        }
    }
}

