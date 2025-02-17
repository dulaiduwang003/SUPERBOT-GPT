package com.cn.common.utils;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.UUID;


/**
 * The type Upload util.
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Component
@SuppressWarnings("all")
@Slf4j
public class UploadUtil {

    @Value("${ali.oss.domain}")
    private String ossDomain;

    @Value("${ali.oss.endpoint}")
    private String endpoint;

    @Value("${ali.certified.access-key}")
    private String accessKey;

    @Value("${ali.certified.secret-key}")
    private String secretKey;

    @Value("${ali.oss.bucket-name}")
    private String bucketName;


    public String uploadFile(final MultipartFile file, final String path) {

        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKey, secretKey);
        try (InputStream inputStream = file.getInputStream()) {
            String originalFileName = file.getOriginalFilename();

            assert originalFileName != null;
            String fileName;
            fileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf('.'));

            String filePath = path + "/" + fileName;
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            ossClient.putObject(bucketName, filePath, inputStream, objectMetadata);
            return ossDomain + "/" + filePath;

        } catch (IOException e) {
            throw new OSSException();
        } finally {
            ossClient.shutdown();
        }
    }

    public String uploadExcelToOSS(byte[] excelBytes, String path) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, secretKey);
        try (InputStream inputStream = new ByteArrayInputStream(excelBytes)) {
            String originalFileName = "data.xlsx";
            String fileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf('.'));
            String filePath = path + "/" + fileName;
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            ossClient.putObject(bucketName, filePath, inputStream, objectMetadata);
            return ossDomain + "/" + filePath;
        } catch (IOException e) {
            throw new RuntimeException("上传至OSS失败", e);
        } finally {
            ossClient.shutdown();
        }
    }

    public String uploadDrawingUrl(String imageUrl, String path) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, secretKey);
        try {
            URL url = new URL(imageUrl);
            String pathWithoutQuery = url.getPath();
            String fileNameWithExt = null;

            // 尝试从查询参数中获取文件名
            String query = url.getQuery();
            if (query != null && query.contains("filename=")) {
                String[] params = query.split("&");
                for (String param : params) {
                    if (param.startsWith("filename=")) {
                        fileNameWithExt = URLDecoder.decode(param.substring("filename=".length()), "UTF-8");
                        break;
                    }
                }
            }

            // 如果没有从查询参数中获取到文件名，则使用 URL 路径中的文件名
            if (fileNameWithExt == null) {
                fileNameWithExt = pathWithoutQuery.substring(pathWithoutQuery.lastIndexOf('/') + 1);
            }

            String fileName = path + "/" + UUID.randomUUID().toString() + "." + getFileExtension(fileNameWithExt);

            InputStream inputStream = url.openStream();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(getMimeType(fileNameWithExt));

            ossClient.putObject(bucketName, fileName, inputStream, objectMetadata);

            return ossDomain + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload media from URL: " + imageUrl, e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    private String getMimeType(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".webp")) {
            return "image/webp";
        }  else if (fileName.endsWith(".mp4")) {
            return "video/mp4";
        } else if (fileName.endsWith(".avi")) {
            return "video/x-msvideo";
        } else if (fileName.endsWith(".mp3")) {
            return "audio/mpeg";
        } else if (fileName.endsWith(".wav")) {
            return "audio/x-wav";
        }
        // 默认返回二进制流类型
        return "application/octet-stream";
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return ""; // 没有扩展名
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    public String uploadBase64Image(final String base64Image, String path) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, secretKey);
        try {
            // use the uuid to generate a new file name
            String fileName = path + "/" + UUID.randomUUID().toString() + ".jpg";
            // 将Base64图片转换为字节数组
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpg");
            // 创建PutObjectRequest对象并上传图片
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, new ByteArrayInputStream(imageBytes), objectMetadata);
            ossClient.putObject(request);
            return ossDomain + "/" + fileName;
        } catch (Exception e) {
            throw new OSSException();
        } finally {
            //disable  oss client
            ossClient.shutdown();
        }

    }

    /**
     * Deleted file.
     *
     * @param path the path
     */
    public void deletedFile(final String path) {
        OSS ossClient = new OSSClientBuilder()
                .build(endpoint, accessKey, secretKey);
        try {

            if (path.startsWith("/")) {
                ossClient.deleteObject(bucketName, path.substring(1));
            } else {
                ossClient.deleteObject(bucketName, path);
            }
        } catch (OSSException | ClientException e) {
            throw new OSSException();
        } finally {
            ossClient.shutdown();
        }
    }

}
