package test;

import dtos.UserDTO;
import org.junit.Assert;
import org.junit.Test;
import repositories.IUserRepository;
import repositories.UserRepository;

import java.util.List;

public final class UserRepositoryTest extends RepositoryTestBase<UserDTO, IUserRepository> {

    private UserRepository repository;
    private UserDTO testObject;

    @Test
    public void add() {
        int count = repository.getCount();
        repository.add(testObject);
        Assert.assertEquals(count + 1, repository.getCount());
    }

    @Test
    public void update() {
        add();
        testObject.setPassword("12345678");
        repository.update(testObject);
        Assert.assertTrue(repository.findById("loginExample").getPassword().equals("12345678"));
    }

    @Test
    public void addOrUpdate() {
        testObject.setLogin("myExample");
        repository.addOrUpdate(testObject);
        Assert.assertTrue(repository.findById("myExample") != null);
        Assert.assertFalse(repository.findById("wontBeFound") != null);
    }

    @Test
    public void delete() {
        UserDTO dto = new UserDTO(666, "123", "321");
        repository.add(dto);
        int count = repository.getCount();
        repository.delete(dto);
        if (count > 0) Assert.assertEquals(count - 1, repository.getCount());
        else Assert.assertEquals(0, repository.getCount());
    }

    @Test
    public void findById() {
        UserDTO dto = new UserDTO(666, "123", "321");
        repository.add(dto);
        UserDTO foundObject = repository.findById("123");
        Assert.assertTrue(foundObject.getLogin().equals("123") && foundObject.getPassword().equals("321"));
    }

    @Test
    public void findByName() {
        UserDTO dto = new UserDTO("123", "321");
        repository.add(dto);
        List<UserDTO> foundObjects = repository.findByName("123");
        Assert.assertEquals(1, foundObjects.size());

        dto = new UserDTO("124", "321");
        repository.add(dto);
        List<UserDTO> foundObjects2 = repository.findByName("124");
        Assert.assertEquals(2, foundObjects.size() + foundObjects2.size());
    }

    @Override
    protected IUserRepository Create() {
        repository = new UserRepository();
        testObject = new UserDTO(0, "loginExample", "passwd");
        repository.beginTransaction();
        return repository;
    }
}