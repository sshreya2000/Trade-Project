package com.tp.tradexcelsior.service.admin.impl;

import com.tp.tradexcelsior.dto.response.ReferenceDTO;
import com.tp.tradexcelsior.entity.Reference;
import com.tp.tradexcelsior.exception.DuplicateReferenceException;
import com.tp.tradexcelsior.exception.InvalidReferenceException;
import com.tp.tradexcelsior.exception.ReferenceNotFoundException;
import com.tp.tradexcelsior.repo.ReferenceRepository;
import com.tp.tradexcelsior.service.admin.IAdminReferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReferenceService implements IAdminReferenceService {
    @Autowired
    private final ReferenceRepository referenceRepository;

    public ReferenceService(ReferenceRepository referenceRepository) {
        this.referenceRepository = referenceRepository;
    }

    // Get all references
    @Override
    public List<ReferenceDTO> getAllReferences() {
        log.info("Fetching all references.");
        List<Reference> references = referenceRepository.findAll();
        log.info("Total references found: {}", references.size());
        return references.stream()
                .map(reference -> new ReferenceDTO(reference.getId(), reference.getName(), reference.getType(), reference.getLink(), reference.getImage()))
                .toList();
    }

    // Get a reference by ID
    @Override
    public ReferenceDTO getReferenceById(String id) {
        log.info("Fetching reference with ID: {}", id);
        Reference reference = referenceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Reference with ID {} not found.", id);
                    return new ReferenceNotFoundException("Reference with ID " + id + " not found.");
                });
        log.info("Reference found with ID: {}", id);
        return new ReferenceDTO(reference.getId(), reference.getName(), reference.getType(), reference.getLink(), reference.getImage());
    }

    // Create a new reference
    @Override
    public ReferenceDTO createReference(ReferenceDTO referenceDTO) {
        log.info("Attempting to add a new reference: {}", referenceDTO.getName());

        // Validate fields
        if (referenceDTO.getName() == null || referenceDTO.getName().trim().isEmpty()) {
            log.error("Reference name is empty");
            throw new InvalidReferenceException("Reference name cannot be empty.");
        } else if (referenceDTO.getType() == null || referenceDTO.getType().trim().isEmpty()) {
            log.error("Reference type is empty");
            throw new InvalidReferenceException("Reference type cannot be empty.");
        } else if (referenceDTO.getLink() == null || referenceDTO.getLink().trim().isEmpty()) {
            log.error("Reference link is empty");
            throw new InvalidReferenceException("Reference link cannot be empty.");
        } else if (referenceDTO.getImage() == null || referenceDTO.getImage().trim().isEmpty()) {
            log.error("Reference image is empty");
            throw new InvalidReferenceException("Reference image cannot be empty.");
        }

        // Check for duplicate reference
        boolean exists = referenceRepository.existsByNameAndType(referenceDTO.getName(), referenceDTO.getType());
        if (exists) {
            log.warn("Reference with name '{}' and type '{}' already exists", referenceDTO.getName(), referenceDTO.getType());
            throw new DuplicateReferenceException("A reference with the same name and type already exists.");
        }

        // Convert DTO to Entity
        Reference reference = new Reference();
        reference.setName(referenceDTO.getName());
        reference.setType(referenceDTO.getType());
        reference.setLink(referenceDTO.getLink());
        reference.setImage(referenceDTO.getImage());

        // Save reference
        Reference savedReference = referenceRepository.save(reference);
        log.info("Reference added successfully with ID: {}", savedReference.getId());

        // Convert Entity to DTO and return
        return new ReferenceDTO(savedReference.getId(), savedReference.getName(), savedReference.getType(), savedReference.getLink(), savedReference.getImage());
    }


    // Update an existing reference
    @Override
    public Map<String, Object> updateReference(String id, ReferenceDTO referenceDTO) {
        log.info("Updating reference with ID: {}", id);

        Reference existingReference = referenceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot update. Reference with ID {} not found.", id);
                    return new ReferenceNotFoundException("Cannot update. Reference with ID " + id + " not found.");
                });

        existingReference.setName(referenceDTO.getName());
        existingReference.setType(referenceDTO.getType());
        existingReference.setLink(referenceDTO.getLink());
        existingReference.setImage(referenceDTO.getImage());

        Reference updatedReference = referenceRepository.save(existingReference);
        log.info("Reference updated successfully with ID: {}", updatedReference.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reference updated successfully");
        response.put("updatedReference", new ReferenceDTO(updatedReference.getId(), updatedReference.getName(), updatedReference.getType(), updatedReference.getLink(), updatedReference.getImage()));
        return response;
    }


    // Delete a reference by ID
    @Override
    public Map<String, Object> deleteReference(String id) {
        log.info("Deleting reference with ID: {}", id);

        Reference existingReference = referenceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot delete. Reference with ID {} not found.", id);
                    return new ReferenceNotFoundException("Cannot delete. Reference with ID " + id + " not found.");
                });

        referenceRepository.deleteById(id);
        log.info("Reference deleted successfully with ID: {}", id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reference deleted successfully");
        return response;
    }
}
