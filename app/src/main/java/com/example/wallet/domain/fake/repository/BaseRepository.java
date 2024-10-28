package com.example.wallet.domain.fake.repository;

import com.example.wallet.domain.models.Entity;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseRepository<TEntity extends Entity> {
    public abstract TEntity get(String id);
    public abstract ArrayList<TEntity> getAll();

    protected final ArrayList<Entity> dbContext;
    public BaseRepository(ArrayList<Entity> dbContext){
        this.dbContext = dbContext;
    }

    public boolean add(Entity entity) {
        entity.generateId();
        return dbContext.add(entity);
    }

    public boolean delete(String id) {
        return dbContext.removeIf(entity -> entity.getId().equals(id));
    }

    public boolean update(Entity entity) {
        boolean result = false;

        Optional<Entity> optionalEntity = this.dbContext.stream()
                .filter(e -> e.getId().equals(entity.getId()))
                .findFirst();

        if (optionalEntity.isPresent()) {
            //Entity _entity = optionalEntity.get();
            result = true;
        }
        return result;
    }
    protected <TObjet extends  Entity> ArrayList<TObjet> filterByClass(Class<TObjet> clazz) {
        return dbContext.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
