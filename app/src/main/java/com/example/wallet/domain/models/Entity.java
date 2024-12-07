package com.example.wallet.domain.models;

import java.util.UUID;

public abstract class Entity {
    protected  String id;

    public String getId() { return this.id; }
    public void setId(String id){
        this.id = id;
    }

    public void generateId(){
        this.id = UUID.randomUUID().toString();
    }

}
