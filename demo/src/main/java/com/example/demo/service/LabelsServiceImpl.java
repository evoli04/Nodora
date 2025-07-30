package com.example.demo.service;

import com.example.demo.dto.request.LabelsRequest;
import com.example.demo.dto.response.LabelsResponse;
import com.example.demo.exception.LabelNotFoundException;
import com.example.demo.model.boards.Boards;
import com.example.demo.model.labels.Labels;
import com.example.demo.repository.LabelsRepository;
import com.example.demo.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabelsServiceImpl implements LabelsService {

    private final LabelsRepository labelsRepository;
    private final BoardRepository boardsRepository;

    public LabelsServiceImpl(LabelsRepository labelsRepository, BoardRepository boardsRepository) {
        this.labelsRepository = labelsRepository;
        this.boardsRepository = boardsRepository;
    }

    @Override
    public LabelsResponse createLabel(LabelsRequest request) {
        Boards board = boardsRepository.findById(request.getBoardId())
                .orElseThrow(() -> new LabelNotFoundException("Board not found"));

        Labels label = new Labels();
        label.setBoardId(board.getBoardId());
        label.setLabelName(request.getLabelName());
        label.setColor(request.getColor());

        Labels saved = labelsRepository.save(label);

        return mapToResponse(saved);
    }

    @Override
    public List<LabelsResponse> getAllLabels() {
        return labelsRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LabelsResponse getLabelById(Integer id) {
        Labels label = labelsRepository.findById(id)
                .orElseThrow(() -> new LabelNotFoundException("Label not found"));
        return mapToResponse(label);
    }

    @Override
    public void deleteLabel(Integer id) {
        Labels label = labelsRepository.findById(id)
                .orElseThrow(() -> new LabelNotFoundException("Label not found"));
        labelsRepository.delete(label);
    }

    private LabelsResponse mapToResponse(Labels label) {
        LabelsResponse response = new LabelsResponse();
        response.setLabelId(label.getLabelId());
        response.setBoardId(label.getBoardId());  // Burada getId() kullanılmalı
        response.setLabelName(label.getLabelName());
        response.setColor(label.getColor());
        return response;
}
}