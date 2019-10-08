package models.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;

//import models.auth.Operator;

@MappedSuperclass
public class BasicCodeModel extends Model {
   @Id
   @Column(columnDefinition = "varchar(32) COMMENT '编码'")
   public String code;

   @Column(nullable = false, columnDefinition = "tinyint DEFAULT 1 COMMENT '数据是否有效 0/1'")
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
}
