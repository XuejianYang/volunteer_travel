package hue.edu.xiong.volunteer_travel.service;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.core.ResultGenerator;
import hue.edu.xiong.volunteer_travel.model.User;
import hue.edu.xiong.volunteer_travel.model.UserLike;
import hue.edu.xiong.volunteer_travel.model.UserYuYue;
import hue.edu.xiong.volunteer_travel.model.YuYueNum;
import hue.edu.xiong.volunteer_travel.repository.UserRepository;
import hue.edu.xiong.volunteer_travel.repository.UserYuYueRepository;
import hue.edu.xiong.volunteer_travel.util.CookieUitl;
import hue.edu.xiong.volunteer_travel.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Author yxj
 * @Description //TODO $
 * @Date $ $
 * @Version 1.0
 **/
@Service
public class UserYuYueService {
    @Autowired
    private UserYuYueRepository userYuYueRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public Result saveYuYue(HttpServletRequest request, List<UserYuYue> yuYueList) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            return ResultGenerator.genFailResult("用户没有登录!");
        }
        User user = userRepository.findUserByUsername(cookie.getValue());
        yuYueList.forEach(s->{
            s.setId(IdGenerator.id());
        userYuYueRepository.saveAndFlush(s);
        });

        return ResultGenerator.genSuccessResult();
    }
    @Transactional(rollbackFor = Exception.class)
    public Result getYuyueList() {
        List<UserYuYue> all = userYuYueRepository.findAll();
        return ResultGenerator.genSuccessResult(all);
    }
    @Transactional(rollbackFor = Exception.class)
    public Result getYuyueNum( String date) {
        List<UserYuYue> userYuYueByDate = userYuYueRepository.findUserYuYueByDate(date);
        int size = userYuYueByDate.size();
        YuYueNum yuYueNum = new YuYueNum();
        yuYueNum.setPm(100);
        yuYueNum.setAm(100);
        if(size==0){
            return ResultGenerator.genSuccessResult(yuYueNum);
        }
        Long amCount = userYuYueByDate.stream().filter(s -> s.getDuring() == 0).count();
        int intValue = amCount.intValue();
        yuYueNum.setAm(100-intValue);
        yuYueNum.setPm(100-size - amCount.intValue());
        return ResultGenerator.genSuccessResult(yuYueNum);
    }
}
