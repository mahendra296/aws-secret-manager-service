package com.aws.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestService {
    @Value("${rest.api-key}")
    String apiKey;
    @Value("${api-gw-key}")
    String getwayKey;
    @Value("${rest.api-version}")
    String apiVersion;

    public Map<String,String > getSecret() {
      Map<String,String > secretMap = new HashMap<>();
      secretMap.put("api-key",apiKey);
      secretMap.put("getway-key",getwayKey);
      secretMap.put("api-version",apiVersion);
      return secretMap;
    }
}
