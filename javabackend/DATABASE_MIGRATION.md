# 数据库技术栈迁移指南

## 概述

本文档描述了Coze Studio JavaBackend从JPA/Hibernate迁移到PostgreSQL + MyBatis的完整过程。

## 迁移内容

### 1. 技术栈变更

**之前：**
- Spring Data JPA + Hibernate
- MySQL/H2数据库
- @Entity注解的实体类
- Repository接口

**之后：**
- MyBatis + MyBatis Plus
- PostgreSQL/H2数据库
- @TableName注解的POJO类
- Mapper接口

### 2. 依赖变更

#### 移除的依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

#### 新增的依赖
```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>3.0.3</version>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.5</version>
</dependency>
```

### 3. 配置变更

#### 数据库配置
```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/coze_studio
    username: coze_user
    password: coze_password

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.coze.studio.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: ASSIGN_ID
      db-type: POSTGRESQL
      logic-delete-field: deleted
```

### 4. 实体类变更

#### JPA实体类（之前）
```java
@Entity
@Table(name = "workflow_variables")
public class WorkflowVariable extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "value", columnDefinition = "TEXT")
    private String value;
}
```

#### MyBatis POJO类（之后）
```java
@TableName("workflow_variables")
public class WorkflowVariable extends BaseEntity {
    @TableField("name")
    private String name;
    
    @TableField("value")
    private String value;
}
```

### 5. 数据访问层变更

#### Repository接口（之前）
```java
public interface WorkflowVariableRepository extends JpaRepository<WorkflowVariable, Long> {
    Optional<WorkflowVariable> findByNameAndUserId(String name, Long userId);
    List<WorkflowVariable> findByScope(String scope);
}
```

#### Mapper接口（之后）
```java
@Mapper
public interface WorkflowVariableMapper extends BaseMapper<WorkflowVariable> {
    @Select("SELECT * FROM workflow_variables WHERE name = #{name} AND user_id = #{userId} AND deleted = false")
    WorkflowVariable findByNameAndUserId(@Param("name") String name, @Param("userId") Long userId);
    
    @Select("SELECT * FROM workflow_variables WHERE scope = #{scope} AND deleted = false")
    List<WorkflowVariable> findByScope(@Param("scope") String scope);
}
```

### 6. 服务层变更

#### 依赖注入变更
```java
// 之前
private final WorkflowVariableRepository workflowVariableRepository;

// 之后
private final WorkflowVariableMapper workflowVariableMapper;
```

#### 方法调用变更
```java
// 之前
workflowVariableRepository.save(variable);
workflowVariableRepository.findById(id);

// 之后
workflowVariableMapper.insert(variable);
workflowVariableMapper.selectById(id);
```

## 数据库初始化

### PostgreSQL生产环境
```bash
# 1. 创建数据库和用户
createdb coze_studio
createuser coze_user

# 2. 执行初始化脚本
psql -d coze_studio -f src/main/resources/sql/schema.sql
psql -d coze_studio -f src/main/resources/sql/data.sql
```

### H2测试环境
测试环境会自动使用H2内存数据库，并执行schema-h2.sql初始化脚本。

## 迁移验证

### 1. 启动应用
```bash
mvn spring-boot:run
```

### 2. 检查数据库连接
访问 http://localhost:8888/actuator/health 检查应用健康状态。

### 3. 测试API接口
```bash
# 测试变量管理API
curl -X GET http://localhost:8888/api/variables/workflow

# 测试提示词管理API
curl -X GET http://localhost:8888/api/prompts/templates

# 测试模型管理API
curl -X GET http://localhost:8888/api/models/providers
```

### 4. 查看日志
检查应用启动日志，确保没有JPA相关的错误，MyBatis正常初始化。

## 性能优化

### 1. 连接池配置
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000
```

### 2. MyBatis配置优化
```yaml
mybatis-plus:
  configuration:
    cache-enabled: true
    lazy-loading-enabled: true
    multiple-result-sets-enabled: true
```

### 3. 索引优化
确保所有查询字段都有适当的索引，特别是：
- 外键字段
- 经常用于WHERE条件的字段
- 排序字段

## 注意事项

1. **逻辑删除**：使用MyBatis Plus的逻辑删除功能，deleted字段自动处理
2. **自动填充**：created_at和updated_at字段通过MetaObjectHandler自动填充
3. **分页查询**：使用MyBatis Plus的分页插件
4. **事务管理**：继续使用Spring的@Transactional注解
5. **SQL注入防护**：使用参数化查询，避免字符串拼接

## 回滚方案

如果需要回滚到JPA：
1. 恢复pom.xml中的JPA依赖
2. 恢复实体类的JPA注解
3. 恢复Repository接口
4. 恢复application.yml中的JPA配置
5. 恢复服务层的Repository调用

## 后续优化

1. **XML映射文件**：对于复杂查询，可以创建XML映射文件
2. **缓存集成**：集成Redis缓存提升性能
3. **读写分离**：配置主从数据库读写分离
4. **监控告警**：添加数据库性能监控和告警
