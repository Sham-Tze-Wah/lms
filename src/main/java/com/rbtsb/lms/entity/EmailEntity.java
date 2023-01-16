//package com.rbtsb.lms.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.rbtsb.lms.util.SqlDataType;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.NonNull;
//import org.hibernate.annotations.GenericGenerator;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import java.util.UUID;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Entity
//@Table(name = "Email")
//public class EmailEntity {
//    @Id
//    @Column(name="email_id", unique = true, nullable = false)
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
//    @JsonIgnore
//    private String id = UUID.randomUUID().toString();
//
//    @Column(name="from_email", length= SqlDataType.VARCHAR64, nullable=false)
//    private String fromEmail;
//
//    @Column(name="to_email", length= SqlDataType.VARCHAR64, nullable=false)
//    private String toEmail;
//
//    @Column(name="subject", length= SqlDataType.VARCHAR255, nullable=false)
//    private String subject;
//
//    @Column(name="subject", length= SqlDataType.VARCHAR2000, nullable=false)
//    private String body;
//
//    @Column(name="subject", length= SqlDataType.VARCHAR2000, nullable=false)
//    private String[] attachments;
//}
