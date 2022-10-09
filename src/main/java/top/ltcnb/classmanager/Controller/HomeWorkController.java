package top.ltcnb.classmanager.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ltcnb.classmanager.Entity.*;
import top.ltcnb.classmanager.Mapper.HomeworkPackageMapper;
import top.ltcnb.classmanager.Mapper.SubmitMapper;
import top.ltcnb.classmanager.Response.R;
import top.ltcnb.classmanager.Service.*;
import top.ltcnb.classmanager.Util.MinioUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequestMapping("/homework")
@RestController
public class HomeWorkController {
    @Value("${minio.homeworkBucket}")
    private String homeworkBucket;
    private final IHomeworkInfoService homeworkInfoService;
    private final IUserClassService userClassService;
    private final ISubmitService submitService;
    private final IUserService userService;
    private final IHomeworkPackageService homeworkPackageService;
    MinioUtil minioUtil;
    SubmitMapper submitMapper;
    HomeworkPackageMapper homeworkPackageMapper;
    ExecutorService pool;

    @Autowired
    public HomeWorkController(HomeworkPackageMapper homeworkPackageMapper, IHomeworkPackageService homeworkPackageService, SubmitMapper submitMapper, IUserService userService, ISubmitService submitService, IHomeworkInfoService homeworkInfoService, MinioUtil minioUtil, IUserClassService userClassService) {
        this.homeworkInfoService = homeworkInfoService;
        this.minioUtil = minioUtil;
        this.userClassService = userClassService;
        this.submitService = submitService;
        this.userService = userService;
        this.submitMapper = submitMapper;
        this.homeworkPackageService = homeworkPackageService;
        this.homeworkPackageMapper = homeworkPackageMapper;
        pool = Executors.newFixedThreadPool(5);
        pool.execute(new Thread(() -> {
            while (true) {
                List<HomeworkPackage> expireList = homeworkPackageMapper.getExpirePackage();
                for (HomeworkPackage e : expireList) {
                    File f = new File(e.getFilename());
                    f.delete();
                    homeworkPackageService.remove(new LambdaQueryWrapper<HomeworkPackage>().eq(HomeworkPackage::getFilename, e.getFilename()));
                }
                try {
                    Thread.sleep(60000*60*24);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
    }

    @PostMapping("getClassHomework")
    public R<List<HomeworkInfo>> getClassHomework(@RequestParam Long classId) {
        return R.success(homeworkInfoService.list(new LambdaQueryWrapper<HomeworkInfo>().eq(HomeworkInfo::getClassId, classId)));
    }

    @PostMapping("getSubmitHistory")
    public R<List<Submit>> getSubmitHistory(HttpServletRequest request, @RequestParam String homeworkId) {
        Long id = Long.valueOf(request.getHeader("id"));
        return R.success(submitService.list(new LambdaQueryWrapper<Submit>().eq(Submit::getUserId, id).eq(Submit::getHomeworkId, homeworkId)));
    }

    @PostMapping("postHomework")
    public R<Object> postHomeWork(HttpServletRequest request, @RequestParam String classId, @RequestParam String title, @RequestParam String content) {
        try {
            UserClass user;
            Long id = Long.valueOf(request.getHeader("id"));
            if ((user = userClassService.getOne(new LambdaQueryWrapper<UserClass>().eq(UserClass::getUserId, id).eq(UserClass::getClassId, classId))) == null || user.getIsAdmin() == 0) {
                return R.failed("权限不足");
            }
            if (title.length() < 4) return R.failed("字数不足");
            HomeworkInfo homeworkInfo = new HomeworkInfo();
            homeworkInfo.setPosterId(id);
            homeworkInfo.setTitle(title);
            homeworkInfo.setClassId(Long.valueOf(classId));
            homeworkInfo.setDescription(content.length() > 128 ? content.substring(0, 128) : content);
            return R.success(homeworkInfoService.save(homeworkInfo));
        } catch (Exception e) {
            System.out.println(e);
            return R.failed(e.toString());
        }
    }


    @PostMapping("submitHomework")
    public R<String> submit(HttpServletRequest request, @RequestParam MultipartFile file) {
        String appendInfo = request.getHeader("appendInfo");
        String homeworkId = request.getHeader("homeworkId");

        try {
            Long id = Long.valueOf(request.getHeader("id"));
            String newName = minioUtil.upload(file, homeworkBucket);
            User user = userService.getById(id);
            Submit submit = new Submit();
            submit.setFileLocation(newName);
            submit.setHomeworkId(Long.valueOf(homeworkId));
            submit.setUserId(id);
            submit.setFilename(user.getAccount() + "-" + user.getName() + ((appendInfo.isEmpty()) ? "" : ("-" + appendInfo)) + newName.substring(newName.indexOf(".")));
            submitService.save(submit);
            return R.success(submit.getFilename());
        } catch (Exception e) {
            System.out.println(e);
            return R.failed(e.toString());
        }
    }

    @GetMapping("getMyHomework/{name}")
    public void getSubmittedHomework(HttpServletResponse response, @PathVariable String name) {
        try (InputStream in = minioUtil.download(homeworkBucket, name);
             OutputStream out = response.getOutputStream()) {
            ContentInfo info = ContentInfoUtil.findExtensionMatch(name);
            response.setContentType(info.getMimeType());
            byte[] data = new byte[1024];
            while (in.read(data) != -1) {
                out.write(data);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //获取班级所有人的作业
    @PostMapping("getClassPackage")
    public void getLastSubmit(HttpServletRequest request, HttpServletResponse response, @RequestParam Long homeworkId) {
        Long id = Long.valueOf(request.getHeader("id"));
        Submit submit = submitMapper.getLastHomeworkInfo(id, homeworkId);
        try (InputStream in = minioUtil.download(homeworkBucket, submit.getFileLocation());
             OutputStream out = response.getOutputStream()) {
            ContentInfo info = ContentInfoUtil.findExtensionMatch(submit.getFilename());
            response.setContentType(info.getMimeType());
            byte[] data = new byte[1024];
            while (in.read(data) != -1) {
                out.write(data);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @GetMapping("downloadPackage/{filename}")
    public void getPackage(@PathVariable String filename, HttpServletResponse response) {
        File f = new File(filename);
        if (!f.exists()) return;
        try (OutputStream out = response.getOutputStream(); InputStream in = Files.newInputStream(f.toPath())) {
            byte[] temp = new byte[2048];
            response.setContentType(ContentInfoUtil.findExtensionMatch(filename).getMimeType());
            while (in.read(temp) > -1) {
                out.write(temp);
                out.flush();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @PostMapping("getPackageList")
    public R<List<HomeworkPackage>> getPackageList(HttpServletRequest request, @RequestParam String classId, @RequestParam String homeworkId) {
        Long id = Long.valueOf(request.getHeader("id"));
        List<HomeworkPackage> hpl = homeworkPackageService.list(new LambdaQueryWrapper<HomeworkPackage>().eq(HomeworkPackage::getPackageUser, id).eq(HomeworkPackage::getHomeworkId, homeworkId).eq(HomeworkPackage::getClassId, classId).orderByDesc(HomeworkPackage::getTime));
        return R.success(hpl);
    }

    @PostMapping("packageHomework")
    public R<String> packageHomework(HttpServletRequest request, String homeworkId, String classId) {
        Long id = Long.valueOf(request.getHeader("id"));
        Long cid = Long.valueOf(classId);
        UserClass uc;
        if ((uc = userClassService.getOne(new LambdaQueryWrapper<UserClass>().eq(UserClass::getUserId, id).eq(UserClass::getClassId, cid))) == null || uc.getIsAdmin() == 0) {
            return R.failed("权限不足");
        }
        Long hid = Long.valueOf(homeworkId);
        Thread t1 = new Thread(() -> {
            try {
                List<Submit> list = submitMapper.getAllHomeworkName(hid, cid);
                File fIn = minioUtil.downloadZipPackage(list, homeworkBucket);
                File fOut = new File(UUID.randomUUID() + ".zip");
                HomeworkPackage hp = new HomeworkPackage();
                hp.setHomeworkId(hid);
                hp.setPackageUser(id);
                hp.setClassId(cid);
                homeworkPackageService.save(hp);

                byte[] temp = new byte[1024];
                try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(fOut.toPath()))) {
                    for (File f : Objects.requireNonNull(fIn.listFiles())) {
                        zos.putNextEntry(new ZipEntry(f.getName()));
                        try (InputStream in = Files.newInputStream(f.toPath())) {
                            while (in.read(temp) > -1) {
                                zos.write(temp);
                            }
                        }
                    }
                }
                hp.setFilename(fOut.getName());
                homeworkPackageService.update(hp, new LambdaQueryWrapper<HomeworkPackage>().eq(HomeworkPackage::getHomeworkId, hp.getHomeworkId()).eq(HomeworkPackage::getPackageUser, hp.getPackageUser()).eq(HomeworkPackage::getClassId, hp.getClassId()));
                FileUtils.deleteDirectory(fIn);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        pool.execute(t1);
        return R.success("服务器已收到打包请求,请稍后查看打包记录");
    }


}
