package hue.edu.xiong.volunteer_travel.service;

import hue.edu.xiong.volunteer_travel.core.Result;
import hue.edu.xiong.volunteer_travel.core.ResultGenerator;
import hue.edu.xiong.volunteer_travel.core.ServiceException;
import hue.edu.xiong.volunteer_travel.enums.StatusEnum;
import hue.edu.xiong.volunteer_travel.model.*;
import hue.edu.xiong.volunteer_travel.repository.*;
import hue.edu.xiong.volunteer_travel.util.CookieUitl;
import hue.edu.xiong.volunteer_travel.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private TravelStrategyRepository travelStrategyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCommentRepository userCommentRepository;


    public Page<UserComment> findCommentByNameAndUserId(String content, Pageable pageable) {
        //查询通过后台审核的攻略列表
        Page<UserComment> travelStrategyPage = userCommentRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add((cb.equal(root.get("status"), 0)));
            //攻略name模糊查询
            if (!StringUtils.isEmpty(content)) {
                predicates.add((cb.like(root.get("title"), "%" + content + "%")));
            }
            query.where(predicates.toArray(new Predicate[]{}));
            query.orderBy(cb.desc(root.get("createDate")));
            return null;
        }, pageable);
        return travelStrategyPage;
    }





//    @Transactional(rollbackFor = Exception.class)
//    public Result travelStrategyLike(HttpServletRequest request, String id) {
//        Cookie cookie = CookieUitl.get(request, "username");
//        if (cookie == null) {
//            return ResultGenerator.genFailResult("用户没有登录!");
//        }
//        User user = userRepository.findUserByUsername(cookie.getValue());
//        UserLike like = new UserLike();
//        like.setId(IdGenerator.id());
//        like.setCreateTime(new Date());
//        like.setUserId(user.getId());
//        like.setItemId(id);
//        like.setItemType(3);
//        likeRepository.saveAndFlush(like);
//        return ResultGenerator.genSuccessResult();
//    }

    @Transactional(rollbackFor = Exception.class)
    public Result saveComment(HttpServletRequest request, UserComment userComment) {
        Cookie cookie = CookieUitl.get(request, "username");
        if (cookie == null) {
            return ResultGenerator.genFailResult("用户没有登录!");
        }
        User user = userRepository.findUserByUsername(cookie.getValue());
        UserComment comment = new UserComment();
        comment.setId(IdGenerator.id());
        comment.setCreateTime(new Date());
        comment.setUserId(user.getId());
        comment.setUserName(user.getUsername());
        comment.setItemId(userComment.getItemId());
        comment.setContent(userComment.getContent());
        userCommentRepository.saveAndFlush(comment);
        return ResultGenerator.genSuccessResult();
    }
}
