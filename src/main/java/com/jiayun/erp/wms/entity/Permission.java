package com.jiayun.erp.wms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Getter
@Setter
@ToString
@ApiModel("权限")
@TableName("permissions")
public class Permission {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String permissionKey;
    private String description;

    private Timestamp createDate;
    private Timestamp updateDate;
    private Timestamp deleteDate;

    public Permission() {}

    public Permission(String name, String permissionKey, String description) {
        this.name = name;
        this.permissionKey = permissionKey;
        this.description = description;
    }

    public String getCreateDate() {
        if (createDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(createDate);
        }else {
            return null;
        }
    }

    public String getUpdateDate() {
        if (updateDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(updateDate);
        }else {
            return null;
        }
    }

    public String getDeleteDate() {
        if (deleteDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(deleteDate);
        }else {
            return null;
        }
    }
}
