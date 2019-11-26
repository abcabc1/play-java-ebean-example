package models.ecommerce.promotion;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ebean.Finder;
import models.base.BasicModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/*
一个活动(Activity)的规则集合(List<ActivityRange>)里有且只有1个名单（白名单或黑黑名单.
即一个活动的规则集合有两种情况（1白名单活动规则，1黑名单活动规则）其实串货的规则更合理，即可以有黑白同时存在，让集合按照顺序加减
同一个商品和用户只会在活动规则集合中被包含一次
 */
@Entity
@Table
public class Activity extends BasicModel {

    private static final long serialVersionUID = 1L;

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
    public List<ActivityRange> activityRangeList;

//    public Set<String> build() {
//        Set<String> rangeSet = new HashSet<>();
//        List<String> rangeMerchandiseValueList = rangeMerchandiseList.stream().map(v -> buildActivityMerchandise(v)).collect(Collectors.toList());
//        List<String> rangeUserValueList = rangeUserList.stream().map(v -> buildActivityUser(v)).collect(Collectors.toList());
//        for (String rangeMerchandiseValue : rangeMerchandiseValueList) {
//            for (String rangeUserValue : rangeUserValueList) {
//                rangeSet.add(rangeMerchandiseValue + "|" + rangeUserValue);
//            }
//        }
//        return rangeSet;
//    }

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

        Merchandise("MM"), MerchandiseTag("MG"), User("UU"), UserTag("UG"), UserArea("UA"), UserType("UT");

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