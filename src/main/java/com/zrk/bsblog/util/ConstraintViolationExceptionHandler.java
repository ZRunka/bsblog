package com.zrk.bsblog.util;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;

public class ConstraintViolationExceptionHandler {

    //获取批量异常信息
    public static String getMessage(ConstraintViolationException e){
        List<String> msgList=new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation:e.getConstraintViolations()){
            msgList.add(constraintViolation.getMessage());
        }
        String message=StringUtils.join(msgList.toArray(),";");
        return message;
    }
}
