package top.ltcnb.classmanager.Util;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import top.ltcnb.classmanager.Entity.Submit;

import java.io.*;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @description： minio工具类
 * @version：3.0
 */
@Component
public class MinioUtil {
    private final MinioClient minioClient;

    @Autowired
    public MinioUtil(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Value("${minio.bucketName}")
    private String bucketName;

    public GetObjectResponse download(String bucketName, String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(name).build());
    }

    public File downloadZipPackage(List<Submit> fileList, String bucketName) throws Exception {
        File f = new File(UUID.randomUUID().toString());
        if (f.mkdir()) {
            String base = f.getAbsolutePath();
            for (Submit e : fileList) {
                minioClient.downloadObject(DownloadObjectArgs.builder().bucket(bucketName).filename(base + "/" + e.getFilename()).object(e.getFileLocation()).build());
            }
        }
        return f;

    }

    public String upload(InputStream in, String bucketName) throws Exception {
        String fileName = UUID.randomUUID() + ".md";
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(in, in.available(), -1)
                .build());

        return fileName;
    }

    /**
     * description: 上传文件
     *
     * @param multipartFile
     * @return: java.lang.String
     */
    public String upload(MultipartFile multipartFile, String bucketName) throws Exception {
        String originName = multipartFile.getOriginalFilename();
        assert originName != null;
        String fileName = UUID.randomUUID() + "." + originName.split("\\.")[1];
        InputStream in = multipartFile.getInputStream();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(in, in.available(), -1)
                .contentType(multipartFile.getContentType())
                .build());


        return fileName;
    }


}


