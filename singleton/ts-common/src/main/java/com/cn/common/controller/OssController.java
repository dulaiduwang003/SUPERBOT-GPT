package com.cn.common.controller;


import com.cn.common.exceptions.OssException;
import com.cn.common.msg.Result;
import com.cn.common.service.OssService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Oss controller.
 *
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/oss")
@RequiredArgsConstructor
public class OssController {

    private final OssService ossService;


    /**
     * Upload file result.
     *
     * @param file the file
     * @return the result
     */
    @PostMapping(value = "/upload/file", consumes = "multipart/form-data")
    public Result uploadFile(@Valid @NotNull(message = "上传文件不能为空") final MultipartFile file) {
        try {
            return Result.data(ossService.uploadFile(file));
        } catch (OssException ex) {
            return Result.error(ex.getMessage());
        }

    }
}
