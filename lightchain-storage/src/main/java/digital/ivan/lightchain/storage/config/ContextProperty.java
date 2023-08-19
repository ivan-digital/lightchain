package digital.ivan.lightchain.storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lightchain.storage")
public class ContextProperty {
    private S3StorageProperties s3;

    public S3StorageProperties getS3() {
        return s3;
    }

    public void setS3(S3StorageProperties s3) {
        this.s3 = s3;
    }

    private static class S3StorageProperties implements IS3StorageProperty {
        private String accessKey;
        private String secretKey;
        private String url;
        private String bucketName;

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }
    }
}
