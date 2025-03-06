package com.tp.tradexcelsior.controller.admin;

import com.tp.tradexcelsior.dto.response.ReferenceDTO;
import com.tp.tradexcelsior.entity.Reference;
import com.tp.tradexcelsior.service.admin.impl.ReferenceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/reference")
@CrossOrigin("*")
public class ReferenceController {
    private final ReferenceService referenceService;

    public ReferenceController(ReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    // Get all references
    @Operation(summary = "Get all references")
    @GetMapping("/all")
    public ResponseEntity<List<ReferenceDTO>> getAllReferences() {
        return ResponseEntity.ok(referenceService.getAllReferences());
    }

    // Get a single reference by ID
    @Operation(summary = "Get a reference by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReferenceDTO> getReferenceById(@PathVariable String id) {
        return ResponseEntity.ok(referenceService.getReferenceById(id));
    }

    // Create a new reference
    @Operation(summary = "Add a new reference")
    @PostMapping("/add")
    public ResponseEntity<ReferenceDTO> createReference(@Valid @RequestBody ReferenceDTO referenceDTO) {
        return ResponseEntity.ok(referenceService.createReference(referenceDTO));
    }

    // Update an existing reference
    @Operation(summary = "Update a reference")
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateReference(@PathVariable String id, @Valid @RequestBody ReferenceDTO referenceDTO) {
        Map<String, Object> response = referenceService.updateReference(id, referenceDTO);
        return ResponseEntity.ok(response);
    }

    // Delete a reference by ID
    @Operation(summary = "Delete a reference")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteReference(@PathVariable String id) {
        return ResponseEntity.ok(referenceService.deleteReference(id));
    }
}
