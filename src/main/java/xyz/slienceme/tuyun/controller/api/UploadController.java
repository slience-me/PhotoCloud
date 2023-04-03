package xyz.slienceme.tuyun.controller.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import xyz.slienceme.tuyun.common.entity.Result;
import xyz.slienceme.tuyun.service.FileService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
@Slf4j
@Api(tags = "【上传静态文件】接口")
@RestController
@RequestMapping("/upload")
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("上传图片接口")
    @PostMapping("/img/{type}")
    public Result<?> uploadImg(@RequestHeader("x-access-token") String accessToken,
                               @RequestBody MultipartFile[] multipartFiles,
                               @PathVariable Integer type) throws Exception {
        log.info("上传图片接口调用-----------------------post----------------------</upload/img>:");
        return fileService.uploadImage(accessToken, multipartFiles, type);
    }

    @ApiOperation("上传文件接口(私有)")
    @PostMapping("/file")
    public Result<?> uploadFile(@RequestHeader("x-access-token") String accessToken,
                                @RequestBody MultipartFile[] multipartFiles) throws Exception {
        log.info("上传文件接口调用-----------------------post----------------------</upload/file>:");
        return fileService.uploadFile(accessToken, multipartFiles);
    }

    @ApiOperation("删除文件接口")
    @GetMapping("/delfile")
    public Result<?> delFile(@RequestHeader("x-access-token") String accessToken, @RequestParam("id") Integer id) throws Exception {
        log.info("删除文件接口调用-----------------------delete----------------------</upload/delfile>:");
        return fileService.delFile(accessToken, id);
    }

    @ApiOperation("下载文件接口")
    @GetMapping("/download")
    public Result<?> downloadFile(@RequestParam("id") Integer id) throws Exception {
        log.info("下载文件接口调用-----------------------get----------------------</upload/download>:");
        return fileService.downloadFile(id);
    }
}