package com.example.demo.service;

import com.example.demo.dto.request.WorkspaceRequest;
import com.example.demo.dto.response.WorkspaceResponse;
import com.example.demo.model.workspaces.Workspaces;
import com.example.demo.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    @Override
    @Transactional
    public WorkspaceResponse createWorkspace(WorkspaceRequest request) {
        Workspaces newWorkspace = new Workspaces(request.getMemberId(), request.getWorkspaceName());
        Workspaces saved = workspaceRepository.save(newWorkspace);

        WorkspaceResponse response = new WorkspaceResponse();
        response.setWorkspaceId(saved.getWorkspaceId());
        response.setMemberId(saved.getMemberId());
        response.setWorkspaceName(saved.getWorkspaceName());

        return response;
    }

    @Override
    public List<WorkspaceResponse> getWorkspacesByMemberId(Integer memberId) {
        return workspaceRepository.findByMemberId(memberId).stream()
                .map(workspace -> {
                    WorkspaceResponse response = new WorkspaceResponse();
                    response.setWorkspaceId(workspace.getWorkspaceId());
                    response.setMemberId(workspace.getMemberId());
                    response.setWorkspaceName(workspace.getWorkspaceName());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
