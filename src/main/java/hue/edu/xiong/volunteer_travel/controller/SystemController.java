package hue.edu.xiong.volunteer_travel.controller;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.core.ResultGenerator;
import hue.edu.xiong.volunteer_travel.model.*;
import hue.edu.xiong.volunteer_travel.repository.UserCommentRepository;
import hue.edu.xiong.volunteer_travel.repository.UserRepository;
import hue.edu.xiong.volunteer_travel.repository.UserYuYueRepository;
import hue.edu.xiong.volunteer_travel.service.SystemService;
import hue.edu.xiong.volunteer_travel.service.UserYuYueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private SystemService systemService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCommentRepository userCommentRepository;
    @Autowired
    private UserYuYueRepository userYuYueRepository;


    @RequestMapping("")
    public String loginUI() {
        return "system/login/login";
    }

    @RequestMapping("/login")
    @ResponseBody
    public Result login(SysUser sysUser, HttpServletResponse response) {

       return systemService.login(sysUser,response);
    }
    @RequestMapping("/userListUI")
    public String userListUI(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<User> page = systemService.getUserPage(pageable);
        model.addAttribute("page",page);
        return "system/user/list";
    }

    @RequestMapping("/saveUser")
    @ResponseBody
    public Result saveUser(User user) {
        return systemService.saveUser(user);
    }
    @RequestMapping("/deleteUser")
    @ResponseBody
    public Result deleteUser(String id) {
         userRepository.deleteById(id);
        return ResultGenerator.genSuccessResult("删除成功！");
    }

    @RequestMapping("/getUserById")
    @ResponseBody
    public Result getUserById(String id) {
        return ResultGenerator.genSuccessResult(systemService.getUserById(id));
    }



    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
       systemService.logout(request,response);
        return "redirect:/system";
    }

    @RequestMapping("/hotelListUI")
    public String hotelListUI(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<Hotel> page = systemService.getHotelPage(pageable);
        model.addAttribute("page", page);
        return "system/hotel/list";
    }

    @RequestMapping("/saveHotel")
    @ResponseBody
    public Result saveHotel(Hotel hotel) {
        return systemService.saveHotel(hotel);
    }

    @RequestMapping("/updateStatus")
    @ResponseBody
    public Result updateStatus(String id) {
        return systemService.updateStatus(id);
    }

    @RequestMapping("/getHotelById")
    @ResponseBody
    public Result getHotelById(String id) {
        return ResultGenerator.genSuccessResult(systemService.getHotelById(id));
    }

    @RequestMapping("/attractionsListUI")
    public String attractionsListUI(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<Attractions> page = systemService.getAttractionsPage(pageable);
        model.addAttribute("page", page);
        return "system/attractions/list";
    }

    @RequestMapping("/getAttractionsById")
    @ResponseBody
    public Result getAttractionsById(String id) {
        return ResultGenerator.genSuccessResult(systemService.getAttractionsById(id));
    }

    @RequestMapping("/updateAttractionsStatus")
    @ResponseBody
    public Result updateAttractionsStatus(String id) {
        return systemService.updateAttractionsStatus(id);
    }

    @RequestMapping("/saveAttractions")
    @ResponseBody
    public Result saveAttractions(Attractions attractions) {
        return systemService.saveAttractions(attractions);
    }

    @RequestMapping("/travelRouteListUI")
    public String travelRouteListUI(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<TravelRoute> page = systemService.getTravelRoutePage(pageable);
        model.addAttribute("page", page);
        return "system/route/list";
    }

    @RequestMapping("/getTravelRouteById")
    @ResponseBody
    public Result getTravelRouteById(String id) {
        return ResultGenerator.genSuccessResult(systemService.getTravelRouteById(id));
    }
    @RequestMapping("/deleteComment")
    @ResponseBody
    public Result deleteComment(String id) {
        userCommentRepository.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }


    @RequestMapping("/updateTravelRouteStatus")
    @ResponseBody
    public Result updateTravelRouteStatus(String id) {
        return systemService.updateTravelRouteStatus(id);
    }

    @RequestMapping("/saveTravelRoute")
    @ResponseBody
    public Result saveTravelRoute(TravelRoute travelRoute) {
        return systemService.saveTravelRoute(travelRoute);
    }

    @RequestMapping("/travelStrategyListUI")
    public String travelStrategyListUI(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<TravelStrategy> page = systemService.getTravelStrategyPage(pageable);
        model.addAttribute("page", page);
        return "system/strategy/list";
    }
    @RequestMapping("/yuyueUI")
    public String yuyue(Model model,@PageableDefault(size = 10) Pageable pageable) {
        Page<UserYuYue> page = userYuYueRepository.findAll(pageable);
        //
        List<UserYuYue> content = page.getContent();
        Map<String, List<UserYuYue>> listMap = content.stream().collect(Collectors.groupingBy(s -> s.getDate()));
        List<YuYueDateNum> yuYueResultList = new ArrayList();

        for (String s: listMap.keySet()){
            List<UserYuYue> yuYueList = listMap.get(s);
            YuYueDateNum yuYueDateNum = new YuYueDateNum();
            yuYueDateNum.setDate(s);
            yuYueDateNum.setNum(yuYueList.size()+"");
            yuYueResultList.add(yuYueDateNum);
            if(yuYueResultList.size()>7){
                break;
            }
        }
        String dateStr = yuYueResultList.stream().map(s -> s.getDate()).collect(Collectors.joining(","));
        String numStr = yuYueResultList.stream().map(s -> s.getNum()).collect(Collectors.joining(","));
        YuYueDateNum result = new YuYueDateNum();
        result.setNum(numStr);
        result.setDate(dateStr);
        // pm  am
        Long amCount = content.stream().filter(s -> s.getDuring() == 0).count();
        Long pmCount = content.size() - amCount;
        YuYueNum yuYueNum = new YuYueNum();
        yuYueNum.setAm(amCount.intValue());
        yuYueNum.setPm(pmCount.intValue());
        model.addAttribute("page", page);
        model.addAttribute("dateTotal", result);
        model.addAttribute("ampmTotal", yuYueNum);
        return "system/yuyue/list";
    }
    @RequestMapping("/deletYuyue")
    @ResponseBody
    public Result deletYuyue(String id) {
        userYuYueRepository.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping("/getTravelStrategyById")
    @ResponseBody
    public Result getTravelStrategyById(String id) {
        return ResultGenerator.genSuccessResult(systemService.getTravelStrategyById(id));
    }

    @RequestMapping("/updateTravelStrategyStatus")
    @ResponseBody
    public Result updateTravelStrategyStatus(String id) {
        return systemService.updateTravelStrategyStatus(id);
    }

    @RequestMapping("/saveTravelStrategy")
    @ResponseBody
    public Result saveTravelStrategy(HttpServletRequest request,TravelStrategy travelStrategy) {
        return systemService.saveTravelStrategy(request,travelStrategy);
    }
}
