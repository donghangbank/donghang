package bank.donghang.donghang_api.cardproduct.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardProductRepository{

    private final CardProductJpaRepository cardProductJpaRepository;
}
