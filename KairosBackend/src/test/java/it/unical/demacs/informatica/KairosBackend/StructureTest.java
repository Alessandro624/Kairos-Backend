package it.unical.demacs.informatica.KairosBackend;

import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.data.entities.embeddables.Address;
import it.unical.demacs.informatica.KairosBackend.data.services.StructureService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class StructureTest
{
    @Value("classpath:data/structures.csv")
    private Resource structures;

    private final StructureService structureService;
    private UUID testStructureId = null;
    private static boolean isInitialized = false;

    public StructureTest(StructureService structureService)
    {
        this.structureService = structureService;
    }

    @BeforeEach
    public void setupTestData() throws IOException
    {
        if (!isInitialized)
        {
            CSVParser structureCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .withHeader("name", "street", "city", "postalCode", "country")
                    .withSkipHeaderRecord(true)
                    .parse(new InputStreamReader(structures.getInputStream()));

            for (CSVRecord record : structureCsv)
            {
                Structure structure = insertStructureInDB(
                        record.get("name"),
                        record.get("street"),
                        record.get("city"),
                        record.get("postalCode"),
                        record.get("country")
                );

                if (testStructureId == null)
                {
                    Optional<Structure> createdStructure = structureService.findByName(structure.getName());
                    testStructureId = createdStructure.map(Structure::getId).orElse(null);
                }
            }
            isInitialized = true;
        }
    }

    private Structure insertStructureInDB(
            String name,
            String street,
            String city,
            String postalCode,
            String country
    ) {
        Structure structureDTO = new Structure();
        structureDTO.setId(UUID.randomUUID());
        structureDTO.setName(name);

        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        address.setZipCode(postalCode);
        address.setStreet(country);

        structureDTO.setAddress(address);
        return structureService.saveStructure(structureDTO);
    }

    @Test
    public void testGetAllStructures()
    {
        List<Structure> allStructures = structureService.getAllStructures();
        assertFalse(allStructures.isEmpty());
    }

    @Test
    public void testGetStructureById()
    {
        assertNotNull(testStructureId);
        Optional<Structure> structure = structureService.getStructureById(testStructureId);
        assertTrue(structure.isPresent());
    }

    @Test
    public void testCreateAndDeleteStructure()
    {
        Structure newStructure = new Structure();
        newStructure.setId(UUID.randomUUID());
        newStructure.setName("Test Structure");

        Address address = new Address();
        address.setStreet("123 Test St");
        address.setCity("Test City");
        address.setZipCode("12345");
        address.setStreet("Test Country");
        newStructure.setAddress(address);

        Structure savedStructure = structureService.saveStructure(newStructure);
        assertNotNull(savedStructure.getId());

        structureService.deleteStructure(savedStructure.getId());
        assertFalse(structureService.getStructureById(savedStructure.getId()).isPresent());
    }

    @Test
    public void testFindByName()
    {
        Optional<Structure> structure = structureService.findByName("Existing Structure Name");
        assertTrue(structure.isPresent());
    }
}