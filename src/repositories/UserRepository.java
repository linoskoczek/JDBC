package repositories;

import dtos.UserDTO;
import java.util.List;

public class UserRepository extends BaseRepository implements IUserRepository {

    private final String ENTITY_NAME = "users";
    private final String LOGIN = "user_login";
    private final String PASSWORD = "user_password";

    @Override
    public List<UserDTO> findByName(String username) {
        return null;
    }

    @Override
    public void add(UserDTO dto) {

    }

    @Override
    public void update(UserDTO dto) {

    }

    @Override
    public void addOrUpdate(UserDTO dto) {

    }

    @Override
    public void delete(UserDTO dto) {

    }

    @Override
    public UserDTO findById(int id) {
        return null;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean exists(UserDTO dto) {
        return false;
    }
}
