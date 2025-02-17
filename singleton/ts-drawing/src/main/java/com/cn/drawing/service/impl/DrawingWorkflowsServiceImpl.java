package com.cn.drawing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.common.configuration.DrawingConfiguration;
import com.cn.common.entity.Workflows;
import com.cn.common.entity.WorkflowsCategory;
import com.cn.common.entity.WorkflowsForm;
import com.cn.common.enums.FilePathEnum;
import com.cn.common.enums.WorkflowsNodeEnum;
import com.cn.common.mapper.WorkflowsCategoryMapper;
import com.cn.common.mapper.WorkflowsFormMapper;
import com.cn.common.mapper.WorkflowsMapper;
import com.cn.common.utils.RedisUtils;
import com.cn.common.utils.StringUtils;
import com.cn.common.utils.UploadUtil;
import com.cn.drawing.constant.DrawingWorkflowsConstant;
import com.cn.drawing.dto.UploadWorkflowsDto;
import com.cn.drawing.exceptions.DrawingException;
import com.cn.drawing.service.DrawingWorkflowsService;
import com.cn.drawing.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import static com.cn.drawing.utils.DrawingErrorParsingUtil.parsingError;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2025/1/3 14:21
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DrawingWorkflowsServiceImpl implements DrawingWorkflowsService {

    private final DrawingConfiguration drawingConfiguration;

    private final UploadUtil uploadUtil;

    private final WorkflowsFormMapper workflowsFormMapper;

    private final WorkflowsCategoryMapper workflowsCategoryMapper;

    private final WorkflowsMapper workflowsMapper;

    private final RedisUtils redisUtils;


    @Override
    public List<WorkflowsCategoryVo> getWorkflowsCategoryList() {
        return workflowsCategoryMapper.selectList(null).stream()
                .map(c -> new WorkflowsCategoryVo().setWorkflowsCategoryId(c.getWorkflowsCategoryId()).setCategoryName(c.getCategoryName())).toList();
    }


    @Override
    public WorkflowsInterfaceVo getWorkflowsInterface(final Long workflowsId) {
        final Workflows workflows = workflowsMapper.selectOne(new QueryWrapper<Workflows>().lambda().eq(Workflows::getWorkflowsId, workflowsId));
        if (workflows == null) {
            throw new DrawingException("该工作流已下架或不存在");
        }

        final List<WorkflowsInterfaceVo.Container> list = workflowsFormMapper.selectList(new QueryWrapper<WorkflowsForm>().lambda().eq(WorkflowsForm::getWorkflowsId, workflowsId)).stream().map(c -> new WorkflowsInterfaceVo.Container().setNodeKey(c.getNodeKey()).setNodeDigital(c.getNodeDigital()).setType(c.getType()).setTips(c.getTips())).toList();
        return new WorkflowsInterfaceVo().setWorkflowsId(workflowsId).setContainers(list).setTitle(workflows.getTitle()).setEnergy(workflows.getEnergy());
    }

    @Override
    public WorkflowsVo getWorkflows(final Long workflowsId) {
        final Workflows workflows = workflowsMapper.selectOne(new QueryWrapper<Workflows>().lambda().eq(Workflows::getWorkflowsId, workflowsId));
        if (workflows == null) {
            throw new DrawingException("该工作流已下架或不存在");
        }

        final Long l = redisUtils.keySetSize(DrawingWorkflowsConstant.VISITED_WORKFLOWS + workflowsId);
        return new WorkflowsVo().setVisited(l).setWorkflowsId(workflows.getWorkflowsId()).setTitle(workflows.getTitle()).setIntroduced(workflows.getIntroduced()).setNowImage(workflows.getNowImage()).setOriginalImage(workflows.getOriginalImage()).setCreateTime(workflows.getCreateTime());
    }

    @Override
    public IPage<WorkflowsPageVo> getWorkflowsPage(final Long pageNum, final String prompt, final Long workflowsCategoryId) {
        return workflowsMapper.selectPage(new Page<>(pageNum, 10),
                        new QueryWrapper<Workflows>()
                                .lambda()
                                .eq(Objects.nonNull(workflowsCategoryId), Workflows::getWorkflowsCategoryId, workflowsCategoryId)
                                .like(StringUtils.notEmpty(prompt), Workflows::getTitle, prompt))
                .convert(c -> {
                    final Long l = redisUtils.keySetSize(DrawingWorkflowsConstant.VISITED_WORKFLOWS + c.getWorkflowsId());
                    WorkflowsCategory workflowsCategory = workflowsCategoryMapper.selectOne(new QueryWrapper<WorkflowsCategory>()
                            .lambda()
                            .eq(WorkflowsCategory::getWorkflowsCategoryId, c.getWorkflowsCategoryId())
                    );
                    return new WorkflowsPageVo()
                            .setCategoryName(workflowsCategory.getCategoryName())
                            .setCreateTime(c.getCreateTime())
                            .setTitle(c.getTitle())
                            .setNowImage(c.getNowImage())
                            .setWorkflowsId(c.getWorkflowsId())
                            .setOriginalImage(c.getOriginalImage())
                            .setTitle(c.getTitle())
                            .setVisited(l);
                });
    }


    @Override
    public DrawingUploadFileVo uploadWorkflowsComponentFile(final MultipartFile file) {
        try {
            byte[] fileContent = FileCopyUtils.copyToByteArray(file.getInputStream());
            ByteArrayResource resource = new ByteArrayResource(fileContent) {
                @Override
                public String getFilename() {
                    final String originalFileName = file.getOriginalFilename();

                    if (originalFileName != null) {
                        return UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf('.'));
                    }
                    throw new DrawingException("解析文件名称失败");
                }

            };
            final MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("image", resource, MediaType.APPLICATION_OCTET_STREAM);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            final String block = WebClient.builder().baseUrl(drawingConfiguration.getServer()).build().post().uri("/upload/image").headers(httpHeaders -> httpHeaders.addAll(headers)).body(BodyInserters.fromMultipartData(builder.build())).retrieve().onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (clientResponse -> clientResponse.bodyToMono(String.class).flatMap(errorBody -> Mono.error(new DrawingException(parsingError(errorBody)))))).bodyToMono(String.class).block();

            JSONObject json = JSONObject.parseObject(block);
            assert json != null;
            //上传预览图
            String preview = uploadUtil.uploadFile(file, FilePathEnum.TEMP.getDec());

            return new DrawingUploadFileVo().setFileName(json.getString("name")).setPreview(preview);
        } catch (Exception e) {
            log.error("上传文件失败:{}", e.getMessage());
            throw new DrawingException("上传图片失败");
        }
    }

    @Override
    public ParsingWorkflowsVo parsingWorkflowJson(final MultipartFile file) {
        // 检查文件扩展名是否为 .json
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.endsWith(".json")) {
            throw new DrawingException("请上传.json文件");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String jsonString = stringBuilder.toString();
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            ParsingWorkflowsVo vo = new ParsingWorkflowsVo();
            List<ParsingWorkflowsVo.Node> resultList = getNodes(jsonObject);

            final List<ParsingWorkflowsVo.InputNode> inputNodeList = new ArrayList<>();
            // 遍历JSON对象的键
            for (String key : jsonObject.keySet()) {
                JSONObject node = jsonObject.getJSONObject(key);

                // 初始化存储title的map
                ParsingWorkflowsVo.InputNode inputNode = new ParsingWorkflowsVo.InputNode();
                inputNode.setNodeKey(key);
                // 获取_meta的title，如果没有则设置为null
                JSONObject meta = node.getJSONObject("_meta");
                String title = meta != null && meta.containsKey("title") ? meta.getString("title") : null;
                inputNode.setTips(title);
                inputNodeList.add(inputNode);
            }
            vo.setInputNodeList(inputNodeList);
            return vo.setNodeList(resultList).setJson(jsonObject.toString());

        } catch (Exception e) {
            throw new DrawingException("无法正确解析该json文件内容");

        }
    }

    @Override
    public void uploadWorkflows(final UploadWorkflowsDto dto) {
        final Workflows workflows = new Workflows()
                .setWorkflowsCategoryId(dto.getWorkflowsCategoryId())
                .setTitle(dto.getTitle())
                .setJson(dto.getJson())
                .setIntroduced(dto.getIntroduced())
                .setOriginalImage(dto.getOriginalImage())
                .setNowImage(dto.getNowImage())
                .setInputNode(dto.getInputNode())
                .setResultType(dto.getResultType());
        dto.getWorkflowsFormList()
                .forEach(c -> workflowsFormMapper.insert(new WorkflowsForm().setWorkflowsId(workflows.getWorkflowsId()).setType(c.getType()).setTips(c.getTips()).setNodeDigital(c.getNodeDigital()).setNodeKey(c.getNodeKey())));
    }

    private static List<ParsingWorkflowsVo.Node> getNodes(final JSONObject jsonObject) {
        List<ParsingWorkflowsVo.Node> resultList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String node = entry.getKey();
            JSONObject nodeObject = (JSONObject) entry.getValue();
            //获取注释
            JSONObject meta = nodeObject.getJSONObject("_meta");
            JSONObject inputs = nodeObject.getJSONObject("inputs");
            if (inputs != null) {
                if (inputs.containsKey("text")) {
                    ParsingWorkflowsVo.Node resultNode = new ParsingWorkflowsVo.Node();
                    resultNode.setNodeKey(node);
                    resultNode.setType(WorkflowsNodeEnum.TEXT_PROMPT.getDec());
                    resultNode.setNodeDigital("text");
                    resultNode.setTips(meta != null ? meta.getString("title") : null);
                    resultList.add(resultNode);
                } else if (inputs.containsKey("image")) {
                    ParsingWorkflowsVo.Node resultNode = new ParsingWorkflowsVo.Node();
                    resultNode.setNodeKey(node);
                    resultNode.setType(WorkflowsNodeEnum.IMAGE_UPLOAD.getDec());
                    resultNode.setNodeDigital("image");
                    resultNode.setTips(meta != null ? meta.getString("title") : null);
                    resultList.add(resultNode);
                } else if (inputs.containsKey("video")) {
                    ParsingWorkflowsVo.Node resultNode = new ParsingWorkflowsVo.Node();
                    resultNode.setNodeKey(node);
                    resultNode.setType(WorkflowsNodeEnum.VIDEO_UPLOAD.getDec());
                    resultNode.setNodeDigital("video");
                    resultNode.setTips(meta != null ? meta.getString("title") : null);
                    resultList.add(resultNode);
                }

            }
        }
        return resultList;
    }
}
