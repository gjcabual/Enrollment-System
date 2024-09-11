

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Client {

	private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres"; // postgresql
	private static final String DB_USER = "postgres";
	private static final String DB_PASSWORD = "password";
	private static Connection connection;

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

			// Locate the registry
			Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9100);

			// Get the references of exported object from the RMI Registry
			EnrollmentInitialInterface p1 = (EnrollmentInitialInterface) registry.lookup("access_enrollment");
			EnrollmentInterface p2 = (EnrollmentInterface) registry.lookup("access_enrollment");
			InsertStudentInterface p3 = (InsertStudentInterface) registry.lookup("insert_student");

			p1.truncateTable("students");
			p1.truncateTable("courses");
			p1.truncateTable("enrolled_students");
			p2.clearArrayList();

			File xmlFile = new File("XML-Files/Student.xml");
			File xml = new File("XML-Files/Course.xml");
			File enrolledXML = new File("XML-Files/Enrolled.xml");

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document1 = parseXMLFile(dBuilder, xmlFile);
			Document document2 = parseXMLFile(dBuilder, xml);
			Document document3 = parseXMLFile(dBuilder, enrolledXML);

			// Parse Student XML
			NodeList studentList = document1.getElementsByTagName("student");

			for (int i = 0; i < studentList.getLength(); i++) {
				Element studentElement = (Element) studentList.item(i);

				int id = Integer.parseInt(studentElement.getElementsByTagName("id").item(0).getTextContent());
				String name = studentElement.getElementsByTagName("name").item(0).getTextContent();
				String program = studentElement.getElementsByTagName("program").item(0).getTextContent();
				int age = Integer.parseInt(studentElement.getElementsByTagName("age").item(0).getTextContent());
				String address = studentElement.getElementsByTagName("address").item(0).getTextContent();        
				String contact = studentElement.getElementsByTagName("contact").item(0).getTextContent();        
				
				p1.initializeStudents(id, name, program, age, address, contact);

				// Insert student data into the database
				p3.insertStudentIntoDatabase(id, name, program, age, address, contact);
			}

			// Parse Course XML
			NodeList courseList = document2.getElementsByTagName("course");

			for (int i = 0; i < courseList.getLength(); i++) {
				Element courseElement = (Element) courseList.item(i);

				int courseId = Integer
						.parseInt(courseElement.getElementsByTagName("course-id").item(0).getTextContent());
				String name = courseElement.getElementsByTagName("title").item(0).getTextContent();
				String description = courseElement.getElementsByTagName("description").item(0).getTextContent();

				p1.initializeCourses(courseId, name, description);
				p3.insertCourseIntoDatabase(courseId, name, description);
			}

			// Parse Enroll XML
			NodeList enrolledList = document3.getElementsByTagName("enrolled");

			for (int i = 0; i < enrolledList.getLength(); i++) {
				Element student = (Element) enrolledList.item(i);

				int std_id = Integer.parseInt(student.getElementsByTagName("student_id").item(0).getTextContent());
				int course_id = Integer.parseInt(student.getElementsByTagName("course").item(0).getTextContent());

				p1.insertEnrolledStudent(std_id, course_id);
				p1.initializeEnrolledStudent(std_id, course_id);

			}

			Scanner scanner = new Scanner(System.in);
			int userSelection = 0;
			// Start Menu
			do {
				System.out.println("\n\n ==== Menu ==== \n");
				System.out.println("Key [1] - Display All Students");
				System.out.println("Key [2] - Display All Courses");
				System.out.println("Key [3] - Enroll Student");
				System.out.println("Key [4] - Add new student");
				System.out.println("Key [0] - Exit");
				System.out.print("Type here: ");
				userSelection = scanner.nextInt();
				if (userSelection == 1) {
					System.out.println(p2.displayStudents());
				} else if (userSelection == 2) {
					System.out.println(p2.displayCourses());
				} else if (userSelection == 3) {
					System.out.print("\n\nEnter Student ID: ");
					int studentID = scanner.nextInt();
					System.out.print("Enter Course Code: ");
					int studentCourseCode = scanner.nextInt();
					int result = 0;
					result = p2.enrollCourse(studentID, studentCourseCode);
					if (result == 0) {
						System.out.println("Invalid student or course");
					} else {

						System.out.println(
								"Student has been successfully enrolled in a course with a code " + studentCourseCode);
						// Need
						p1.insertEnrolledStudent(studentID, studentCourseCode);

						// final
						insertEnrollStudentXML(studentID, studentCourseCode);

					}
				} else if (userSelection == 4) {
					System.out.println("Enter Student");
					System.out.print("Student Id: ");
					int id = scanner.nextInt();
					scanner.nextLine(); // Consume newline
					
					System.out.print("Student name:  ");
					String name = scanner.nextLine();

					System.out.print("Student program:  ");
					String program = scanner.nextLine();
					
					System.out.print("Student age:  ");
					int age = scanner.nextInt();
					scanner.nextLine();

					System.out.print("Student address:  ");
					String address = scanner.nextLine();

					System.out.print("Student contact:  ");
					String contact = scanner.nextLine();

					int std = p2.addStudent(id, name, program, age, address, contact);
					if (std == 0) {
						System.out.println("Student already exists, please try again");
					} else {
						System.out.println("New Student added");
						p3.insertStudentIntoDatabase(id, name, program, age, address, contact);
						insertStudentXML(id, name, program, age, address, contact);

					}

				} else {
					System.out.println("\n\nInvalid key!");
				}
			} while (userSelection != 0);
			p2.clearArrayList();
			System.out.println("\n\nProgram successfully exited.");
			scanner.close();
		} catch (Exception e) {
			System.out.println("Client side error..." + e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static Document parseXMLFile(DocumentBuilder dBuilder, File xmlFile) {
		try {
			return dBuilder.parse(xmlFile);
		} catch (SAXException | IOException e) {
			System.out.println("Error parsing XML file: " + xmlFile.getName() + " - " + e.getMessage());
			return null;
		}
	}

	private static void insertEnrollStudentXML(int std_id, int course_id)
			throws SAXException, IOException, TransformerException {
		File enrollXML = new File("XML-Files/Enrolled.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(enrollXML);

			document.getDocumentElement().normalize();

			Node root = document.getDocumentElement();

			Element enroll_student = document.createElement("enrolled");

			String str_id = String.valueOf(std_id);
			Element id = document.createElement("student_id");
			id.appendChild(document.createTextNode(str_id));
			enroll_student.appendChild(id);

			String str_course = String.valueOf(course_id);
			Element enroll_course = document.createElement("course");
			enroll_course.appendChild(document.createTextNode(str_course));
			enroll_student.appendChild(enroll_course);

			root.appendChild(enroll_student);

			removeWhitespaceNodes(document);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

			DOMSource domSource = new DOMSource(document);

			StreamResult streamResult = new StreamResult(enrollXML);

			transformer.transform(domSource, streamResult);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private static void insertStudentXML(int id, String name, String program,int age, String address, String contact)
			throws SAXException, IOException, TransformerException {
		File studentXML = new File("XML-Files/Student.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(studentXML);

			document.getDocumentElement().normalize();

			Node root = document.getDocumentElement();

			Element student = document.createElement("student");

			String str_id = String.valueOf(id);
			Element id_element = document.createElement("id");
			id_element.appendChild(document.createTextNode(str_id));
			student.appendChild(id_element);

			Element student_name = document.createElement("name");
			student_name.appendChild(document.createTextNode(name));
			student.appendChild(student_name);

			Element student_program = document.createElement("program");
			student_program.appendChild(document.createTextNode(program));
			student.appendChild(student_program);

			String str_age = String.valueOf(age);
			Element age_element = document.createElement("age");
			age_element.appendChild(document.createTextNode(str_age));
			student.appendChild(age_element);

			Element student_address = document.createElement("address");
			student_address.appendChild(document.createTextNode(address));
			student.appendChild(student_address);

			Element student_contact = document.createElement("contact");
			student_contact.appendChild(document.createTextNode(contact));
			student.appendChild(student_contact);

			root.appendChild(student);

			removeWhitespaceNodes(document);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

			DOMSource domSource = new DOMSource(document);

			StreamResult streamResult = new StreamResult(studentXML);

			transformer.transform(domSource, streamResult);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private static void removeWhitespaceNodes(Document document) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			NodeList emptyTextNodes = (NodeList) xPath.evaluate("//text()[normalize-space(.) = '']", document,
					XPathConstants.NODESET);

			for (int i = 0; i < emptyTextNodes.getLength(); i++) {
				Node emptyTextNode = emptyTextNodes.item(i);
				emptyTextNode.getParentNode().removeChild(emptyTextNode);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
}
