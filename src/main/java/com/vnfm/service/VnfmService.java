package com.vnfm.service;

import com.vnfm.dao.VnfmDao;
import com.vnfm.domain.Package;
import com.vnfm.domain.Resource;
import com.vnfm.domain.Vnf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***
 * Created on 2018/2/13 at 16:11.  
 ***/
@Service
public class VnfmService {
    @Autowired
    private VnfmDao vnfmDao;

    public VnfmService() {
    }

    public Vnf getVnf(final String vnfId) {
        try {
            return vnfmDao.findVnfById(vnfId);
        } catch (final Exception e) {
            return null;
        }
    }

    // 注册包，测试时仅保存包，现实中的VNFM还应解析包文件获取VNFD相关参数，以便于对基于此包实例化的VNF进行管理
    public Package savePackage(final Package aPackage) {
        return vnfmDao.storePackage(aPackage);
    }

    // 实例化VNF
    public Vnf saveVnf(final Vnf vnf) {
        return vnfmDao.storeVnf(vnf);
    }

    // VNF扩容
    public Vnf scaleout(final String vnfId, final String resourceId, final String memory, final String storage) {
        int m = Integer.parseInt(memory);
        int s = Integer.parseInt(storage);
        Resource resource = new Resource(resourceId, m, s);
        Vnf vnf = vnfmDao.findVnfById(vnfId);
        vnf.addResource(resource);
        return vnfmDao.storeVnf(vnf);
    }

    // 删除VNFM，测试时始终返回true，实际内容应是删除自身的逻辑
    public boolean deleteVnfm() {
        return true;
    }
}
