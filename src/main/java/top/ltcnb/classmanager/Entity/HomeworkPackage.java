package top.ltcnb.classmanager.Entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class HomeworkPackage {
    private Long packageUser;
    private Long classId;
    private Long homeworkId;
    private String filename;
    private Timestamp time;
}
