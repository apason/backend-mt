package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;


public class S3Helper {
    
    private String accessKey;
    private String secretAccessKey;
    private BasicAWSCredentials basicAWSCredentials;
    private AmazonS3Client s3Client;
    
    
    public S3Helper(String accessKey, String secretAccessKey) {
        this.accessKey = accessKey;
        this.secretAccessKey = secretAccessKey;
        
        basicAWSCredentials = new BasicAWSCredentials(accessKey, secretAccessKey);
        
        s3Client = new AmazonS3Client(basicAWSCredentials);
    }
    
    public String generateSignedDownloadUrl(String bucketName, String objectKey) {
        return generateSignedUrl(bucketName, objectKey, HttpMethod.GET);
    }

    public String generateSignedUploadUrl(String bucketName, String objectKey) {
        return generateSignedUrl(bucketName, objectKey, HttpMethod.PUT);
    }
    
    private String generateSignedUrl(String bucketName, String objectKey, HttpMethod method) {
        java.util.Date expiration = new java.util.Date();
        long milliSeconds = expiration.getTime();
        milliSeconds += 1000 * 60 * 15; // Set timeout to 15 minutes.
        expiration.setTime(milliSeconds);

        GeneratePresignedUrlRequest generatePresignedUrlRequest
                = new GeneratePresignedUrlRequest(bucketName, objectKey);
        generatePresignedUrlRequest.setMethod(method);
        generatePresignedUrlRequest.setExpiration(expiration);
        return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }
}
