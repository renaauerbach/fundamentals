package school;
import java.util.ArrayList;

public class User {
		
	//Initialize variables
	protected String username;
	protected String password;
	protected String fName;
	protected String lName;
	protected ArrayList<Student> studentList = new ArrayList<Student>(); //List of all students in a particular class
	protected ArrayList<Student> allStudents = new ArrayList<Student>(); //List of all students in the school

	//Default constructor
	User() {	
	}
	
	//Getters & Setters
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setFirstName(String fName) {
		this.fName = fName;
	}
	public String getFirstName() {
		return fName;
	}
	public void setLastName(String lName) {
		this.lName = lName;
	}
	public String getLastName() {
		return lName;
	}
	public ArrayList<Student> getAllStudents() {
		return this.allStudents;
	}
	public void setAllStudents(ArrayList<Student> s) {
		allStudents.addAll(s);
	}
		
	//Register for a course
	public int registerCourse(String c, int sec, String fn, String ln, ArrayList<Course> courseList) {
		int count = 0;
		ArrayList<Course> myCourseList = new ArrayList<Course>();
		for (int i = 0; i<courseList.size(); i++) {
			if (courseList.get(i).getCourse().equals(c) && courseList.get(i).getSection() == sec) {
				if (courseList.get(i).getCurrentStudents() != courseList.get(i).getMaxStudents()){
					for (int j = 0; j<allStudents.size(); j++) {
						if (allStudents.get(j).getFirstName().equals(fn) && allStudents.get(j).getLastName().equals(ln)) {
							myCourseList = allStudents.get(j).getMyCourses();
							myCourseList.add(courseList.get(i));
							allStudents.get(j).setMyCourses(myCourseList);
						}
					}
					courseList.get(i).newStudent(fn, ln);
					count = 1;
					int studentCount = (courseList.get(i).getCurrentStudents()) +1;
					courseList.get(i).setCurrentStudents(studentCount);	
					break;
				}
				else break;
			}
		}
		return count;	//count = 1: registration successful; count = 0: registration unsuccessful
	}
	
	//View all courses
	public void printAllCourses(ArrayList<Course> courseList) {
		for (int i = 0; i<courseList.size(); i++)
			courseList.get(i).adminCourseInfo(courseList);
	}
	
	//View full courses
	public ArrayList<String> courseFull(ArrayList<Course> courseList) {
		ArrayList<String> fullList = new ArrayList<String>();
		int count = 0;
		
		for (int i = 0; i<courseList.size(); i++) {
			if (courseList.get(i).getMaxStudents() == courseList.get(i).getCurrentStudents()) {
				fullList.add(courseList.get(i).getCourse());
				count = 1;
			}
		}
		if (count == 0)
			System.out.println("There are no full courses.");
		return fullList;

	}
}
	


