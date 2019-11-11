package models.ecommerce.sale.promotion;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ebean.Finder;
import models.base.BasicModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table
public class Activity extends BasicModel {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String code;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '名称'")
    public String name;

    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT -1 COMMENT '类别:[1 秒杀Ms,2 满减Mj,3 满折Mz]'")
    public String type;

    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 1 COMMENT '级别:[1,2...]'")
    public String level;

    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT -1 COMMENT '是否启用:[启用 1,禁用 -1]'")
    public Boolean enabled;

    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT -1 COMMENT '运行状态:[停止 -1,运行 1]'")
    public Boolean running;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(nullable = false, columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '开始时间'")
    public Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(nullable = false, columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '结束时间'")
    public Date endTime;

    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date startFrom;
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date endTo;

    public static final Finder<Long, Activity> find = new Finder<>(Activity.class, "ecommerce");

/*
    @JsonBackReference(value = "operatorPass")
    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '111111' COMMENT '密码'")
    public String operatorPass;
*/
        
 /*
    @ManyToOne
    @JoinColumn(name = "operator_gender")
    public Config operatorGender;

    @ManyToMany(mappedBy = "operators")
    @JsonIgnoreProperties(value = {"operators", "childs"})
    public List<Org> orgs;
 */

/*
    @ManyToMany
    @JoinTable(name = "org_operator")
    @JsonIgnoreProperties(value = {"roles", "orgs"})
    public List<Operator> operators;
*/
}