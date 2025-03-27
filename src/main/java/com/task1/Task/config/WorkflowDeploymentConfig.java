package com.task1.Task.config;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

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

