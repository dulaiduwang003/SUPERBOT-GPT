package com.cn.drawing.controller;


import com.cn.common.msg.Result;
import com.cn.drawing.dto.UploadWorkflowsDto;
import com.cn.drawing.exceptions.DrawingException;
import com.cn.drawing.service.DrawingWorkflowsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/12 下午5:29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/drawing-workflows")
public class DrawingWorkflowsController {


    private final DrawingWorkflowsService drawingWorkflowsService;


    @PostMapping(value = "/parsing/workflows/json", consumes = "multipart/form-data")
    public Result parsingWorkflowsJson(@Valid @NotNull(message = "上传文件不能为空") @RequestParam final MultipartFile file) {
        try {
            return Result.data(drawingWorkflowsService.parsingWorkflowJson(file));
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }


    @PostMapping(value = "/update/workflows", consumes = "multipart/form-data")
    public Result updateWorkflows(@RequestBody @Validated final UploadWorkflowsDto dto) {
        try {
            drawingWorkflowsService.uploadWorkflows(dto);
            return Result.ok();
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }


    @GetMapping(value = "/get/workflows/category/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getWorkflowsCategoryList() {
        return Result.data(drawingWorkflowsService.getWorkflowsCategoryList());
    }


    @GetMapping(value = "/get/workflows/interface", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getWorkflowsInterface(final Long workflowsId) {
        try {
            return Result.data(drawingWorkflowsService.getWorkflowsInterface(workflowsId));
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }


    @GetMapping(value = "/get/workflows", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getWorkflows(final Long workflowsId) {
        try {
            return Result.data(drawingWorkflowsService.getWorkflows(workflowsId));
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }


    @GetMapping(value = "/get/workflows/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getWorkflowsPage(@RequestParam(defaultValue = "1") final Long pageNum, final String prompt, final Long workflowsCategoryId) {
        try {
            return Result.data(drawingWorkflowsService.getWorkflowsPage(pageNum, prompt, workflowsCategoryId));
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }


    @PostMapping(value = "/upload/file", consumes = "multipart/form-data")
    public Result uploadWorkflowsComponentFile(@Valid @NotNull(message = "上传资源不能为空") final MultipartFile file) {
        try {
            return Result.data(drawingWorkflowsService.uploadWorkflowsComponentFile(file));
        } catch (DrawingException ex) {
            return Result.error(ex.getMessage());
        }
    }


}
