package com.praveen.mailservice.exception;

import com.praveen.mailservice.model.ErrorModel;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
class GlobalDefaultExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView
    defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = FileDownloadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ErrorModel showCustomMessage(FileDownloadException e){
        return new ErrorModel("Error during file download", e.getMessage());
    }
}