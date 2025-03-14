package bank.donghang.donghang_api.bank.presentation;

import bank.donghang.donghang_api.bank.application.BankService;
import bank.donghang.donghang_api.bank.domain.Bank;
import bank.donghang.donghang_api.bank.dto.request.BankRequest;
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
@RequestMapping("/api/v1/banks")
public class BankController {

    private final BankService bankService;
    private final S3FileService s3FileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createBank(
            @RequestPart(value = "request") BankRequest request,
            @RequestPart(value = "image") MultipartFile image
    ) {

        String logoUrl = uploadImageToS3(image);
        Long bankId = bankService.createBank(
                request,
                logoUrl
        );

        return ResponseEntity.created(URI.create("/api/v1/banks/" + bankId))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<Bank>> getAllBanks() {
        return ResponseEntity.ok().body(bankService.getAllBanks());
    }

    @PatchMapping(value = "/{bankId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateBank(
            @PathVariable(name = "bankId") Long bankId,
            @RequestPart(value = "request") BankRequest request,
            @RequestPart(value = "image") MultipartFile newLogo
    ){

        String newLogoUrl = (newLogo != null) ? uploadImageToS3(newLogo) : null;
        bankService.updateBank(
                request,
                newLogoUrl,
                bankId
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bankId}")
    public ResponseEntity<Void> deleteBank(@PathVariable(name = "bankId") Long bankId) {
        return ResponseEntity.ok().build();
    }

    private String uploadImageToS3(MultipartFile image) {
        return s3FileService.uploadFileToS3(image, "bank");
    }
}
