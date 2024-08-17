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

@ToString
@Getter
@Setter
@ApiModel("数据权限测试表")
@TableName("data_permission_test")
public class DataPermissionTest {

    @ApiModelProperty(value = "ID", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @TableId(type = IdType.AUTO)    //如果使用BaseMapper.insert(T entity)则可以直接更新新插入的主键ID
    private Integer id;

    @ApiModelProperty(value = "业务内容")
    private String context;

    @ApiModelProperty(value = "业务描述")
    private String desc;

    @ApiModelProperty(value = "部门ID")
    private String deptId;

    @ApiModelProperty(value = "用户ID/员工ID")
    private String userId;

    @ApiModelProperty(value = "创建日期", hidden = true, position = 8)
    private Timestamp createDate;
    @ApiModelProperty(value = "创建用户ID/创建员工ID")
    private String createId;

    @ApiModelProperty(value = "更新日期", hidden = true, position = 9)
    private Timestamp updateDate;
    @ApiModelProperty(value = "更新用户ID/更新员工ID")
    private String updateId;

    @ApiModelProperty(value = "删除日期", hidden = true, position = 10)
    private Timestamp deleteDate;
    @ApiModelProperty(value = "删除用户ID/删除员工ID")
    private String deleteId;


    public DataPermissionTest(){}

    public DataPermissionTest(String context, String deptId, String userId, Timestamp createDate, String createId, Timestamp updateDate, String updateId, Timestamp deleteDate, String deleteId) {
        this.context = context;
        this.deptId = deptId;
        this.userId = userId;
        this.createDate = createDate;
        this.createId = createId;
        this.updateDate = updateDate;
        this.updateId = updateId;
        this.deleteDate = deleteDate;
        this.deleteId = deleteId;
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
