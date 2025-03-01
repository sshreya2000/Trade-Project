package com.tp.tradexcelsior.service.admin;

import com.tp.tradexcelsior.dto.response.ReferenceDTO;

import java.util.List;
import java.util.Map;

public interface IAdminReferenceService {
    List<ReferenceDTO> getAllReferences();
    ReferenceDTO getReferenceById(String id);
    ReferenceDTO createReference(ReferenceDTO referenceDTO);
    Map<String, Object> updateReference(String id, ReferenceDTO referenceDTO);
    Map<String, Object> deleteReference(String id);
}
