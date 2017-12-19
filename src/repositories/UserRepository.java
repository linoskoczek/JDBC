package repositories;

import dtos.UserDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends BaseRepository implements IUserRepository {

    private final String ENTITY = "users";
    private final String LOGIN = "user_login";
    private final String PASSWORD = "user_password";

    @Override
    public List<UserDTO> findByName(String name) {
        String query = "SELECT * FROM " + ENTITY + " WHERE " + LOGIN + " = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, name);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<UserDTO> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(
                        new UserDTO(
                                resultSet.getString(1),
                                resultSet.getString(2)
                        )
                );
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void add(UserDTO dto) {
        String query = "INSERT INTO " + ENTITY + " VALUES (?, ?)";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, dto.getLogin());
            statement.setString(2, dto.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(UserDTO dto) {
        String query = "UPDATE " + ENTITY +
                " SET " + PASSWORD + " = ? WHERE " + LOGIN + " = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, dto.getPassword());
            statement.setString(2, dto.getLogin());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addOrUpdate(UserDTO dto) {
        if (exists(dto))
            update(dto);
        else
            add(dto);
    }

    @Override
    public void delete(UserDTO dto) {
        String query = "DELETE FROM " + ENTITY + " WHERE " + LOGIN + " = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, dto.getLogin());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserDTO findById(int id) {
        //cannot be implemented, we have only login and password
        return null;
    }

    public UserDTO findById(String login) {
        String query = "SELECT * FROM " + ENTITY + " WHERE " + LOGIN + " = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, login);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return new UserDTO(
                        resultSet.getString(1),
                        resultSet.getString(2)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        String query = "SELECT COUNT(*) FROM " + ENTITY;
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean exists(UserDTO dto) {
        String query = "SELECT * FROM " + ENTITY +
                " WHERE " + LOGIN + " = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, dto.getLogin());
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
