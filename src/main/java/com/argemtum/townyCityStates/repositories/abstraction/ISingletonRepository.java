package com.argemtum.townyCityStates.repositories.abstraction;

public interface ISingletonRepository<T> {
    T GetInstance();
    void load();
    void save();
}
