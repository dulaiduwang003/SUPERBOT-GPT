package com.cn.drawing.controller;


import com.cn.common.msg.Result;
import com.cn.drawing.dto.CreateCommentsDto;
import com.cn.drawing.dto.DeleteCommentsDto;
import com.cn.drawing.exceptions.DrawingException;
import com.cn.drawing.service.DrawingCommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/12 下午5:29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/drawing-comments")
public class DrawingCommentsController {

    private final DrawingCommentsService drawingCommentsService;


    @PostMapping(value = "/create/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result createComments(@RequestBody @Validated CreateCommentsDto dto) {
        try {
            drawingCommentsService.createComments(dto);
            return Result.ok();
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }

    @PostMapping(value = "/delete/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteComments(@RequestBody @Validated DeleteCommentsDto dto) {
        try {
            drawingCommentsService.deleteComments(dto);
            return Result.ok();
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }

    @GetMapping(value = "/get/comments/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getCommentsPage(final Long pageNum, final Long workflowsId) {
        try {
            return Result.data(drawingCommentsService.getCommentsPage(pageNum, workflowsId));
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }


}
