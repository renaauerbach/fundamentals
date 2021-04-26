package school;
import java.util.*;
import java.io.*;

public class Admin extends User implements AdminInterface {
	
	//Default constructor
	Admin() {
	}
	
	Admin(String user, String pass) {
		super.username = user;
		super.password = pass;
	}
	
	//Create a new course
	public void createCourse(Course c, ArrayList<Course> courseList) {
		courseList.add(c);
	}
	
	//Deletes a course of a given ID
	public int deleteCourse(String id, ArrayList<Course> courseList) {
		for (int i = 0; i<courseList.size(); i++) {
			if (courseList.get(i).getCourseID().equals(id)) {
				courseList.remove(i);
				return 1;	//Course successfully deleted
			}
		} 
		return 0;	//Course not found
	}
	
	//Edit a course by course ID
	public int editCourse(String id, ArrayList<Course> courseList) {
		Scanner in = new Scanner(System.in);
		
		for (int i = 0; i<courseList.size(); i++) {
			if (courseList.get(i).getCourseID().equalsIgnoreCase(id)) {
				System.out.println("What would you like to edit? ");
				System.out.println("1. Course name");
				System.out.println("2. Course ID");
				System.out.println("3. Course Instructor");
				int edit = in.nextInt();
				
				if (edit == 1) {
					System.out.println("Enter new course name: ");
					String newName = in.nextLine();
					courseList.get(i).setCourse(newName);
				}
				if (edit == 2) {
					System.out.println("Enter new course ID: ");
					String newID = in.next();
					courseList.get(i).setCourseID(newID);
				}
				if (edit == 3) {
					System.out.println("Enter new course instructor: ");
					String newInstructor = in.nextLine();
					courseList.get(i).setInstructor(newInstructor);
				}
				return 1;	//Course successfully edited
			}
			in.close();
		}return 0;	//Course not found
	}	
	
	//Display course info by course ID
	public void displayInfo(String id, ArrayList<Course> courseList) {
		for (int i = 0; i<courseList.size(); i++) {
			if (courseList.get(i).getCourseID().equals(id)) {
				courseList.get(i).displayAllInfo(courseList);
				break;
			}
		}
	}
	
	//Register a student - overloading inherited registerCourse method
	public int registerCourse(String fn, String ln, String id, ArrayList<Course> courseList) {
		int count = 0;
		ArrayList<Course> myCourseList = new ArrayList<Course>();
		for (int i = 0; i<courseList.size(); i++) {
			if (courseList.get(i).getCourseID().equals(id)) {
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
		}
		return count;	//count = 1: registration successful; count = 0: registration unsuccessful
	}
	
	//View all courses
	public void viewAll(ArrayList<Course> courseList) {
		super.printAllCourses(courseList);
	}
	
	//View full courses
	public void full(ArrayList<Course> courseList) {
		System.out.println(super.courseFull(courseList));
	}
	
	//Write to a file all full courses
	public void fileCoursesFull(ArrayList<Course> courseList) {
		FileWriter writer;
		try {
			writer = new FileWriter("CoursesFull.txt");
			for(String str: super.courseFull(courseList)) {
				writer.write(str);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//View list of students registered for specific course
	public void viewRegStudents(String id, ArrayList<Course> courseList) {
		ArrayList<Student> regList = new ArrayList<Student>();
		for(int i = 0; i<courseList.size(); i++) {
			if (courseList.get(i).getCourseID().equals(id))
				regList = courseList.get(i).getStudentList();
		}
		for(int j = 0; j<regList.size(); j++)
			System.out.println(regList.get(j).getFirstName() + ' ' + regList.get(j).getLastName());
	}
	
	//View list of courses a student is registered for
	public void viewStudentsCourses(String fn, String ln, ArrayList<Course> courseList) {
		ArrayList<Course> myCourseList = new ArrayList<Course>();
			for (int i = 0; i<allStudents.size(); i++) {
				if (allStudents.get(i).getFirstName().equals(fn) && allStudents.get(i).getLastName().equals(ln)) {
					myCourseList = allStudents.get(i).getMyCourses();
				}
			}
		for(Course c : myCourseList)
			c.displayAllInfo(courseList);
	}
	
	//Sort courses by number of students currently registered
	public void sortCourses(ArrayList<Course> courseList) {
		Course c = new Course();
		for (int i = 0; i<courseList.size()/2; i++) {
			for (int j = 0; j<courseList.size(); j++) {
				if (courseList.get(i).getCurrentStudents() > courseList.get(j).getCurrentStudents()) {
					c = courseList.get(i);
					courseList.set(i, courseList.get(j));
					courseList.set(j, c);
				}
			}
		}
	}
}
