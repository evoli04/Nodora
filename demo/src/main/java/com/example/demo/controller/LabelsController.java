package com.example.demo.controller;

import com.example.demo.dto.request.LabelsRequest;
import com.example.demo.dto.response.LabelsResponse;
import com.example.demo.service.LabelsService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelsController {

    private final LabelsService labelsService;

    public LabelsController(LabelsService labelsService) {
        this.labelsService = labelsService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','OWNER','LEADER','MEMBER')")
    @GetMapping
    public List<LabelsResponse> getAllLabels() {
        return labelsService.getAllLabels();
    }

    @PreAuthorize("hasAnyRole('ADMIN','OWNER','LEADER','MEMBER')")
    @GetMapping("/{id}")
    public LabelsResponse getLabel(@PathVariable Integer id) {
        return labelsService.getLabelById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','OWNER','LEADER')")
    @PostMapping
    public LabelsResponse createLabel(@Valid @RequestBody LabelsRequest request) {
        return labelsService.createLabel(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable Integer id) {
        labelsService.deleteLabel(id);
}
}