package top.ltcnb.classmanager.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ltcnb.classmanager.Mapper.UserMapper;
import top.ltcnb.classmanager.Entity.User;
import top.ltcnb.classmanager.Service.IUserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
}
