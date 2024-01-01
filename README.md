# aws-secret-manager-service
1.  Go to aws secret manager and create secret and store the secrets in key value format.
2.  Configure aws console with system or we can use accessKey and secretKey directly.
3.  To  fetch that secrets store the secret name and region in application.yml
4.  Implement EnvironmentPostProcessor  and configure that class in  Resources/META-INF/spring.factories 
5.  in that implemented class get the secrets using AWSSecretsManagerClientBuilder and store them into the ConfigurableEnvironment propertySources


