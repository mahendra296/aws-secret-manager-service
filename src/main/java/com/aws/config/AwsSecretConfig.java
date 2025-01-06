package com.aws.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import java.util.Map;


@Configuration
public class AwsSecretConfig implements EnvironmentPostProcessor {
    
    
    /*
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
    } */

     @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            if (!IS_INITIALIZED) {
                ObjectMapper objectMapper = new ObjectMapper();
                String secretsEnv = "proof-backend/dev"; // env.getProperty("aws.secret");
                if (secretsEnv == null) return;

                Region region = Region.of("eu-north-1"); // env.getProperty("aws.region");
                if (region == null) {
                    throw new IllegalAccessException("Missing AwsSecretsManager Region");
                }

                String[] secrets = toSecretList(secretsEnv);
                SecretsManagerClient client = SecretsManagerClient.builder().region(region).build();

                if (secrets != null) {
                    for (String secret : secrets) {
                        log.debug("processing secret: " + secret);
                        GetSecretValueRequest req = GetSecretValueRequest.builder().secretId(secret).build();
                        String secretString = client.getSecretValue(req).secretString();
                        try {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> map = objectMapper.readValue(secretString, Map.class);
                            environment.getPropertySources().addFirst(new MapPropertySource("secrets-" + secret, map));
                        } catch (Exception e) {
                            log.error("Error processing secret: " + secret, e);
                        }
                    }
                }

                client.close();
                IS_INITIALIZED = false;
            }
        } catch (Exception ex) {
            log.error("Error while configure aws secret manager");
        }
    }

    private String[] toSecretList(String secrets) {
        if (secrets == null) {
            return null;
        }
        return secrets.replaceAll("\\s", "").split(",");
    }
}

