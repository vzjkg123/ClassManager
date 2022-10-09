package top.ltcnb.classmanager.Service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ltcnb.classmanager.Mapper.PermissionsMapper;
import top.ltcnb.classmanager.Entity.Permissions;
import top.ltcnb.classmanager.Service.IPermissionsService;

@Service
public class PermissionsServiceImpl extends ServiceImpl<PermissionsMapper, Permissions> implements IPermissionsService {
}
