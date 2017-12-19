package test;

import dtos.GroupDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repositories.GroupRepository;
import repositories.IGroupRepository;

import java.util.List;

public class GroupRepositoryTest extends RepositoryTestBase<GroupDTO, IGroupRepository> {

    private GroupRepository repository;
    private GroupDTO testObject;

    @Before
    public void createRepositoryAndBeginTransaction() {
        repository = new GroupRepository();
        testObject = new GroupDTO(repository.getMaxId(), "Group ABC", "Group for testing");
        repository.beginTransaction();
    }

    @After
    public void whatToDoWithTransaction() {
//		repository.rollbackTransaction();
        repository.commitTransaction();
    }

    @Test
    public void add() {
        repository.add(testObject);
    }

    @Test
    public void update() {
        System.out.println(testObject.getId());
        testObject.setName("ANOTHER COOL NAME");
        testObject.setDescription("ANOTHER COOL DESCRIPTION");
        repository.update(testObject);
    }

    @Test
    public void addOrUpdate() {
        testObject.setName("Name 2");
        repository.addOrUpdate(testObject);
    }

    @Test
    public void delete() {
        repository.delete(testObject);
    }

    @Test
    public void findById() {
        GroupDTO foundObject = repository.findById(0);
    }

    @Test
    public void findByName() {
        List<GroupDTO> foundObjects = repository.findByName("ABC");
    }

    @Override
    protected IGroupRepository Create() {
        //throw new UnimplementedException();
        return null;
    }
}