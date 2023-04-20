package com.ayderbek.springbootexample.bucket;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName{

    PROFILE_IMAGE("ayderbek-image-upload");
    private final String bucketName;



}
