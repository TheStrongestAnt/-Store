package com.dong.store.controller;

import com.dong.store.controller.ex.*;
import com.dong.store.entity.UserEntity;
import com.dong.store.service.IUserService;
import com.dong.store.util.JsonResult;
import com.dong.store.controller.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController extends BaseController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping("reg")
    public JsonResult<Void> reg(UserEntity userEntity) {
        iUserService.reg(userEntity);
        return new JsonResult<>(OK);
    }

    /*try {
        iUserService.reg(userEntity);
        result.setState(200);
        result.setMessage("注册成功");
    } catch (UsernameDuplicateException e) {
        result.setState(4000);
        result.setMessage("用户名已被使用");
    } catch (InsertException e) {
        result.setState(5000);
        result.setMessage("注册失败，请联系系统管理员");
    }
    return result;
    */
    @RequestMapping("login")
    public JsonResult<UserEntity> login(String username, String password, HttpSession session) {
        UserEntity data = iUserService.login(username, password);
        //向session对象中完成数据的绑定（session全局的）
        session.setAttribute("uid", data.getUid());
        session.setAttribute("username", data.getUsername());


        System.out.println(getUidFromSession(session));
        System.out.println(getUserNameFromSession(session));
        return new JsonResult<>(OK, data);
    }

    @RequestMapping("change_password")
    public JsonResult<Void> changePassword(String oldPassword, String newPassword, HttpSession session) {
        // 调用session.getAttribute("")获取uid和username
        Integer uid = getUidFromSession(session);
        String username = session.getAttribute("username").toString();
        // 调用业务对象执行修改密码
        iUserService.changePassword(uid, username, oldPassword, newPassword);
        // 返回成功
        return new JsonResult<>(OK);
    }

    @RequestMapping("change_info")
    public JsonResult<Void> changeInfo(UserEntity userEntity, HttpSession session) {
        // 从HttpSession对象中获取uid和username
        Integer uid = getUidFromSession(session);
        String username = getUserNameFromSession(session);
        // 调用业务对象执行修改用户资料
        iUserService.changeInfo(uid, username, userEntity);
        // 响应成功
        return new JsonResult<>(OK);
    }

    @RequestMapping("get_by_uid")
    public JsonResult<UserEntity> getByUid(HttpSession session) {
        // 从HttpSession对象中获取uid
        Integer uid = getUidFromSession(session);
        // 调用业务对象执行获取数据
        UserEntity byUid = iUserService.getByUid(uid);
        // 响应成功和数据
        return new JsonResult<>(OK, byUid);
    }

    /**MultipartFile接口是SpringMVC提供一个接口,这个接口为我们包装了获取文件类型的数据(任何类型的file都可以接收),
     * SpringBoot它有整合了SpringMVC,只需要在处理请求的方法参数列表上声明一个参数类型为Multipart的参数,
     * 然后SpringBoot自动将传递给服务的文件数据赋值赋值给这个参数
     *
     * 注解@RequestParam 表示请求中的参数,将请求中的参数注入请求处理方法的某个参数上,如果名称不一致则可以使
     * 用@RequestParam注解进行标记和映射
     *
     * @param file 上传的文件
     * @param session 当前用户的session对象
     * @return 上传文件的储存路径
     */
    @PostMapping("change_avatar")
    public JsonResult<String> changeAvatar(@RequestParam("file") MultipartFile file, HttpSession session) {
        // 判断上传的文件是否为空
        if (file.isEmpty()) {
            // 是：抛出异常
            throw new FileEmptyException("上传的头像文件不允许为空");
        }

        // 判断上传的文件大小是否超出限制值
        if (file.getSize() > AVATAR_MAX_SIZE) { // getSize()：返回文件的大小，以字节为单位
            // 是：抛出异常
            throw new FileSizeException("不允许上传超过" + (AVATAR_MAX_SIZE / 1024) + "KB的头像文件");
        }

        // 判断上传的文件类型是否超出限制
        String contentType = file.getContentType();
        // public boolean list.contains(Object o)：当前列表若包含某元素，返回结果为true；若不包含该元素，返回结果为false。
        if (!AVATAR_TYPES.contains(contentType)) {
            // 是：抛出异常
            throw new FileTypeException("不支持使用该类型的文件作为头像，允许的文件类型：\n" + AVATAR_TYPES);
        }

        // 获取当前项目的绝对磁盘路径
        String parent = session.getServletContext().getRealPath("upload");
        System.out.println(parent);
        // 保存头像文件的文件夹
        File dir = new File(parent);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 保存的头像文件的文件名
        String suffix = "";
        String originalFilename = file.getOriginalFilename();
        int beginIndex = originalFilename.lastIndexOf(".");
        if (beginIndex > 0) {
            suffix = originalFilename.substring(beginIndex);
        }
        String filename = UUID.randomUUID().toString() + suffix;

        // 创建文件对象，表示保存的头像文件
        File dest = new File(dir, filename);
        // 执行保存头像文件
        try {
            file.transferTo(dest);
        } catch (IllegalStateException e) {
            // 抛出异常
            throw new FileStateException("文件状态异常，可能文件已被移动或删除");
        } catch (IOException e) {
            // 抛出异常
            throw new FileUploadIOException("上传文件时读写错误，请稍后重尝试");
        }

        // 头像路径
        String avatar = "/upload/" + filename;
        // 从Session中获取uid和username
        Integer uid = getUidFromSession(session);
        String username = getUserNameFromSession(session);
        // 将头像写入到数据库中
        iUserService.changeAvatar(uid, username, avatar);

        // 返回成功头像路径
        return new JsonResult<>(OK, avatar);
    }
    /** 头像文件大小的上限值(10MB) */
    public static final int AVATAR_MAX_SIZE = 10 * 1024 * 1024;
    /** 允许上传的头像的文件类型 */
    public static final List<String> AVATAR_TYPES = new ArrayList<>();

    /* 初始化允许上传的头像的文件类型 */
    static {
        AVATAR_TYPES.add("image/jpeg");
        AVATAR_TYPES.add("image/png");
        AVATAR_TYPES.add("image/bmp");
        AVATAR_TYPES.add("image/gif");
    }

}
