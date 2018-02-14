package com.vnfm.resource;

import com.vnfm.domain.Image;
import com.vnfm.domain.Package;
import com.vnfm.domain.Resource;
import com.vnfm.domain.Vnf;
import com.vnfm.service.VnfmService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Path("v1.0")
public class VnfmResource {
    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;

    @Autowired
    private VnfmService vnfmService;


    //接收Package文件
    @POST
    @Path("packages/receive")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Package receivePackage() throws Exception {
        //在项目路径下设置imageFiles文件夹作为路径
        String upload_file_path = request.getSession().getServletContext().getRealPath("/") + "packageFiles/";

        if (!Paths.get(upload_file_path).toFile().exists()) {
            Paths.get(upload_file_path).toFile().mkdirs();
        }
        // 设置工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置文件存储位置
        factory.setRepository(Paths.get(upload_file_path).toFile());
        // 设置大小，如果文件小于设置大小的话，放入内存中，如果大于的话则放入磁盘中,单位是byte
        factory.setSizeThreshold(0);
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 中文文件名处理
        upload.setHeaderEncoding("utf-8");
        String fileName = new String();
        String packageId = new String();
        String packageName = new String();
        List<FileItem> list = upload.parseRequest(request);
        for (FileItem item : list) {
            if (item.isFormField()) {
                String name = item.getFieldName();
                String value = item.getString("utf-8");
                if (name.equals("id"))
                    packageId = value;
                else if (name.equals("name"))
                    packageName = value;
                item.delete();
                request.setAttribute(name, value);
            } else {
                String name = item.getFieldName();
                String value = item.getName();
                System.out.println(name);
                System.out.println(value);

                fileName = Paths.get(value).getFileName().toString();
                request.setAttribute(name, fileName);
                // 写文件到upload_file_path目录，文件名为filename
                item.write(new File(upload_file_path, fileName));
            }
        }
        // 解析package文件，获取package参数
        File file = new File(upload_file_path + fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String info = br.readLine();
        JSONObject packageInfo = new JSONObject(info);
        String packageVersion = packageInfo.getString("version");
        String packageSupplier = packageInfo.getString("supplier");
        String[] imageIds = packageInfo.getString("images").split(":");
        String[] resources = packageInfo.getString("resources").split(",");
        List<Image> imageList = new ArrayList<>();
        for (String image : imageIds)
            imageList.add(new Image(image));
        List<Resource> resourceList = new ArrayList<>();
        for (String resource : resources)
            resourceList.add(new Resource(resource));
        Package aPackage = new Package(packageId, packageName, upload_file_path + fileName, packageVersion, packageSupplier, imageList, resourceList);
        return vnfmService.savePackage(aPackage);
    }

    @POST
    @Path("vnfs/instantiate")
    @Produces(MediaType.TEXT_PLAIN)
    public String instantiateVnf(@QueryParam("packageId") final String packageId) {
        Vnf vnf = new Vnf(packageId);
        vnfmService.saveVnf(vnf);
        return vnf.getId();
    }

    @POST
    @Path("vnfs/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Vnf addVnf(@QueryParam("vnfId") final String vnfId, @QueryParam("vnfUrl") final String vnfUrl) {
        Vnf vnf = vnfmService.getVnf(vnfId);
        vnf.setVnfUrl(vnfUrl);
        return vnfmService.saveVnf(vnf);
    }

    @POST
    @Path("vnfs/scaleout")
    @Produces(MediaType.APPLICATION_JSON)
    public Vnf scaleout(@QueryParam("vnfId") final String vnfId, @QueryParam("resourceId") final String resourceId,@QueryParam("memory") final String memory, @QueryParam("storage") final String storage) {
        final Vnf vnf = vnfmService.scaleout(vnfId, resourceId,memory, storage);
        return vnf;
    }

    @POST
    @Path("vnfs/scalein")
    @Produces(MediaType.APPLICATION_JSON)
    public Vnf scalein(@QueryParam("vnfId") final String vnfId, @QueryParam("storage") final String storage) {
        Vnf vnf = vnfmService.scaleout(vnfId);
        return vnf;
    }


    @DELETE
    @Path("deleteVnfm")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete() {
        if (vnfmService.deleteVnfm()) {
            return "Successed!";
        } else {
            return "Failed!";
        }
    }


}