package models.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ebean.Finder;
import io.ebean.Model;
//import models.auth.Operator;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class BaseModel extends Model {
   @Id
   @Column(columnDefinition = "bigint COMMENT 'ID'")
   public Long id;

   @Column(nullable = false, columnDefinition = "tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效'")
   public Boolean status;
//
//   @ManyToOne
//   @JoinColumn(name = "creator_id")
//   public Operator creator;
//   @ManyToOne
//   @JoinColumn(name = "updator_id")
//   public Operator updator;
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
   @Column(nullable = false, columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间'")
   public Date createTime;
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
   @Column(nullable = false, columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间'")
   public Date updateTime = new Date();

   @Transient
   @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
   public Date timeFrom;
   @Transient
   @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
   public Date timeTo;

   public static final Finder<Long, BaseModel> find = new Finder<>(BaseModel.class);
}
