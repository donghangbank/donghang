package bank.donghang.donghang_api.cardproduct.presentation;

import bank.donghang.donghang_api.cardcompany.dto.request.CardCompanyRequest;
import bank.donghang.donghang_api.cardproduct.application.CardProductService;
import bank.donghang.donghang_api.cardproduct.domain.enums.CardProductType;
import bank.donghang.donghang_api.cardproduct.dto.request.CardProductCreateRequest;
import bank.donghang.donghang_api.cardproduct.dto.request.CardProductUpdateRequest;
import bank.donghang.donghang_api.cardproduct.dto.response.CardProductDetailResponse;
import bank.donghang.donghang_api.cardproduct.dto.response.CardProductSummaryResponse;
import bank.donghang.donghang_api.s3.application.S3FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cardproducts")
@Slf4j
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

    @GetMapping("/{cardProductId}")
    public ResponseEntity<CardProductDetailResponse> findCardProduct(@PathVariable(name = "cardProductId") Long cardProductId) {

        CardProductDetailResponse response = cardProductService.getCardProductDetail(cardProductId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CardProductSummaryResponse>> getCardProductSummaries(
            @RequestParam(name = "cardProductType", required = false) CardProductType cardProductType,
            @RequestParam(name = "cardCompanyName", required = false) String cardCompanyName
    ){
        List<CardProductSummaryResponse> response = cardProductService.getCardProductSummaries(
                cardProductType,
                cardCompanyName
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{cardProductId}")
    public ResponseEntity<Void> updateCardProduct(
            @PathVariable(name = "cardProductId") Long cardProductId,
            @RequestPart(value = "request") CardProductUpdateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile newImage
    ) {

        String cardImageUrl = uploadImageToS3(newImage);

        cardProductService.updateCardProduct(
                cardProductId,
                cardImageUrl,
                request
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cardProductId}")
    public ResponseEntity<Void> deleteCardProduct(@PathVariable(name = "cardProductId") Long cardProductId) {

        cardProductService.deleteCardProduct(cardProductId);

        return ResponseEntity.noContent().build();
    }


    private String uploadImageToS3(MultipartFile image) {
        return s3FileService.uploadFileToS3(image, "cardproduct");
    }
}
