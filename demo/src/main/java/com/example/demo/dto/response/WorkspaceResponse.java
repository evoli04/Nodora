package com.example.demo.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkspaceResponse {
    private Integer workspaceId;
    private Integer memberId;
    private String workspaceName;
    private Integer roleId;

    public WorkspaceResponse(Integer workspaceId, String workspaceName, Integer memberId, Integer roleId) {
        this.workspaceId = workspaceId;
        this.workspaceName = workspaceName;
        this.memberId = memberId;
        this.roleId = roleId;
    }
}