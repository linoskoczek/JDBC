package repositories;

import dtos.GroupDTO;
import dtos.UserDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends BaseRepository implements IUserRepository {

    static final String ENTITY = "users";
    static final String LOGIN = "user_login";
    private final String ID = "group_id";
    static final String PASSWORD = "user_password";
    private final String GROUPS_USERS = "groups_users";

    @Override
    public List<UserDTO> findByName(String name) {
        String query = "SELECT " +LOGIN+","+PASSWORD + " FROM " + ENTITY + " WHERE " + LOGIN + " = ?";
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
        String query = "INSERT INTO "+ENTITY +"(" + LOGIN+","+PASSWORD + ") VALUES (?, ?)";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, dto.getLogin());
            statement.setString(2, dto.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        addToGroupUsers(dto);
    }

    private void addToGroupUsers(UserDTO dto) {
        String query = "INSERT INTO "+GROUPS_USERS +"(" + LOGIN+","+GROUPS_USERS+"."+ID + ") VALUES (?, ?)";
        List<GroupDTO> groups = dto.getGroups();
        if(groups == null) return;
        groups
            .stream()
            .map(g -> g.getId())
            .forEach(groupId -> {
                try {
                    PreparedStatement statement = getConnection().prepareStatement(query);
                    statement.setString(1, dto.getLogin());
                    statement.setInt(2, groupId);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
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

        if(dto.getGroups() == null || dto.getGroups().size() == 0) return;

        query = "DELETE FROM " + GROUPS_USERS + " WHERE " + LOGIN + " = ?";

        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, dto.getLogin());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        addToGroupUsers(dto);
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
        String query = "SELECT " +LOGIN+","+PASSWORD + " FROM " + ENTITY + " WHERE " + LOGIN + " = ?";
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
