package bank.donghang.donghang_api.loanProduct.presentation;

import bank.donghang.donghang_api.loanProduct.application.LoanProductService;
import bank.donghang.donghang_api.loanProduct.domain.LoanProduct;
import bank.donghang.donghang_api.loanProduct.dto.request.LoanProductCreateRequest;
import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}
