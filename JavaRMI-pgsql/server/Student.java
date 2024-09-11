import java.rmi.RemoteException;
import java.util.ArrayList;

public class Student implements StudentInterface{
	// Attributes of product
	private int id;
	private String fullName;
	private String program;
	private int age;
	private String address;
	private String contact;
	ArrayList<Integer> course = new ArrayList<>();

	Student(int id, String fullName, String program, int age, String address, String contact) throws RemoteException{
		this.id = id;
		this.fullName = fullName;
		this.program = program;
		this.age = age;
		this.address = address;
		this.contact = contact;
	}
	public int getId() throws RemoteException{
		return this.id;
	}
	public String getFullName() throws RemoteException{
		return this.fullName;
	}
	public String getProgram() throws RemoteException{
		return this.program;
	}
	public int getAge() throws RemoteException{
		return this.age;
	}
	public String getAddress() throws RemoteException{
		return this.address;
	}
	public String getContact() throws RemoteException{
		return this.contact;
	}
	public ArrayList<Integer> getCourseEnrolled() throws RemoteException{
		return this.course;
	}
	public void setCourse(int courseCode) throws RemoteException{
		this.course.add(courseCode);
	}

}