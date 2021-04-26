package school;
import java.util.ArrayList;

public class Course {
	//Initialize variables
	private String course;
	private String courseID;
	private int maxStudents = -1; //set to -1 to indicate empty course entry
	private int currentStudents = 0;
	private ArrayList<Student> studentList = new ArrayList<Student>(); //List of all students in a particular class
	private String instructor;
	private int section;
	private String location;
	
	//Default constructor
	Course() {
	}

	//Constructor for new courses - doesn't include currentStudents or studentList because a new class doesn't have student yet
	Course(String c, String id, int max, String i, int sec, String local) {
		this.course = c;
		this.courseID = id;
		this.maxStudents = max;
		this.instructor = i;
		this.section = sec;
		this.location = local;	
	}
	
	//Getters & Setters
	public String getCourse() {
		return this.course;
	}
	public void setCourse(String c) {
		this.course = c;
	}
	public String getCourseID() {
		return this.courseID;
	}
	public void setCourseID(String id) {
		this.courseID = id;
	}
	public int getMaxStudents() {
		return this.maxStudents;
	}
	public void setMaxStudents(int max) {
		this.maxStudents = max;
	}
	public int getCurrentStudents() {
		return this.currentStudents;
	}
	public void setCurrentStudents(int cs) {
		this.currentStudents = cs;
	}
	public ArrayList<Student> getStudentList() {
		return studentList;
	}
	public void setStudentList(ArrayList<Student> studentList) {
		this.studentList = studentList;
	}
	public void newStudent(String fn, String ln) {
		Student s = new Student(fn, ln);
		this.studentList.add(s);
	}
	public void removeStudent(String fn, String ln) {
		for(Student s: this.studentList) {
			if(s.getFirstName().equalsIgnoreCase(fn) && s.getLastName().equalsIgnoreCase(ln))
				this.studentList.remove(s);
		}
	}
	public String getInstructor() {
		return this.instructor;
	}
	public void setInstructor(String i) {
		this.instructor = i;
	}
	public int getSection() {
		return this.section;
	}
	public void setSection(int s) {
		this.section = s;
	}
	public String getLocation() {
		return this.location;
	}
	public void setLocation(String local) { 
		this.location = local;
	}
	
	//Prints information depending on the user making the request
	public void displayAllInfo(ArrayList<Course> courseList) {
		System.out.println("Course Name: " + this.course + " , Course ID: " + " , Max. Number of Student: " + this.maxStudents
				+ " , Current Number Enrolled: " + this.currentStudents + " , Student List: " + this.studentList 
				+ " , Instructor: " + this.instructor + " , Section: " + this.section + " , Location: " + this.location);
	}
	public void adminCourseInfo(ArrayList<Course> courseList) {
		System.out.println("Course Name: " + this.course + ", Course ID: " + ", Max. Number of Student: " + this.maxStudents
				+ ", Current Number Enrolled: " + this.currentStudents);
	}
	public void studentCourseInfo(ArrayList<Course> courseList) {
		System.out.println(this.course);
	}
	
}
