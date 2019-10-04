package models.iplay.common.config;

import models.base.BaseCodeModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Config extends BaseCodeModel {
    @Column(nullable = false, columnDefinition = "VARCHAR(64) DEFAULT '' COMMENT '配置名称'")
    public String configName;
}
