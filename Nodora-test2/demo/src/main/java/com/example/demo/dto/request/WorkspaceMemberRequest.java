package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMemberRequest {

    @NotNull(message = "Workspace ID boş olamaz")
    @Positive(message = "Workspace ID pozitif olmalı")
    private Integer workspaceId;

    @NotNull(message = "Member ID boş olamaz")
    @Positive(message = "Member ID pozitif olmalı")
    private Integer memberId;

    @NotNull(message = "Role ID boş olamaz")
    @Positive(message = "Role ID pozitif olmalı")
    private Integer roleId;
}