# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

-- init script create procs
-- Inital script to create stored procedures etc for mysql platform
DROP PROCEDURE IF EXISTS usp_ebean_drop_foreign_keys;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_foreign_keys TABLE, COLUMN
-- deletes all constraints and foreign keys referring to TABLE.COLUMN
--
CREATE PROCEDURE usp_ebean_drop_foreign_keys(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE c_fk_name CHAR(255);
  DECLARE curs CURSOR FOR SELECT CONSTRAINT_NAME from information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE() and TABLE_NAME = p_table_name and COLUMN_NAME = p_column_name
      AND REFERENCED_TABLE_NAME IS NOT NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN curs;

  read_loop: LOOP
    FETCH curs INTO c_fk_name;
    IF done THEN
      LEAVE read_loop;
    END IF;
    SET @sql = CONCAT('ALTER TABLE ', p_table_name, ' DROP FOREIGN KEY ', c_fk_name);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
  END LOOP;

  CLOSE curs;
END
$$

DROP PROCEDURE IF EXISTS usp_ebean_drop_column;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_column TABLE, COLUMN
-- deletes the column and ensures that all indices and constraints are dropped first
--
CREATE PROCEDURE usp_ebean_drop_column(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
  CALL usp_ebean_drop_foreign_keys(p_table_name, p_column_name);
  SET @sql = CONCAT('ALTER TABLE ', p_table_name, ' DROP COLUMN ', p_column_name);
  PREPARE stmt FROM @sql;
  EXECUTE stmt;
END
$$
create table config (
  config_code                   varchar(32) COMMENT '配置编码' not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  config_name                   varchar(32) DEFAULT '' COMMENT '配置名称' not null,
  config_category               varchar(32) DEFAULT '' COMMENT '配置类型' not null,
  config_category_name          varchar(32) DEFAULT '' COMMENT '配置类型名称' not null,
  config_order                  TINYINT UNSIGNED DEFAULT 0 COMMENT '配置次序' not null,
  parent                        varchar(32) COMMENT '配置编码',
  constraint uq_config_config_code_config_category unique (config_code,config_category),
  constraint pk_config primary key (config_code)
);

create table listen (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  listen_topic                  varchar(64) DEFAULT '' COMMENT '听力主题' not null,
  listen_level                  varchar(16) DEFAULT '' COMMENT '听力级别' not null,
  source                        varchar(32) COMMENT '配置编码',
  constraint uq_listen_listen_topic unique (listen_topic),
  constraint pk_listen primary key (id)
);

create table listen_dialog (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  listen_dialog_question_en     varchar(256) DEFAULT '' COMMENT '听力对话问题英文' not null,
  listen_dialog_answer_en       varchar(256) DEFAULT '' COMMENT '听力对话回答英文' not null,
  listen_dialog_question_cn     varchar(256) DEFAULT '' COMMENT '听力对话问题中文' not null,
  listen_dialog_answer_cn       varchar(256) DEFAULT '' COMMENT '听力对话回答中文' not null,
  listen                        bigint COMMENT 'ID',
  constraint pk_listen_dialog primary key (id)
);

create table listen_word (
  word_en                       varchar(32) DEFAULT '' COMMENT '单词英文' not null,
  listen_id                     BIGINT DEFAULT 0 COMMENT '听力ID' not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  constraint pk_listen_word primary key (word_en,listen_id)
);

create table word (
  word_en                       varchar(32) DEFAULT '' COMMENT '单词英文' not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  word_letter                   char(1) DEFAULT '' COMMENT '单词首字母' not null,
  source                        varchar(32) COMMENT '配置编码',
  constraint pk_word primary key (word_en)
);

create table word_py (
  word                          varchar(16) COMMENT '单词中文' not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  py                            varchar(16) DEFAULT '' COMMENT '单词拼音' not null,
  constraint pk_word_py primary key (word)
);

create table word_sentence (
  word_en                       varchar(32) DEFAULT '' COMMENT '单词英文' not null,
  word_type                     varchar(16) DEFAULT '' COMMENT '单词类别' not null,
  sentence_en                   varchar(256) DEFAULT '' COMMENT '单词例句英文' not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  sentence_cn                   varchar(256) DEFAULT '' COMMENT '单词例句中文' not null,
  constraint pk_word_sentence primary key (word_en,word_type,sentence_en)
);

create table word_trans (
  word_en                       varchar(32) DEFAULT '' COMMENT '单词英文' not null,
  word_type                     varchar(16) DEFAULT '' COMMENT '单词类别' not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  word_cn                       varchar(64) DEFAULT '' COMMENT '单词中文' not null,
  constraint pk_word_trans primary key (word_en,word_type)
);

create index ix_config_parent on config (parent);
alter table config add constraint fk_config_parent foreign key (parent) references config (config_code) on delete restrict on update restrict;

create index ix_listen_source on listen (source);
alter table listen add constraint fk_listen_source foreign key (source) references config (config_code) on delete restrict on update restrict;

create index ix_listen_dialog_listen on listen_dialog (listen);
alter table listen_dialog add constraint fk_listen_dialog_listen foreign key (listen) references listen (id) on delete restrict on update restrict;

create index ix_listen_word_word_en on listen_word (word_en);
alter table listen_word add constraint fk_listen_word_word_en foreign key (word_en) references word (word_en) on delete restrict on update restrict;

create index ix_listen_word_listen_id on listen_word (listen_id);
alter table listen_word add constraint fk_listen_word_listen_id foreign key (listen_id) references listen (id) on delete restrict on update restrict;

create index ix_word_source on word (source);
alter table word add constraint fk_word_source foreign key (source) references config (config_code) on delete restrict on update restrict;

create index ix_word_sentence_wordtrans on word_sentence (word_en,word_type);
alter table word_sentence add constraint fk_word_sentence_wordtrans foreign key (word_en,word_type) references word_trans (word_en,word_type) on delete restrict on update restrict;

create index ix_word_trans_word_en on word_trans (word_en);
alter table word_trans add constraint fk_word_trans_word_en foreign key (word_en) references word (word_en) on delete restrict on update restrict;


# --- !Downs

alter table config drop foreign key fk_config_parent;
drop index ix_config_parent on config;

alter table listen drop foreign key fk_listen_source;
drop index ix_listen_source on listen;

alter table listen_dialog drop foreign key fk_listen_dialog_listen;
drop index ix_listen_dialog_listen on listen_dialog;

alter table listen_word drop foreign key fk_listen_word_word_en;
drop index ix_listen_word_word_en on listen_word;

alter table listen_word drop foreign key fk_listen_word_listen_id;
drop index ix_listen_word_listen_id on listen_word;

alter table word drop foreign key fk_word_source;
drop index ix_word_source on word;

alter table word_sentence drop foreign key fk_word_sentence_wordtrans;
drop index ix_word_sentence_wordtrans on word_sentence;

alter table word_trans drop foreign key fk_word_trans_word_en;
drop index ix_word_trans_word_en on word_trans;

drop table if exists config;

drop table if exists listen;

drop table if exists listen_dialog;

drop table if exists listen_word;

drop table if exists word;

drop table if exists word_py;

drop table if exists word_sentence;

drop table if exists word_trans;

