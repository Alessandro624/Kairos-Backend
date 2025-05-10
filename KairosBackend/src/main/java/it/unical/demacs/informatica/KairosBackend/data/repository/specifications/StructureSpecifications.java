package it.unical.demacs.informatica.KairosBackend.data.repository.specifications;

import it.unical.demacs.informatica.KairosBackend.data.entities.Structure;
import it.unical.demacs.informatica.KairosBackend.dto.structure.StructureFilterDTO;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.*;

public class StructureSpecifications
{
    private StructureSpecifications() {}

    public static Specification<Structure> filterStructure(StructureFilterDTO structureFilterDTO)
    {
        return (root, cq, cb) ->
        {
            List<Predicate> predicates = new ArrayList<>();

            if (structureFilterDTO.getName() != null)
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + structureFilterDTO.getName().toLowerCase() + "%"));

            if (structureFilterDTO.getAddress() != null && structureFilterDTO.getAddress().getStreet() != null)
                predicates.add(cb.like(cb.lower(root.get("address_street")), "%" + structureFilterDTO.getAddress().getStreet().toLowerCase() + "%"));

            if (structureFilterDTO.getCountry() != null)
                predicates.add(cb.like(cb.lower(root.get("country")), "%" + structureFilterDTO.getCountry().toLowerCase() + "%"));

            if (predicates.isEmpty())
                predicates.add(cb.equal(root.get("id"), -1L));

            return cq.where(cb.and(predicates.toArray(new Predicate[0])))
                    .distinct(true)
                    .getRestriction();
        };
    }
}
