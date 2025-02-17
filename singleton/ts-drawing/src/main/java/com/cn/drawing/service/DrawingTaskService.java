package com.cn.drawing.service;

import com.cn.drawing.dto.SubmitTaskDto;
import com.cn.drawing.vo.DrawingProgressVo;

import java.util.List;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/12 下午5:30
 */
public interface DrawingTaskService {

    String submitTask(final SubmitTaskDto dto);

    DrawingProgressVo getTaskProgress(final String taskId);

    List<DrawingProgressVo> getTaskProgressList();
}
