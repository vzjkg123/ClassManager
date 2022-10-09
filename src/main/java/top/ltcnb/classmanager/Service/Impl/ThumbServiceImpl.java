package top.ltcnb.classmanager.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ltcnb.classmanager.Mapper.ThumbsMapper;
import top.ltcnb.classmanager.Entity.Thumbs;
import top.ltcnb.classmanager.Service.IThumbsService;

@Service
public class ThumbServiceImpl extends ServiceImpl<ThumbsMapper, Thumbs> implements IThumbsService {
}
