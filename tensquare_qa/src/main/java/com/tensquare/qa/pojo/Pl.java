package com.tensquare.qa.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "tb_pl")
@IdClass(value = Pl.class)// @IdClass ~ 该类中间表。
public class Pl implements Serializable {

    // 联合主键。
    @Id
    private String problemid;// 问答 id。

    @Id
    private String labelid;// 标签 id。

    public String getProblemid() {
        return problemid;
    }

    public void setProblemid(String problemid) {
        this.problemid = problemid;
    }

    public String getLabelid() {
        return labelid;
    }

    public void setLabelid(String labelid) {
        this.labelid = labelid;
    }

}
