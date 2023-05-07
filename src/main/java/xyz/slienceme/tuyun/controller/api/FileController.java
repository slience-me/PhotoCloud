package xyz.slienceme.tuyun.controller.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.slienceme.tuyun.common.entity.Result;
import xyz.slienceme.tuyun.service.FileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
@Slf4j
//@Api(tags = "【上传静态文件】接口")
@RestController
@RequestMapping("")
public class FileController {

    @Autowired
    private FileService fileService;
//    @ApiOperation("删除文件接口")
    @DeleteMapping("/file")
    public Result<?> delFile(@RequestHeader("x-access-token") String accessToken, @RequestParam("id") Integer id) throws Exception {
        log.info("删除文件接口调用-----------------------delete----------------------</upload/delfile>:");
        return fileService.delFile(accessToken, id);
    }

//    @ApiOperation("下载文件接口")
    @GetMapping("/download")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") Integer id) throws Exception {
        log.info("下载文件接口调用-----------------------get----------------------</upload/download>:");
        fileService.downloadFile(request, response, id);
    }
}