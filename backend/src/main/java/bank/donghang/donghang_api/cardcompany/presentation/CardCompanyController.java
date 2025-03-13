package bank.donghang.donghang_api.cardcompany.presentation;

import bank.donghang.donghang_api.cardcompany.application.CardCompanyService;
import bank.donghang.donghang_api.cardcompany.domain.CardCompany;
import bank.donghang.donghang_api.cardcompany.dto.request.CardCompanyRequest;
import bank.donghang.donghang_api.cardcompany.dto.response.CardCompanySummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cardcompanies")
public class CardCompanyController {

    private final CardCompanyService cardCompanyService;

    @PostMapping
    public ResponseEntity<CardCompany> createCardCompany(@RequestBody CardCompanyRequest request) {

        Long cardCompanyId = cardCompanyService.createCardCompany(request);

        return ResponseEntity.created(URI.create("/api/v1/cardcompanies/" + cardCompanyId))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<CardCompanySummaryResponse>> findAllCardCompanySummaries(){

        List<CardCompanySummaryResponse> response = cardCompanyService.findAllCardCompanies();

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{cardCompanyId}")
    public ResponseEntity<Void> updateCardCompany(
            @PathVariable Long cardCompanyId,
            @RequestBody CardCompanyRequest request
    ) {

        cardCompanyService.updateCardCompany(
                cardCompanyId,
                request
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cardCompanyId}")
    public ResponseEntity<Void> deleteCardCompany(@PathVariable(name = "cardCompanyId") Long cardCompanyId) {
        cardCompanyService.deleteCardCompany(cardCompanyId);

        return ResponseEntity.noContent().build();
    }
}
