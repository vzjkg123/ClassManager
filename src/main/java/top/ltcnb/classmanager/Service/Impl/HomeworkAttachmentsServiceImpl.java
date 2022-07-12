package top.ltcnb.classmanager.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ltcnb.classmanager.Dao.HomeworkAttachmentsMapper;
import top.ltcnb.classmanager.Entity.HomeworkAttachments;
import top.ltcnb.classmanager.Service.IHomeworkAttachmentsService;

@Service
public class HomeworkAttachmentsServiceImpl extends ServiceImpl<HomeworkAttachmentsMapper, HomeworkAttachments> implements IHomeworkAttachmentsService {
}
