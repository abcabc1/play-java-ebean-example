package models.iplay.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ebean.Model;
import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Endpoint extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "varchar(256) COMMENT '编码'")
    public String pathPattern;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '请求方式'")
    public String controllerMethod;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT ''")
    public String httpMethod;

    @ManyToMany(mappedBy = "endpoints")
    @JsonIgnoreProperties(value = {"endpoints"})
    public List<Permission> permissions;

}
