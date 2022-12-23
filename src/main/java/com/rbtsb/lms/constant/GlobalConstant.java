package com.rbtsb.lms.constant;

public interface GlobalConstant {
    /**
     * Date format for DateUtils to parseDate
     * e.g. DateUtils.parseDate("12/12/2012", Global.dateFormat[0])
     */
    final static String[] dateFormat = {"dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd"};

    final static String JSON_DATE_FORMAT = "dd/MM/yyyy";

    final static String DateTime = "DATETIME";

    final static String ATTACHMENT_PATH = "C:\\Practice\\lms\\src\\main\\resources\\leaveAttachment";
}
