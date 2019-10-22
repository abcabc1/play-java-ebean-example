CREATE or REPLACE view word_sentence_view as (
    SELECT t.word_letter, t.source, t.word_en, t.word_cn, t.word_type, t.sentence_en, t.status, t.create_time, t.update_time, t.sentence_cn, t.sentence_blank_cn, t.is_show
         , IF(ISNULL(key_word), '', concat(SUBSTRING(t.sentence_en,1,REGEXP_INSTR(t.sentence_en, t.key_word)-1), '(', t.key_word, ')', SUBSTRING(t.sentence_en,REGEXP_INSTR(t.sentence_en, key_word)+length(key_word)))) sentence_blank_en, t.wordEn FROM (
                                                                                                                                                                                                                                                            SELECT w.word_letter, w.source, wt.word_cn, ws.word_en, ws.word_type, ws.sentence_en, ws.status, ws.create_time, ws.update_time, ws.sentence_cn, ws.is_show
                                                                                                                                                                                                                                                                 , REGEXP_SUBSTR(ws.sentence_en,concat(SUBSTRING(ws.word_en,1,length(ws.word_en)-1),'[a-zA-Z]*')) AS key_word
                                                                                                                                                                                                                                                                 , REGEXP_REPLACE(ws.sentence_en,concat(ws.word_en,'[a-zA-Z]*'),concat('(',wt.word_cn,')')) AS sentence_blank_cn
                                                                                                                                                                                                                                                                 , concat(ws.word_en, ': (', ws.word_type, ')', wt.word_cn) wordEn
                                                                                                                                                                                                                                                            FROM word_sentence ws, wordEn w, word_trans wt WHERE ws.word_en = w.word_en and ws.word_en = wt.word_en and ws.word_type = wt.word_type) t
);

SELECT * FROM (
                  SELECT @num:=if(wsv.word_en=@wordEn,cast(@num+1 as UNSIGNED),1) num, wsv.sentence_blank_en, wsv.sentence_blank_cn, wsv.wordEn, wsv.sentence_cn, @wordEn:= wsv.word_en FROM words.word_sentence_view wsv, (SELECT @num:=0) v WHERE wsv.status = 1 and wsv.is_show = 1 and wsv.word_letter = 'A' order by wsv.word_letter, wsv.word_en, wsv.create_time) v where v.num < 2;


CREATE ALGORITHM=UNDEFINED DEFINER=`bob`@`%` SQL SECURITY DEFINER VIEW word_sentence_h_view (letter
    , code, type, sentence, status, create_time, update_time, translation, word_translation,
                                                                                             translation_word, sentence_blank_cn, sentence_blank_en, wordEn) AS
SELECT
    `t`.`letter`                              AS `letter`,
    `t`.`code`                                AS `code`,
    `t`.`type`                                AS `type`,
    `t`.`sentence`                            AS `sentence`,
    `t`.`status`                              AS `status`,
    `t`.`create_time`                         AS `create_time`,
    `t`.`update_time`                         AS `update_time`,
    `t`.`translation`                         AS `translation`,
    `t`.`word_translation`                    AS `word_translation`,
    concat(`t`.`type`,`t`.`word_translation`) AS `translation_word`,
    `t`.`sentence_blank_cn`                   AS `sentence_blank_cn`,
    IF((`t`.`key_word` IS NULL),'',concat(SUBSTR(`t`.`sentence`,1,(regexp_instr(`t`.`sentence`,
                                                                                `t`.`key_word`) - 1)),'(',`t`.`key_word`,')',SUBSTR(`t`.`sentence`,(regexp_instr(`t`.`sentence`
                                                                                                                                                        ,`t`.`key_word`) + LENGTH(`t`.`key_word`))))) AS `sentence_blank_en`,
    `t`.`wordEn`                                    AS `wordEn`
FROM
    (
        SELECT
            `w`.`letter`       AS `letter`,
            `wt`.`translation` AS `word_translation`,
            `ws`.`code`        AS `code`,
            `ws`.`type`        AS `type`,
            `ws`.`sentence`    AS `sentence`,
            `ws`.`status`      AS `status`,
            `ws`.`create_time` AS `create_time`,
            `ws`.`update_time` AS `update_time`,
            `ws`.`translation` AS `translation`,
            regexp_substr(`ws`.`sentence`,concat(SUBSTR(`ws`.`code`,1,(LENGTH(`ws`.`code`) - 1)),
                                                 '[a-zA-Z]*')) AS `key_word`,
            regexp_replace(`ws`.`sentence`,concat(`ws`.`code`,'[a-zA-Z]*'),concat('(',
                                                                                  `wt`.`translation`,')'))                                     AS `sentence_blank_cn`,
            concat(`ws`.`code`,': (',`ws`.`type`,')',`wt`.`translation`) AS `wordEn`
        FROM
            ((`word_sentence_h` `ws`
                JOIN
                `word_h` `w`)
                JOIN
            `word_trans_h` `wt`)
        WHERE
            ((
                     `ws`.`code` = `w`.`code`)
                AND (
                     `ws`.`code` = `wt`.`code`)
                AND (
                     `ws`.`type` = `wt`.`type`))) `t`;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"config_code", "config_category"}))