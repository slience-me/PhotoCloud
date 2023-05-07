package xyz.slienceme.tuyun.pojos;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *      文件表
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 源文件名称
     */
    private String fileName;

    /**
     * MD5文件名称
     */
    private String fileMdName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 文件描述
     */
    private String fileDescribe;

    /**
     * 状态
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 创建人
     */
    private Long createdBy;

    public File(String fileName, String fileMdName, String fileType, String filePath, String fileUrl, String fileSize, String fileDescribe, Long createdBy) {
        this.fileName = fileName;
        this.fileMdName = fileMdName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.fileDescribe = fileDescribe;
        this.createdBy = createdBy;
    }
}
