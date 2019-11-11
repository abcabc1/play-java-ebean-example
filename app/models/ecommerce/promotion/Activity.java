package models.ecommerce.promotion;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ebean.Finder;
import models.base.BasicModel;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany
    public List<RangeMerchandise> rangeMerchandiseList;
    @OneToMany
    public List<RangeMerchandiseTag> rangeMerchandiseTagList;
    @OneToMany
    public List<RangeUser> rangeUserList;
    @OneToMany
    public List<RangeUserTag> rangeUserTagList;

    public Set<String> build() {
        Set<String> value = new HashSet<>();
        for (RangeMerchandise rangeMerchandise : this.rangeMerchandiseList) {
            value.add(buildActivityMerchandise(rangeMerchandise));
        }
        for (RangeMerchandiseTag rangeMerchandiseTag : this.rangeMerchandiseTagList) {
            value.add(buildActivityMerchandiseTag(rangeMerchandiseTag));
        }
        for (RangeUser rangeUser : this.rangeUserList) {
            value.add(buildActivityUser(rangeUser));
        }
        for (RangeUserTag rangeUserTag : this.rangeUserTagList) {
            value.add(buildActivityUserTag(rangeUserTag));
        }
        return value;
    }

    public static String buildActivityUserTag(RangeUserTag rangeUserTag) {
        return rangeUserTag.blackWhite + "UT" + rangeUserTag.id;
    }

    public static String buildActivityUser(RangeUser rangeUser) {
        return rangeUser.blackWhite + "U" + rangeUser.id;
    }

    public static String buildActivityMerchandiseTag(RangeMerchandiseTag rangeMerchandiseTag) {
        return rangeMerchandiseTag.blackWhite + "MT" + rangeMerchandiseTag.id;
    }

    public static String buildActivityMerchandise(RangeMerchandise rangeMerchandise) {
        return rangeMerchandise.blackWhite + "M" + rangeMerchandise.id;
    }


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

    private enum LabelBrief {

        Merchandise("M"), MerchandiseTag("MT"), User("U"), UserTag("UT");

        private String value;

        LabelBrief(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum BlackWhiteList {

        White("+", "白名单"), Black("-", "黑名单");

        private String key;
        private String value;

        BlackWhiteList(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }
    }
}