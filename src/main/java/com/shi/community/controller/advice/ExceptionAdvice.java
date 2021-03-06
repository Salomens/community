package com.shi.community.controller.advice;

import com.shi.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);
    @ExceptionHandler({Exception.class})
    public  void exception(Exception e, HttpServletRequest request, HttpServletResponse response){
        logger.error("服务器发生异常",e.getMessage());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            logger.error(stackTraceElement.toString());
        }
        String header = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(header)){
            response.setContentType("application/plain;charset = utf-8");
            try {
                PrintWriter writer = response.getWriter();
                writer.write(CommunityUtil.getJSONString(1,"服务器异常"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }else {
            try {
                response.sendRedirect(request.getContextPath()+"/error");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
