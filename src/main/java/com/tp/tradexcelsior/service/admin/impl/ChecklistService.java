package com.tp.tradexcelsior.service.admin.impl;

import com.tp.tradexcelsior.dto.response.ChecklistDTO;
import com.tp.tradexcelsior.entity.Checklist;
import com.tp.tradexcelsior.exception.ChecklistNotFoundException;
import com.tp.tradexcelsior.exception.DuplicateChecklistException;
import com.tp.tradexcelsior.exception.InvalidChecklistException;
import com.tp.tradexcelsior.repo.ChecklistRepository;
import com.tp.tradexcelsior.service.admin.IAdminChecklistService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class ChecklistService implements IAdminChecklistService {
    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ChecklistService(ChecklistRepository checklistRepository) {
        this.checklistRepository = checklistRepository;
    }

    @Override
    public ChecklistDTO addChecklist(ChecklistDTO checklistDTO) {
        log.info("Attempting to add a new checklist");

        if (checklistDTO.getDescription() == null || checklistDTO.getDescription().trim().isEmpty()) {
            log.error("Checklist description is empty");
            throw new InvalidChecklistException("Checklist description cannot be empty.");
        } else if (checklistDTO.getLink() == null || checklistDTO.getLink().trim().isEmpty()) {
            log.error("Checklist link is empty");
            throw new InvalidChecklistException("Checklist link cannot be empty.");
        } else if (checklistDTO.getButtonName() == null || checklistDTO.getButtonName().trim().isEmpty()) {
            log.error("Checklist button name is empty");
            throw new InvalidChecklistException("Checklist button name cannot be empty.");
        }

        boolean exists = checklistRepository.existsByDescriptionAndButtonName(checklistDTO.getDescription(), checklistDTO.getButtonName());
        if (exists) {
            log.warn("Checklist with description '{}' and button name '{}' already exists",
                    checklistDTO.getDescription(), checklistDTO.getButtonName());
            throw new DuplicateChecklistException("A checklist with the same description already exists.");
        }

        Checklist savedChecklist = checklistRepository.save(modelMapper.map(checklistDTO, Checklist.class));
        log.info("Checklist added successfully with ID: {}", savedChecklist.getId());

        return modelMapper.map(savedChecklist, ChecklistDTO.class);
    }


    @Override
    public List<ChecklistDTO> getAllChecklists() {
        log.info("Fetching all checklists from the database");
        List<Checklist> checklists = checklistRepository.findAll();
        List<ChecklistDTO> checklistDTOS = checklists.stream().map(checklist -> modelMapper.map(checklist, ChecklistDTO.class)).toList();
        return checklistDTOS;
    }

    @Override
    public ChecklistDTO getChecklistById(String id) {
        log.info("Fetching checklist with ID: {}", id);

        return checklistRepository.findById(id)
                .map(checklist -> {
                    log.info("Checklist found with ID: {}", id);
                    return new ChecklistDTO(checklist.getId(), checklist.getDescription(), checklist.getLink(), checklist.getButtonName());
                })
                .orElseThrow(() -> {
                    log.error("Checklist with ID {} not found", id);
                    return new ChecklistNotFoundException("Checklist with ID " + id + " not found.");
                });
    }


    @Override
    public Map<String, Object> updateChecklist(String id, ChecklistDTO updatedChecklist) {
        log.info("Updating checklist with ID: {}", id);

        Checklist existingChecklist = checklistRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot update. Checklist with ID {} not found", id);
                    return new ChecklistNotFoundException("Cannot update. Checklist with ID " + id + " not found.");
                });

        // Update fields
        existingChecklist.setDescription(updatedChecklist.getDescription());
        existingChecklist.setLink(updatedChecklist.getLink());
        existingChecklist.setButtonName(updatedChecklist.getButtonName());

        // Save the updated checklist
        checklistRepository.save(existingChecklist);
        log.info("Checklist updated successfully with ID: {}", id);

        // Construct the response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Checklist updated successfully");

        return response;
    }

    @Override
    public Map<String, Object> deleteChecklist(String id) {
        log.info("Deleting checklist with ID: {}", id);

        Checklist existingChecklist = checklistRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot delete. Checklist with ID {} not found", id);
                    return new ChecklistNotFoundException("Cannot delete. Checklist with ID " + id + " not found.");
                });

        // Capture checklist details before deletion
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Checklist deleted successfully");

        checklistRepository.deleteById(id);
        log.info("Checklist deleted successfully with ID: {}", id);

        return response;
    }

}
