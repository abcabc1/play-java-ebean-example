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
create table author (
  id                            integer,
  name                          varchar(255),
  age                           integer
);

create table book (
  id                            integer,
  name                          varchar(255),
  publisher                     varchar(255)
);

create table word (
  code                          varchar(32) COMMENT '编码' not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  constraint pk_word primary key (code)
);

create table word_en2 (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  letter                        char(1) DEFAULT '' COMMENT '首字母' not null,
  word                          varchar(20) DEFAULT '' COMMENT '单词' not null,
  word_trans                    varchar(256) DEFAULT '' COMMENT '单词解释' not null,
  sentence                      varchar(256) DEFAULT '' COMMENT '例句' not null,
  sentence_trans                varchar(256) DEFAULT '' COMMENT '例句翻译' not null,
  constraint uq_word_en2_word_sentence unique (word,sentence),
  constraint pk_word_en2 primary key (id)
);

create table word_translation (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(32) COMMENT '编码',
  translation                   varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_word_translation_translation unique (translation),
  constraint pk_word_translation primary key (id)
);

create index ix_word_translation_code on word_translation (code);
alter table word_translation add constraint fk_word_translation_code foreign key (code) references word (code) on delete restrict on update restrict;


# --- !Downs

alter table word_translation drop foreign key fk_word_translation_code;
drop index ix_word_translation_code on word_translation;

drop table if exists author;

drop table if exists book;

drop table if exists word;

drop table if exists word_en2;

drop table if exists word_translation;

