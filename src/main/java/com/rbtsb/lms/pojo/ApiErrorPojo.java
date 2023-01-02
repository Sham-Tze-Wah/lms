package com.rbtsb.lms.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorPojo {
    String responseStatus;
    String responseMessage;
}
