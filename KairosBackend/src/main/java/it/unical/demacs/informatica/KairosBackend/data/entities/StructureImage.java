package it.unical.demacs.informatica.KairosBackend.data.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "structure_image")
public class StructureImage {

    @Id
    @GeneratedValue
    @Column(name = "id", length = 36, nullable = false, updatable = false, unique = true, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "structure_id", nullable = false)
    private Structure structure;
}