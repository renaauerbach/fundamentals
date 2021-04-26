import java.util.ArrayList;

public class BST<T extends Comparable<T>>{ 

	private Node<T> root;
	
	public BST() {
		root = null;
	}

	public T find(Node<T> root, T key){
		if (root == null) 
			return null;
		else if (key.compareTo(root.key) < 0) 
            return find(root.leftChild, key);
        else if (key.compareTo(root.key) > 0)                       
            return find(root.rightChild, key);
        else
        	return root.key; 	
	}
	public T helperFind(String key) {
		T newKey = (T) key;
		return find(this.root, newKey);
	}
	
	public Node<T> insert(Node<T> root, T key) {
		if (root == null)                
			root = new Node<T>(key);
		else if (key.compareTo(root.key) < 0) 
			root.leftChild =  insert(root.leftChild, key);
		else 
			root.rightChild = insert(root.rightChild, key);
		return root;
	} 
	public void helperInsert(T key) {
		this.root = insert(this.root, key);
	}
	
	//find all children of x
	public void allChildren(int TargetSSN, Node<Person> root, ArrayList<Integer> children) {
		if (root != null) {
			if (root.key.getMotherSSN() == TargetSSN || root.key.getFatherSSN() == TargetSSN) {
				children.add(root.key.getSSN());
			}
			allChildren(TargetSSN, root.leftChild, children);
			allChildren(TargetSSN, root.rightChild, children);
		}
	}
	public ArrayList<Integer> helperAllChildren (int TargetSSN) {
		ArrayList<Integer> kids = new ArrayList<Integer>();
		allChildren(TargetSSN, (Node<Person>) this.root, kids);
		return kids;
	}

	//find all half-sibs of x
	public void halfSibs(Person Target, Node<Person> root, ArrayList<Integer> sibs) {
		if (root != null) {
			if ((root.key.getMotherSSN() == Target.getMotherSSN() && root.key.getFatherSSN() != Target.getFatherSSN())
					|| (root.key.getMotherSSN() != Target.getMotherSSN() && root.key.getFatherSSN() == Target.getFatherSSN())) {
				sibs.add(root.key.getSSN());
			}
			halfSibs(Target, root.leftChild, sibs);
			halfSibs(Target, root.rightChild, sibs);
		}
	}
	public ArrayList<Integer> helperHalfSibs(Person Target) {
		ArrayList<Integer> sibs = new ArrayList<Integer>();
		halfSibs(Target, (Node<Person>) this.root, sibs);
		return sibs;
	}
	
	//find all full-sibs of x
	public void fullSibs(Person Target, Node<Person> root, ArrayList<Integer> sibs) {
		if (root != null) {
			if ((root.key.getMotherSSN() == Target.getMotherSSN() && root.key.getFatherSSN() == Target.getFatherSSN())) {
				sibs.add(root.key.getSSN());
			}
			fullSibs(Target, root.leftChild, sibs);
			fullSibs(Target, root.rightChild, sibs);
		}
	}
	public ArrayList<Integer> helperFullSibs(Person Target) {
		ArrayList<Integer> sibs = new ArrayList<Integer>();
		fullSibs(Target, (Node<Person>) this.root, sibs);
		return sibs;
	}
	
	//find all Persons with mutual friendships of x 
	public void mutualFriends(Person Target, Node<Person> root, ArrayList<Integer> friends) {
		if (root != null) {
			if (Target.getFriends().contains(root.key.getSSN()) && root.key.getFriends().contains(Target.getSSN())) {
				friends.add(root.key.getSSN());
			}
			mutualFriends(Target, root.leftChild, friends);
			mutualFriends(Target, root.rightChild, friends);
		}
	}
	public ArrayList<Integer> helperMutualFriends(Person Target) {
		ArrayList<Integer> mutualFriends = new ArrayList<Integer>();
		mutualFriends(Target, (Node<Person>) this.root, mutualFriends);
		Target.setMutualFriends(mutualFriends);
		return mutualFriends;
	}

	//find all the Persons whom consider x their friend
	public void theirFriend(int TargetSSN, Node<Person> root, ArrayList<Integer> friends) {
		if (root != null) {
			if (root.key.getFriends().contains(TargetSSN))
				friends.add(root.key.getSSN());
			theirFriend(TargetSSN, root.leftChild, friends);
			theirFriend(TargetSSN, root.rightChild, friends);
		}
	}
			
	public ArrayList<Integer> helperTheirFriend(int TargetSSN) {
		ArrayList<Integer> friends = new ArrayList<Integer>();
		theirFriend(TargetSSN, (Node<Person>)this.root, friends);
		return friends;
	}
	
	//find the Person with the most mutual friends 
	public Person mostMF(Node<Person> root) {
		if (root != null) {
			Person Target = root.key;
			Person childL = mostMF(root.leftChild);
			Person childR = mostMF(root.rightChild);
			if (childL == null && childR == null) 	//current node = leaf
				return Target; 
			else if (childL == null) {
				if (Target.getMutualFriends().size() > childR.getMutualFriends().size())
					return Target;
				else 
					return childR;
			}
			else if (childR == null) {
				if (Target.getMutualFriends().size() > childL.getMutualFriends().size())
					return Target;
				else 
					return childL;
			}
			else {
				if (Target.getMutualFriends().size() > childL.getMutualFriends().size()) {
					if (Target.getMutualFriends().size() > childR.getMutualFriends().size()) {
						return Target;
					}
					else {
						return childR;
					}
				}
				else if (Target.getMutualFriends().size() > childR.getMutualFriends().size()) {
					if (Target.getMutualFriends().size() > childL.getMutualFriends().size()) {
						return Target;
					}
					else {
						return childL;
					}	
				}
				else {
					if (childR.getMutualFriends().size() > childL.getMutualFriends().size()) {
						return childR;
					}
					else {
						return childL;
					}
				}
			}		
		}
		else { 
			return null;
		}
	}
 
	public Person helperMostMF() {
		populate((Node<Person>)this.root);
		return mostMF((Node<Person>)this.root);
	}
	
	public void populate(Node<Person> root) {
		if (root != null) {
			helperMutualFriends(root.key);
			populate(root.leftChild);
			populate(root.rightChild);
		}
	}
	
	private void preOrder(Node<T> r) {
		if (r != null) {
			System.out.print(r.key + " ");
			preOrder(r.leftChild);
			preOrder(r.rightChild);
		}
	}
	
	public void preOrderTraversal() {
		preOrder(root);
	}
	private void inOrder(Node<T> r) {
		if (r != null) {
			inOrder(r.leftChild);
			System.out.print(r.key + " ");
			inOrder(r.rightChild);
		}
    }
	public void inOrderTraversal() {
		inOrder(root);
	}
	
	private void postOrder(Node<T> r) {
		if (r != null) {
			postOrder(r.leftChild);
			postOrder(r.rightChild);
			System.out.print(r.key + " ");
		}
    }
	public void postOrderTraversal() {
		postOrder(root);
	}
	
}
