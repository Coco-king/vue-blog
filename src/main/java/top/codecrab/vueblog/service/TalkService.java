package top.codecrab.vueblog.service;

import top.codecrab.vueblog.common.response.Result;
import top.codecrab.vueblog.entity.Talk;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author codecrab
 * @since 2021-02-25
 */
public interface TalkService extends IService<Talk> {

    Result addTalk(Talk talk);

    Result talkList(Long blogId);

    Result getAllList();

    Result getAllNoVisibleList();

    Result pass(Long id);

    Result noPass(Long id);
}
