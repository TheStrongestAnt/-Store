package com.dong.store.controller;

import com.dong.store.controller.ex.*;
import com.dong.store.service.ex.*;
import com.dong.store.service.ex.*;
import com.dong.store.util.JsonResult;
import com.dong.store.controller.ex.*;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

/** 控制器类的基类 */
public class BaseController {
    /** 操作成功的状态码 */
    public static final int OK=200;

    /**
     *  @ExceptionHandler 注解用于统一处理方法抛出的异常。当我们使用这个注解时，需要定义一个
     *  异常的处理方法，再给这个方法加上@ExceptionHandler注解，这个方法就会处理类中其他方法
     * （被@RequestMapping注解）抛出的异常。@ExceptionHandler注解中可以添加参数，参数是某
     * 个异常类的class，代表这个方法专门处理该类异常。
     *
     * 用于统一处理方法抛出的异常
     * @param e 程序抛出的异常
     * @return 相应给前端浏览器异常类型结果
     */
    @ExceptionHandler({ServiceException.class, FileUploadException.class})
    public JsonResult<Void> handlerException(Throwable e){
        JsonResult<Void> result=new JsonResult<>(e);
        if (e instanceof UsernameDuplicateException){
            result.setState(4000);
//            result.setMessage("用户名已被使用");
        }else if (e instanceof UserNotFoundException){
            result.setState(4001);
//            result.setMessage("用户不存在");
        }else if (e instanceof PasswordNotMatchException){
            result.setState(4002);
//            result.setMessage("密码不正确");
        }else if (e instanceof InsertException){
            result.setState(5000);
//            result.setMessage("注册失败，请联系系统管理员");
        }else if (e instanceof UpdateException){
            result.setState(5001);
//            result.setMessage("修改密码时发生异常，请联系系统管理员");
        }else if (e instanceof FileEmptyException){
            result.setState(6000);
        }else if (e instanceof FileSizeException){
            result.setState(6001);
        }else if (e instanceof FileTypeException){
            result.setState(6002);
        }else if (e instanceof FileStateException){
            result.setState(6003);
        }else if (e instanceof FileUploadIOException){
            result.setState(6004);
        }else if (e instanceof AddressCountLimitException){
            result.setState(4003);
        }else if (e instanceof AddressNotFoundException){
            result.setState(4004);
        }else if (e instanceof AccessDeniedException){
            result.setState(4005);
        }else if (e instanceof DeleteException){
            result.setState(5002);
        }else if (e instanceof ProductNotFoundException){
            result.setState(4006);
        }
        return result;
    }
    protected final Integer getUidFromSession(HttpSession session){
        String uid = session.getAttribute("uid").toString();
        return Integer.valueOf(uid);
    }
    protected final String getUserNameFromSession(HttpSession session){
        return session.getAttribute("username").toString();
    }
}
