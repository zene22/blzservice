package info.furbach.blz.rest.blz;

import info.furbach.blz.rest.blz.exception.BLZServiceException;
import info.furbach.blz.rest.blz.exception.BadRequestException;
import info.furbach.blz.rest.blz.exception.NotFoundException;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Api(value = "BLZ", tags = {"Bankleitzahl Service"}, basePath = "/api")
@RestController
@EnableSwagger2
@RequestMapping("/api")
public class BankleitzahlResource {
    @Autowired
    private BankleitzahlRepository bankleitzahlRepository;

    @ApiOperation(value = "Liefert alle Bankleitzahlen",
            tags = {"GET"},
            produces = "application/json; charset=utf-8",
            response = Iterable.class)
    @GetMapping("/blz")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK: Bankleitzahlen werden geliefert"),
            @ApiResponse(code = 500, message = "INTERNAL ERROR: Interner Fehler, siehe Message / LOG!")
    })
    public Iterable<Bankleitzahl> retrieveAllBankleitzahlen() throws BLZServiceException {
        return bankleitzahlRepository.findAll();
    }

    @ApiOperation(value = "Liefert die BIC für eine Bankleitzahl <blz>",
            tags = {"GET"},
            produces = "test/plain; charset=utf-8",
            response = String.class)
    @GetMapping("/blz/{blz:[0-9]+}")
    public List<Bankleitzahl> retrieveBic(@ApiParam(value = "BLZ ([0-9]+)") @PathVariable long blz) throws BLZServiceException {
        try {
            return bankleitzahlRepository.findByBlz(blz);
        } catch (Exception e) {
            throw new BLZServiceException("Kann die Bankleitzahlen nicht liefern", e);
        }
    }


    @ApiOperation(value = "Erneuert die Liste der Bankleitzahlen in dem die Resource blz.txt erneut eingelesen wird",
            tags = {"PUT"},
            produces = "test/plain; charset=utf-8",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK: Daten wurden erneuert!"),
            @ApiResponse(code = 404, message = "NOT_FOUND: Datei wurde nicht gefunden!"),
            @ApiResponse(code = 500, message = "INTERNAL ERROR: Interner Fehler, siehe Message / LOG!")
    })
    @PutMapping("/blz")
    public void refresh() throws BLZServiceException {
        try {
            Stream<String> stream = Files.lines(Paths.get("/import/blz.txt"));
            bankleitzahlRepository.deleteAll();
            stream.filter(line -> !StringUtils.isEmpty(line.substring(139, 150).trim()) && (bankleitzahlRepository.findByBlz(Long.parseLong(line.substring(0, 8))).isEmpty()))
                    .forEach(line -> bankleitzahlRepository.save(Bankleitzahl.builder()
                            .bic(line.substring(139, 150))
                            .blz(Long.parseLong(line.substring(0, 8)))
                            .plz(line.substring(67, 72))
                            .name(line.substring(9, 67).trim())
                            .ort(line.substring(72, 107).trim())
                            .build()));
        } catch (IOException e) {
            throw new NotFoundException("Kann blz.txt nicht lesen", e);
        } catch (Exception e) {
            throw new BLZServiceException("Kann blz.txt nicht lesen", e);
        }
    }

    @ApiOperation(value = "Liefert die BIC für eine IBAN <param>",
            tags = {"GET"},
            produces = "test/plain; charset=utf-8",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK: BIC wurde geliefert!"),
            @ApiResponse(code = 404, message = "NOT_FOUND: BIC wurde nicht gefunden!"),
            @ApiResponse(code = 400, message = "BAD_REQUEST: Ungültiger Aufruf"),
            @ApiResponse(code = 500, message = "INTERNAL ERROR: Interner Fehler, siehe Message / LOG!")
    })
    @GetMapping("/bic")
    public String mapBic(@ApiParam(value = "IBAN (DE[0-9]+)") @RequestParam("iban") String iban) throws BLZServiceException {
        Long blz = null;
        List<Bankleitzahl> bankleitzahl = new ArrayList<>();
        try {
            blz = Long.parseLong(iban.substring(4, 12));
        } catch (Exception e) {
            throw new BadRequestException("Kann die iban nicht lesen " + iban);
        }

        try {
            bankleitzahl = bankleitzahlRepository.findByBlz(blz);
        } catch (Exception e) {
            throw new BLZServiceException("Kann das Mapping nicht lesen ", e);
        }
        if (bankleitzahl == null || bankleitzahl.isEmpty() || bankleitzahl.size() > 1) {
            throw new NotFoundException("Finde kein Ergebnis zur Bankleitzahl " + blz);
        }
        return bankleitzahl.get(0).getBic();

    }


}
