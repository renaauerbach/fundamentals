import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BSTImplementation {

	public static void main(String[] args) {
		
		BST<Person> tree = new BST<Person>();
		File cF = new File(args[0]);
		Scanner input1;
		try {
			input1 = new Scanner(cF);
			int counter = 0;
			Person m = new Person();
			
			while (input1.hasNext()) {
				String nextLine = input1.nextLine();
				if (nextLine.equals("")) continue;
				if (counter == 1) {
					m = new Person();
					m.fName = nextLine.split(":")[1];
				}
				else if (counter == 2)
					m.lName = nextLine.split(":")[1];
				else if (counter == 3) 
					m.setSSN(Integer.parseInt(nextLine.split(":")[1]));
				else if (counter == 4)
					m.setFatherSSN(Integer.parseInt(nextLine.split(":")[1]));
				else if (counter == 5)
					m.setMotherSSN(Integer.parseInt(nextLine.split(":")[1]));
				else if (counter == 6) {
					String[] friends = nextLine.split(":")[1].split(",");
					for (int i = 0; i<friends.length; i++) {
						m.friends.add(Integer.parseInt(friends[i]));
					}
				}
				counter = (counter + 1) % 6;
			}				
			input1.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		File cQ = new File(args[1]);
		Scanner input2;
		try {
			input2 = new Scanner(cQ);
			File output = new File("output.txt");
			FileWriter outputWriter = new FileWriter(output);
			ArrayList<String> list = new ArrayList<String>();

			while (input2.next().equals("\n")) {
				//TODO: friends list
				while (input2.hasNextLine()) {									}
					if (list.get(0).startsWith("NAME-OF")) {
						String key = list.get(0).split(" ")[1];
						Person p = tree.helperFind(key);
						
						outputWriter.write(list.get(0) + ": " + p.getFName() + " " + p.getLName() + "\n");
					}
					if (list.get(1).startsWith("FATHER-OF")) {
						String key = list.get(1).split(" ")[1];
						Person p = tree.helperFind(key);

						outputWriter.write(list.get(1) + ": " + p.getFatherSSN() + "\n");
					}
					if (list.get(2).startsWith("MOTHER-OF")) {
						String key = list.get(2).split(" ")[1];
						Person p = tree.helperFind(key);
						
						outputWriter.write(list.get(2) + ": " + p.getMotherSSN() + "\n");
					}
					if (list.get(3).startsWith("MOTHER-OF")) {
						String key = list.get(3).split(" ")[1];
						Person p = tree.helperFind(key);
						
						outputWriter.write(list.get(3) + ": " + p.getMotherSSN() + "\n");
					}
					if (list.get(4).startsWith("HALF-SIBLINGS-OF")) {
						String key = list.get(4).split(" ")[1];
						Person p = tree.helperFind(key);
						ArrayList<Integer> halfsibs = new ArrayList<Integer>(tree.helperHalfSibs(p));
						//for each person in arraylist print person name
						
						outputWriter.write(list.get(4) + ": " + halfsibs + "\n");
					}
					if (list.get(5).startsWith("FULL-SIBLINGS-OF")) {
						String key = list.get(5).split(" ")[1];
						Person p = tree.helperFind(key);
						ArrayList<Integer> fullsibs = new ArrayList<Integer>(tree.helperFullSibs(p));
						
						outputWriter.write(list.get(5) + ": " + fullsibs + "\n");
					}
					if (list.get(6).startsWith("CHILDREN-OF")) {
						String key = list.get(6).split(" ")[1];
						int newKey = Integer.parseInt(key);
						ArrayList<Integer> kids = new ArrayList<Integer>(tree.helperAllChildren(newKey));
						
						outputWriter.write(list.get(6) + ": " + kids + "\n");
					}
					if (list.get(7).startsWith("CHILDREN-OF")) {
						String key = list.get(7).split(" ")[1];
						int newKey = Integer.parseInt(key);
						ArrayList<Integer> kids = new ArrayList<Integer>(tree.helperAllChildren(newKey));
						
						outputWriter.write(list.get(7) + ": " + kids + "\n");
					}	
					if (list.get(8).startsWith("MUTUAL-FRIENDS-OF")) {
						String key = list.get(8).split(" ")[1];
						Person p = tree.helperFind(key);
						ArrayList<Integer> mutual = new ArrayList<Integer>(tree.helperMutualFriends(p));

						outputWriter.write(list.get(7) + ": " + mutual + "\n");
					}	
					if (list.get(9).startsWith("INVERSE-FRIENDS-OF")) {
						String key = list.get(9).split(" ")[1];
						int newKey = Integer.parseInt(key);
						ArrayList<Integer> inverse = new ArrayList<Integer>(tree.helperTheirFriend(newKey));
						
						outputWriter.write(list.get(7) + ": " + inverse + "\n");
					}	
					if (list.get(10).startsWith("WHO-HAS-MOST-MUTUAL-FRIENDS")) {
						outputWriter.write(list.get(7) + ": " + tree.helperMostMF() + "\n");
					}	
			}				
			input2.close();
			outputWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
