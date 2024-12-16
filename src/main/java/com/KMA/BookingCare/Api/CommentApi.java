package com.KMA.BookingCare.Api;

import com.KMA.BookingCare.Dto.CommentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.KMA.BookingCare.Service.CommentService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/api")
public class CommentApi {

	private final Logger log = LoggerFactory.getLogger(CommentApi.class);
	
	@Autowired
	private CommentService commentService;

	
	@PostMapping(value = {"/comment/delete"})
	public ResponseEntity<?> deleteHandbook(@RequestBody Long id) {
		try {
			commentService.delete(id);
			System.out.println("đã xoá cmt");
			
		} catch (Exception e ) {
			e.printStackTrace();
			System.out.println("không thể xoá cmt");
		}
		return ResponseEntity.ok("true");
	}

	@GetMapping(value = "/comment/handbook/{id}")
	public ResponseEntity<?> getAllById(@PathVariable("id") Long id) {
		log.debug("Request to get comment by handbook id {}", id);
		if (commentService.existsByHandbookId(id)) {
			List<CommentDto> lst = commentService.findAllByHandbookId(id);
			return  ResponseEntity.ok(lst);
		} else {
			return ResponseEntity.ok(Collections.emptyList());
		}

	}
}
