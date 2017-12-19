package repositories;

import dtos.GroupDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupRepository extends BaseRepository implements IGroupRepository {

    private final String ENTITY = "groups";
    private final String ID = "group_id";
    private final String NAME = "group_name";
    private final String DESCRIPTION = "group_description";

    @Override
    public List<GroupDTO> findByName(String name) {
        String query = "SELECT * FROM " + ENTITY + " WHERE " + NAME + " = ?";
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
            return groups;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void add(GroupDTO dto) {
        String query = "INSERT INTO " + ENTITY + " VALUES (?, ?, ?)";
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
        String query = "SELECT * FROM " + ENTITY + " WHERE " + ID + " = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return new GroupDTO(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
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
    public boolean exists(GroupDTO dto) {
        String query = "SELECT * FROM " + ENTITY +
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
}
