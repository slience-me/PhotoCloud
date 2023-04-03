package xyz.slienceme.tuyun.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface VerifyService {

    void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException;

//    String checkCode(String code, HttpServletRequest request);
}
