package com.vnfm.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "resource")
public class Resource implements Serializable {
    private static final long serialVersionUID = 1L;
    //对于VIM上的Resource,NFVO将其id存为"vimId-resourceId"以确保id唯一性，所以长度为41
    private static final int ID_LENGTH = 41;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // id中不能含有字符'-'
    private String id;
    private int memory;
    private int storage;

    public Resource() {
    }

    // 以字符串初始化Resource，格式为"memory:storage"
    public Resource(String s) {
        String[] resource = s.split(":");
        this.memory = Integer.parseInt(resource[0]);
        this.storage = Integer.parseInt(resource[1]);
    }

    public Resource(int memory, int storage) {
        this.memory = memory;
        this.storage = storage;
    }

    public Resource(String id, int memory, int storage) {
        this.id = id;
        this.memory = memory;
        this.storage = storage;
    }

    @Column(unique = true, nullable = false, length = Resource.ID_LENGTH, name = "ID")
    public String getId() {
        return id;
    }

    @Column(name = "MEMORY")
    public int getMemory() {
        return memory;
    }

    @Column(name = "STORAGE")
    public int getStorage() {
        return storage;
    }

    public void setId(String i) {
        id = i;
    }

    public void setMemory(int n) {
        memory = n;
    }

    public void setStorage(int n) {
        storage = n;
    }


    @Override
    public String toString() {
        return id + ":" + memory + ":" + storage;
    }
}
