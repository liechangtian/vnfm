package com.vnfm.domain;

import javax.persistence.*;
import java.io.Serializable;

/***
 * Created on 2017/11/6 at 15:48.
 ***/
@Entity
@Table(name = "image")
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int ID_LENGTH = 20;
    private static final int NAME_LENGTH = 100;
    private static final int POS_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String name;
    private String position;

    public Image() {
    }

    public Image(String id) {
        this.id = id;
    }
    public Image(String id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    @Column(unique = true, nullable = false, length = Image.ID_LENGTH, name = "ID")
    public String getId() {
        return id;
    }

    @Column(length = Image.NAME_LENGTH, name = "NAME")
    public String getName() {
        return name;
    }

    @Column(length = Image.POS_LENGTH, name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setId(String i) {
        id = i;
    }

    public void setName(String n) {
        name = n;
    }

    public void setPosition(String n) {
        position = n;
    }


    @Override
    public String toString() {
        return id + ":" + name;
    }
}
