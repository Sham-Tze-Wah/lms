package com.rbtsb.lms.config;

public interface APIPath {
    static final String attch_prefix = "/api/attachment";
    static final String edu_prefix = "/api/education";
    static final String emp_prefix = "/api/emp";
    static final String leave_prefix = "/api/leave";
    static final String reg_prefix = "/api";
    static final String work_prefix = "/api/workexp";
    static final String assign_prefix = "/api/assignWork";
    static final String role_prefix = "/api/role";

    static final String[] WHITE_LIST_URLS_FOR_ANNOYMOUS = {
            "/api/login",
            reg_prefix + "/register",
            reg_prefix + "/verifyRegistration*",
            reg_prefix + "/resendVerifyToken*"
    };

    static final String[] WHITE_LIST_URLS_FOR_EVERYONE={
            "/api/authenticate",
            reg_prefix + "/resetPassword",
            reg_prefix + "/savePassword",
            reg_prefix + "/changePassword",
            role_prefix + "/post",
            role_prefix + "/get/all",
            role_prefix + "/put",
            role_prefix + "/delete/{id}",
            emp_prefix + "/add",
    };

    static final String[] WHITE_LIST_URLS_FOR_EMPLOYEE = {
            emp_prefix + "/get",
            emp_prefix + "/get/{id}",
            edu_prefix + "/post",
            edu_prefix + "/get",
            edu_prefix + "/get/{id}",
            work_prefix + "/post",
            work_prefix + "/get",
            work_prefix + "/get/{id}",
            leave_prefix + "/post",
            leave_prefix + "/get",
            attch_prefix + "/post",
            attch_prefix + "/get",
            attch_prefix + "/post/download"
    };

    static final String[] WHITE_LIST_URLS_FOR_HR = { //WHITE_LIST_URLS_FOR_EMPLOYEE

            emp_prefix + "/put/{id}",
            emp_prefix + "/delete/{id}",
            edu_prefix + "/put/{id}",
            edu_prefix + "/delete/{id}",
            work_prefix + "/put/{id}",
            work_prefix + "/delete/{id}",
            leave_prefix + "/put/{id}",
            leave_prefix + "/delete/{id}",
            assign_prefix + "/get/all", // TODO complete it
            attch_prefix + "/put",
            attch_prefix + "/delete/{id}",
            attch_prefix + "/displayImage"
    };

    static final String[] WHITE_LIST_URLS_FOR_MANAGER = { //WHITE_LIST_URLS_FOR_HR
            "/assignRole", // TODO complete it
            "/unassignRole" // TODO complete it
    };

    static final String[] WHITE_LIST_URLS_FOR_BOSS = {
            emp_prefix + "/get/all",
            edu_prefix + "/get/all",
            work_prefix + "/get/all",
            leave_prefix + "/get/all",
            leave_prefix + "/approve/{id}",
            leave_prefix + "/reject/{id}",
            attch_prefix + "/get/all",
            role_prefix + "/assignRole",
            role_prefix + "/unassignRole"
//            role_prefix + "/post",
//            role_prefix + "/get/all",
//            role_prefix + "/put",
//            role_prefix + "/delete/{id}"
    };
}
