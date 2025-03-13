package bank.donghang.donghang_api.s3.application;

import bank.donghang.donghang_api.common.exception.BadRequestException;
import bank.donghang.donghang_api.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3FileService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.s3.region}")
    private String region;

    public void downloadFileFromS3(
            String objectKey,
            String downloadFilePath
    ) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        try {
            s3Client.getObject(
                    getObjectRequest,
                    Paths.get(downloadFilePath)
            );
            log.info("File downloaded successfully to " + downloadFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to download the file : " + e.getMessage());
        }
    }

    public String uploadFileToS3(
            MultipartFile multipartFile,
            String dirName
    ) {
        try {
            String fileName =
                    dirName
                            + "/"
                            + System.currentTimeMillis()
                            + "-"
                    + multipartFile.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(multipartFile.getContentType())
                    .build();

            RequestBody requestBody = RequestBody.fromInputStream(
                    multipartFile.getInputStream(),
                    multipartFile.getSize()
            );

            s3Client.putObject(
                    putObjectRequest,
                    requestBody
            );

            return String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucketName,
                    region,
                    fileName
            );
        } catch (Exception e) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST);
        }
    }

    public List<String> uploadFiles(
            List<MultipartFile> multipartFiles,
            String dirName
    ) {

        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String url = uploadFileToS3(multipartFile, dirName);
            fileUrls.add(url);
        }

        return fileUrls;
    }

    public String getFileUrl(String fileName) {
        return "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/\"+fileName";
    }
}
