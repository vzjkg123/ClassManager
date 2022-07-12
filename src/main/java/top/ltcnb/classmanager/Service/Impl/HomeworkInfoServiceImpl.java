package top.ltcnb.classmanager.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ltcnb.classmanager.Dao.HomeworkInfoMapper;
import top.ltcnb.classmanager.Entity.HomeworkInfo;
import top.ltcnb.classmanager.Service.IHomeworkInfoService;

@Service
public class HomeworkInfoServiceImpl extends ServiceImpl<HomeworkInfoMapper, HomeworkInfo> implements IHomeworkInfoService {
}
