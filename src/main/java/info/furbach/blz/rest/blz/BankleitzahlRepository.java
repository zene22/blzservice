package info.furbach.blz.rest.blz;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankleitzahlRepository extends JpaRepository<Bankleitzahl, Long>
{
    List<Bankleitzahl> findByBlz(Long blz);
}
