package top.ltcnb.classmanager.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ltcnb.classmanager.Entity.UserClass;
import top.ltcnb.classmanager.Mapper.UserClassMapper;
import top.ltcnb.classmanager.Service.IUserClassService;

@Service
public class UserClassServiceImpl extends ServiceImpl<UserClassMapper, UserClass> implements IUserClassService {
}
