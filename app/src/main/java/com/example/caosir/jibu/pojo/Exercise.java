package com.example.caosir.jibu.pojo;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Mapping;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;
import com.litesuits.orm.db.enums.Relation;

import java.nio.file.attribute.UserDefinedFileAttributeView;

/**
 * 创建人: caosir
 * 创建时间：2018/3/9
 * 修改人：
 * 修改时间：
 * 类说明：
 */

@Table("exercise")
public class Exercise {
    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    @Column("date")
    private String date;
    @Column("step")
    private int step;
    @Column("pash_up")
    private int pushUp;
    @Column("set_up")
    private int setUp;
    @Column("user_name")
    private String userNamer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getPushUp() {
        return pushUp;
    }

    public void setPushUp(int pushUp) {
        this.pushUp = pushUp;
    }

    public int getSetUp() {
        return setUp;
    }

    public void setSetUp(int setUp) {
        this.setUp = setUp;
    }

    public String getUserNamer() {
        return userNamer;
    }

    public void setUserNamer(String userNamer) {
        this.userNamer = userNamer;
    }
}
