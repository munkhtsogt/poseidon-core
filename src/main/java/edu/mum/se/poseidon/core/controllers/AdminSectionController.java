package edu.mum.se.poseidon.core.controllers;

import edu.mum.se.poseidon.core.controllers.dto.SectionDto;
import edu.mum.se.poseidon.core.controllers.mapper.SectionMapper;
import edu.mum.se.poseidon.core.controllers.wrapper.ErrorResponseWrapper;
import edu.mum.se.poseidon.core.controllers.wrapper.FailResponseWrapper;
import edu.mum.se.poseidon.core.repositories.models.Section;
import edu.mum.se.poseidon.core.services.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminSectionController {

    private SectionService sectionService;
    private static final Logger log = LoggerFactory.getLogger(AdminSectionController.class);
    private SectionMapper sectionMapper;

    @Autowired
    public AdminSectionController(SectionService sectionService, SectionMapper sectionMapper) {
        this.sectionService = sectionService;
        this.sectionMapper = sectionMapper;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/sections/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteSection(@PathVariable(name = "id") long id) {
        log.debug("'Delete section' request is received. Section ID=" + id);
        try {
            sectionService.deleteSection(id);
            return new ResponseEntity(new SectionDto(), HttpStatus.OK);
        } catch (PoseidonException pe) {
            FailResponseWrapper failResponse = new FailResponseWrapper(pe.getMessage());
            return new ResponseEntity(failResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponseWrapper errorResponseWrapper = new ErrorResponseWrapper("Failed to execute a request.");
            return new ResponseEntity(errorResponseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/sections/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getSection(@PathVariable long id) {
        log.debug("'Get section' request is received. section ID=" + id);
        try {
            SectionDto sectionDto = sectionMapper.getSectionDtoFrom(sectionService.getSection(id));
            return new ResponseEntity<>(sectionDto, HttpStatus.OK);
        } catch (Exception e) {
            ErrorResponseWrapper errorResponseWrapper = new ErrorResponseWrapper("Failed to execute a request.");
            return new ResponseEntity(errorResponseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/sections/edit", method = RequestMethod.POST)
    public ResponseEntity<?> editSection(@RequestBody SectionDto sectionDto) {
        log.debug("'Edit section' request is received. section ID=" + sectionDto.getId());
        try {
            Section section = sectionMapper.getSectionFrom(sectionDto);
            SectionDto sectionDto1 = sectionMapper.getSectionDtoFrom(sectionService.editSection(section));
            return new ResponseEntity<>(sectionDto1, HttpStatus.OK);
        } catch (PoseidonException pe) {
            FailResponseWrapper failResponse = new FailResponseWrapper(pe.getMessage());
            return new ResponseEntity(failResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponseWrapper errorResponseWrapper = new ErrorResponseWrapper("Failed to execute a request.");
            return new ResponseEntity(errorResponseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/sections", method = RequestMethod.GET)
    public ResponseEntity<?> getSectionList() {
        try {
            List<Section> sectionList = sectionService.getSectionList();
            List<SectionDto> sectionDtoList = sectionList.stream()
                    .map(e -> sectionMapper.getSectionDtoFrom(e))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(sectionDtoList, HttpStatus.OK);
        } catch (Exception e) {
            ErrorResponseWrapper errorResponseWrapper = new ErrorResponseWrapper("Failed to execute a request.");
            return new ResponseEntity(errorResponseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}