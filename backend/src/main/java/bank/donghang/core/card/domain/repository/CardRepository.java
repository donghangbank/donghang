package bank.donghang.core.card.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardRepository {

    private final CardJpaRepository cardJpaRepository;


}
