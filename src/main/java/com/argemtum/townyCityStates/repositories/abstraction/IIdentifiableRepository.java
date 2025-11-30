package com.argemtum.townyCityStates.repositories.abstraction;

import java.util.Collection;
import java.util.Optional;

public interface IIdentifiableRepository<T, ID> {
    // TODO load(ID id)
    Optional<T> get(ID id);
    Collection<T> getAll();
    void save(T entity);
    void saveAll(Collection<T> entities);
    void loadAll();
}