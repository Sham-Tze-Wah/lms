package com.rbtsb.lms.util.validation;

import lombok.NoArgsConstructor;

import java.util.Scanner;

@NoArgsConstructor
public class EmployeeValidation {

    public static boolean isInteger(String phoneNo) {
        return isInteger(phoneNo, 10);
    }

    private static boolean isInteger(String phoneNo, int radix) {
        Scanner sc = new Scanner(phoneNo.trim());
        if (!sc.hasNextInt(radix)) return false;
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt(radix);
        return !sc.hasNext();

        //    private static boolean isIntegers(String phoneNo, int radix) {
//        return phoneNo.matches("-?(0|[1-9]\\d*)");
//    }
    }


}
