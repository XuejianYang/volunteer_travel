package hue.edu.xiong.volunteer_travel.controller;

import hue.edu.xiong.volunteer_travel.model.Attractions;
import hue.edu.xiong.volunteer_travel.model.Hotel;
import hue.edu.xiong.volunteer_travel.repository.HotelRepository;
import hue.edu.xiong.volunteer_travel.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yxj
 * @Description //TODO $
 * @Date $ $
 * @Version 1.0
 **/
@Controller
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
   private HotelRepository hotelRepository;
    @Autowired
   private ReserveService reserveService;


    @RequestMapping("/hotelTypeUI")
    public String reserveHotelListUI(Model model, @ModelAttribute("searchName") String searchName,@ModelAttribute("type") String type, @PageableDefault(size = 10) Pageable pageable) {
        Page<Hotel> hotelPage = hotelRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //status状态,查询状态为0,启动的酒店
//            predicates.add((cb.equal(root.get("status"), 0)));
            //酒店name模糊查询
            if (!StringUtils.isEmpty(searchName)) {
                predicates.add((cb.like(root.get("name"), "%" + searchName + "%")));
            }
            if (!StringUtils.isEmpty(type)) {
                predicates.add((cb.like(root.get("type"), "%" + type + "%")));
            }
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        List<Hotel> top10Hotel = reserveService.getTop10Hotel();
        List<Attractions> top10Attractions = reserveService.getTop10Attractions();
        List<Hotel> all = hotelRepository.findAll();
        List<String> typeList = all.stream().map(s -> s.getType()).distinct().collect(Collectors.toList());
        model.addAttribute("top10Hotel", top10Hotel);
//        model.addAttribute("top10Attractions", top10Attractions);
        model.addAttribute("page", hotelPage);
        model.addAttribute("typeList", typeList);
        return "reserve/hotel-type-details";
    }

}
