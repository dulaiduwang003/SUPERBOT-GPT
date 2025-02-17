package com.cn.drawing.controller;


import com.cn.common.msg.Result;
import com.cn.drawing.dto.DeleteWorkflowsWorksDto;
import com.cn.drawing.exceptions.DrawingException;
import com.cn.drawing.service.DrawingWorksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/12 下午5:29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/drawing-works")
public class DrawingWorksController {


    private final DrawingWorksService drawingWorksService;


    @PostMapping(value = "/delete/works", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result deleteWorks(@RequestBody @Validated DeleteWorkflowsWorksDto dto) {
        try {
            drawingWorksService.deleteWorks(dto);
            return Result.ok();
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }

    @GetMapping(value = "/get/works/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getWorksPage(final Long pageNum) {
        try {
            return Result.data(drawingWorksService.getWorksPage(pageNum));
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }


    @GetMapping(value = "/get/works", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getWorks(final Long workflowsWorksId) {
        try {
            return Result.data(drawingWorksService.getWorks(workflowsWorksId));
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }


}
