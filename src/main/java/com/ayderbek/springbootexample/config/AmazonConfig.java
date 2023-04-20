package com.ayderbek.springbootexample.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.groundstation.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {


    @Bean
    public AmazonS3 s3(){
        AWSCredentials awsCredentials = new BasicAWSCredentials(
                "AKIA43UPJH7HRDGZ4PRQ",
                "MY2V4T1MOOGMTfIc/x6xS6h2p56aCUfK2quPs3bw"
        );
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }


}
