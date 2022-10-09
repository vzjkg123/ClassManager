package top.ltcnb.classmanager.Entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Submit {
    private Long userId;
    private Long homeworkId;
    private String fileLocation;
    private String filename;
    private Timestamp time;
}
