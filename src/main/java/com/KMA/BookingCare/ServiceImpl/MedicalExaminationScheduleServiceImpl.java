package com.KMA.BookingCare.ServiceImpl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.KMA.BookingCare.Api.form.ChangeTimeCloseForm;
import com.KMA.BookingCare.Api.form.UploadMedicalRecordsForm;
import com.KMA.BookingCare.Dto.*;
import com.KMA.BookingCare.Entity.WorkTimeEntity;
import com.KMA.BookingCare.Repository.WorkTimeRepository;
import com.KMA.BookingCare.Service.MessageService;
import com.KMA.BookingCare.common.Constant;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.KMA.BookingCare.Api.form.BookingForm;
import com.KMA.BookingCare.Entity.MedicalExaminationScheduleEntity;
import com.KMA.BookingCare.Entity.UserEntity;
import com.KMA.BookingCare.Mapper.MedicalMapper;
import com.KMA.BookingCare.Repository.MedicalExaminationScheduleRepository;
import com.KMA.BookingCare.Repository.UserRepository;
import com.KMA.BookingCare.Service.MedicalExaminationScheduleService;
import org.springframework.util.CollectionUtils;

@Service
public class MedicalExaminationScheduleServiceImpl implements MedicalExaminationScheduleService {

	@Autowired
	private MedicalExaminationScheduleRepository medicalRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private Cloudinary cloudinary;

	@Autowired
	private WorkTimeRepository workTimeRepository;

	@Autowired
	private InteractiveServiceImpl interactiveService;

	@Autowired
	private MessageService messageService;
	
	@Override
	public Long  save(BookingForm form) throws JsonProcessingException {
		MedicalExaminationScheduleEntity entity= new MedicalExaminationScheduleEntity();
		entity.setDate(form.getDate());
		entity.setLocation(form.getLocation());
		entity.setNamePatient(form.getNamePatient());
		entity.setNameScheduler(form.getNameScheduler());
		entity.setPhonePatient(form.getPhonePatient());
		entity.setPhoneScheduer(form.getPhoneScheduer());
		entity.setReason(form.getReason());
		entity.setSex(form.getSex());
		entity.setWorkTimeID(form.getIdWorktime());
		UserEntity userEntity= userRepository.findOneById(form.getIdDoctor());
		entity.setHospitalName(userEntity.getHospital().getName());
		entity.setDoctor(userEntity);
		entity.setExaminationPrice(userEntity.getExaminationPrice());
		entity.setYearOfBirth(form.getYearOfBirth());
		entity.setStatus(1);
		entity.setType(form.getType() == null ? "off" : form.getType());
		if(form.getUserId() != null) {
			UserEntity userEntity2= userRepository.findOneById(form.getUserId());
			entity.setUser(userEntity2);
		}
		entity.setStatusPayment(form.getStatusPayment() == null ? Constant.payment_unPaid : form.getStatusPayment());
		entity.setCreatedDate(new Date());
		entity = medicalRepository.save(entity);
//		sendKafkaNotification(entity, Constant.NOTIFICATION_TYPE_USER_BOOKING_SCHEDULE);
		return entity.getId();
	}

	@Override
	public Long saveForMobile(BookingForm form) throws JsonProcessingException {
		MedicalExaminationScheduleEntity entity= new MedicalExaminationScheduleEntity();
		entity.setDate(form.getDate());
		entity.setLocation(form.getLocation());
		entity.setNamePatient(form.getNamePatient());
		entity.setNameScheduler(form.getNameScheduler());
		entity.setPhonePatient(form.getPhonePatient());
		entity.setPhoneScheduer(form.getPhoneScheduer());
		entity.setReason(form.getReason());
		entity.setSex(form.getSex());
		entity.setWorkTimeID(form.getIdWorktime());
		UserEntity userEntity= userRepository.findOneById(form.getIdDoctor());
		entity.setHospitalName(userEntity.getHospital().getName());
		entity.setDoctor(userEntity);
		entity.setExaminationPrice(userEntity.getExaminationPrice());
		entity.setYearOfBirth(form.getYearOfBirth());
		entity.setStatus(1);
		entity.setType(form.getType() == null ? "off" : form.getType());
		if(form.getUserId() != null) {
			UserEntity userEntity2= userRepository.findOneById(form.getUserId());
			entity.setUser(userEntity2);
		}
		entity.setStatusPayment(form.getStatusPayment() == null ? Constant.payment_unPaid : form.getStatusPayment());
		entity.setCreatedDate(new Date());
		entity = medicalRepository.save(entity);
		if("on".equals(entity.getType())) {
			sendMessage(entity);
		}
//		sendKafkaNotification(entity, Constant.NOTIFICATION_TYPE_USER_BOOKING_SCHEDULE);
		return entity.getId();
	}

	private void sendMessage(MedicalExaminationScheduleEntity entity) {
		WorkTimeEntity wk = workTimeRepository.findById(entity.getWorkTimeID()).get();
		StringBuilder content= new StringBuilder("Lịch khám online vào ngày: ");
		content.append(entity.getDate());
		content.append(" , khoảng thời gian: ");
		content.append(wk.getTime());

		MessageDto messageDoctor= new MessageDto();
		messageDoctor.setContent(content.toString());
		messageDoctor.setSenderId(entity.getUser().getId());
		messageDoctor.setReceiverId(entity.getDoctor().getId());

		MessageDto messageUser= new MessageDto();
		messageUser.setContent(content.toString());
		messageUser.setSenderId(entity.getDoctor().getId());
		messageUser.setReceiverId(entity.getUser().getId());

		interactiveService.saveOrUpdate(messageDoctor);
		messageService.save(messageDoctor);
	}

	@Override
	public List<MedicalExaminationScheduleDto> findAllByStatus(Integer status) {
		List<MedicalExaminationScheduleEntity> lstEntity = medicalRepository.findAllByStatus(status);
		List<MedicalExaminationScheduleDto> lstDto = new ArrayList<>();
		for(MedicalExaminationScheduleEntity entity : lstEntity) {
			MedicalExaminationScheduleDto dto = MedicalMapper.convertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public List<MedicalExaminationScheduleDto> findAllByStatusPage(Integer status, Pageable pageable) {
		Page<MedicalExaminationScheduleEntity> page = medicalRepository.findAllByStatusPage(status, pageable);
		List<MedicalExaminationScheduleEntity> lst = page.getContent();
		List<MedicalExaminationScheduleDto> lstDto = new ArrayList<>();
		for(MedicalExaminationScheduleEntity entity : lst) {
			MedicalExaminationScheduleDto dto = MedicalMapper.convertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public Integer getToTalByStatusPage(Integer status, Pageable pageable) {
		Page<MedicalExaminationScheduleEntity> page = medicalRepository.findAllByStatusPage(status, pageable);
		return page.getTotalPages();
	}

	@Override
	public List<MedicalExaminationScheduleDto> findAllByDoctorIdAndStatus(Long doctorID, Integer Status) {
		List<MedicalExaminationScheduleEntity> lstEntity = medicalRepository.findAllByDoctorIdAndStatus(doctorID, Status);
		List<MedicalExaminationScheduleDto> lstDto = new ArrayList<MedicalExaminationScheduleDto>();
		for(MedicalExaminationScheduleEntity entity : lstEntity) {
			MedicalExaminationScheduleDto dto = MedicalMapper.convertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public List<MedicalExaminationScheduleDto> findAllByDoctorIdAndStatusPage(Long doctorID, Integer Status, Pageable pageable) {
		Page<MedicalExaminationScheduleEntity> pages = medicalRepository.findAllByDoctorIdAndStatusAndPage(doctorID, Status, pageable);
		List<MedicalExaminationScheduleEntity> lstEntity = pages.getContent();
		List<MedicalExaminationScheduleDto> lstDto = new ArrayList<>();
		for(MedicalExaminationScheduleEntity entity : lstEntity) {
			MedicalExaminationScheduleDto dto = MedicalMapper.convertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public Integer getTotalByDoctorIdAndStatusPage(Long doctorID, Integer Status, Pageable pageable) {
		Page<MedicalExaminationScheduleEntity> pages = medicalRepository.findAllByDoctorIdAndStatusAndPage(doctorID, Status, pageable);
		return pages.getTotalPages();
	}

	@Override
	public void updateMedicalByStatus(Integer status,List<String> ids) {
		medicalRepository.updateByStatus(status,ids);
		if (status == Constant.MEDICAL_SCHEDULE_IS_CANCEL) {
			NotificationScheduleDTO dto = new NotificationScheduleDTO();
			dto.setIds(ids.stream().map(Long::parseLong).collect(Collectors.toList()));
			dto.setTypeNotification(Constant.NOTIFICATION_TYPE_CANCEL_MEDICAL);
			try {
				sendKafkaNotificationCancel(dto);
			} catch (JsonProcessingException e) {
				System.err.println("Cannot send message kafka when cancel medical");
			}
		}
	}

	@Override
	public List<MedicalExaminationScheduleDto> findAllByUserIdAndStatus(Long userId, Integer status) {
		List<MedicalExaminationScheduleEntity> lstEntity = medicalRepository.findAllByUserIdAndStatus(userId, status);
		List<MedicalExaminationScheduleDto> lstDto = new ArrayList<>();
		for(MedicalExaminationScheduleEntity entity : lstEntity) {
			MedicalExaminationScheduleDto dto = MedicalMapper.convertToDto(entity);
			lstDto.add(dto);
		}
		return lstDto;
	}

	@Override
	public boolean changTimeClose(ChangeTimeCloseForm changeTimeCloseForm) throws JsonProcessingException {
		Long idWk = changeTimeCloseForm.getIdWk();
		MedicalExaminationScheduleEntity itemUpdate = medicalRepository.findById(changeTimeCloseForm.getIdMedical()).get();
		if(!validateChangTimeClose(changeTimeCloseForm, itemUpdate)) return false;
		String date = "";
		String dateOld = "";
		Long count = medicalRepository.countMedicalWhenChangeWk(
				itemUpdate.getDate(),itemUpdate.getDoctor().getId(), itemUpdate.getWorkTimeID(), idWk
		);
		List<MedicalExaminationScheduleEntity> lstUpdate = new ArrayList<>();

		if(count == 0) {
			//update
			itemUpdate.setWorkTimeID(idWk);
			medicalRepository.save(itemUpdate);
			sendKafkaNotification(itemUpdate, Constant.NOTIFICATION_TYPE_CHANGE_SCHEDULE);
		} else {
			// lst medical, id > changTimeClose.idMedical
			boolean isDataOfNewDate = false;
			do {
				List<MedicalExaminationScheduleEntity> lst;
				if (Strings.isEmpty(date)) {
					// lst get theo id
					lst = medicalRepository.findAllByDateAndDoctorId(itemUpdate.getDate(), itemUpdate.getDoctor().getId(), itemUpdate.getWorkTimeID());
				}else {
					// lst get theo id va date
					if (date.equals(dateOld)) break;
					lst = medicalRepository.findAllByDateAndDoctorId(date, itemUpdate.getDoctor().getId());
					dateOld = date;
					isDataOfNewDate = true;
				}

				if(CollectionUtils.isEmpty(lst)) break;
				for (MedicalExaminationScheduleEntity item : lst) {
					if (item.getWorkTimeID() == idWk) {
						break;
					}
					if (item.getWorkTimeID() < idWk ) {
						if (idWk <= 8 ) {
							// truong hop tang nhưng chua vuot qua ca cuoi ngay
							item.setWorkTimeID(idWk);
							idWk = idWk +1;
						} else {
							// truong hop vuot qua ca cuoi ngay
							//cong ngay
							isDataOfNewDate = false;
							if(Strings.isEmpty(date) || Strings.isBlank(date)) {
								date = plusDate(itemUpdate.getDate());
							} else {
								date = plusDate(date);
							}
							idWk = 1L;
							item.setWorkTimeID(idWk);
							item.setDate(date);
							idWk ++;
						}
						lstUpdate.add(item);
					}
					else {
						if(isDataOfNewDate) break;
						if(!Strings.isEmpty(date)) {
							item.setWorkTimeID(idWk);
							item.setDate(date);
							idWk = idWk +1;
						} else {
							date = "";
							break;
						}
					}
				}
			} while (!Strings.isBlank(date));
			if (!CollectionUtils.isEmpty(lstUpdate)) {
				medicalRepository.saveAll(lstUpdate);
				sendKafkaNotification(lstUpdate, Constant.NOTIFICATION_TYPE_CHANGE_SCHEDULE);
			}
		}
		return  true;
	}

	@Override
	public void handleSendMedicalRecords(UploadMedicalRecordsForm form) {
		try {
			Map result= cloudinary.uploader().upload(form.getMedicalRecords().getBytes(),
					ObjectUtils.asMap("resource_type","auto"));
			String url = (String) result.get("secure_url");
			NotificationScheduleDTO notificationSchedule = new NotificationScheduleDTO();
			notificationSchedule.setIds(List.of(form.getMedicalId()));
			notificationSchedule.setTypeNotification(Constant.NOTIFICATION_TYPE_SEND_MEDICAL_RECORDS);
			notificationSchedule.setFile(url);
			sendKafkaNotification(notificationSchedule);
		} catch (Exception e) {
			System.out.println("upload img fail");
		}
	}

	@Override
	public Optional<MedicalExaminationScheduleEntity> findOneById(Long id) {
		return medicalRepository.findById(id);
	}

	@Override
	public void update(MedicalExaminationScheduleEntity entity) {
		medicalRepository.save(entity);
	}

	@Override
	public void update(BookingForm form) throws JsonProcessingException {
		MedicalExaminationScheduleEntity entity = medicalRepository.findById(form.getId()).get();
		entity.setPhoneScheduer(form.getPhoneScheduer());
		entity.setPhonePatient(form.getPhonePatient());
		entity.setNamePatient(form.getNamePatient());
		entity.setNameScheduler(form.getNameScheduler());
		entity.setSex(form.getSex());
		entity.setType(form.getType());
		entity.setReason(form.getReason());
		entity.setYearOfBirth(form.getYearOfBirth());
		entity.setLocation(form.getLocation());
		entity.setStatusPayment(form.getStatusPayment());
		medicalRepository.save(entity);
	}

	@Override
	public void updateTime(BookingForm form) {
		MedicalExaminationScheduleEntity entity = medicalRepository.findById(form.getId()).get();
		UserEntity user = userRepository.findOneById(form.getIdDoctor());
		NotificationChangeTimeDTO dto = createNotification(entity, user, form.getDate(), form.getIdWorktime());
		entity.setDoctor(user);
		entity.setExaminationPrice(user.getExaminationPrice());
		entity.setDate(form.getDate());
		entity.setWorkTimeID(form.getIdWorktime());
		entity.setHospitalName(user.getHospital().getName());
		medicalRepository.save(entity);
		sendNotification(dto);
	}

	private NotificationChangeTimeDTO createNotification(MedicalExaminationScheduleEntity medical, UserEntity doctorNew, String date, Long idWorktime) {
		NotificationChangeTimeDTO dto = new NotificationChangeTimeDTO();
		dto.setDate(date);
		dto.setNewDoctorName(doctorNew.getFullName());
		dto.setNewDoctorEmail(doctorNew.getEmail());
		dto.setOldDoctorName(medical.getDoctor().getFullName());
		dto.setOlrDoctorEmail(medical.getDoctor().getEmail());
		String wk = doctorNew.getWorkTimeEntity()
				.stream()
				.filter(e -> Objects.equals(e.getId(), idWorktime))
				.findFirst()
				.get()
				.getName();
		dto.setWorkTime(wk);
		dto.setUserName(medical.getUser().getFullName());
		dto.setUserEmail(medical.getUser().getEmail());
		dto.setHospitalName(doctorNew.getHospital().getName());
		return dto;
	}

	private void sendNotification(NotificationChangeTimeDTO dto) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json ;
		try {
			json = ow.writeValueAsString(dto);
			kafkaTemplate.send(Constant.NOTIFICATION_CHANGE_TIME_TOPIC, json);
		} catch (JsonProcessingException e) {
			System.out.println("Convert to json to send kafka error");
		}

	}

	@Override
	public Optional<MedicalExaminationScheduleEntity> findOneByIdAndUserId(Long medicalId, Long userId) {
		return medicalRepository.findOneByMedicalIdAndUserId(medicalId, userId);
	}

	@Override
	public void cancelMedical(Long medicalId) {
		medicalRepository.updateByStatus(Constant.MEDICAL_SCHEDULE_IS_CANCEL, List.of(String.valueOf(medicalId)));
		NotificationScheduleDTO dto = new NotificationScheduleDTO();
		dto.setIds(List.of(medicalId));
		dto.setTypeNotification(Constant.NOTIFICATION_TYPE_CANCEL_MEDICAL);
		try {
			sendKafkaNotificationCancel(dto);
		} catch (JsonProcessingException e) {
			System.err.println("Cannot send message kafka when cancel medical");
		}
	}

	@Override
	public void completePayment(List<String> ids) {
		medicalRepository.updateByStatusPayment(Constant.payment_paid, ids);
	}

	@Override
	public boolean isMedicalCompletePayment(List<String> ids) {
		Long total = medicalRepository.isMedicalCompletePayment(Constant.payment_paid, ids);
		return total > 0;
	}

	@Override
	public boolean isAllMedicalCompletePayment(List<String> ids) {
		Long total = medicalRepository.isMedicalCompletePayment(Constant.payment_paid, ids);
		return Objects.equals(Long.valueOf(ids.size()), total);
	}

	@Override
	public boolean isValidTimeChangeTimeClose(ChangeTimeCloseForm changeTimeCloseForm) {
		MedicalExaminationScheduleEntity medical = medicalRepository.findById(changeTimeCloseForm.getIdMedical()).get();
		WorkTimeEntity workTime = workTimeRepository.findById(medical.getWorkTimeID()).get();
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		String timeStart = workTime.getTime().split("-")[0];
		int hourStart = Integer.parseInt(timeStart.split("h")[0]);
		String timeEnd = workTime.getTime().split("-")[1];
		int hourEnd = Integer.parseInt(timeEnd.split("h")[0]);
		return ((hourStart <= hour && hour <= hourEnd) || hourEnd <= hour);
	}

	@Override
	public boolean isValidDateChangeTimeClose(ChangeTimeCloseForm changeTimeCloseForm) {
		MedicalExaminationScheduleEntity medical = medicalRepository.findById(changeTimeCloseForm.getIdMedical()).get();
		Date currentDate = new Date();
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = sp.format(currentDate);
		return medical.getDate().equals(dateToString);
	}

	public String plusDate(String date) {
		String[] arr = date.split("-");
		LocalDate l = LocalDate.of(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
		l = l.plusDays(1);
		date = l.toString();
		return date;
	}

	public boolean validateChangTimeClose(ChangeTimeCloseForm changeTimeCloseForm, MedicalExaminationScheduleEntity entity) {
		if(changeTimeCloseForm.getIdWk() == null || changeTimeCloseForm.getIdWk().equals("") ) return false;
		if (changeTimeCloseForm.getIdWk() <= entity.getWorkTimeID()) return false;
		return  true;
	}

	private void sendMail(List<MedicalExaminationScheduleEntity> lstUpdate) {
		Long[] ids = new Long[lstUpdate.size()];
		for(int i = 0; i<lstUpdate.size();i++) {
			ids[i] = lstUpdate.get(i).getId();
		}
		List<String> lstEmail = userRepository.getEmailByIds(ids);
		StringBuilder content = new StringBuilder("Bookingcare xin chào bạn. Lời đầu tiên BookingCare cảm ơn bạn đã tin tưởng và đồng ");
		content.append("cùng chúng tôi. ");
		content.append("Do hiện tại số lượng bệnh nhân cũng như khách hàng quá đông, hệ thông đang có tình trạng tắc nghẽn ");
		content.append("cho nên ca khám của bạn đã được đẩy lùi muộn hơn so với dự kiến. ");
		content.append("Mời bạn truy cập vào hệ thống để cập nhập lại tình hình ca khám của mình. ");
		content.append("Bookingcare rất xin lỗi vì sự cố trên. ");
		content.append("Chúng tôi xin chân thành cảm ơn!");
		if(lstEmail != null && lstEmail.size() != 0) {
			lstEmail.forEach(p->{
				SimpleMailMessage message = new SimpleMailMessage();
				message.setTo(p);
				message.setSubject("Hệ thống đăng kí lịch khám BookingCare ");
				message.setText(content.toString());
				emailSender.send(message);
			});
		}
	}

	private void sendKafkaNotification(MedicalExaminationScheduleEntity entity, String typeNotification) throws JsonProcessingException {
		NotificationScheduleDTO notificationSchedule = new NotificationScheduleDTO();
		notificationSchedule.setIds(List.of(entity.getId()));
		notificationSchedule.setTypeNotification(typeNotification);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(notificationSchedule);
		kafkaTemplate.send(Constant.NOTIFICATION_TOPIC, json);
	}

	private void sendKafkaNotification(List<MedicalExaminationScheduleEntity> entities, String typeNotification) throws JsonProcessingException {
		NotificationScheduleDTO notificationSchedule = new NotificationScheduleDTO();
		List<Long> ids = entities.stream().map(item ->item.getId()).collect(Collectors.toList());
		notificationSchedule.setIds(ids);
		notificationSchedule.setTypeNotification(typeNotification);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(notificationSchedule);
		kafkaTemplate.send(Constant.NOTIFICATION_TOPIC, json);
	}

	private void sendKafkaNotification(NotificationScheduleDTO dto) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(dto);
		kafkaTemplate.send(Constant.NOTIFICATION_TOPIC, json);
	}

	private void sendKafkaNotificationCancel(NotificationScheduleDTO dto) throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(dto);
		kafkaTemplate.send(Constant.NOTIFICATION_TOPIC, json);
	}

//	public static void main(String[] args) {

//
//		boolean isValid = false;
//		WorkTimeEntity workTime = new WorkTimeEntity();
//		workTime.setTime("9h-10h");
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(2023, 05, 26, 8, 0);
//		int hour = calendar.get(Calendar.HOUR_OF_DAY);
//		int minus = calendar.get(Calendar.MINUTE);
//		int hourCheckBook = hour;
//		if (minus > 0) {
//			hourCheckBook = hour + 1;
//		}
//		String timeStart = workTime.getTime().split("-")[0];
//		int hourStart = Integer.parseInt(timeStart.split("h")[0]);
//		String timeEnd = workTime.getTime().split("-")[1];
//		int hourEnd = Integer.parseInt(timeEnd.split("h")[0]);
//		if ( (hourStart<= hour && hour <= hourEnd) || hourStart - 1 < hourCheckBook) {
//			isValid= false;
//		} else {
//			isValid= true;
//		}
//
//
//		System.out.println(isValid);
//	}

}
