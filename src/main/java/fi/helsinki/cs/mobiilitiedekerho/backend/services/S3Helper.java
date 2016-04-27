package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;


public class S3Helper {
    
    private String accessKey;
    private String secretAccessKey;
    private BasicAWSCredentials basicAWSCredentials;
    private AmazonS3Client s3Client;
    
    
    public S3Helper(String accessKey, String secretAccessKey) {
        System.setProperty("SDKGlobalConfiguration.ENFORCE_S3_SIGV4_SYSTEM_PROPERTY", "true");
        
        this.accessKey = accessKey;
        this.secretAccessKey = secretAccessKey;
        
        basicAWSCredentials = new BasicAWSCredentials(accessKey, secretAccessKey);
        
        s3Client = new AmazonS3Client(basicAWSCredentials);
        s3Client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.EU_CENTRAL_1));
    }
    
    public String generateSignedDownloadUrl(String bucketName, String objectKey) {
        return generateSignedUrl(bucketName, objectKey, HttpMethod.GET, null);
    }

    public String generateSignedUploadUrl(String bucketName, String objectKey, String MimeTypee) {
        return generateSignedUrl(bucketName, objectKey, HttpMethod.PUT,  MimeTypee);
    }
    
    private String generateSignedUrl(String bucketName, String objectKey, HttpMethod method, String mimeType) {
        java.util.Date expiration = new java.util.Date();
        long milliSeconds = expiration.getTime();
        milliSeconds += 1000 * 60 * 15; // Set timeout to 15 minutes.
        expiration.setTime(milliSeconds);

        // Generates signed url
        GeneratePresignedUrlRequest generatePresignedUrlRequest
                = new GeneratePresignedUrlRequest(bucketName, objectKey);
        generatePresignedUrlRequest.setMethod(method);
        generatePresignedUrlRequest.setExpiration(expiration);
        
        if (mimeType != null) {
            ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides();
            headerOverrides.setContentDisposition("inline");
            headerOverrides.setContentType(mimeType);
            generatePresignedUrlRequest.withResponseHeaders(headerOverrides);
        }
        
        String escapedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        
        return escapedUrl;
    }
}
