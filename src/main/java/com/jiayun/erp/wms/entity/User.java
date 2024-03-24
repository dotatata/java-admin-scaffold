package com.jiayun.erp.wms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

@ToString
@Getter
@Setter
@ApiModel("用户")
@TableName("users")
public class User {

    @ApiModelProperty(value = "用户ID", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @TableId(type = IdType.AUTO)    //如果使用BaseMapper.insert(T entity)则可以直接更新新插入的主键ID
    private Integer id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "岗位")
    private String title;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "密码", hidden = true)
    private String pwd;

    @ApiModelProperty(value = "创建日期", hidden = true, position = 8)
    private Timestamp createDate;

    @ApiModelProperty(value = "更新日期", hidden = true, position = 9)
    private Timestamp updateDate;

    @ApiModelProperty(value = "删除日期", hidden = true, position = 10)
    private Timestamp deleteDate;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private int[] roleIds;      //接收创建用户请求时的角色ID

    public User(){}

    public User(String name, String title, String phone, String pwd, int[] roleIds) {
        this.name = name;
        this.title = title;
        this.phone = phone;
        this.pwd = pwd;
        this.roleIds = roleIds;
    }

    public boolean checkSuperAdmin(){
        return roles.stream().anyMatch(role -> role.getRoleKey().equals("super-admin"));
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
