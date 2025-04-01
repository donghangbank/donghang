package bank.donghang.core.card.application;

import bank.donghang.core.card.domain.repository.CardRepository;
import bank.donghang.core.card.dto.request.CardPasswordRequest;
import bank.donghang.core.card.dto.response.CardPasswordResponse;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public CardPasswordResponse checkCardPassword(CardPasswordRequest request) {

        if (!cardRepository.existsByCardNumber(request.cardNumber())){
            throw new BadRequestException(ErrorCode.CARD_NOT_FOUND);
        }

        CardPasswordResponse response = cardRepository.checkCardPassword(request.cardNumber());

        if (!response.password().equals(request.password())){
            throw new BadRequestException(ErrorCode.PASSWORD_MISMATCH);
        }

        return response;
    }
}
