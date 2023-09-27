package self.cases.teams.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 数据实体类
 * 缴费记录
 */


public class PayLogVos implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @ExcelProperty("id")
    @ColumnWidth(20)
    private String id;



    /**
     * 社团名称
     */
    @ExcelProperty("社团名称")
    @ColumnWidth(20)
    private String name;

    /**
     * 社团名称
     */
    @ExcelProperty("成员姓名")
    @ColumnWidth(20)
    private String username;

    /**
     * 成员性别
     */
    @ExcelProperty("成员性别")
    @ColumnWidth(20)
    private String userSex;

    /**
     * 成员性别
     */
    @ExcelProperty("成员电话")
    @ColumnWidth(20)
    private String userPhone;



    /**
     * 缴费时间
     */
    @ExcelProperty("缴费时间")
    @ColumnWidth(20)
    @TableField(value = "create_time")
    private String createTime;


    /**
     * 缴费时间
     */
    @ExcelProperty("缴纳费用")
    @ColumnWidth(20)
    @TableField(value = "payMoney")
    private String payMoney;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }
}