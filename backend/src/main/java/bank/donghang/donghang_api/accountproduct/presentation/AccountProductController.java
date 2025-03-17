package bank.donghang.donghang_api.accountproduct.presentation;

import bank.donghang.donghang_api.accountproduct.application.AccountProductService;
import bank.donghang.donghang_api.accountproduct.dto.response.AccountProductDetail;
import bank.donghang.donghang_api.accountproduct.dto.response.AccountProductSummary;
import bank.donghang.donghang_api.accountproduct.dto.request.AccountProductCreationRequest;
import bank.donghang.donghang_api.accountproduct.dto.response.AccountProductListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/accountproducts")
@RequiredArgsConstructor
public class AccountProductController {
    private final AccountProductService productService;

    @GetMapping()
    public ResponseEntity<AccountProductListResponse> getAccountProducts() {
        return ResponseEntity.ok(productService.getAllAccountProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<AccountProductDetail> getAccountProductDetail(@PathVariable(name = "productId", required = true) Long productId) {
        return ResponseEntity.ok(productService.getAccountProductDetail(productId));
    }

    @PostMapping()
    public ResponseEntity<AccountProductSummary> createAccountProduct(@RequestBody AccountProductCreationRequest accountProductCreationRequest) {
        return ResponseEntity.ok(productService.registerAccountProduct(accountProductCreationRequest));
    }
}
