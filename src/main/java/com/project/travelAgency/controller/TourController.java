package com.project.travelAgency.controller;

import com.project.travelAgency.dto.TourDto;
import com.project.travelAgency.model.entity.Country;
import com.project.travelAgency.model.entity.Tour;
import com.project.travelAgency.model.repository.TourRepository;
import com.project.travelAgency.service.impl.TourServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tours")
public class TourController {

    final static Logger logger = Logger.getLogger(TourController.class);
    private final TourServiceImpl tourService;
    private final TourRepository tourRepository;

    @GetMapping
    public String list(Model model) {
        List<TourDto> list = tourService.getAll();
        model.addAttribute("tours", list);
        return "tours";
    }

    @GetMapping("/{id}/cart")
    public String addCart(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/tours";
        }
        tourService.addToUserCart(id, principal.getName());
        return "redirect:/tours";
    }

    @GetMapping("/showCreateTour")
    public String showCreateTour(Model model) {
        logger.info("Create a new Tour");
        model.addAttribute("tour", new TourDto());
        return "createTour";
    }

    @PostMapping("/createTour")
    public String createTour(@Valid @ModelAttribute("tour") TourDto tourDto, BindingResult result,
                             Model model) {
        logger.info("Save a new Tour");
        if (result.hasErrors()) {
            return "createTour";
        }
        tourService.save(tourDto);
        List<TourDto> list = tourService.getAll();
        model.addAttribute("tours", list);
        return "tours";
    }

    @GetMapping("/{id}/deleteTour")
    public String deleteTour(@PathVariable Long id, Model model) {
        logger.info("Delete Tour");
        tourRepository.deleteById(id);
        List<TourDto> list = tourService.getAll();
        List<TourDto> newList = tourService.getAll();
        model.addAttribute("tours", newList);
        return "tours";
    }

    @GetMapping("/{id}")
    public String editTour(@PathVariable Long id, Model model) {
        logger.info("Edit Tour");
        model.addAttribute("tour", tourService.getById(id));
        return "editTour";

    }

    @PostMapping("/editTour")
    public String editTour(@ModelAttribute Tour tour, @ModelAttribute Country country, Model model) {
        logger.info("Save changed Tour");
        model.addAttribute("country", new Country());
        tourRepository.save(tour);
        List<TourDto> list = tourService.getAll();
        model.addAttribute("tours", list);
        return "tours";
    }
}