package com.example.springhotel.controllers;

import com.example.springhotel.models.Room;
import com.example.springhotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
public class RoomsController {

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/rooms")
    public String RoomsMain(Model model) {
        Iterable<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        return "rooms-main";
    }

    @GetMapping("/rooms/add")
    public String RoomsAdd(Model model) {
        Iterable<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        return "rooms-add";
    }

    @PostMapping("/rooms/add")
    public String RoomsAddPost(@RequestParam int number, @RequestParam int price, @RequestParam String ranking) {
        Room room = new Room(number, price, ranking);
        roomRepository.save(room);
        return "redirect:/rooms";
    }

    @PostMapping("/rooms/{id}/remove")
    public String RoomsRemovePost(@PathVariable(value = "id") long id) {
        roomRepository.deleteById(id);
        return "redirect:/rooms";
    }
}
