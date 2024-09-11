import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertStudent extends DatabaseConnection implements InsertStudentInterface {

    @Override
    public void insertStudentIntoDatabase(int id, String name, String program, int age, String address, String contact) throws RemoteException {
        String query = "INSERT INTO students (id, name, program, age, address, contact) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, program);
            statement.setInt(4, age);
            statement.setString(5, address);
            statement.setString(6, contact);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error inserting student into database", e);
        }
    }

    @Override
    public void insertCourseIntoDatabase(int courseId, String name, String description) throws RemoteException {
        String query = "INSERT INTO courses (id, name, description) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, courseId);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Error inserting course into database", e);
        }
    }
}
