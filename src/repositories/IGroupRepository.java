package repositories;

import java.util.List;

import dtos.GroupDTO;

public interface IGroupRepository extends IRepository<GroupDTO> {

	List<GroupDTO> findByName(String name);
}