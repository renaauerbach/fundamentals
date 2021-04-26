package school;

import java.util.ArrayList;

public interface AdminInterface {

	public abstract void createCourse(Course c, ArrayList<Course> courseList);
	public abstract int deleteCourse(String id, ArrayList<Course> courseList);
	public abstract int editCourse(String id, ArrayList<Course> courseList);
	public abstract void displayInfo(String id, ArrayList<Course> courseList);
	public abstract int registerCourse(String fn, String ln, String id, ArrayList<Course> courseList);
	public abstract void viewAll(ArrayList<Course> courseList);
	public abstract void full(ArrayList<Course> courseList);
	public abstract void fileCoursesFull(ArrayList<Course> courseList);
	public abstract void viewRegStudents(String id, ArrayList<Course> courseList);
	public abstract void viewStudentsCourses(String fn, String ln, ArrayList<Course> courseList);
	public abstract void sortCourses(ArrayList<Course> courseList);
	
}
