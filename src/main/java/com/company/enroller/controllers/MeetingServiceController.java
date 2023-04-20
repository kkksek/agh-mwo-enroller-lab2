package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
@RestController
@RequestMapping("/meetings")
public class MeetingServiceController {

        @Autowired
        MeetingService meetingService;

        @Autowired
        ParticipantService participantService;

        @RequestMapping(value = "", method = RequestMethod.GET)
        public ResponseEntity<?> getMeetings(){
            Collection<Meeting> meetings = meetingService.getAll();
            return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.GET)
        public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
            Meeting meeting = meetingService.findById(id);
            if (meeting == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
        }

        @RequestMapping(value = "/participants/{id}", method = RequestMethod.GET)
        public ResponseEntity<Collection> getMeetingParticipants(@PathVariable("id") Long id) {
            Meeting meeting = meetingService.findById(id);
            if (meeting == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            Collection<Participant> participants = meeting.getParticipants();
            return new ResponseEntity<>(participants, HttpStatus.OK);
        }


        @RequestMapping(value = "", method = RequestMethod.POST)
        public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
             if (meetingService.findById(meeting.getId()) != null) {
                return new ResponseEntity<String>(
                    "Unable to create. A meeting with id " + meeting.getId() + " already exist.",
                    HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
            public ResponseEntity<?> deleteMeeting(@PathVariable("id") Long id) {
            Meeting meeting = meetingService.findById(id);
            if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            meetingService.delete(meeting);
            return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
            public ResponseEntity<?> updateMeeting(@PathVariable("id") long id,@RequestBody Meeting meeting){
            Meeting foundMeeting = meetingService.findById(id);
            foundMeeting.setTitle(meeting.getTitle());
            foundMeeting.setDescription(meeting.getDescription());
            foundMeeting.setDate(meeting.getDate());
            meetingService.update(foundMeeting);
            return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.NO_CONTENT);
        }

        @RequestMapping(value = "/{id}", method = RequestMethod.POST)
            public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id, @RequestBody Participant participant) {
            Meeting meeting = meetingService.findById(id);
            if (meeting ==null){
                return new ResponseEntity<String>(
                    "Unable to add participant. A meeting with id " + meeting.getId() + " not exist",
                    HttpStatus.CONFLICT);
            }
            if (participantService.findByLogin(participant.getLogin()) == null){
                return new ResponseEntity<String>(
                        "Unable to add participant. A participant with login " + participant.getLogin() + " not exist",
                        HttpStatus.CONFLICT);
            }
            meetingService.addParticipant(meeting,participant);
            return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
        }

        @RequestMapping(value = "remove/{id}", method = RequestMethod.DELETE)
            public ResponseEntity<?> deleteParticipantFromMeeting(@PathVariable("id") Long id,  @RequestBody Participant participant) {
            Meeting meeting = meetingService.findById(id);
            Participant foundParticipant = participantService.findByLogin(participant.getLogin());
            if (meeting == null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            if (!meeting.getParticipants().contains(participant)){
                return new ResponseEntity<String>(
                        "Unable to remove participant. A participant with login " + participant.getLogin() + " not exist in meeting",
                        HttpStatus.CONFLICT);
            }
            meetingService.removeParticipant(meeting, foundParticipant);
            return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
        }

    }
