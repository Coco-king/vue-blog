package top.codecrab.vueblog.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.codecrab.vueblog.entity.Talk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.codecrab.vueblog.entity.ext.TalkNode;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author codecrab
 * @since 2021-02-25
 */
@Mapper
public interface TalkMapper extends BaseMapper<Talk> {

    List<TalkNode> getList(Long blogId);

    List<TalkNode> getAllList();

    List<TalkNode> getAllNoVisibleList();
}
