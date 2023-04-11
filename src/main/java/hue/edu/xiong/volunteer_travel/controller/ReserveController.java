package hue.edu.xiong.volunteer_travel.controller;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.core.ResultGenerator;
import hue.edu.xiong.volunteer_travel.model.*;
import hue.edu.xiong.volunteer_travel.repository.LikeRepository;
import hue.edu.xiong.volunteer_travel.repository.UserCommentRepository;
import hue.edu.xiong.volunteer_travel.service.ReserveService;
import hue.edu.xiong.volunteer_travel.util.CookieUitl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/reserve")
public class ReserveController {

    @Autowired
    private ReserveService reserveService;

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserCommentRepository userCommentRepository;

    @RequestMapping("/reserveHotelListUI")
    public String reserveHotelListUI(Model model, @ModelAttribute("searchName") String searchName, @PageableDefault(size = 10) Pageable pageable) {
        Page<Hotel> page = reserveService.reserveHotelListUI(searchName, pageable);
        List<Hotel> top10Hotel = reserveService.getTop10Hotel();
        List<Attractions> top10Attractions = reserveService.getTop10Attractions();
        model.addAttribute("top10Hotel", top10Hotel);
        model.addAttribute("top10Attractions", top10Attractions);
        model.addAttribute("page", page);
        return "reserve/reserve-hotel";
    }

    @RequestMapping("/hotelDetailsUI")
    public String hotelDetailsUI(Model model, HttpServletRequest request, @RequestParam(name = "id") String id) {
        Hotel hotel = reserveService.findHotelById(id);
        //如果用户显示已经预约,就是查看预约列表
        Boolean flag = reserveService.isReserveHotel(request, id);
        List<Hotel> top10Hotel = reserveService.getTop10Hotel();
        List<Attractions> top10Attractions = reserveService.getTop10Attractions();
        model.addAttribute("top10Hotel", top10Hotel);
        model.addAttribute("top10Attractions", top10Attractions);
        model.addAttribute("hotel", hotel);
        model.addAttribute("flag", flag);
        return "reserve/reserve-hotel-details";
    }

    @RequestMapping("/reserveManageUI")
    public String reserveManageUI(Model model, HttpServletRequest request) {
        List<UserHotel> userHotelList = reserveService.getReserveHotelByUser(request);
        List<UserAttractions> userAttractionsList = reserveService.getReserveAttractionsByUser(request);
        model.addAttribute("userHotelList", userHotelList);
        model.addAttribute("userAttractionsList",userAttractionsList);
        return "reserve/reserve-user-manage";
    }

    @RequestMapping("/cancelReserve")
    @ResponseBody
    public Result cancelReserve(HttpServletRequest request,String id) {
        return reserveService.cancelReserve(request,id);
    }

    @RequestMapping("/reserveAttractionsListUI")
    public String reserveAttractionsListUI(Model model, @ModelAttribute("searchName") String searchName, @PageableDefault(size = 10) Pageable pageable) {
        Page<Attractions> page = reserveService.reserveAttractionsListUI(searchName,pageable);
        List<Hotel> top10Hotel = reserveService.getTop10Hotel();
        List<Attractions> top10Attractions = reserveService.getTop10Attractions();
        model.addAttribute("top10Hotel", top10Hotel);
        model.addAttribute("top10Attractions", top10Attractions);
        model.addAttribute("page", page);
        return "reserve/reserve-attractions";
    }

    @RequestMapping("/attractionsDetailsUI")
    public String attractionsDetailsUI(Model model, HttpServletRequest request, @RequestParam(name = "id") String id) {
        Attractions attractions = reserveService.findAttractionsById(id);
        //如果用户显示已经预约,就是查看预约列表
        Boolean flag = reserveService.isReserveAttractions(request, id);
        List<Hotel> top10Hotel = reserveService.getTop10Hotel();
        List<Attractions> top10Attractions = reserveService.getTop10Attractions();
        List<UserLike> likeList = likeRepository.findLikeByItemId(id);
        List<UserComment> commentList = userCommentRepository.findUserCommentByItemId(id);
        int numb = likeList.size();
        model.addAttribute("top10Hotel", top10Hotel);
        model.addAttribute("top10Attractions", top10Attractions);
        model.addAttribute("attractions", attractions);
        model.addAttribute("numb", likeList.size());
        model.addAttribute("commentList", commentList);

        model.addAttribute("flag", flag);
        return "reserve/reserve-attractions-details";
    }


    @RequestMapping("/cancelAttractionsReserve")
    @ResponseBody
    public Result cancelAttractionsReserve(HttpServletRequest request,String id) {
        return reserveService.cancelAttractionsReserve(request,id);
    }

}
