package com.cn.common.service.impl;


import com.cn.common.enums.FilePathEnum;
import com.cn.common.enums.FileTypeEnum;
import com.cn.common.exceptions.OssException;
import com.cn.common.service.OssService;
import com.cn.common.utils.UploadUtil;
import com.cn.common.vo.FileVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Oss service.
 *
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OssServiceImpl implements OssService {

    private final UploadUtil uploadUtil;

    @SuppressWarnings("all")
    public FileVo uploadFile(final MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            switch (contentType) {
                case "application/pdf":
                    return uploadFileForContentType(file,
                            FileTypeEnum.PDF.getDec(),
                            FilePathEnum.TEMP.getDec());
                case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                    return uploadFileForContentType(file,
                            FileTypeEnum.XLSX.getDec(),
                            FilePathEnum.TEMP.getDec());
                case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                    return uploadFileForContentType(file,
                            FileTypeEnum.DOCX.getDec(),
                            FilePathEnum.TEMP.getDec());
                case "image/jpeg":
                case "image/webp":
                case "image/jpg":
                case "image/png":
                    return uploadFileForContentType(file,
                            FileTypeEnum.IMAGE.getDec(),
                            FilePathEnum.TEMP.getDec());
            }
        }

        throw new OssException("不支持上传该类型文件");
    }

    private FileVo uploadFileForContentType(MultipartFile file, String fileType, String filePath) {
        final String url = uploadUtil.uploadFile(file, filePath);
        return new FileVo()
                .setFileUrl(url)
                .setFileType(fileType);
    }

}
