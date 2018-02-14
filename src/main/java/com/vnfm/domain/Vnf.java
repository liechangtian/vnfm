package com.vnfm.domain;


import javax.persistence.*;
import java.util.List;

/***
 * Created on 2018/2/8 at 17:12.
 ***/
@Entity
@Table(name = "vnf")
public class Vnf {
    private static final long serialVersionUID = 1L;
    private static final int ID_LENGTH = 20;
    private static final int URL_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // id中不能含有字符'-'
    private String id;
    private String packageId;
    private String vnfUrl;
    @OneToMany(targetEntity = Resource.class)
    private List<Resource> resources;

    public Vnf() {
    }

    public Vnf(String packageId) {
        this.packageId = packageId;
    }

    public Vnf(String packageId, String vnfUrl) {
        this.packageId = packageId;
        this.vnfUrl = vnfUrl;
    }

    public Vnf(String id, String packageId, String vnfUrl) {
        this.id = id;
        this.packageId = packageId;
        this.vnfUrl = vnfUrl;
    }

    public Vnf(String id, String packageId, String vnfUrl, List<Resource> resources) {
        this.id = id;
        this.packageId = packageId;
        this.vnfUrl = vnfUrl;
        this.resources = resources;
    }


    @Column(unique = true, nullable = false, name = "ID")
    public String getId() {
        return id;
    }

    @Column(length = Vnf.ID_LENGTH, name = "PACKAGEID")
    public String getPackageId() {
        return packageId;
    }

    @Column(length = Vnf.URL_LENGTH, name = "VNFURL")
    public String getVnfUrl() {
        return vnfUrl;
    }

    public List getResources() {
        return resources;
    }

    public void setId(String i) {
        id = i;
    }

    public void setPackageId(String i) {
        packageId = i;
    }

    public void setVnfUrl(String i) {
        vnfUrl = i;
    }

    public void setResources(List resources) {
        this.resources = resources;
    }

    public boolean addResource(Resource resource) {
        return this.resources.add(resource);
    }

    public boolean deleteResource(String resourceId) {
        for (Resource resource : this.resources) {
            if (resource.getId().equals(resourceId))
                return this.resources.remove(resource);
        }
        return false;
    }

    @Override
    public String toString() {
        return id + ":" + packageId + ":" + vnfUrl;
    }
}
