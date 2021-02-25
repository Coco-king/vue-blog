package top.codecrab.vueblog.entity.ext;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.codecrab.vueblog.entity.Talk;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class TalkNode extends Talk {

    private List<TalkNode> subTalks;

}
