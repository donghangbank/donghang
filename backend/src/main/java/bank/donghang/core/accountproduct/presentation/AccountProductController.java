package bank.donghang.core.accountproduct.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.donghang.core.accountproduct.application.AccountProductService;
import bank.donghang.core.accountproduct.dto.request.AccountProductCreationRequest;
import bank.donghang.core.accountproduct.dto.response.AccountProductDetail;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/accountproducts")
@RequiredArgsConstructor
public class AccountProductController {
	private final AccountProductService productService;

	@GetMapping()
	public ResponseEntity<List<AccountProductSummary>> getAccountProducts() {
		return ResponseEntity.ok(productService.getAllAccountProducts());
	}

	@GetMapping("/{productId}")
	public ResponseEntity<AccountProductDetail> getAccountProductDetail(
			@PathVariable(name = "productId", required = true) Long productId) {
		return ResponseEntity.ok(productService.getAccountProductDetail(productId));
	}

	@PostMapping()
	public ResponseEntity<AccountProductSummary> createAccountProduct(
			@RequestBody @Valid AccountProductCreationRequest accountProductCreationRequest) {
		return ResponseEntity.ok(productService.registerAccountProduct(accountProductCreationRequest));
	}

	@GetMapping("/demands")
	public ResponseEntity<List<AccountProductSummary>> getDemandProducts() {
		return ResponseEntity.ok(productService.getDemandProducts());
	}

	@GetMapping("/deposits")
	public ResponseEntity<List<AccountProductSummary>> getDepositProducts() {
		return ResponseEntity.ok(productService.getDepositProducts());
	}

	@GetMapping("/installments")
	public ResponseEntity<List<AccountProductSummary>> getInstallmentProducts() {
		return ResponseEntity.ok(productService.getInstallmentProducts());
	}
}
