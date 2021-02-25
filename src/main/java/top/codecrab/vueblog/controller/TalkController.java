package top.codecrab.vueblog.controller;


import org.springframework.web.bind.annotation.*;

import top.codecrab.vueblog.base.BaseController;
import top.codecrab.vueblog.common.response.Result;
import top.codecrab.vueblog.entity.Talk;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author codecrab
 * @since 2021-02-25
 */
@RestController
@RequestMapping("/talk")
public class TalkController extends BaseController {

    @PostMapping("/add")
    public Result addTalk(@Valid @RequestBody Talk talk) {
        return talkService.addTalk(talk);
    }

    @GetMapping("/{blogId}/list")
    public Result talkList(@PathVariable("blogId") Long blogId) {
        return talkService.talkList(blogId);
    }

    @GetMapping("/dbfda84701bc47b49451028e355d6fc7/allList")
    public Result getAllList() {
        return talkService.getAllList();
    }

    @GetMapping("/dbfda84701bc47b49451028e355d6fc7/allNoVisibleList")
    public Result getAllNoVisibleList() {
        return talkService.getAllNoVisibleList();
    }

    @PutMapping("/dbfda84701bc47b49451028e355d6fc7/pass/{id}")
    public Result pass(@PathVariable("id") Long id) {
        return talkService.pass(id);
    }

    @PutMapping("/dbfda84701bc47b49451028e355d6fc7/noPass/{id}")
    public Result noPass(@PathVariable("id") Long id) {
        return talkService.noPass(id);
    }

}
