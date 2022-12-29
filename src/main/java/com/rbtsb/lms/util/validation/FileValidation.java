package com.rbtsb.lms.util.validation;

public class FileValidation {
    public static boolean isPicture(String fileExtension){
        if(fileExtension.equalsIgnoreCase(".gif") ||
                fileExtension.equalsIgnoreCase(".png") ||
                fileExtension.equalsIgnoreCase(".jpg") ||
                fileExtension.equalsIgnoreCase(".jpeg")){
            return true;
        }
        else{
            return false;
        }
    }
}
