package com.KMA.BookingCare.ServiceImpl;

import com.KMA.BookingCare.Api.form.SalesReportFrom;
import com.KMA.BookingCare.Dto.*;
import com.KMA.BookingCare.Entity.CommentEntity;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Entity.WorkTimeEntity;
import com.KMA.BookingCare.Repository.HospitalRepository;
import com.KMA.BookingCare.Repository.MedicalExaminationScheduleRepository;
import com.KMA.BookingCare.Repository.UserRepository;
import com.KMA.BookingCare.Repository.WorkTimeRepository;
import com.KMA.BookingCare.Service.SaleReportService;
import com.KMA.BookingCare.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.*;
import static java.util.Comparator.*;

@Service
public class SaleReportServiceImpl implements SaleReportService {

    @Autowired
    private MedicalExaminationScheduleRepository medicalExaminationScheduleRepository;

    @Autowired
    private WorkTimeRepository workTimeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public ReportResponse getSaleReport(SalesReportFrom form) {
        if (Objects.equals(form.getTypeReport(), Constant.total_revenue_type_report)) {
            return getSaleReportByTotalRevenue(form);
        }

        if (Objects.equals(form.getTypeReport(), Constant.type_of_medical_examination_type_report)) {
            return getSaleReportByTypeOfMedical(form);
        }

        if (Objects.equals(form.getTypeReport(), Constant.revenue_by_hospital_type_report)) {
            return getSaleReportByHospital(form);
        }

        if (Objects.equals(form.getTypeReport(), Constant.revenue_by_doctor_type_report)) {
            return getSaleReportByDoctor(form);
        }
        return null;
    }

    private ReportResponse getSaleReportByTotalRevenue(SalesReportFrom form) {
        if (Objects.equals(form.getTimeReport(), Constant.last_week_time_report)
                ||
                Objects.equals(form.getTimeReport(), Constant.this_month_time_report)
                ||
                Objects.equals(form.getTimeReport(), Constant.last_month_time_report)) {
            LocalDate startDate = getDateStart(form.getTimeReport());
            LocalDate endDate = getEndDate(form.getTimeReport());
            return createReportResponseBetweenTwoDate(startDate, endDate, Constant.group_by_day);
        }
        if (Constant.today_time_report.equals(form.getTimeReport()) || Objects.equals(form.getTimeReport(), Constant.yesterday_time_report)) {
            LocalDate  date = LocalDate.now();
            if (Objects.equals(form.getTimeReport(), Constant.yesterday_time_report)) {
                date = date.minusDays(1);
            }
            return createReportResponseByOneDate(date);
        }else {
            long daysBetween = DAYS.between(form.getStartTimeReport(), form.getEndTimeReport());
            boolean isSameMont = form.getStartTimeReport().getMonth() == form.getEndTimeReport().getMonth();
            boolean isSameYear = form.getStartTimeReport().getYear() == form.getEndTimeReport().getYear();
            boolean isSameDay = form.getStartTimeReport().getDayOfMonth() == form.getEndTimeReport().getDayOfMonth();
            if (isSameDay && isSameMont && isSameYear) {
                createReportResponseByOneDate(form.getStartTimeReport());
            }
            if (daysBetween <= 30 || (isSameMont && isSameYear)) {
                return createReportResponseBetweenTwoDate(form.getStartTimeReport(), form.getEndTimeReport(),Constant.group_by_day);
            } else if (daysBetween <= 365) {
                return createReportResponseBetweenTwoDate(form.getStartTimeReport(), form.getEndTimeReport(),Constant.group_by_month);
            } else {
                return createReportResponseBetweenTwoDate(form.getStartTimeReport(), form.getEndTimeReport(),Constant.group_by_year);
            }
        }
    }

    private ReportResponse createReportResponseBetweenTwoDate(LocalDate startDate, LocalDate endDate, String groupBy) {
        ReportResponse reportResponse = new ReportResponse();
        OverviewReportDto reportBar = createReportTotalRevenueAll(startDate, endDate, groupBy);
        OverviewReportDto overviewReportDtoByTypeMedical = createReportTotalRevenueByTypeMedical(startDate, endDate, groupBy);
        OverviewReportDto reportDonut = createReportTotalRevenueByTypeMedicalForDonut(startDate, endDate);
        createInformationTotalSchedule(startDate, endDate, reportResponse);
        reportResponse.setArea(overviewReportDtoByTypeMedical);
        reportResponse.setBar(reportBar);
        reportResponse.setDonut(reportDonut);
        return reportResponse;
    }

    private ReportResponse createReportResponseByOneDate(LocalDate date) {
        ReportResponse reportResponse = new ReportResponse();
        OverviewReportDto reportBar = createReportTotalRevenueAll(date);
        OverviewReportDto reportArea = createReportTotalRevenueByTypeMedical(date);
        OverviewReportDto reportDonut = createReportTotalRevenueByTypeMedicalForDonut(date);
        createInformationTotalSchedule(date, reportResponse);
        reportResponse.setBar(reportBar);
        reportResponse.setArea(reportArea);
        reportResponse.setDonut(reportDonut);
        return reportResponse;
    }

    private OverviewReportDto createReportTotalRevenueByTypeMedical(LocalDate startDate, LocalDate endDate, String groupBy) {
        Map<String, Object> formResultOn = createFormContainResult(startDate, endDate, groupBy);
        Map<String, Object> formResultOff = createFormContainResult(startDate, endDate, groupBy);
        String formatTime = createFormatTimeForSQL(groupBy);
        List<Map<String, Object>> reportByTypeOn = medicalExaminationScheduleRepository.getSaleReportByDateAndTypeMedicalGroupByDate(startDate, endDate, "on", formatTime);
        List<Map<String, Object>> reportByTypeOff = medicalExaminationScheduleRepository.getSaleReportByDateAndTypeMedicalGroupByDate(startDate, endDate, "off", formatTime);
        mapperToFormContainResult(formResultOn, reportByTypeOn);
        mapperToFormContainResult(formResultOff, reportByTypeOff);
        OverviewReportDto overviewReportDtoByType = createResponse(formResultOn, formResultOff, "", groupBy);
        overviewReportDtoByType.setCharType("area");
        overviewReportDtoByType.getSeries().get(0).setName("On");
        overviewReportDtoByType.getSeries().get(1).setName("Off");
        return overviewReportDtoByType;
    }

    private OverviewReportDto createReportTotalRevenueByTypeMedical(LocalDate date) {
        Map<String, Object> formResultOn = createFormContainResultByWorkTime();
        Map<String, Object> formResultOff = createFormContainResultByWorkTime();
        List<Map<String, Object>> reportByTypeOn = medicalExaminationScheduleRepository.getSaleReportByOneDateAndTypeMedicalGroupByWorkTimeId(date, "on");
        List<Map<String, Object>> reportByTypeOff = medicalExaminationScheduleRepository.getSaleReportByOneDateAndTypeMedicalGroupByWorkTimeId(date, "off");
        mapperToFormContainResult(formResultOn, reportByTypeOn);
        mapperToFormContainResult(formResultOff, reportByTypeOff);
        OverviewReportDto overviewReportDtoByType = createResponse(formResultOn, formResultOff, "work_time", "");
        overviewReportDtoByType.setCharType("area");
        overviewReportDtoByType.getSeries().get(0).setName("On");
        overviewReportDtoByType.getSeries().get(1).setName("Off");
        return overviewReportDtoByType;
    }

    private OverviewReportDto createReportTotalRevenueByTypeMedicalForDonut(LocalDate date) {
        Long totalPriceOn = medicalExaminationScheduleRepository.getTotalPriceByOneDateAndTypeMedical(date, "on");
        Long totalPriceOff = medicalExaminationScheduleRepository.getTotalPriceByOneDateAndTypeMedical(date, "off");
        OverviewReportDto overviewReportDto = new OverviewReportDto();
        double percentOn = 0.0;
        double percentOf = 0.0;
        if (totalPriceOff != 0 || totalPriceOn != 0) {
            percentOn = 100 * Double.valueOf(totalPriceOn) / (totalPriceOn + totalPriceOff);
            percentOf = 100 - percentOn;
        }

        Series seriesOn = new Series();
        seriesOn.setDatas(List.of(percentOn));
        seriesOn.setName("On");

        Series seriesOff = new Series();
        seriesOff.setDatas(List.of(percentOf));
        seriesOff.setName("Off");
        overviewReportDto.setSeries(List.of(seriesOn, seriesOff));
        return overviewReportDto;
    }

    private OverviewReportDto createReportTotalRevenueByTypeMedicalForDonut(LocalDate startDate, LocalDate endDate) {
        Long totalPriceOn = medicalExaminationScheduleRepository.getTotalPriceByDateAndTypeMedicalGroupByDate(startDate, endDate, "on");
        Long totalPriceOff = medicalExaminationScheduleRepository.getTotalPriceByDateAndTypeMedicalGroupByDate(startDate, endDate, "off");
        OverviewReportDto overviewReportDto = new OverviewReportDto();
        double percentOn = 0.0;
        double percentOf = 0.0;
        if (totalPriceOn != 0 || totalPriceOff != 0) {
            percentOn = 100 * Double.valueOf(totalPriceOn) / (totalPriceOn + totalPriceOff);
            percentOf = 100 - percentOn;
        }

        Series seriesOn = new Series();
        seriesOn.setDatas(List.of(percentOn));
        seriesOn.setName("On");

        Series seriesOff = new Series();
        seriesOff.setDatas(List.of(percentOf));
        seriesOff.setName("Off");
        overviewReportDto.setSeries(List.of(seriesOn, seriesOff));
        return overviewReportDto;
    }

    private OverviewReportDto createReportTotalRevenueAll(LocalDate startDate, LocalDate endDate, String groupBy) {
        Map<String, Object> formResult = createFormContainResult(startDate, endDate, groupBy);
        String formatTime = createFormatTimeForSQL(groupBy);
        List<Map<String, Object>> reports = medicalExaminationScheduleRepository.getSaleReportByDateAndGroupByDate(startDate, endDate, formatTime);
        mapperToFormContainResult(formResult, reports);
        OverviewReportDto overviewReportDto = createResponse(formResult, "", groupBy);
        overviewReportDto.setCharType("bar");
        overviewReportDto.getSeries().get(0).setName("price");
        return overviewReportDto;
    }

    private String createFormatTimeForSQL(String groupBy) {
        String formatTime = "%Y-%m-%d";
        if (groupBy.equals(Constant.group_by_month)) {
            formatTime = "%Y-%m";
        }
        if (groupBy.equals(Constant.group_by_year)) {
            formatTime = "%Y";
        }
        return formatTime;
    }

    private DateTimeFormatter createDateTimeFormat(String groupBy) {
        String formatTime = "yyyy-MM-dd";
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern(formatTime)
                .toFormatter();
        if (groupBy.equals(Constant.group_by_month)) {
            formatTime = "yyyy-MM";
            formatter = new DateTimeFormatterBuilder()
                    .appendPattern(formatTime)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter();
        }
        if (groupBy.equals(Constant.group_by_year)) {
            formatTime = "yyyy";
            formatter = new DateTimeFormatterBuilder()
                    .appendPattern(formatTime)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                    .toFormatter();
        }
        return formatter;
    }

    private OverviewReportDto createReportTotalRevenueAll(LocalDate date) {
        Map<String, Object> formResult = createFormContainResultByWorkTime();
        List<Map<String, Object>> reports = medicalExaminationScheduleRepository.getSaleReportByOneDateAndGroupByWorkTimeId(date);
        mapperToFormContainResult(formResult, reports);
        OverviewReportDto overviewReportDto = createResponse(formResult, "work_time", "");
        overviewReportDto.setCharType("bar");
        overviewReportDto.getSeries().get(0).setName("price");
        return overviewReportDto;
    }

    private ReportResponse getSaleReportByTypeOfMedical(SalesReportFrom form) {
        LocalDate startDate = getDateStart(form.getTimeReport());
        LocalDate endDate = getEndDate(form.getTimeReport());
        if (Objects.equals(form.getTimeReport(), Constant.time_option_time_report)) {
            startDate = form.getStartTimeReport();
            endDate = form.getEndTimeReport();
        }
        List<Map<String, Object>> result = medicalExaminationScheduleRepository.getSaleReportGroupByTypeMedical(startDate, endDate);
        TableReportDto tableReportDto = convertToTableReportByTypeMedical(result);
        OverviewReportDto reportDto = createChartDonutTable(result);
        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setTable(tableReportDto);
        Long totalMedical = tableReportDto.getRecords().stream().reduce(0L, (subtotal, element) -> subtotal + element.getTotalSchedule(), Long::sum);
        Long totalPrice = tableReportDto.getRecords().stream().reduce(0L, (subtotal, element) -> subtotal + element.getPrice(), Long::sum);
        reportResponse.setTotalMedical(totalMedical);
        reportResponse.setTotalPrice(totalPrice);
        reportResponse.setDonut(reportDto);
        return reportResponse;
    }

    private ReportResponse getSaleReportByHospital(SalesReportFrom form) {
        LocalDate startDate = getDateStart(form.getTimeReport());
        LocalDate endDate = getEndDate(form.getTimeReport());
        if (Objects.equals(form.getTimeReport(), Constant.time_option_time_report)) {
            startDate = form.getStartTimeReport();
            endDate = form.getEndTimeReport();
        }
        List<Map<String, Object>> result = medicalExaminationScheduleRepository.getSaleReportGroupByHospital(startDate, endDate);
        List<String> hospitalNames = result.stream().map(e -> String.valueOf(e.get("hospitalName"))).collect(Collectors.toList());
        List<HospitalDto> hospitalDtos = hospitalRepository.findAllNotContainHospitalName(hospitalNames);
        TableReportDto tableReportDto = convertToTableReportByHospital(result, hospitalDtos);
        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setTable(tableReportDto);
        Long totalMedical = tableReportDto.getRecords().stream().reduce(0L, (subtotal, element) -> subtotal + element.getTotalSchedule(), Long::sum);
        Long totalPrice = tableReportDto.getRecords().stream().reduce(0L, (subtotal, element) -> subtotal + element.getPrice(), Long::sum);
        reportResponse.setTotalMedical(totalMedical);
        reportResponse.setTotalPrice(totalPrice);
        return reportResponse;
    }

    private ReportResponse getSaleReportByDoctor(SalesReportFrom form) {
        LocalDate startDate = getDateStart(form.getTimeReport());
        LocalDate endDate = getEndDate(form.getTimeReport());
        if (Objects.equals(form.getTimeReport(), Constant.time_option_time_report)) {
            startDate = form.getStartTimeReport();
            endDate = form.getEndTimeReport();
        }
        List<Map<String, Object>> result = medicalExaminationScheduleRepository.getSaleReportGroupByDoctorId(startDate, endDate);
        List<Long> doctorIdNotHasSchedules = result.stream().map(e -> (Long) e.get("doctorId")).collect(Collectors.toList());
        List<UserEntity> doctorNotSchedules = userRepository.findAllDoctorNotContainIds(doctorIdNotHasSchedules);
        TableReportDto tableReportDto = convertToTableReport(result, doctorNotSchedules);
        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setTable(tableReportDto);
        Long totalMedical = tableReportDto.getRecords().stream().reduce(0L, (subtotal, element) -> subtotal + element.getTotalSchedule(), Long::sum);
        Long totalPrice = tableReportDto.getRecords().stream().reduce(0L, (subtotal, element) -> subtotal + element.getPrice(), Long::sum);
        reportResponse.setTotalMedical(totalMedical);
        reportResponse.setTotalPrice(totalPrice);
        return reportResponse;
    }

    private LocalDate getDateStart(Integer type) {
        Calendar calendar = Calendar.getInstance();
        if (Objects.equals(type, Constant.last_week_time_report)) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 7);
            return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        if (Objects.equals(type, Constant.this_month_time_report)) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        if (Objects.equals(type, Constant.last_month_time_report)) {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) -1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if (Objects.equals(type, Constant.yesterday_time_report)) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
            return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalDate getEndDate(Integer type) {
        Calendar calendar = Calendar.getInstance();
        if (Objects.equals(type, Constant.last_month_time_report)) {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) -1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if (Objects.equals(type, Constant.yesterday_time_report)) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
            return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Map<String, Object> createFormContainResult(LocalDate startDate, LocalDate endDate, String groupBy) {
        if (groupBy.equals(Constant.group_by_day)) {
            long numOfDays = DAYS.between(startDate, endDate) + 1;
            Map<String, Object> result = Stream.iterate(startDate, date -> date.plusDays(1))
                    .limit(numOfDays)
                    .map(e -> e.toString())
                    .collect(Collectors.toMap(s->s, s-> 0));
            return result;
        }

        if (groupBy.equals(Constant.group_by_month)) {
            long numOfMonths = MONTHS.between(startDate, endDate) + 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            Map<String, Object> result = Stream.iterate(startDate, date -> date.plusMonths(1))
                    .limit(numOfMonths)
                    .map(e -> e.format(formatter))
                    .collect(Collectors.toMap(s->s, s-> 0));
            return result;
        }

        if (groupBy.equals(Constant.group_by_year)) {
            long numOfYears = endDate.getYear() - startDate.getYear() + 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
            Map<String, Object> result = Stream.iterate(startDate, date -> date.plusYears(1))
                    .limit(numOfYears)
                    .map(e -> e.format(formatter))
                    .collect(Collectors.toMap(s->s, s-> 0));
            return result;
        }
        return new HashMap<>();
    }

    private Map<String, Object> createFormContainResultByWorkTime() {
        List<WorkTimeEntity> workTimeEntities = workTimeRepository.findAll(Sort.by("id"));
        Map<String, Object> result = workTimeEntities
                .stream()
                .map(WorkTimeEntity::getTime)
                .collect(Collectors.toMap(s-> s, s-> 0));
        return result;
    }

    private Map<String, Object> mapperToFormContainResult(Map<String, Object> form, List<Map<String, Object>> reports) {
       for (Map<String, Object> item : reports) {
           if (!form.containsKey(item.get("time"))) continue;
           form.put(String.valueOf(item.get("time")), item.get("price"));
       }
        return form;
    }

    private OverviewReportDto createResponse(Map<String, Object> form, String typeCategory, String groupBy) {
        OverviewReportDto report = new OverviewReportDto();
        Xaxis xaxis = new Xaxis();
        List<String> categories;
        if (!"work_time".equals(typeCategory)) {
            DateTimeFormatter formatter = createDateTimeFormat(groupBy);
            categories = new ArrayList<>(form.keySet());
            categories.sort(comparing(o -> LocalDate.parse(o, formatter)));
        } else {
            categories = workTimeRepository.findAll(Sort.by("id")).stream().map(WorkTimeEntity::getTime).collect(Collectors.toList());
        }
        xaxis.setCategories(categories);
        report.setXaxis(xaxis);

        Series series = new Series();
        List<Object> data = categories.stream()
                .map(e -> form.get(e))
                .collect(Collectors.toList());
        series.setDatas(data);
        report.setSeries(List.of(series));
        return report;
    }

    private OverviewReportDto createResponse(Map<String, Object> formOn, Map<String, Object> formOff, String typeCategory, String groupBy) {
        OverviewReportDto report = new OverviewReportDto();
        Xaxis xaxis = new Xaxis();
        List<String> categories;
        if (!"work_time".equals(typeCategory)) {
            DateTimeFormatter formatter = createDateTimeFormat(groupBy);
            categories = formOn.keySet().stream().collect(Collectors.toList());
            categories.sort(comparing(o -> LocalDate.parse(o, formatter)));
        } else {
            categories = workTimeRepository.findAll(Sort.by("id")).stream().map(WorkTimeEntity::getTime).collect(Collectors.toList());
        }
        xaxis.setCategories(categories);
        report.setXaxis(xaxis);

        Series series = new Series();
        List<Object> data = categories.stream()
                .map(e -> formOn.get(e))
                .collect(Collectors.toList());
        series.setDatas(data);

        Series seriesOff = new Series();
        List<Object> dataOff = categories.stream()
                .map(e -> formOff.get(e))
                .collect(Collectors.toList());
        seriesOff.setDatas(dataOff);
        report.setSeries(List.of(series, seriesOff));
        return report;
    }

    private void createInformationTotalSchedule(LocalDate startDate, LocalDate endDate, ReportResponse reportResponse) {
        Long totalScheduleIsComplete = medicalExaminationScheduleRepository.getTotalScheduleBetweenTwoDateAndStatus(startDate, endDate, List.of(Constant.MEDICAL_SCHEDULE_IS_COMPLETE));
        Long totalScheduleIsWait = medicalExaminationScheduleRepository.getTotalScheduleBetweenTwoDateAndStatus(startDate, endDate, List.of(Constant.MEDICAL_SCHEDULE_IS_WAITING));
        Long totalScheduleIsCancel = medicalExaminationScheduleRepository.getTotalScheduleBetweenTwoDateAndStatus(startDate, endDate, List.of(Constant.MEDICAL_SCHEDULE_IS_CANCEL));
        Long totalSchedule = medicalExaminationScheduleRepository.getTotalScheduleBetweenTwoDateAndStatus(startDate, endDate, List.of(Constant.MEDICAL_SCHEDULE_IS_WAITING, Constant.MEDICAL_SCHEDULE_IS_CANCEL, Constant.MEDICAL_SCHEDULE_IS_COMPLETE));
        reportResponse.setTotalMedical(totalSchedule);
        reportResponse.setTotalMedicalComplete(totalScheduleIsComplete);
        reportResponse.setTotalMedicalCancel(totalScheduleIsCancel);
        reportResponse.setTotalMedicalWait(totalScheduleIsWait);
    }

    private void createInformationTotalSchedule(LocalDate date, ReportResponse reportResponse) {
        Long totalScheduleIsComplete = medicalExaminationScheduleRepository.getTotalScheduleByOneDateAndStatus(date, List.of(2));
        Long totalScheduleIsWait = medicalExaminationScheduleRepository.getTotalScheduleByOneDateAndStatus(date, List.of(0));
        Long totalScheduleIsCancel = medicalExaminationScheduleRepository.getTotalScheduleByOneDateAndStatus(date, List.of(1));
        Long totalSchedule = medicalExaminationScheduleRepository.getTotalScheduleByOneDateAndStatus(date, List.of(0, 1, 2));
        reportResponse.setTotalMedical(totalSchedule);
        reportResponse.setTotalMedicalComplete(totalScheduleIsComplete);
        reportResponse.setTotalMedicalCancel(totalScheduleIsCancel);
        reportResponse.setTotalMedicalWait(totalScheduleIsWait);
    }

    private TableReportDto convertToTableReport(List<Map<String, Object>> result, List<UserEntity> doctorNotSchedules) {
        TableReportDto tableReportDto = new TableReportDto();
        List<RecordTableReportDto> records = result.stream().map(e -> RecordTableReportDto.builder()
                .id((Long) e.get("doctorId"))
                .title(String.valueOf(e.get("fullName")))
                .price((Long) e.get("price"))
                .totalSchedule((Long) e.get("totalSchedule")).build())
                .collect(Collectors.toList());
        List<RecordTableReportDto> recordDoctors = doctorNotSchedules.stream().map(e -> RecordTableReportDto.builder()
                .id(e.getId())
                .title(e.getFullName())
                .price(0L)
                .totalSchedule(0L)
                .build())
                .collect(Collectors.toList());
        records.addAll(recordDoctors);
        tableReportDto.setRecords(records);
        return tableReportDto;
    }

    private TableReportDto convertToTableReportByHospital(List<Map<String, Object>> result, List<HospitalDto> hospitalDtos) {
        TableReportDto tableReportDto = new TableReportDto();
        List<RecordTableReportDto> records = result.stream().map(e -> RecordTableReportDto.builder()
                        .title(String.valueOf(e.get("hospitalName")))
                        .price((Long) e.get("price"))
                        .totalSchedule((Long) e.get("totalSchedule")).build())
                .collect(Collectors.toList());
        List<RecordTableReportDto> recordDoctors = hospitalDtos.stream().map(e -> RecordTableReportDto.builder()
                        .title(e.getName())
                        .price(0L)
                        .totalSchedule(0L)
                        .build())
                .collect(Collectors.toList());
        records.addAll(recordDoctors);
        tableReportDto.setRecords(records);
        return tableReportDto;
    }

    private TableReportDto convertToTableReportByTypeMedical(List<Map<String, Object>> result) {
        TableReportDto tableReportDto = new TableReportDto();
        List<RecordTableReportDto> records = result.stream().map(e -> RecordTableReportDto.builder()
                        .title("on".equalsIgnoreCase(e.get("type") != null ? e.get("type").toString() : "") ? "Khám online" : "Khám tại cơ sở")
                        .price((Long) e.get("price"))
                        .totalSchedule((Long) e.get("totalSchedule")).build())
                .collect(Collectors.toList());
        if (records.size() == 1) {
            Optional<RecordTableReportDto> reportDto = records.stream().filter(e -> "Khám online".equalsIgnoreCase(e.getTitle())).findFirst();
            if (reportDto.isPresent()) {
                RecordTableReportDto record = RecordTableReportDto.builder().title("Khám tại cơ sở").price(0L).totalSchedule(0L).build();
                records.add(record);
            } else {
                RecordTableReportDto record = RecordTableReportDto.builder().title("Khám online").price(0L).totalSchedule(0L).build();
                records.add(record);

            }
        }
        tableReportDto.setRecords(records);
        return tableReportDto;
    }

    private OverviewReportDto createChartDonutTable(List<Map<String, Object>> result) {
        if (CollectionUtils.isEmpty(result)) return null;
        OverviewReportDto overviewReportDto = new OverviewReportDto();
        double percentOn = 0.0;
        double percentOf = 0.0;
        Optional<Map<String, Object>> off = result.stream().filter(e -> "off".equalsIgnoreCase(e.get("type") != null ? e.get("type").toString() : "")).findFirst();
        Optional<Map<String, Object>> on = result.stream().filter(e -> "on".equalsIgnoreCase(e.get("type") != null ? e.get("type").toString() : "")).findFirst();
        Long totalPriceOff = off.isPresent() ? (Long) off.get().get("price") : 0L;
        Long totalPriceOn = on.isPresent() ? (Long) on.get().get("price") : 0L;
        if (totalPriceOff != 0 || totalPriceOn != 0) {
            percentOn = 100 * Double.valueOf(totalPriceOn) / (totalPriceOn + totalPriceOff);
            percentOf = 100 - percentOn;
        }

        Series seriesOn = new Series();
        seriesOn.setDatas(List.of(percentOn));
        seriesOn.setName("On");

        Series seriesOff = new Series();
        seriesOff.setDatas(List.of(percentOf));
        seriesOff.setName("Off");
        overviewReportDto.setSeries(List.of(seriesOn, seriesOff));
        return overviewReportDto;
    }

}
