package com.KMA.BookingCare.Api;

import com.KMA.BookingCare.Api.form.DeleteForm;
import com.KMA.BookingCare.Api.form.HolidayForm;
import com.KMA.BookingCare.Service.HolidayService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class HolidayApi {

    @Autowired
    private HolidayService holidayService;

    @PostMapping(value = "/holiday")
    public ResponseEntity<?> save(@RequestBody HolidayForm form) {
        if(form.getDate() == null) {
            return ResponseEntity.badRequest().body("Bạn vui lòng chọn ngày nghỉ để hoàn thành việc đăng kí nghỉ");
        }
        if(CollectionUtils.isEmpty(form.getWorkTimeIds())) {
            return ResponseEntity.badRequest().body("Bạn vui lòng chọn ca nghỉ để hoàn thành việc đăng kí nghỉ");
        }
        if(holidayService.isExistHolidayByDate(form.getDate())) {
            return ResponseEntity.badRequest().body("Ngày nghỉ đã tồn tại, vui lòng kiểm tra lại dữ liệu");
        }
        String errorMsg = holidayService.isExistMedicalByDateAndWorkTime(form);
        if (!Strings.isBlank(errorMsg)) {
            return ResponseEntity.badRequest().body(errorMsg);
        }
        holidayService.save(form);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/holiday/update")
    public ResponseEntity<?> update(@RequestBody HolidayForm form) {
        if(form.getDate() == null) {
            return ResponseEntity.badRequest().body("Bạn vui lòng chọn ngày nghỉ để hoàn thành việc đăng kí nghỉ");
        }
        if(CollectionUtils.isEmpty(form.getWorkTimeIds())) {
            return ResponseEntity.badRequest().body("Bạn vui lòng chọn ca nghỉ để hoàn thành việc cập nhật");
        }
        if(form.getDate() == null) {
            return ResponseEntity.badRequest().body("Bạn vui lòng chọn ngày nghỉ để hoàn thành việc cập nhật");
        }
        String errorMsg = holidayService.isExistMedicalByDateAndWorkTime(form);
        if (!Strings.isBlank(errorMsg)) {
            return ResponseEntity.badRequest().body(errorMsg);
        }
        holidayService.save(form);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/holiday/delete")
    public ResponseEntity<?> update(@RequestBody DeleteForm form) {
        holidayService.delete(form);
        return ResponseEntity.ok().build();
    }
}
