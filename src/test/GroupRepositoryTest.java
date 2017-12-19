package test;

import dtos.GroupDTO;
import org.junit.Assert;
import org.junit.Test;
import repositories.GroupRepository;
import repositories.IGroupRepository;

import java.util.List;

public class GroupRepositoryTest extends RepositoryTestBase<GroupDTO, IGroupRepository> {

    private GroupRepository repository;
    private GroupDTO testObject;

    @Test
    public void add() {
        int count = repository.getCount();
        repository.add(testObject);
        Assert.assertEquals(count + 1, repository.getCount());
    }

    @Test
    public void update() {
        add();
        testObject.setName("ANOTHER COOL NAME");
        testObject.setDescription("ANOTHER COOL DESCRIPTION");
        repository.update(testObject);
        Assert.assertTrue(repository.findById(testObject.getId()).getName().equals("ANOTHER COOL NAME"));
        Assert.assertTrue(repository.findById(testObject.getId()).getDescription().equals("ANOTHER COOL DESCRIPTION"));
    }

    @Test
    public void addOrUpdate() {
        testObject.setName("Name 2");
        repository.addOrUpdate(testObject);
        Assert.assertTrue(repository.findById(testObject.getId()).getName().equals("Name 2"));
    }

    @Test
    public void delete() {
        GroupDTO dto = new GroupDTO(666, "123", "321");
        repository.add(dto);
        int count = repository.getCount();
        repository.delete(dto);
        if (count > 0) Assert.assertEquals(count - 1, repository.getCount());
        else Assert.assertEquals(0, repository.getCount());
    }

    @Test
    public void findById() {
        GroupDTO dto = new GroupDTO(666, "123", "321");
        repository.add(dto);
        GroupDTO foundObject = repository.findById(666);
        Assert.assertTrue(foundObject.getName().equals("123") && foundObject.getDescription().equals("321"));
    }

    @Test
    public void findByName() {
        GroupDTO dto = new GroupDTO(666, "123", "321");
        repository.add(dto);
        List<GroupDTO> foundObjects = repository.findByName("123");
        Assert.assertEquals(1, foundObjects.size());

        dto = new GroupDTO(667, "123", "321");
        repository.add(dto);
        foundObjects = repository.findByName("123");
        Assert.assertEquals(2, foundObjects.size());
    }

    @Override
    protected IGroupRepository Create() {
        repository = new GroupRepository();
        testObject = new GroupDTO(repository.getNextId(), "Group ABC", "Group for testing");
        repository.beginTransaction();
        return repository;
    }
}