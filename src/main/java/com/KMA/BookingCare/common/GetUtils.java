package com.KMA.BookingCare.common;

import com.KMA.BookingCare.Dto.Role;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class GetUtils {

    public static String getRole(Set<Role> roles) {
        if(roles.stream().anyMatch(item -> item.getName().equals("ROLE_ADMIN"))) return "ROLE_ADMIN";
        if(roles.stream().anyMatch(item -> item.getName().equals("ROLE_DOCTOR"))) return "ROLE_DOCTOR";
        return "ROLE_USER";
    }

    public static boolean isValidWorkTime(String workTimeTime, Calendar calendar) {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minus = calendar.get(Calendar.MINUTE);
		int hourCheckBook = hour;
		if (minus > 0) {
			hourCheckBook = hour + 1;
		}
		String timeStart = workTimeTime.split("-")[0];
		int hourStart = Integer.parseInt(timeStart.split("h")[0]);
		String timeEnd = workTimeTime.split("-")[1];
		int hourEnd = Integer.parseInt(timeEnd.split("h")[0]);
		if ( (hourStart<= hour && hour <= hourEnd) || hourStart - 1 < hourCheckBook) {
			return false;
		} else {
			return true;
		}
    }
}
