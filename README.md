[Flowable_Notes.docx](https://github.com/user-attachments/files/19480491/Flowable_Notes.docx)
# Flowable Process Setup

This project integrates the Flowable BPMN engine with Spring Boot and Liquibase. Below are the setup instructions and configurations required.

---

## Dependencies

Ensure the following dependencies are added to your `pom.xml` file:

### Spring Boot Parent:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.18</version>
    <relativePath/>
</parent>
```

### Flowable:

```xml
<dependency>
    <groupId>org.flowable</groupId>
    <artifactId>flowable-spring-boot-starter</artifactId>
    <version>6.7.0</version>
</dependency>
```

### Liquibase:

```xml
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <version>4.19.0</version>
</dependency>
```

### Important Notes:

- The above versions are compatible with Flowable.
- Change `jakarta` to `javax`, ensuring no dependencies on `jakarta` are present.

---

## Configuration

### 1. **DataSource Configuration** (`DataSourceConfig.java`)

This configuration class defines and initializes multiple data sources for the application. The primary data source is used for general database operations, while the Flowable-specific data source is separately configured for handling BPMN workflows.

```java
package com.task1.Task.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.sql.DataSource;

@Configuration
@Log4j2
public class DataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        log.debug("Creating primary data source");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "transactionManager")
    @Primary
    public JpaTransactionManager dbTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(primaryDataSource());
        return transactionManager;
    }

    @Bean("flowableDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.flowable")
    public DataSource flowableDataSource() {
        log.debug("Creating flowable data source");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "flowableTransactionManager")
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(flowableDataSource());
        return transactionManager;
    }
}
```

---

### 2. **Security Configuration** (`SecurityConfig.java`)

This class configures security settings for the application. It disables CSRF for API interactions and defines which endpoints require authentication and which are publicly accessible.

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable() // Disable CSRF for APIs
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS).permitAll()
        .antMatchers("/api/user/create", "/api/user/login").permitAll()
        .antMatchers("/api/**").authenticated()
        .anyRequest().permitAll();

    return http.build();
}
```

---

### 3. **Flowable Engine Configuration** (`WorkflowAppEngineConfig.java`)

This configuration class integrates the Flowable BPMN engine with Spring Boot. It ensures that Flowable uses the correct data source and transaction manager.

```java
package com.task1.Task.config;

import lombok.extern.log4j.Log4j2;
import org.flowable.app.spring.SpringAppEngineConfiguration;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@Log4j2
public class WorkflowAppEngineConfig implements EngineConfigurationConfigurer<SpringAppEngineConfiguration> {
    @Autowired
    @Qualifier("flowableDataSource")
    private DataSource dataSource;

    @Autowired
    @Qualifier("flowableTransactionManager")
    private PlatformTransactionManager transactionManager;

    @Override
    public void configure(SpringAppEngineConfiguration springAppEngineConfiguration) {
        log.debug("Configuring datasource for flowable");
        springAppEngineConfiguration.setDataSource(dataSource);
        springAppEngineConfiguration.setTransactionManager(transactionManager);
        springAppEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    }
}
```

---

### 4. **Workflow Deployment Configuration** (`WorkflowDeploymentConfig.java`)

This configuration class is responsible for deploying BPMN process definitions from a specified resource path. It loads workflow processes from ZIP files and registers them in the Flowable engine.

```java
package com.task1.Task.config;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

@Configuration
public class WorkflowDeploymentConfig {

    @Autowired
    private RepositoryService repositoryService;

    @Value("${velocious.workflow.process-resource-path}")
    private String workflowProcessResourcePath;

    @Value("#{${velocious.workflow.processResources}}")
    private List<String> workflowProcessResources;

    @Bean
    public void deploy() throws IOException {
        for (String workflowProcessResource : workflowProcessResources) {
            String barFileName = workflowProcessResourcePath + "/" + workflowProcessResource;
            ZipInputStream inputStream = new ZipInputStream(new FileInputStream(barFileName));
            repositoryService.createDeployment()
                    .name(workflowProcessResource)
                    .addZipInputStream(inputStream)
                    .enableDuplicateFiltering()
                    .deploy();
            inputStream.close();
        }
    }
}
```

---

## How to Use

### 1. **Run the Application**

Ensure that you have all required configurations set up in `application.properties`. Then, build and run the application using:

```sh
mvn spring-boot:run
```

### 2. **Access APIs**

Use the following endpoints:

- `POST /api/user/create` - Create a new user
- `POST /api/user/login` - User authentication
- `GET /api/**` - Secure APIs requiring authentication

---

## Notes

- Ensure that the database is set up correctly before running the application.
- Liquibase will handle database migrations.
- The Flowable process definitions should be placed in the configured resource path.

---

