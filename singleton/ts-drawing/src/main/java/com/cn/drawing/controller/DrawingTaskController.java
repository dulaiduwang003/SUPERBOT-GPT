package com.cn.drawing.controller;


import com.cn.common.exceptions.EnergyException;
import com.cn.common.msg.Result;
import com.cn.drawing.dto.SubmitTaskDto;
import com.cn.drawing.exceptions.DrawingException;
import com.cn.drawing.service.DrawingTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/12 下午5:29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/drawing-task")
public class DrawingTaskController {


    private final DrawingTaskService drawingTaskService;


    @GetMapping(value = "/get/task/progress", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getTaskProgress(final String taskId) {
        try {
            return Result.data(drawingTaskService.getTaskProgress(taskId));
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }


    @GetMapping(value = "/get/task/progress/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getTaskProgressList() {
        try {
            return Result.data(drawingTaskService.getTaskProgressList());
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }



    @PostMapping(value = "/submit/task", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result submitTask(@RequestBody @Validated final SubmitTaskDto dto) {
        try {
            return Result.data(drawingTaskService.submitTask(dto));
        } catch (DrawingException | EnergyException ex) {
            return Result.error(ex.getMessage());
        }
    }


}
