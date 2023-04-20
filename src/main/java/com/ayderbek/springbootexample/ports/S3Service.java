package com.ayderbek.springbootexample.ports;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Autowired
    private AmazonS3 amazonS3;

    public byte[] downloadImage(String bucketName, String fileName) throws IOException {
        S3Object s3Object = amazonS3.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        return IOUtils.toByteArray(inputStream);
    }
}
