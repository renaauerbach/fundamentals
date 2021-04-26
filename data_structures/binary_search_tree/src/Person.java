import java.util.ArrayList;

public class Person implements Comparable<Person>{

	protected int ssn;
	protected String fName;
	protected String lName;
	protected int motherSSN;		//SSN of mother
	protected int fatherSSN;		//SSN of father
	protected ArrayList<Integer> friends = new ArrayList<Integer>();		//comma-separated list of friends' SSNs
	protected ArrayList<Integer> mutualFriends = new ArrayList<Integer>();
	
	public Person() {
	}
	
	public Person(int ssn, String fName, String lName, int mSSN, int fSSN, ArrayList<Integer> friends) {
		this.ssn = ssn;
		this.fName = fName;
		this.lName = lName;
		this.motherSSN = mSSN;
		this.fatherSSN = fSSN;
		//TODO set info for ArrayList
	}

	public int getSSN() {
		return ssn;
	}

	public void setSSN(int ssn) {
		this.ssn = ssn;
	}

	public String getFName() {
		return fName;
	}

	public void setFName(String fName) {
		this.fName = fName;
	}

	public String getLName() {
		return lName;
	}

	public void setLName(String lName) {
		this.lName = lName;
	}

	public int getMotherSSN() {
		return motherSSN;
	}

	public void setMotherSSN(int mother) {
		this.motherSSN = mother;
	}

	public int getFatherSSN() {
		return fatherSSN;
	}

	public void setFatherSSN(int father) {
		this.fatherSSN = father;
	}

	public ArrayList<Integer> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<Integer> friends) {
		this.friends = friends;
	}
	
	public ArrayList<Integer> getMutualFriends() {
		return mutualFriends;
	}

	public void setMutualFriends(ArrayList<Integer> mFriends) {
		this.mutualFriends = mFriends;
	}
	
	public int compareTo(Person P) {
		if (this.ssn < P.getSSN()) {
			return -1;
		}
		else if (this.ssn > P.getSSN()) {
			return 1;
		}
		else return 0;
	}
    
}
