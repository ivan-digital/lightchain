package digital.ivan.lightchain.storage.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import digital.ivan.lightchain.storage.config.ContextProperty;
import digital.ivan.lightchain.storage.config.IS3StorageProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty("lightchain.storage.s3.url")
public class S3StorageAccessLayer {
    private final IS3StorageProperty storageProperties;
    private final AmazonS3 s3;

    public S3StorageAccessLayer(ContextProperty contextProperty) {
        this.storageProperties = contextProperty.getS3();
        var awsCreds = new BasicAWSCredentials(
                storageProperties.getAccessKey(),
                storageProperties.getSecretKey());

        s3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        storageProperties.getUrl(), null))
                .withPathStyleAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public void uploadFile(String keyName, String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_16);
        InputStream dataStream = new ByteArrayInputStream(bytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        metadata.setLastModified(Date.from(Instant.now()));
        s3.putObject(new PutObjectRequest(
                storageProperties.getBucketName(),
                keyName,
                dataStream,
                metadata));
    }

    public List<String> listFiles(String keyPrefix, boolean filterLastDay) {
        var request = new ListObjectsV2Request().withBucketName(
                        storageProperties.getBucketName())
                .withPrefix(keyPrefix);

        ListObjectsV2Result result;
        List<String> allKeys = new ArrayList<>();
        Date cutoffTime = Date.from(Instant.now().minus(24, ChronoUnit.HOURS));

        do {
            result = s3.listObjectsV2(request);
            List<String> keys = result.getObjectSummaries().stream()
                    .filter(object -> !filterLastDay || object.getLastModified().after(cutoffTime))
                    .map(S3ObjectSummary::getKey)
                    .collect(Collectors.toList());
            allKeys.addAll(keys);

            String token = result.getNextContinuationToken();
            request.setContinuationToken(token);
        } while (result.isTruncated());

        return allKeys;
    }

    public String getFile(String keyName) {
        S3Object object = s3.getObject(
                new GetObjectRequest(storageProperties.getBucketName(), keyName)
        );
        S3ObjectInputStream objectContent = object.getObjectContent();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(objectContent, StandardCharsets.UTF_16))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String keyName) {
        s3.deleteObject(storageProperties.getBucketName(), keyName);
    }
}
