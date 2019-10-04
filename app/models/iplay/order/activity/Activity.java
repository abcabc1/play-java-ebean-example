package models.iplay.order.activity;

import io.ebean.Finder;
import models.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Activity extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String activityCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '名称'")
    public String activityName;

    @Column(nullable = false, columnDefinition = "tinyint DEFAULT 1 COMMENT '级别'")
    public Byte activityLevel;

    @Column(nullable = false, columnDefinition = "tinyint DEFAULT 1 COMMENT '类别 1 平台/2 站点/3 店铺'")
    public Byte activityType;

    @Column(nullable = false, columnDefinition = "tinyint DEFAULT 1 COMMENT '生效状态 0 失效/1 生效'")
    public Byte activityStatus;

    @Column(nullable = false, columnDefinition = "tinyint DEFAULT 1 COMMENT '运行状态 0 未运行/1 已启动/2 暂停中/ 3 已结束'")
    public Byte activityRunStatus;



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

    public static final Finder<Long, Activity> find = new Finder<>(Activity.class);
}