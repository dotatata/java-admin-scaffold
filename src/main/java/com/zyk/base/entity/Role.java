package com.zyk.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("角色")
@TableName("roles")
public class Role {

    @ApiModelProperty(value = "用户ID", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @TableId(type = IdType.AUTO)    //如果使用BaseMapper.insert(T entity)则可以直接更新新插入的主键ID
    private Integer id;

    @ApiModelProperty(value = "角色键值")
    private String roleKey;
    @ApiModelProperty(value = "角色名称")
    private String name;
    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "创建日期", hidden = true, position = 8)
    private Timestamp createDate;
    @ApiModelProperty(value = "更新日期", hidden = true, position = 9)
    private Timestamp updateDate;
    @ApiModelProperty(value = "删除日期", hidden = true, position = 10)
    private Timestamp deleteDate;

    @TableField(exist = false)
    private List<Permission> permissions;

    @TableField(exist = false)
    private int[] permissionIds;

    public Role(){}

    public Role(String roleKey, String name, String description, int[] permissionIds){
        this.roleKey = roleKey;
        this.name = name;
        this.description = description;
        this.permissionIds = permissionIds;
    }

    public Role(Integer id, String roleKey, String name, String description, Timestamp createDate, Timestamp updateDate, Timestamp deleteDate) {
        this.id = id;
        this.roleKey = roleKey;
        this.name = name;
        this.description = description;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.deleteDate = deleteDate;
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
