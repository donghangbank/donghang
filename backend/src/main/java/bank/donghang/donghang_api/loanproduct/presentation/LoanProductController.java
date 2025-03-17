package bank.donghang.donghang_api.loanproduct.presentation;

import bank.donghang.donghang_api.loanproduct.application.LoanProductService;
import bank.donghang.donghang_api.loanproduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanproduct.dto.request.LoanProductCreateRequest;
import bank.donghang.donghang_api.loanproduct.dto.request.LoanProductUpdateRequest;
import bank.donghang.donghang_api.loanproduct.dto.response.LoanProductDetailResponse;
import bank.donghang.donghang_api.loanproduct.dto.response.LoanProductSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/loanproducts")
public class LoanProductController {

    private final LoanProductService loanProductService;

    @PostMapping
    public ResponseEntity<Void> createLoanProduct(@RequestBody LoanProductCreateRequest request) {

        Long loadProductId = loanProductService.createLoanProduct(request);

        return ResponseEntity.created(URI.create("/api/v1/loan-products/" + loadProductId))
                .build();
    }

    @GetMapping("/{loanProductId}")
    public ResponseEntity<LoanProductDetailResponse> getLoanProductDetail(@PathVariable("loanProductId") Long loanProductId) {

        LoanProductDetailResponse response = loanProductService.getLoanProductDetail(loanProductId);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<List<LoanProductSummaryResponse>> getAllLoanProducts(@RequestParam(required = false) LoanType loanType) {

        List<LoanProductSummaryResponse> response = loanProductService.getLoanProductSummaries(loanType);

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("{loanProductId}")
    public ResponseEntity<Void> updateLoanProduct(
            @PathVariable("loanProductId") Long loanProductId,
            @RequestBody LoanProductUpdateRequest request
    ){

        loanProductService.updateLoanProduct(
                loanProductId,
                request
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{loanProductId}")
    public ResponseEntity<Void> deleteLoanProduct(@PathVariable("loanProductId") Long loanProductId) {

        loanProductService.deleteLoanProduct(loanProductId);
        return ResponseEntity.noContent().build();
    }
}
