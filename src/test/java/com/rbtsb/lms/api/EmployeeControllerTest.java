package com.rbtsb.lms.api;

import com.rbtsb.lms.constant.Position;
import com.rbtsb.lms.constant.Role;
import com.rbtsb.lms.entity.EmployeeEntity;
import com.rbtsb.lms.pojo.EmployeePojo;
import com.rbtsb.lms.service.EmployeeService;
import com.rbtsb.lms.service.mapper.EmployeeMapper;
import com.rbtsb.lms.service.serviceImpl.EmployeeServiceImpl;
import com.rbtsb.lms.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private EmployeeEntity employeeEntity;

    private EmployeePojo employeePojo;

    @BeforeEach
    void setUp() throws ParseException {
        this.employeeEntity = EmployeeEntity.builder()
                .empId(3)
                .name("Abu")
                .phoneNo("0143456789")
                .email("abu@gmail.com")
                .address("Jalan Kota")
                .position(Position.BackEnd)
                .role(Role.Employee)
                .dateJoined(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSXXX").parse("2022-12-21T01:57:55.000+00:00"))
                .build();

        this.employeePojo = EmployeeMapper.entityToPojo(employeeEntity);
    }

    @Test //positive
    void insertEmployee() throws Exception {

        this.employeeEntity = EmployeeEntity.builder()
                .name("Abu")
                .phoneNo("0143456789")
                .email("abu@gmail.com")
                .address("Jalan Kota")
                .position(Position.BackEnd)
                .role(Role.Employee)
                .dateJoined(new Date())
                .build();

        this.employeePojo = EmployeeMapper.entityToPojo(employeeEntity);

        Mockito.when(employeeService.insertEmployee(employeePojo))
                .thenReturn("Insert successfully.");

        mockMvc.perform(post("/api/emp/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"empId\" : \"3\",\n" +
                        "    \"name\" : \"Abu\",\n" +
                        "    \"phoneNo\" : \"0143456789\",\n" +
                        "    \"email\" : \"abu@gmail.com\",\n" +
                        "    \"address\" : \"Jalan Kota\",\n" +
                        "    \"position\" : \"BackEnd\",\n" +
                        "    \"role\" : \"Employee\",\n" +
                        "    \"dateJoined\" : \"2022-12-21\"\n" +
                        "}"))
                .andExpect(status().isCreated());
    }

    @Test //negative
    void insertEmployeeNeg() throws Exception {
        EmployeePojo employeePojo = new EmployeePojo();

        Mockito.when(employeeService.insertEmployee(employeePojo))
                .thenReturn("name cannot be null.");

        mockMvc.perform(post("/api/emp/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "}"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getAllEmployee() throws Exception {
        List<EmployeePojo> employeeEntities = employeeService.getAllEmployee();

        Mockito.when(employeeService.getAllEmployee())
                .thenReturn(employeeEntities);

        mockMvc.perform(get("/api/emp/get/all")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getEmployeeById() throws Exception {
        int id = 1;
        //Optional<EmployeePojo> employeePojo1 = employeeService.getEmployeeById(id);

        Mockito.when(employeeService.
                getEmployeeById(id)
        ).thenReturn(Optional.of(employeePojo));

        mockMvc.perform(get("/api/emp/get/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value(employeePojo.getName())
                )
                .andExpect(jsonPath("$.phoneNo")
                    .value(employeePojo.getPhoneNo())
                )
                .andExpect(jsonPath("$.address")
                    .value(employeePojo.getAddress())
                )
                .andExpect(jsonPath("$.position")
                        .value(employeePojo.getPosition().toString())
                )
                .andExpect(jsonPath("$.role")
                        .value(employeePojo.getRole().toString())
                )
                .andExpect(jsonPath("$.dateJoined")
                        .value("2022-12-21T01:57:55.000+00:00")
                );
    }

    @Test
    void updateEmployeeById() throws Exception {
        int id =1;
        this.employeeEntity = EmployeeEntity.builder()
                .empId(1)
                .name("Abu")
                .phoneNo("0143456789")
                .email("abu@gmail.com")
                .address("Jalan Kota")
                .position(Position.BackEnd)
                .role(Role.Employee)
                .dateJoined(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSXXX").parse("2022-12-21T01:57:55.000+00:00"))
                .build();

        this.employeePojo = EmployeeMapper.entityToPojo(employeeEntity);

        Mockito.when(employeeService.getEmployeeById(id))
                .thenReturn(Optional.of(employeePojo));

        mockMvc.perform(get("/api/emp/get/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value(employeePojo.getName())
                );

        Mockito.when(employeeService.updateEmployeeById(id,employeePojo))
                .thenReturn("updated successfully.");

        mockMvc.perform(put("/api/emp/put/" + id)
        .contentType(MediaType.APPLICATION_JSON).content(
                        "{\n" +
                                "    \"empId\" : \"1\",\n" +
                                "    \"name\" : \"Abu\",\n" +
                                "    \"phoneNo\" : \"0143456789\",\n" +
                                "    \"email\" : \"abu@gmail.com\",\n" +
                                "    \"address\" : \"Jalan Kota\",\n" +
                                "    \"position\" : \"BackEnd\",\n" +
                                "    \"role\" : \"Employee\",\n" +
                                "    \"dateJoined\" : \"2022-12-21\"\n" +
                                "}"
                )).andExpect(status().isOk());
    }

    @Test
    void deleteEmployeeById() throws Exception {
        int id = 1;

        Mockito.when(employeeService.deleteEmployeeById(id))
                .thenReturn("deleted successfully.");

        mockMvc.perform(delete("/api/emp/delete/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}