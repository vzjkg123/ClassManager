package top.ltcnb.classmanager.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.ltcnb.classmanager.Entity.HomeworkAttachments;
import top.ltcnb.classmanager.Response.R;
import top.ltcnb.classmanager.Service.IHomeworkAttachmentsService;
import top.ltcnb.classmanager.Util.MinioUtil;

import javax.servlet.http.HttpServletRequest;


@RequestMapping("/data")
@RestController
public class FileController {
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.imageBucket}")
    private String imageBucketName;
    @Value("${minio.endpoint}/${minio.imageBucket}/")
    private String imageBashPath;
    MinioUtil minioUtil;
    IHomeworkAttachmentsService homeworkAttachmentsService;

    @Autowired
    public FileController(MinioUtil minioUtil, IHomeworkAttachmentsService homeworkAttachmentsService) {
        this.minioUtil = minioUtil;
        this.homeworkAttachmentsService = homeworkAttachmentsService;
    }

    @PostMapping("uploadImage")
    public R uploadImage(@RequestParam MultipartFile image) {
        try {
            String name = minioUtil.upload(image, imageBucketName);
            return R.success(imageBashPath + name);
        } catch (Exception e) {
            return R.failed(e.toString());
        }
    }


    @PostMapping("/uploadAttachments")
    public R<String> uploadFile(@RequestParam MultipartFile file, @RequestParam Long homeworkId, HttpServletRequest request) {
        String originName = file.getOriginalFilename();
        if (originName == null) return R.failed("文件名未知");
        String currentName;
        try {
            currentName = minioUtil.upload(file, "test");
        } catch (Exception e) {
            return R.failed(e.toString());
        }
        HomeworkAttachments attach = new HomeworkAttachments();
        attach.setPosterId(Long.valueOf(request.getHeader("id")));
        attach.setHomeworkId(homeworkId);
        attach.setOriginName(originName);
        attach.setCurrentName(currentName);
        homeworkAttachmentsService.save(attach);
        return R.success(currentName);
    }

}
