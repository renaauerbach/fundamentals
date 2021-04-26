package school;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CourseSearch implements Serializable {

	public static void main(String[] args) {
		
		ArrayList<Course> courseList = new ArrayList<Course>(); //List of courses read from CSV file
		
		File fileCSV = new File("MyUniversityCourses.csv");
		File fileSER = new File("CourseList.ser");
		
		//Deserialization
		if (fileSER.exists()) {
			try{
				//FileInputSystem recieves bytes from a file
				FileInputStream fis = new FileInputStream("CourseList.ser");
				ObjectInputStream ois = new ObjectInputStream(fis);
	     
				Object de =  ois.readObject();
				ois.close();
				fis.close();
			}
			catch(IOException ioe) {
				ioe.printStackTrace();
				return;
			}
			catch(ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
				return;
			}
		} else { 
			try{
				Scanner input = new Scanner(fileCSV);
				String header = input.nextLine();
				while (input.hasNext()) {
					String line = input.nextLine();
					String[] parsed = line.split(",");
				
					String courseName = parsed[0];
					String courseID = parsed[1];
					int max = Integer.parseInt(parsed[2]);
					String instructor = parsed[5];
					int section = Integer.parseInt(parsed[6]);
					String location = parsed[7];
					Course c = new Course(courseName, courseID, max, instructor, section, location);
					courseList.add(c);
					}
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				//FileOutput Stream writes data to a file
				FileOutputStream fos = new FileOutputStream("CourseList.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
								
				oos.writeObject(fileSER);
				
				//Close both streams
				oos.close();
				fos.close();
				System.out.println("Serialization complete.");
			} 
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		//Initializes new scanner
		Scanner in = new Scanner(System.in);
		
		//Login
		System.out.println("Enter username: ");
		String username = in.next();
		System.out.println("Enter password: ");
		String password = in.next();
	
		int pick;
		
		if (username.equalsIgnoreCase("Admin") && password.equals("Admin001")) {
			Admin admin = new Admin();
			
			System.out.println("Please enter your first and last name: ");
			String Fn = in.next();
			String Ln = in.next();
			admin.setFirstName(Fn);
			admin.setLastName(Ln);
		
			do {
				//Displays Admin menu
				System.out.println("Please make a selection from the menu below:");
				System.out.println("1. Create a new course");
				System.out.println("2. Delete a course");
				System.out.println("3. Edit a course");
				System.out.println("4. Display info for a given course");
				System.out.println("5. Register a student for a course");
				System.out.println("6. View all courses");
				System.out.println("7. View all full courses");
				System.out.println("8. Create a file of all full courses");
				System.out.println("9. View list of students registered for a specific course");
				System.out.println("10. View course list of a specific student");
				System.out.println("11. Sort courses buy current number of registered students");
				System.out.println("12. Exit");
			
				pick = in.nextInt();
				//Acts according to user selection
				if (pick == 1) {
					System.out.println("Enter Course Name: ");
					String name = in.nextLine();
					System.out.println("Enter Course ID: ");
					String id = in.nextLine();
					System.out.println("Enter maximum number of students for the course: ");
					int max = in.nextInt();
					System.out.println("Enter the instructor's name: ");
					String i = in.nextLine();
					System.out.println("Enter course section number: ");
					int sec = in.nextInt();
					System.out.println("Enter the course location: ");
					String local = in.nextLine();
			
					Course c = new Course(name, id, max, i, sec, local);
					admin.createCourse(c, courseList);
				}
				else if (pick == 2) {
					System.out.println("Enter the course ID of the course you want to delete: ");
					String id = in.nextLine();
				
					if (admin.deleteCourse(id, courseList) == 1)
						System.out.println("The course has been successfully deleted.");
					else 
						System.out.println("The course could not be found.");
				}
				else if (pick == 3) {
					System.out.println("Enter the course ID of the course you want to edit: ");
					String id = in.nextLine();
				
					if (admin.editCourse(id, courseList) == 1)
						System.out.println("The course has been successfully edited.");
					else 
						System.out.println("The course could not be found.");
				}
				else if (pick == 4) {
					System.out.println("Enter the course ID to view the course info: ");
					String id = in.nextLine();
				
					admin.displayInfo(id, courseList);
				}
				else if (pick == 5) {
					System.out.print("Enter the course ID to register for the course: ");
					String id = in.nextLine();
					System.out.println("Enter the first and last name of the student you want to register: ");
					String fn = in.next();
					String ln = in.next();
					
					if (admin.registerCourse(fn, ln, id, courseList) == 1)
						System.out.println(fn + " " + ln + " has been successfully registered.");
					else System.out.println("Registration was unsuccessful.");
				}
				else if (pick == 6) {
					admin.viewAll(courseList);
				}
				else if (pick == 7) {
					System.out.println("The following courses are full: ");
					admin.full(courseList);
				}
				else if (pick == 8) {
					admin.fileCoursesFull(courseList);
				}
				else if (pick == 9) {
					System.out.println("Enter the course ID to view the course student list: ");
					String id = in.nextLine();
				
					admin.viewRegStudents(id, courseList);
				}
				else if (pick == 10) {
					System.out.println("Enter the student's first and last name: ");
					String fn = in.next();
					String ln = in.next();
				
					admin.viewStudentsCourses(fn, ln, courseList);
				}
				else if (pick == 11) {
					admin.sortCourses(courseList);
					System.out.println("Courses successfully sorted.");
				}
			} while (pick != 12); 
			System.out.println("Goodbye");
		}
		else if (username.equalsIgnoreCase("Student") && password.equals("Student001")){
			System.out.println("Please enter your first and last name: ");
			String fn = in.next();
			String ln = in.next();
			
			Student student = new Student(fn, ln);
			
			do {
				//Displays Student menu
				System.out.println("Please make a selection from the menu below:");
				System.out.println("1. View all available courses");
				System.out.println("2. View all open courses");
				System.out.println("3. Register for a course");
				System.out.println("4. Withdraw from a course");
				System.out.println("5. View all my registered courses");
				System.out.println("6. Exit");
			
				pick = in.nextInt();
				//Acts according to user selection
				if (pick == 1) {
					student.printAllCourses(courseList);
				}
				else if (pick == 2) {
					System.out.println("The following courses are all open: ");
					student.courseFull(courseList);
				}
				else if (pick == 3) {
					System.out.println("Enter course name: ");
					String c = in.nextLine();
					System.out.println("Enter course section: ");
					int sec = in.nextInt();
					
					if (student.registerCourse(c, sec, fn, ln, courseList) == 1)
						System.out.println("You have been registered for this course.");
					else System.out.println("Registration was unsuccessful.");
				
				}
				else if (pick == 4) {
					System.out.println("Enter the name of the course you want to withdraw from: ");
					String course = in.nextLine();
					System.out.println("Enter the course section you want to withdraw from: ");
					int sec = in.nextInt();
				
					if (student.withdrawCourse(course, sec, fn, ln, courseList) == 1)
						System.out.println("You have successfully withdrawn from this course.");
					else System.out.println("Withdraw could not be completed.");
				}
				else if (pick == 5) {
					student.myCourses(fn, ln, courseList);
				}
			} while (pick != 6);
			System.out.println("Goodbye");
		}
		in.close();
	}
}
