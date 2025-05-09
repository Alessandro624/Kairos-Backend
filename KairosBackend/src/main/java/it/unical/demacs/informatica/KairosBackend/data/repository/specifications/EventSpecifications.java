package it.unical.demacs.informatica.KairosBackend.data.repository.specifications;

import it.unical.demacs.informatica.KairosBackend.data.entities.Event;
import it.unical.demacs.informatica.KairosBackend.data.entities.enumerated.Category;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


final class EventSpecifications {
    private EventSpecifications() {}

    @Data
    public static class Filter {
        private String title;
        private String description;
        private Category category;
        private LocalDateTime from;
        private LocalDateTime to;
        private String organizer;
        private String structure;
    }

    private static String searchTermLike(String p) {
        return "%" + p + "%";
    }

    static Specification<Event> buildSpecification(Filter filter) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getTitle() != null) {
                predicates.add(criteriaBuilder.like(root.get("title"), searchTermLike(filter.title)));
            }
            if (filter.getDescription() != null) {
                predicates.add(criteriaBuilder.like(root.get("description"), searchTermLike(filter.description)));
            }
            if (filter.getCategory() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), filter.getCategory()));
            }
            if (filter.getFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("from"), filter.getFrom()));
            }
            if (filter.getTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("to"), filter.getTo()));
            }
            if (filter.getOrganizer() != null) {
                predicates.add(criteriaBuilder.like(root.get("organizer"), searchTermLike(filter.getOrganizer())));
            }
            if (filter.getStructure() != null) {
                predicates.add(criteriaBuilder.like(root.get("structure"), searchTermLike(filter.getStructure())));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
