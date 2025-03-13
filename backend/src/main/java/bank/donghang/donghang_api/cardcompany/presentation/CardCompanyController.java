package bank.donghang.donghang_api.cardcompany.presentation;

import bank.donghang.donghang_api.cardcompany.application.CardCompanyService;
import bank.donghang.donghang_api.cardcompany.dto.request.CardCompanyRequest;
import bank.donghang.donghang_api.cardcompany.dto.response.CardCompanySummaryResponse;
import bank.donghang.donghang_api.s3.application.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cardcompanies")
public class CardCompanyController {

    private final CardCompanyService cardCompanyService;
    private final S3FileService s3FileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createCardCompany(
            @RequestPart(value = "request") CardCompanyRequest request,
            @RequestPart(value = "image") MultipartFile image
    ) {

        String logoUrl = uploadImageToS3(image);

        Long cardCompanyId = cardCompanyService.createCardCompany(
                request,
                logoUrl
        );

        return ResponseEntity.created(URI.create("/api/v1/cardcompanies/" + cardCompanyId))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<CardCompanySummaryResponse>> findAllCardCompanySummaries(){

        List<CardCompanySummaryResponse> response = cardCompanyService.getAllCardCompanies();

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping(value = "/{cardCompanyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCardCompany(
            @PathVariable(name = "cardCompanyId") Long cardCompanyId,
            @RequestPart(value = "request") CardCompanyRequest request,
            @RequestPart(value = "image", required = false) MultipartFile newLogo
    ) {


        String newLogoUrl = (newLogo != null) ? uploadImageToS3(newLogo) : null;
        cardCompanyService.updateCardCompany(
                cardCompanyId,
                newLogoUrl,
                request
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cardCompanyId}")
    public ResponseEntity<Void> deleteCardCompany(@PathVariable(name = "cardCompanyId") Long cardCompanyId) {
        cardCompanyService.deleteCardCompany(cardCompanyId);

        return ResponseEntity.noContent().build();
    }

    private String uploadImageToS3(MultipartFile image) {
        return s3FileService.uploadFileToS3(image, "logo");
    }
}
