package com.crossover.petitosa.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public abstract class CrudService<TEntity, TId, TRepository extends JpaRepository<TEntity, TId>> {

    @Autowired
    private TRepository repository;

    @Nullable
    public TEntity findById(TId id) {
        return repository.findById(id).orElse(null);
    }

    public Collection<TEntity> findAll() {
        return repository.findAll();
    }

    public boolean existsById(TId id) {
        return repository.existsById(id);
    }

    public void deleteById(TId id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }

    public <S extends TEntity> S save(S item) {
        return repository.save(item);
    }

    public <S extends TEntity> Collection<S> saveAll(Iterable<S> items) {
        return repository.saveAll(items);
    }

    public long countAll() {
        return repository.count();
    }

    public TRepository getRepository() {
        return repository;
    }
}
