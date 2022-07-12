package top.ltcnb.classmanager.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ltcnb.classmanager.Dao.StarsMapper;
import top.ltcnb.classmanager.Entity.Stars;
import top.ltcnb.classmanager.Service.IStarsService;
@Service
public class StarsServiceImpl extends ServiceImpl<StarsMapper, Stars> implements IStarsService {
}
