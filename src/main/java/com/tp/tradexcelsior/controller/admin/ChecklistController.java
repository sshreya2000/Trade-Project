package com.tp.tradexcelsior.controller.admin;

import com.tp.tradexcelsior.dto.response.ChecklistDTO;
import com.tp.tradexcelsior.entity.Checklist;
import com.tp.tradexcelsior.exception.ChecklistNotFoundException;
import com.tp.tradexcelsior.service.admin.impl.ChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Checklist Management", description = "Endpoints for managing checklists")
@RestController
@RequestMapping("/api/v1/admin/checklist")
@CrossOrigin("*")
public class ChecklistController {
    private final ChecklistService checklistService;

    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @Operation(summary = "Add a new checklist")
    @PostMapping("/addchecklist")
    public ResponseEntity<ChecklistDTO> createChecklist(@RequestBody @Valid ChecklistDTO checklistDTO) {
        return ResponseEntity.ok(checklistService.addChecklist(checklistDTO));
    }

    @Operation(summary = "Get all checklists")
    // Get All Checklists
    @GetMapping("/allchecklist")
    public ResponseEntity<List<ChecklistDTO>> getAllChecklists() {
        return ResponseEntity.ok(checklistService.getAllChecklists());
    }

    @Operation(summary = "Get a checklist by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ChecklistDTO> getChecklistById(@PathVariable String id) {
        ChecklistDTO checklist = checklistService.getChecklistById(id);
        return ResponseEntity.ok(checklist);
    }

    @Operation(summary = "Update a checklist")
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateChecklist(@PathVariable String id, @RequestBody ChecklistDTO updatedChecklist) {
        Map<String, Object> response = checklistService.updateChecklist(id, updatedChecklist);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a checklist")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteChecklist(@PathVariable String id) {
        Map<String, Object> response = checklistService.deleteChecklist(id);
        return ResponseEntity.ok(response);
    }
}
