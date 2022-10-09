package top.ltcnb.classmanager.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ltcnb.classmanager.Entity.HomeworkPackage;
import top.ltcnb.classmanager.Mapper.HomeworkPackageMapper;
import top.ltcnb.classmanager.Service.IHomeworkPackageService;

@Service
public class HomeworkPackageServiceImpl extends ServiceImpl<HomeworkPackageMapper, HomeworkPackage> implements IHomeworkPackageService {
}
