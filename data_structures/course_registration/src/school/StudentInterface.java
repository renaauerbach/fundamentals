package school;

import java.util.ArrayList;

public interface StudentInterface {

	public abstract void printAllCourses(ArrayList<Course> courseList);
	public abstract ArrayList<String> courseFull(ArrayList<Course> courseList);
	public abstract int registerCourse(String c, int sec, String fn, String ln, ArrayList<Course> courseList);
	public abstract int withdrawCourse(String c, int sec, String fn, String ln, ArrayList<Course> courseList);
	public abstract void myCourses(String fn, String ln, ArrayList<Course> courseList);
}
