package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
@RestController
@RequestMapping("/meetings")
public class MeetingServiceController {

        @Autowired
        MeetingService meetingService;

        @RequestMapping(value = "", method = RequestMethod.GET)
        public ResponseEntity<?> getMeetings(){
            Collection<Meeting> meetings = meetingService.getAll();
            return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.GET)
        public ResponseEntity<?> getMeeting(@PathVariable("id") Long login) {
            Meeting meeting = meetingService.findById(login);
            if (meeting == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
        }
    }
