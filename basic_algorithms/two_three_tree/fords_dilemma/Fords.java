import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

class Node{
    String guide;
    // Guide points to max key in subtree rooted at node
}

class InternalNode extends Node {
    Node child0, child1, child2;
   // child0 and child1 are always non-null
   // child2 is null iff node has only 2 children
}

class LeafNode extends Node {
    int value;
    // Guide points to the key
}

class TwoThreeTree {
    Node root;
    int height;

    TwoThreeTree() {
        root = null;
        height = -1;
    }
}

class WorkSpace {
// Used to hold return values for the recursive doInsert
// Routine (see below)
    Node newNode;
    int offset;
    boolean guideChanged;
    Node[] scratch;
}

public class Fords {

    static BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out), 4096);

    public static void main(String[] args) throws IOException {

//         File file = new File(args[0]);
//         Scanner in = new Scanner(file);

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        TwoThreeTree tree = new TwoThreeTree();
        //Populate the tree
        for (int i = 0; i <= n; i++) {
            String line = in.nextLine();

            if (line.length() > 0) {
                String[] entry = line.split(" ");
                TwoThree.insert(entry[0], Integer.parseInt(entry[1]), tree);
            }
        }

        n = in.nextInt();
        for (int i = 0; i <= n; i++) {
            String line = in.nextLine();

            if (line.length() > 0) {
                String[] query = line.split(" ");
                   //x <-- the planet with the smaller fee
                String x = query[0].compareTo(query[1]) < 0 ? query[0] : query[1];
                //y <-- the planet with the larger fee
                String y = query[0].compareTo(query[1]) < 0 ? query[1] : query[0];

                Node p = tree.root;
                int h = tree.height;
                printRange(p, x, y, h);
                output.flush();
            }
        }
        in.close();
    }
    public static void printRange(Node p, String x, String y, int h) throws IOException {

        if (h == 0) {
            LeafNode node = (LeafNode)(p);
            if (x.compareTo(node.guide) <= 0 && y.compareTo(node.guide) >= 0) {
                output.write(node.guide + " " + node.value + "\n");
            }
        }
        else {
            InternalNode node = (InternalNode)(p);
            if (node.child2 != null && x.compareTo(node.child2.guide) > 0) {
                ;
             }
            else if (y.compareTo(node.child0.guide) <= 0) {
                printRange(node.child0, x, y, h-1);
            }
            else if (node.child2 == null || y.compareTo(node.child1.guide) <= 0) {
                if (x.compareTo(node.child0.guide) <= 0) {
                    printGE(node.child0, x, h-1);
                    printLE(node.child1, y, h-1);
                }
                else {
                    printRange(node.child1, x, y, h-1);
                }
            }
            else {
                if (x.compareTo(node.child0.guide) <= 0) {
                    printGE(node.child0, x, h-1);
                    printAll(node.child1, h-1);
                    printLE(node.child2, y, h-1);
                }
                else if (x.compareTo(node.child1.guide) <= 0) {
                    printGE(node.child1, x, h-1);
                    printLE(node.child2, y, h-1);
                }
                else {
                    printRange(node.child2, x, y, h-1);
                }
            }
        }
    }

    public static void printGE(Node p, String x, int h) throws IOException {
        if (h == 0) {
            LeafNode node = (LeafNode)(p);
            if (x.compareTo(node.guide) <= 0 ) {
                output.write(node.guide + " " + node.value + "\n");
            }
        }
        else {
            InternalNode node = (InternalNode)(p);
            if (node.child2 == null){
                if (x.compareTo(node.child0.guide) <= 0) {
                    printGE(node.child0, x, h-1);
                    printAll(node.child1, h-1);
                }
                else {
                    printGE(node.child1, x, h-1);
                }
            }
            else {
                if (x.compareTo(node.child1.guide) <= 0) {
                    if (x.compareTo(node.child0.guide) <= 0) {
                        printAll(node.child1, h-1);
                        printAll(node.child2, h-1);
                        printGE(node.child0, x, h-1);
                    }
                    else {
                        printAll(node.child2, h-1);
                        printGE(node.child1, x, h-1);
                    }
                }
                else {
                    printGE(node.child2, x, h-1);
                }
            }
        }
    }

    public static void printLE(Node p, String y, int h) throws IOException {
        if (h == 0) {
            LeafNode node = (LeafNode)(p);
            if (y.compareTo(node.guide) >= 0) {
                output.write(node.guide + " " + node.value + "\n");
            }
        } else {
            InternalNode node = (InternalNode)(p);
            if (y.compareTo(node.child0.guide) <= 0) {
                printLE(node.child0, y, h-1);
            }
            else if (y.compareTo(node.child1.guide) <= 0 || node.child2 == null) {
                printAll(node.child0, h-1);
                printLE(node.child1, y, h-1);
            }
            else {
                printAll(node.child0, h-1);
                printAll(node.child1, h-1);
                printLE(node.child2, y, h-1);
            }
        }
    }

    public static void printAll(Node p, int h) throws IOException {
        if (h <= 0) {
            LeafNode node = (LeafNode)(p);
            output.write(node.guide + " " + node.value + "\n");
        }
        else {
            InternalNode node = (InternalNode)(p);
            printAll(node.child0, h-1);
            printAll(node.child1, h-1);
            if (node.child2 != null) {
                printAll(node.child2, h-1);
            }
        }
    }
}

class TwoThree {

    static void insert(String key, int value, TwoThreeTree tree) {
    // Insert a key value pair into tree (overwrite existing value if key is already present)
        int h = tree.height;

        if (h == -1) {
            LeafNode newLeaf = new LeafNode();
            newLeaf.guide = key;
            newLeaf.value = value;
            tree.root = newLeaf;
            tree.height = 0;
        }
        else {
            WorkSpace ws = doInsert(key, value, tree.root, h);
            if (ws != null && ws.newNode != null) {
                // Create new root
                InternalNode newRoot = new InternalNode();
                if (ws.offset == 0) {
                    newRoot.child0 = ws.newNode;
                    newRoot.child1 = tree.root;
                }
                else {
                    newRoot.child0 = tree.root;
                    newRoot.child1 = ws.newNode;
                }
                resetGuide(newRoot);
                tree.root = newRoot;
                tree.height = h + 1;
            }
        }
    }

    static WorkSpace doInsert(String key, int value, Node p, int h) {
    // Auxiliary recursive routine for insert
        if (h == 0) {
        // At leaf level --> compare --> update value or insert new leaf
            LeafNode leaf = (LeafNode) p;     // Downcast
            int comp = key.compareTo(leaf.guide);

            if (comp == 0) {
                leaf.value = value;
                return null;
            }

            // New leaf node and insert into tree
            LeafNode newLeaf = new LeafNode();
            newLeaf.guide = key;
            newLeaf.value = value;

            int offset = (comp < 0) ? 0 : 1;
            // Offset == 0 => newLeaf inserted as LEFT sibling
            // Offset == 1 => newLeaf inserted as RIGHT sibling

            WorkSpace ws = new WorkSpace();
            ws.newNode = newLeaf;
            ws.offset = offset;
            ws.scratch = new Node[4];

            return ws;
        }
        else {
            InternalNode q = (InternalNode) p;     // Downcast
            int pos;
            WorkSpace ws;

            if (key.compareTo(q.child0.guide) <= 0) {
                pos = 0;
                ws = doInsert(key, value, q.child0, h-1);
            }
            else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
                pos = 1;
                ws = doInsert(key, value, q.child1, h-1);
            }
            else {
                pos = 2;
                ws = doInsert(key, value, q.child2, h-1);
            }

            if (ws != null) {
                if (ws.newNode != null) {
                // Make ws.newNode child # pos + ws.offset of q
                    int sz = copyOutChildren(q, ws.scratch);
                    insertNode(ws.scratch, ws.newNode, sz, pos + ws.offset);
                    if (sz == 2) {
                        ws.newNode = null;
                        ws.guideChanged = resetChildren(q, ws.scratch, 0, 3);
                    }
                    else {
                        ws.newNode = new InternalNode();
                        ws.offset = 1;
                        resetChildren(q, ws.scratch, 0, 2);
                        resetChildren((InternalNode) ws.newNode, ws.scratch, 2, 2);
                    }
                }
                else if (ws.guideChanged) {
                    ws.guideChanged = resetGuide(q);
                }
            }
        return ws;
        }
    }

    static int copyOutChildren(InternalNode q, Node[] x) {
    // Copy children of q into x --> return # of children
        int sz = 2;
        x[0] = q.child0; x[1] = q.child1;
        if (q.child2 != null) {
            x[2] = q.child2;
            sz = 3;
        }
        return sz;
    }

    static void insertNode(Node[] x, Node p, int sz, int pos) {
    // Insert p in x[0..sz) at position pos (moving existing entries to the right)
        for (int i = sz; i > pos; i--)
            x[i] = x[i-1];

        x[pos] = p;
    }

    static boolean resetGuide(InternalNode q) {
    // Reset q.guide --> return true if it changes
        String oldGuide = q.guide;
        if (q.child2 != null)
             q.guide = q.child2.guide;
        else
            q.guide = q.child1.guide;

        return q.guide != oldGuide;
    }

    static boolean resetChildren(InternalNode q, Node[] x, int pos, int sz) {
    // Reset q's children to x[pos..pos+sz), where sz = 2 or 3
    // Resets guide --> returns result
        q.child0 = x[pos];
        q.child1 = x[pos + 1];
         if (sz == 3)
             q.child2 = x[pos + 2];
        else
            q.child2 = null;

        return resetGuide(q);
    }
}
