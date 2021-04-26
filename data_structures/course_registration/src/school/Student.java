package school;
import java.util.ArrayList;

public class Student extends User implements StudentInterface {
	
	//Initialize variables
	private String fn;
	private String ln;
	private ArrayList<Course> myCourses = new ArrayList<Course>();	//List of a specific student's courses
	private ArrayList<Student> allStudents = new ArrayList<Student>(); //List of all students in the school
	
	//Constructors
	Student(String fn, String ln) {
		this.fn = fn;
		this.ln = ln;
	}
	
	Student(String fn, String ln, String user, String pass) {
		this.ln = ln;
		this.fn = fn;
		super.username = user;
		super.password = pass;
	}
	
	//Getters & Setters
	public String getFirstName() {
		return this.fn;
	}
	public void setFirstName(String fn) {
		this.fn = fn;
	}
	public String getLastName() {
		return this.ln;
	}
	public void setLastName(String ln) {
		this.ln = ln;
	}
	public ArrayList<Course> getMyCourses() {
		return this.myCourses;
	}
	public void setMyCourses(ArrayList<Course> myCourseList) {
		myCourses.addAll(myCourseList);
	}
	
	//View all courses
	@Override
	public void printAllCourses(ArrayList<Course> courseList) {
		for (int i = 0; i<courseList.size(); i++)
			courseList.get(i).studentCourseInfo(courseList);
	}
	
	
	//View all classes that are NOT full
	@Override
	public ArrayList<String> courseFull(ArrayList<Course> courseList) {
		ArrayList<String> openList = new ArrayList<String>();
		int count = 0;
		
		for (int i = 0; i<courseList.size(); i++) {
			if (courseList.get(i).getMaxStudents() != courseList.get(i).getCurrentStudents()) {
				openList.add(courseList.get(i).getCourse());
				count = 1;
			}
		}
		if (count == 0)
			System.out.println("All courses are full.");
		return openList;
	}
		
	//Withdraw from a course
	public int withdrawCourse(String c, int sec, String fn, String ln, ArrayList<Course> courseList) {
		int count = 0;
		ArrayList<Course> myCourseList = new ArrayList<Course>();
		for (int i = 0; i<courseList.size(); i++) {
			if (courseList.get(i).getCourse().equals(c) && courseList.get(i).getSection() == sec) {
				for (int j = 0; j<allStudents.size(); j++) {
					if (allStudents.get(j).getFirstName().equals(fn) && allStudents.get(j).getLastName().equals(ln)) {
						myCourseList = allStudents.get(j).getMyCourses();
						myCourseList.remove(courseList.get(i));
						allStudents.get(j).setMyCourses(myCourseList);
					}
				}				
				courseList.get(i).removeStudent(fn, ln);
				count = 1;
				int studentCount = (courseList.get(i).getCurrentStudents()) -1;
				courseList.get(i).setCurrentStudents(studentCount);
				break;
			}
		}
		return count;
	}
	
	//View all courses they are registered for
	public void myCourses(String fn, String ln, ArrayList<Course> courseList) {
		ArrayList<Course> myCourseList = new ArrayList<Course>();
		for (int i = 0; i<allStudents.size(); i++) {
			if (allStudents.get(i).getFirstName().equals(fn) && allStudents.get(i).getLastName().equals(ln)) {
				myCourseList = allStudents.get(i).getMyCourses();
				for(Course c: myCourseList)
					c.studentCourseInfo(courseList);
			}
		}
	}
}
