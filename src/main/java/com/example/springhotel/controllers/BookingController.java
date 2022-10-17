package com.example.springhotel.controllers;

import com.example.springhotel.models.Booking;
import com.example.springhotel.models.Room;
import com.example.springhotel.models.User;
import com.example.springhotel.repository.BookingRepository;
import com.example.springhotel.repository.RoomRepository;
import com.example.springhotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/bookings")
    public String BookingsMain(Model model) {
        Iterable<Booking> bookings = bookingRepository.findAll();
        model.addAttribute("bookings", bookings);

        return "bookings-main";
    }

    @GetMapping("/bookings/add")
    public String BookingsAdd(Model model) {
        Iterable<Booking> bookings = bookingRepository.findAll();
        model.addAttribute("bookings", bookings);

        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        Iterable<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);

        return "bookings-add";
    }

    @PostMapping("/bookings/add")
    public String BookingsAddPost(@RequestParam String date1, @RequestParam String date2
            , @RequestParam Long user_id , @RequestParam Long room_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<Room> room = roomRepository.findById(room_id);

        if(user.isPresent() && room.isPresent()){
            Booking booking = new Booking(date1, date2, user.get(), room.get());
            bookingRepository.save(booking);
        }
        return "redirect:/bookings";
    }

    @PostMapping("/bookings/{id}/remove")
    public String BookingsRemovePost(@PathVariable(value = "id") long id) {
        bookingRepository.deleteById(id);
        return "redirect:/bookings";
    }
}
