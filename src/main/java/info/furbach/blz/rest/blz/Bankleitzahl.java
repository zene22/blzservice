package info.furbach.blz.rest.blz;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class Bankleitzahl {

    @Id
    @GeneratedValue
    @ToString.Include
    private Long id;

    @ToString.Include
    private Long blz;

    @ToString.Include
    private String bic;

    @ToString.Include
    private String name;

    private String plz;

    private String ort;

    @Tolerate
    public Bankleitzahl() {
    }
}
