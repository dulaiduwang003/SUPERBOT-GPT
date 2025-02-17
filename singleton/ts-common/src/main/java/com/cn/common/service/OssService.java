package com.cn.common.service;


import com.cn.common.vo.FileVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
public interface OssService {





    FileVo uploadFile(MultipartFile file);

}
