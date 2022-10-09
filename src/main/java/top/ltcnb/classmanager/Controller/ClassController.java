package top.ltcnb.classmanager.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ltcnb.classmanager.Entity.Classes;
import top.ltcnb.classmanager.Mapper.ClassesMapper;
import top.ltcnb.classmanager.Response.R;
import top.ltcnb.classmanager.Service.IClassesService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("class")
public class ClassController {
    IClassesService classesService;
    ClassesMapper classesMapper;

    @Autowired
    public ClassController(IClassesService classesService, ClassesMapper classesMapper) {
        this.classesService = classesService;
        this.classesMapper = classesMapper;
    }

    @GetMapping("getMyClass")
    public R<List<Classes>> getMyClass(HttpServletRequest request) {
        try {
            return R.success(classesMapper.listMyClass(Long.valueOf(request.getHeader("id"))));
        } catch (Exception e) {
            System.out.println(e);
            return R.failed(e.toString());
        }
    }
}
