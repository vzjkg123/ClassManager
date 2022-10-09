package top.ltcnb.classmanager.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.ltcnb.classmanager.Response.R;
import top.ltcnb.classmanager.Util.MinioUtil;


@RequestMapping("/data")
@RestController
public class FileController {
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.imageBucket}")
    private String imageBucketName;
    @Value("${minio.imageBucket}/")
    private String imageBashPath;
    final MinioUtil minioUtil;

    @Autowired
    public FileController(MinioUtil minioUtil) {
        this.minioUtil = minioUtil;
    }

    @PostMapping("uploadImage")
    public R uploadImage(@RequestParam MultipartFile image) {
        try {
            String name = minioUtil.upload(image, imageBucketName);
            return R.success("https://ltcnb.top/data/"+imageBashPath + name);
        } catch (Exception e) {
            return R.failed(e.toString());
        }
    }




}
