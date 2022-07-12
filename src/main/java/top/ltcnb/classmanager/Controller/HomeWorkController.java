package top.ltcnb.classmanager.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.ltcnb.classmanager.Entity.HomeworkInfo;
import top.ltcnb.classmanager.Response.R;
import top.ltcnb.classmanager.Service.IHomeworkInfoService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/HomeWork")
@RestController
public class HomeWorkController {
    private IHomeworkInfoService homeworkInfoService;

    @Autowired
    public HomeWorkController(IHomeworkInfoService homeworkInfoService) {
        this.homeworkInfoService = homeworkInfoService;
    }

    /**
     * @return 是否成功，新文件的文件名
     * @description 保存文件，并记录数据库
     */
    public R submitHomework() {

        return R.failed("");
    }




    @PostMapping("postHomework")
    public R postHomework(HttpServletRequest request, @RequestParam String title, @RequestParam String desc, @RequestParam List<String> attachments) {

        String id = request.getHeader("id");
        HomeworkInfo homeworkInfo = new HomeworkInfo();
        homeworkInfo.setPosterId(Long.valueOf(id));
        homeworkInfo.setTitle(title);
        homeworkInfo.setDesc(desc);
        homeworkInfo.setAttachments(attachments);
        if (homeworkInfoService.save(homeworkInfo)) {
            return R.success("保存成功");
        }
        return R.failed("保存失败");
    }




}
