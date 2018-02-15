package com.vnfm.dao;

import com.vnfm.domain.Package;
import com.vnfm.domain.Vnf;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/***
 * Created on 2018/2/13 at 23:29.  
 ***/
@Repository
public class VnfmDao {
    @PersistenceContext
    private EntityManager entityManager;

    public VnfmDao() {
    }

    public Vnf findVnfById(final String id) {
        Assert.notNull(id);
        try {
            return entityManager.find(Vnf.class, id);
        } catch (final Exception e) {
            return null;
        }
    }
    // 注册包
    @Transactional
    public Package storePackage(final Package entity) {
        return entityManager.merge(entity);
    }

    // 注册VNF
    @Transactional
    public Vnf storeVnf(final Vnf entity) {
        return entityManager.merge(entity);
    }
}
