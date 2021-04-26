import java.io.*;
import java.util.*;

class Node {
    String guide;
    int value;
}

class InternalNode extends Node {
    Node child0, child1, child2;
}

class LeafNode extends Node {
}

class WorkSpace {
// Used to hold return values for the recursive doInsert
// Routine (see below)
    Node newNode;
    int offset;
    boolean guideChanged;
    Node[] scratch;
}

class TwoThreeTree {
    Node root;
    int height;

    TwoThreeTree() {
        root = null;
        height = -1;
    }
}

public class Hijacking {

    static BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out), 4096);

    public static void main(String[] args) throws IOException {

        File file = new File(args[0]);
        Scanner in = new Scanner(file);
        // Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        TwoThreeTree tree = new TwoThreeTree();
        //Populate the tree
        for (int i = 0; i <= n; i++) {
            String[] query = in.nextLine().split(" ");

            //Case 1: Insert planets with name and entrance fee into db
            if (query[0].equals("1")) {
                TwoThree.insert(query[1], Integer.parseInt(query[2]), tree);
            }
            //Case 2: Increase entrance fees for planets b/w a-b by delta
            else if (query[0].equals("2")) {
                //a <-- the planet with the smaller fee
                String a = query[1].compareTo(query[2]) < 0 ? query[1] : query[2];
                //b <-- the planet with the larger fee
                String b = query[1].compareTo(query[2]) < 0 ? query[2] : query[1];
                int delta = Integer.parseInt(query[3]);

                addRange(a, b, tree.root, tree.height, delta);
            }
            //Case 3: Return entrance fee for planet x
            else if (query[0].equals("3")) {
                String x = query[1];
                output.write(Integer.toString(search(tree.root, tree.height, x, 0)) + "\n");
                output.flush();
            }
        }
        output.close();
        in.close();
    }

    public static int search(Node p, int h, String x, int fee) {

        if (h == 0) {
            if (x.equals(p.guide)) {
                // fee += p.value;
                return p.value;
            }
            else {
                return -1;
            }
        } else {
            InternalNode node = (InternalNode)(p);
            if (x.compareTo(node.child0.guide) <= 0) {
                // fee += node.value;
                fee = search(node.child0, h-1, x, fee);
            }
            else if (x.compareTo(node.child1.guide) <= 0 || node.child2 == null) {
                // fee += node.value;
                fee = search(node.child1, h-1, x, fee);
            }
            else {
                // fee += node.value;
                fee = search(node.child2, h-1, x, fee);
            }
            return fee == -1 ? -1 : fee + node.value;
        }
    }

    public static void addRange(String a, String b, Node p, int h, int delta) {

        if (h == 0) {
            if (a.compareTo(p.guide) <= 0 && b.compareTo(p.guide) >= 0) {
                p.value += delta;
            }
        }
        else {
            Node left = p;
            Node right = p;
            while (!(left instanceof LeafNode)) {
                left = ((InternalNode)(left)).child0;
            }
            while (!(right instanceof LeafNode)) {
                right = ((InternalNode)(right)).child2 == null ? ((InternalNode)(right)).child1 : ((InternalNode)(right)).child2;
            }
            if (left.guide.compareTo(a) >= 0 && right.guide.compareTo(b) <= 0) {
                p.value += delta;
                return;
            }

            InternalNode node = (InternalNode)(p);
            if (node.child2 != null && a.compareTo(node.child2.guide) > 0) {
                ;
            }
            else if (b.compareTo(node.child0.guide) <= 0) {
                addRange(a, b, node.child0, h-1, delta);
            }
            else if (b.compareTo(node.child1.guide) <= 0 || node.child2 == null) {
                if (a.compareTo(node.child0.guide) <= 0) {
                    addGE(node.child0, a, h-1, delta);
                    addLE(node.child1, b, h-1, delta);
                }
                else {
                    addRange(a, b, node.child1, h-1, delta);
                }
            }
            else {
                if (a.compareTo(node.child0.guide) <= 0) {
                    addGE(node.child0, a, h-1, delta);
                    node.child1.value += delta;
                    addLE(node.child2, b, h-1, delta);
                }
                else if (a.compareTo(node.child1.guide) <= 0) {
                    addGE(node.child1, a, h-1, delta);
                    addLE(node.child2, b, h-1, delta);
                }
                else {
                    addRange(a, b, node.child2, h-1, delta);
                }
            }
        }
    }

    public static void addGE(Node p, String a, int h, int delta) {
        if (h == 0) {
            if (a.compareTo(p.guide) <= 0 ) {
                p.value += delta;
            }
        }
        else {
            InternalNode node = (InternalNode)(p);
            if (node.child2 == null){
                if (a.compareTo(node.child0.guide) <= 0) {
                    node.child1.value += delta;
                    addGE(node.child0, a, h-1, delta);
                }
                else {
                    addGE(node.child1, a, h-1, delta);
                }
            }
            else {
                if (a.compareTo(node.child1.guide) <= 0) {
                    if (a.compareTo(node.child0.guide) <= 0) {
                        node.child1.value += delta;
                        node.child2.value += delta;
                        addGE(node.child0, a, h-1, delta);
                    }
                    else {
                        node.child2.value += delta;
                        addGE(node.child1, a, h-1, delta);
                    }
                }
                else {
                    addGE(node.child2, a, h-1, delta);
                }
            }
        }
    }

    public static void addLE(Node p, String b, int h, int delta) {
        if (h == 0) {
            if (b.compareTo(p.guide) >= 0) {
                p.value += delta;
            }
        } else {
            InternalNode node = (InternalNode)(p);
            if (b.compareTo(node.child0.guide) <= 0) {
                addLE(node.child0, b, h-1, delta);
            }
            else if (b.compareTo(node.child1.guide) <= 0 || node.child2 == null) {
                node.child0.value += delta;
                addLE(node.child1, b, h-1, delta);
            }
            else {
                node.child0.value += delta;
                node.child1.value += delta;
                addLE(node.child2, b, h-1, delta);
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

            q.child0.value += q.value;
            q.child1.value += q.value;
            if (q.child2 != null) {
                q.child2.value += q.value;
            }

            if (key.compareTo(q.child0.guide) <= 0) {
                pos = 0;
                q.value = 0;
                ws = doInsert(key, value, q.child0, h-1);
            }
            else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
                pos = 1;
                q.value = 0;
                ws = doInsert(key, value, q.child1, h-1);
            }
            else {
                pos = 2;
                q.value = 0;
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
