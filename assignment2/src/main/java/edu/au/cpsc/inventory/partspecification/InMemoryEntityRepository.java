package edu.au.cpsc.inventory.partspecification;

import java.util.ArrayList;
import java.util.List;

/**
 * My concrete instances store {@link Entity}s.
 *
 * @param <T> the type of {@link Entity} stored by this repository.
 */
public class InMemoryEntityRepository<T extends Entity> {

  protected List<T> entities;
  protected long lastId;

  public InMemoryEntityRepository() {
    entities = new ArrayList<>();
    lastId = 0;
  }

  private Long nextId() {
    return Long.valueOf(lastId++);
  }

  private void ensureId(T entity) {
    if (entity.getId() != null) {
      return;
    }
    entity.setId(nextId());
  }

  /**
   * Store the specified {@link Entity} to this repository.  If the {@link Entity} does not have an
   * id, one will be assigned.
   *
   * @param entity the {@link Entity} to add
   * @return the id of the object to save
   */
  public Long save(T entity) {
    ensureId(entity);
    entities.add(entity);
    return entity.getId();
  }

  /**
   * Return a list of all {@link Entity}s that have been saved in this repository.
   *
   * @return all {@link Entity}s that have been saved in this repository
   */
  public List<T> findAll() {
    return entities;
  }

  /**
   * Given an id, return the part specification with that id or null if that part specification does
   * not exist.
   *
   * @param id the id of the {@link Entity} to find
   * @return the part specification with that id or null if there is none
   */
  public T findOne(Long id) {
    for (var ps : entities) {
      if (ps.getId().equals(id)) {
        return ps;
      }
    }
    return null;
  }

}