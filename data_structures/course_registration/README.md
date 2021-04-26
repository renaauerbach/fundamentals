# University Course Registration (Java)

#### Assignment Instructions:

Consider the situation where you are hired as a software developer by a new university in your hometown. The university administration wants you to design and implement a Course Registration System (CRS). As we discussed in class, the very first step of software development is Requirements Gathering and Analysis. After several meetings with your client who represent the school’s administration that deals with student registration, consider to the minutes mentioned below that define the requirement you need to implement:

-   **Req 01:** The school shall store the following information about each course:
    `Course name`, `course id`, `maximum number of students that can register in the course`, `current number of registered students`, `a list of names of the students currently being registered in the given course`, `course instructor name`, `course section number`, and `course location`.
    _See attached MyUniversityCourses.csv file for your university data._
-   **Req 02:** The system shall allow two types of users: Admin and Student
-   **Req 03:** The system shall allow the Admin to perform the following tasks: (these are the options that will be in their menu that will be displayed your program when the administrator logs in)

    Courses Management for the Admin:

        1. Create a new course
        2. Delete a course
        3. Edit a course (this will allow the admin to the course name, Course ID, and instructor name)
        4. Display information for a given course (by course ID)
        5. Register a student (this option will allow the admin to add a student to some students list (arraylist) without assigning him or her to a course check Req 11 for student’s information – Hint: You might need to have an ArrayList of Students where you store students objects)
        6. Exit

    Reports for the Admin: (still under the Admin menu)

        1. View all courses (for every course the admin should be able to see the list of courses name, course id, and number of students registered and the maximum number of students allowed to be registered)
        2. View all courses that are FULL (reached the maximum number of students)
        3. Write to a file the list of courses that are Full (you should name the file as CoursesFull.txt) (writing to a normal text file not serialization)
        4. View the names of the students being registered in a specific course
        5. View the list of courses that a given student is being registered on (given a student first name and last name the system shall display all the courses that students is being registered in)
        6. Sort courses based on the current number of students registered
        7. Exit

-   **Req 04:** The system shall allow the student to perform the following tasks:

    Course Management for the Students:

        1. View all courses that are available
        2. View all courses that are not FULL
        3. Register on a course (in this case the student must enter the course name, section, and student first name and last name, the name will be added to the appropriate course’s list)
        4. Withdraw from a course (in this case the student will be asked to enter her/his student name and the course name and section, then the name of the student will be taken off from the given course’ list)
        5. View all courses that the current student is being registered in
        6. Exit

During your design meeting with your development team you agreed to adopt the following design:

-   **Req 05:** Define an Interface for admin class that will have the methods’ signatures that will be used by the Admin class.
-   **Req 06:** Define an Interface for a student class that will have the methods’ signatures that will be used by the student.
-   **Req 07:** Both classes Admin and Student inherit from a class named User. A user should have at least the following class member variables: username, password, first name, and last name (You will need to decide on the methods of a User’s class that could be inherited or overridden by the student and the admin class)
-   **Req 08:** At the beginning of launching the program, you will need to read all the courses information from the comma delimited file MyUniversityCourses.csv into an ArrayList of Course Objects.
-   **Req 09:** For simplicity assume that there is one Admin in the program.

At the beginning of the program you will need to read from the the CSV file if that is the first time you will run the program. Then you need to populate (from the CSV) your data structures where you will be storing the course data (you can just use an arraylist). After the program runs once and exists for the first time you will need to save the data permanently using serialization (see **Req 12**), the second time (and any time after the second time) your program should read the data from a serialized file and store it back to a serialized file.

-   **Req 11:** a student class should have at least a username, password, first name and last name. You will need to decide on how to keep track on student’s courses if needed. You might need to decide on how to store a list of students if needed.
-   **Req 12:** Serialization
