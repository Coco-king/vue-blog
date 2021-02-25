package top.codecrab.vueblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import top.codecrab.vueblog.common.response.Result;
import top.codecrab.vueblog.entity.Blog;
import top.codecrab.vueblog.entity.Talk;
import top.codecrab.vueblog.entity.User;
import top.codecrab.vueblog.entity.ext.TalkNode;
import top.codecrab.vueblog.mapper.TalkMapper;
import top.codecrab.vueblog.service.TalkService;
import top.codecrab.vueblog.service.UserService;
import top.codecrab.vueblog.shiro.AccountProfile;
import top.codecrab.vueblog.utils.JwtUtils;
import top.codecrab.vueblog.utils.ShiroUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author codecrab
 * @since 2021-02-25
 */
@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk> implements TalkService {

    @Autowired
    private TalkMapper talkMapper;
    @Autowired
    private UserService userService;

    @Value("${vue-blog.user.default-avatar}")
    private String avatar;

    @Override
    public Result addTalk(Talk talk) {
        Long userId = talk.getUserId();
        if (talk.getBlogId() == null || talk.getBlogId() <= 0) {
            return Result.fail("博客ID不能为空");
        }
        if (talk.getParentId() == null || talk.getParentId() <= 0) {
            //根评论
            talk.setParentId(0L);
        }
        talk.setCommend(0L);
        talk.setCreated(LocalDateTime.now());
        if (userId == null || userId <= 0) {
            //游客评论 负一表示游客
            talk.setUserId(-1L);
            talk.setUsername("游客");
            String[] split = avatar.split(",");
            int i = new Random().nextInt(split.length);
            talk.setAvatar(split[i]);
        } else {
            AccountProfile profile = ShiroUtils.getAccountProfile();
            if (!userId.equals(profile.getId())) {
                return Result.fail("用户ID不匹配");
            }
            talk.setUsername(profile.getUsername());
            User user = userService.getById(userId);
            Assert.notNull(user, "找不到对应的用户");
            talk.setAvatar(user.getAvatar());
        }
        talk.setId(null);
        talk.setVisible(0);
        boolean save = this.save(talk);
        return save ? new Result(200, "发表成功", null) : Result.fail("发表失败");
    }

    @Override
    public Result talkList(Long blogId) {
        if (blogId == null || blogId <= 0) {
            return Result.fail("文章ID为空");
        }
        List<TalkNode> talkList = talkMapper.getList(blogId);
        return Result.success(talkList);
    }

    @Override
    public Result getAllList() {
        return Result.success(talkMapper.getAllList());
    }

    @Override
    public Result getAllNoVisibleList() {
        return Result.success(talkMapper.getAllNoVisibleList());
    }

    @Override
    public Result pass(Long id) {
        Talk talk = this.getById(id);
        talk.setVisible(1);
        boolean update = this.updateById(talk);
        return update ? new Result(201, "通过", null) : Result.fail("操作失败");
    }

    /**
     * 不通过或删除
     */
    @Override
    public Result noPass(Long id) {
        Talk talk = this.getById(id);
        talk.setVisible(0);
        boolean update = this.updateById(talk);
        return update ? new Result(201, "删除或未通过成功", null) : Result.fail("操作失败");
    }
}
