package com.rbtsb.lms.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RolePojo {
    @JsonProperty(value = "roleId")
    private String roleId = UUID.randomUUID().toString();

    @JsonProperty(value = "roleName")
    private String roleName;
}
