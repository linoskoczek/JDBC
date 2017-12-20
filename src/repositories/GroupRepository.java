package repositories;

import dtos.GroupDTO;
import dtos.UserDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GroupRepository extends BaseRepository implements IGroupRepository {

    private final String ENTITY = "groups";
    static final String ID = "group_id";
    private final String NAME = "group_name";
    private final String DESCRIPTION = "group_description";
    static final String GROUPS_USERS = "groups_users";

    @Override
    public List<GroupDTO> findByName(String name) {
        String query = "SELECT " + ID+","+NAME+","+DESCRIPTION + " FROM " + ENTITY + " WHERE " + NAME + " = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, name);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<GroupDTO> groups = new ArrayList<>();
            while (resultSet.next()) {
                groups.add(
                        new GroupDTO(
                                resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3)
                        )
                );
            }
            groups.forEach(g -> addUsersToGroup(g, getUsersFromGroup(g)));
            return groups;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void add(GroupDTO dto) {
        String query = "INSERT INTO "+ENTITY +"("+ID+","+NAME+","+DESCRIPTION+") VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, dto.getId());
            statement.setString(2, dto.getName());
            statement.setString(3, dto.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GroupDTO dto) {
        String query = "UPDATE " + ENTITY +
                " SET " + NAME + " = ?, " + DESCRIPTION + " = ? WHERE " + ID + " = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, dto.getName());
            statement.setString(2, dto.getDescription());
            statement.setInt(3, dto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addOrUpdate(GroupDTO dto) {
        if (exists(dto))
            update(dto);
        else
            add(dto);
    }

    @Override
    public void delete(GroupDTO dto) {
        String query = "DELETE FROM " + ENTITY + " WHERE " + ID + " = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, dto.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GroupDTO findById(int id) {
        String query = "SELECT " + ID+","+NAME+","+DESCRIPTION + " FROM " + ENTITY + " WHERE " + ID + " = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {

                GroupDTO newGroup = new GroupDTO(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
                );
                addUsersToGroup(newGroup, getUsersFromGroup(newGroup));
                return newGroup;
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
    public boolean exists(GroupDTO dto) {
        String query = "SELECT " + ID+","+NAME+","+DESCRIPTION + " FROM " + ENTITY +
                " WHERE " + ID + " = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, dto.getId());
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getNextId() {
        return getMaxId() + 1;
    }

    public int getMaxId() { // returns -1 if no id
        String query = "SELECT IFNULL(MAX(" + ID + "), -1) FROM " + ENTITY;
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private List<UserDTO> getUsersFromGroup(GroupDTO dto) {
        String query = "SELECT " + UserRepository.ENTITY+"."+UserRepository.LOGIN + "," + UserRepository.ENTITY+"."+UserRepository.PASSWORD
                + " FROM " + UserRepository.ENTITY + "," + GROUPS_USERS
                + " WHERE " + UserRepository.ENTITY + "." + UserRepository.LOGIN + " = "
                                            + GROUPS_USERS + "." + UserRepository.LOGIN + " AND "
                            + GROUPS_USERS + "." + ID + " = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, dto.getId());
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            List<UserDTO> users = new LinkedList<>();
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

    private void addUsersToGroup(GroupDTO dto, List<UserDTO> userDTOList) {
        dto.setUsers(userDTOList);
    }
}
