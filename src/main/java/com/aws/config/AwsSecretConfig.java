package com.aws.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;


@Configuration
public class AwsSecretConfig implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
        try{
           // String awsAcessKey =  "access key of aws";
           // String awsSecretKey = "secret key of aws ";
            String secretName = env.getProperty("aws.secret");
            String region = env.getProperty("aws.region");
            AWSSecretsManager client = AWSSecretsManagerClientBuilder
                                       .standard()
                                      // .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAcessKey,awsSecretKey)))  // if we want with use directly otherwise it will pick the system configured aws console.
                                       .withRegion(region)
                                       .build();
            GetSecretValueRequest req = new GetSecretValueRequest();
            req.setSecretId(secretName);

            String secretValue = client.getSecretValue(req).getSecretString();
            Map values = new ObjectMapper().readValue(secretValue, Map.class);
            env.getPropertySources().addFirst(new MapPropertySource("secrets-" + secretName, values));
        } catch ( Exception e){
            throw  new RuntimeException("Eror while getting the secrets from aws ."+e.getMessage());
        }
    }
}

