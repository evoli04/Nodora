package com.example.demo.service;

import com.example.demo.dto.request.WorkspaceRequest;
import com.example.demo.dto.response.WorkspaceResponse;

import java.util.List;

public interface WorkspaceService {

    WorkspaceResponse createWorkspace(WorkspaceRequest request);

    List<WorkspaceResponse> getWorkspacesByMemberId(Integer memberId);
}
