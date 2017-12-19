package test;

import dtos.UserDTO;
import exception.UnimplementedException;
import org.junit.Test;
import repositories.IUserRepository;

public final class UserRepositoryTest extends RepositoryTestBase<UserDTO, IUserRepository> {

    @Test
    public void add() {
    }

    @Test
    public void update() {
    }

    @Test
    public void addOrUpdate() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void findById() {
    }

    @Test
    public void findByName() {
    }

    @Override
    protected IUserRepository Create() {
        throw new UnimplementedException();
    }
}