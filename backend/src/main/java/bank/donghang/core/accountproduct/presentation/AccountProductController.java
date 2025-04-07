package bank.donghang.core.accountproduct.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bank.donghang.core.accountproduct.application.AccountProductService;
import bank.donghang.core.accountproduct.dto.request.AccountProductCreationRequest;
import bank.donghang.core.accountproduct.dto.response.AccountProductDetail;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import bank.donghang.core.common.dto.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/accountproducts")
@RequiredArgsConstructor
public class AccountProductController {
	private final AccountProductService productService;

	@GetMapping()
	public ResponseEntity<PageInfo<AccountProductSummary>> getAccountProducts(
		@RequestParam(required = false) String pageToken
	) {
		return ResponseEntity.ok(productService.getAllAccountProducts(pageToken));
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
	public ResponseEntity<PageInfo<AccountProductSummary>> getDemandProducts(
		@RequestParam(required = false) String pageToken
	) {
		return ResponseEntity.ok(productService.getDemandProducts(pageToken));
	}

	@GetMapping("/deposits")
	public ResponseEntity<PageInfo<AccountProductSummary>> getDepositProducts(
		@RequestParam(required = false) String pageToken
	) {
		return ResponseEntity.ok(productService.getDepositProducts(pageToken));
	}

	@GetMapping("/installments")
	public ResponseEntity<PageInfo<AccountProductSummary>> getInstallmentProducts(
		@RequestParam(required = false) String pageToken
	) {
		return ResponseEntity.ok(productService.getInstallmentProducts(pageToken));
	}
}
