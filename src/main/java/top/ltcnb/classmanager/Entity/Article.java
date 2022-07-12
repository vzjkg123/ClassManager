package top.ltcnb.classmanager.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Article  {

    private Long posterId;
    private String title;
    private String description;
    @TableId
    private String fileName;
    private Timestamp modifyTime;
    private Long stars;
    private Long thumbUps;
}
