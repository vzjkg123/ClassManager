package top.ltcnb.classmanager.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ltcnb.classmanager.Entity.Classes;
import top.ltcnb.classmanager.Mapper.ClassesMapper;
import top.ltcnb.classmanager.Service.IClassesService;

@Service
public class ClassesServiceImpl extends ServiceImpl<ClassesMapper, Classes> implements IClassesService {
}
