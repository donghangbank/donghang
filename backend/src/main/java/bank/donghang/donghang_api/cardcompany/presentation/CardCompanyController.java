package bank.donghang.donghang_api.cardcompany.presentation;

import bank.donghang.donghang_api.cardcompany.application.CardCompanyService;
import bank.donghang.donghang_api.cardcompany.domain.CardCompany;
import bank.donghang.donghang_api.cardcompany.dto.request.CardCompanyCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cardcompanies")
public class CardCompanyController {

    private final CardCompanyService cardCompanyService;

    @PostMapping
    public ResponseEntity<CardCompany> createCardCompany(@RequestBody CardCompanyCreateRequest request) {

        Long cardCompanyId = cardCompanyService.createCardCompany(request);

        return ResponseEntity.created(URI.create("/api/v1/cardcompanies/" + cardCompanyId))
                .build();
    }
}
