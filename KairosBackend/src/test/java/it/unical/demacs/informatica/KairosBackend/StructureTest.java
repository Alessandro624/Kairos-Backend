package it.unical.demacs.informatica.KairosBackend;

import it.unical.demacs.informatica.KairosBackend.data.services.StructureService;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureCreateDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructurePreviewDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureDTO;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureDetailsDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class StructureTest {

    @Value("classpath:data/structures.csv")
    private Resource structures;

    private final StructureService structureService;
    private UUID testStructureId = null;
    private static boolean isInitialized = false;

    public StructureTest(StructureService structureService) {
        this.structureService = structureService;
    }

    @BeforeEach
    public void setupTestData() throws IOException {
        if (!isInitialized) {
            CSVParser structureCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("name", "street", "city", "postalCode", "country")
                    .withSkipHeaderRecord(true)
                    .parse(new InputStreamReader(structures.getInputStream()));

            for (CSVRecord record : structureCsv) {
                StructureDTO structure = insertStructureInDB(
                        record.get("name"),
                        record.get("street"),
                        record.get("city"),
                        record.get("postalCode"),
                        record.get("country")
                );

                if (testStructureId == null) {
                    testStructureId = structure.getId();
                }
            }
            isInitialized = true;
        }
    }

    private StructureDTO insertStructureInDB(
            String name,
            String street,
            String city,
            String postalCode,
            String country
    ) {
        StructureCreateDTO dto = new StructureCreateDTO();
        dto.setName(name);
        dto.getAddress().setStreet(street);
        dto.getAddress().setCity(city);
        dto.getAddress().setZipCode(postalCode);
        dto.setCountry(country);

        return structureService.create(dto);
    }

    @Test
    public void testGetAllStructures() {
        Page<StructureDTO> allStructures = structureService.findAll(0, 10, "id", Sort.Direction.DESC);
        assertFalse(allStructures.isEmpty());
    }

    @Test
    public void testGetStructureById() {
        assertNotNull(testStructureId);
        StructureDetailsDTO structure = structureService.findStructureDetailsById(testStructureId);
        assertNotNull(structure);
    }

    @Test
    public void testCreateAndDeleteStructure() {
        StructureCreateDTO dto = new StructureCreateDTO();
        dto.setName("Test Structure");
        dto.getAddress().setStreet("123 Test St");
        dto.getAddress().setCity("Test City");
        dto.getAddress().setZipCode("12345");
        dto.setCountry("Test Country");

        StructureDTO created = structureService.create(dto);
        assertNotNull(created);
        assertNotNull(created.getId());

        structureService.deleteById(created.getId());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            structureService.findStructureDetailsById(created.getId());
        });

        assertTrue(exception.getMessage().contains("not found"));
    }
}
