package bank.donghang.donghang_api.cardproduct.presentation;

import bank.donghang.donghang_api.cardproduct.application.CardProductService;
import bank.donghang.donghang_api.cardproduct.dto.request.CardProductCreateRequest;
import bank.donghang.donghang_api.s3.application.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cardproducts")
public class CardProductController {

    private final CardProductService cardProductService;
    private final S3FileService s3FileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createCardProduct(
            @RequestPart(value = "request") CardProductCreateRequest request,
            @RequestPart(value = "image") MultipartFile image
    ){

        String cardImageUrl = uploadImageToS3(image);

        Long cardProductId = cardProductService.createCardProduct(
                request,
                cardImageUrl
        );

        return ResponseEntity.created(URI.create("/api/v1/cardproducts/" + cardProductId))
                .build();
    }

    private String uploadImageToS3(MultipartFile image) {
        return s3FileService.uploadFileToS3(image, "cardproduct");
    }
}
