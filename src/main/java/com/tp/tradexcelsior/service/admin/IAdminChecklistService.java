package com.tp.tradexcelsior.service.admin;

import com.tp.tradexcelsior.dto.response.ChecklistDTO;
import com.tp.tradexcelsior.entity.Checklist;

import java.util.List;
import java.util.Map;

public interface IAdminChecklistService {
    ChecklistDTO addChecklist(ChecklistDTO checklistDTO);
    List<ChecklistDTO> getAllChecklists();
    ChecklistDTO getChecklistById(String id);
    Map<String, Object> updateChecklist(String id, ChecklistDTO updatedChecklist);
    Map<String, Object> deleteChecklist(String id);

}
