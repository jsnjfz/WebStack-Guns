package com.cttq.inquiry.service.inquiry.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cttq.framework.common.base.PageResultSet;
import com.cttq.framework.common.base.UserInfo;
import com.cttq.framework.common.base.UserInfoUtils;
import com.cttq.framework.utils.DateUtil;
import com.cttq.inquiry.common.InquiryBizException;
import com.cttq.inquiry.common.constant.*;
import com.cttq.inquiry.common.enums.InquiryEnum;
import com.cttq.inquiry.common.enums.InquiryErrorCodeEnum;
import com.cttq.inquiry.common.enums.MessageTemplateEnum;
import com.cttq.inquiry.common.enums.TraceNodeEnum;
import com.cttq.inquiry.common.utils.NumberUtils;
import com.cttq.inquiry.common.utils.StatusCheckUtil;
import com.cttq.inquiry.converter.inquiry.InquiryItemConverter;
import com.cttq.inquiry.converter.inquiry.InquiryMainConverter;
import com.cttq.inquiry.converter.inquiry.InquirySupplierInfoConverter;
import com.cttq.inquiry.dao.inquiry.InquiryMainMapper;
import com.cttq.inquiry.dto.req.inquiry.*;
import com.cttq.inquiry.dto.resp.inquiry.*;
import com.cttq.inquiry.dto.resp.offer.SupplierOfferRelationResp;
import com.cttq.inquiry.entity.common.InquirySysDictPO;
import com.cttq.inquiry.entity.inquiry.InquiryItemPO;
import com.cttq.inquiry.entity.inquiry.InquiryMainPO;
import com.cttq.inquiry.entity.inquiry.InquiryRoundPO;
import com.cttq.inquiry.entity.supplier.InquirySupplierInfoPO;
import com.cttq.inquiry.service.cache.MaterialService;
import com.cttq.inquiry.service.cache.impl.UnitCache;
import com.cttq.inquiry.service.common.InquirySysDictService;
import com.cttq.inquiry.service.common.InquirySysMessageTemplateService;
import com.cttq.inquiry.service.common.InquirySysTraceLogService;
import com.cttq.inquiry.service.common.dto.MessageUserDto;
import com.cttq.inquiry.service.inquiry.InquiryItemService;
import com.cttq.inquiry.service.inquiry.InquiryMainService;
import com.cttq.inquiry.service.inquiry.InquiryRoundService;
import com.cttq.inquiry.service.offer.InquiryOfferService;
import com.cttq.inquiry.service.supplier.InquirySupplierInfoService;
import com.cttq.materialcenter.domain.master.MaterialMasterDetailsResDO;
import com.cttq.materialcenter.dto.CategoryInfoRspDTO;
import com.cttq.purchase.api.SrmExternalInquiryService;
import com.cttq.purchase.dto.inquiry.req.InquiryReq;
import com.cttq.purchase.dto.inquiry.resp.InquiryPullPurReqDetailResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 询价单主表,包含询价单主要信息(InquiryMain)表服务接口
 *
 * @author 徐晓东
 * @since 2023-04-20 13:25:13
 */
@Slf4j
@Service
public class InquiryMainServiceImpl extends ServiceImpl<InquiryMainMapper, InquiryMainPO>
    implements InquiryMainService {

    @Resource
    private UnitCache unitCache;

    @Resource
    private MaterialService materialService;

    @Resource
    private InquiryItemService inquiryItemService;

    @Resource
    private InquiryRoundService inquiryRoundService;

    @Resource
    private InquiryOfferService inquiryOfferService;

    @Resource
    private InquirySysDictService inquirySysDictService;

    @Resource
    private InquirySysTraceLogService inquirySysTraceLogService;

    @Resource
    private InquirySupplierInfoService inquirySupplierInfoService;

    @Resource
    private InquirySysMessageTemplateService inquirySysMessageTemplateService;

    @Reference(timeout = 60000, registry = "srm", version = "0.0.1")
    private SrmExternalInquiryService srmExternalInquiryService;

    /**
     * 根据条件获取Pr请求对象
     * 
     * @param req 请求对象
     * @return 分页的Pr请求对象
     */
    @Override
    public PageResultSet<GetPrListResp> getPrList(GetPrListReq req) {
        try {
            InquiryReq inquiryReq = new InquiryReq();
            log.info("调用SRM获取请求对象列表,入参{}", JSON.toJSONString(inquiryReq));
            // 调用srm
            PageResultSet<InquiryPullPurReqDetailResp> resp = srmExternalInquiryService.pullPurReqDetail(inquiryReq);
            log.info("调用SRM成功,返参{}", JSON.toJSONString(resp));
            // 转换成功
            return InquiryMainConverter.INSTANCE.srm2Resp(resp);
        } catch (Exception e) {
            log.error("调用SRM失败,错误信息为:{}", ExceptionUtils.getStackTrace(e));
            throw new InquiryBizException(InquiryErrorCodeEnum.THIRD_SYSTEM_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveInquiry(SaveInquiryReq req) {
        // 此处有两种情况 1.该询价单为首次保存,不存在询价单号,此时,将类进行转换后保存至数据库即可
        // 2. 该询价单并非首次保存,存在询价单号,此时,需要更新主表内容,并删除关联表内容,在关联表
        InquiryIdNoDto inquiryIdNoDto;
        if (Objects.nonNull(req.getId()) && StringUtils.isNotBlank(req.getInquiryNo())) {
            // id和单号不为空,则说明非首次保存
            log.info("当前入参id和单号均不为空,非首次保存");
            inquiryIdNoDto = this.updateInquiryTicket(req);
            // 修改节点应当是: 传入节点枚举 单号 下一节点信息
            inquirySysTraceLogService.recordTraceLog(TraceNodeEnum.UPDATE, inquiryIdNoDto.getInquiryNo(), null, null);
        } else {
            // 首次保存
            log.info("当前入参id和单号缺少,为首次保存");
            inquiryIdNoDto = this.firstInsertInquiryTicket(req);
            // 记录节点
            inquirySysTraceLogService.recordTraceLog(TraceNodeEnum.CREATE, inquiryIdNoDto.getInquiryNo(), null, null);
        }
        return inquiryIdNoDto.getInquiryId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long submitInquiry(SubmitInquiryReq req) {
        // 此处由两种情况 1.首次即提交 不存在询价单号 此时需要申请询价单号 其余内容正常保存即可
        // 2. 询价单并非首次保存 存在单号 此时 需要更新主表内容 删除关联表内容 再新增相关信息
        // 处理完毕后 需要发送消息
        InquiryIdNoDto inquiryIdNoDto;
        if (Objects.nonNull(req.getId()) && StringUtils.isNotBlank(req.getInquiryNo())) {
            // id和单号不为空,则说明非首次提交保存
            log.info("id和单号不为空,非首次提交保存");
            inquiryIdNoDto = this.updateSubmitInquiryTicket(req);
            // 记录提交节点
            inquirySysTraceLogService.recordTraceLog(TraceNodeEnum.SUBMIT, inquiryIdNoDto.getInquiryNo(), "供应商报价中",
                null);
        } else {
            // 首次即保存并提交
            inquiryIdNoDto = this.firstSubmitInquiryTicket(req);
            // 记录创建节点和提交节点
            inquirySysTraceLogService.recordTraceLog(TraceNodeEnum.CREATE, inquiryIdNoDto.getInquiryNo(), null, null);
            inquirySysTraceLogService.recordTraceLog(TraceNodeEnum.SUBMIT, inquiryIdNoDto.getInquiryNo(), "供应商报价中",
                null);
        }
        // 调用SRM锁定PR单
        // srmExternalInquiryService.lockPurReqDetail(new InquiryReq());
        log.info("提交并锁定SRM的PR单");
        return inquiryIdNoDto.getInquiryId();
    }

    @Override
    public PageResultSet<GetInquiryListResp> inquiryList(GetInquiryListReq req) {
        // 将创建时间的结束时间 +1天
        Calendar instance = Calendar.getInstance();
        instance.setTime(req.getCreateDateEnd());
        instance.add(Calendar.DAY_OF_MONTH, 1);
        req.setCreateDateEnd(instance.getTime());
        // 添加当前负责人条件 如果为白名单用户 则返回所有内容
        judgeLiablePersonCode(req);
        // 编写SQL,分页查询相关内容
        Page<GetInquiryListReq> page = new Page<>(req.getPageNo(), req.getPageSize());
        IPage<GetInquiryListResp> pageResult = baseMapper.inquiryPageList(page, req);
        // 获取结果列表
        List<GetInquiryListResp> respRecords = pageResult.getRecords();
        // 如果不为空 则进行特殊处理 计算相关内容 填写枚举值
        if (CollectionUtils.isNotEmpty(respRecords)) {
            // 循环外先查询字典表 获取操作对应的列表 并进行分组 取系统配置 key为操作
            List<InquirySysDictPO> inquirySysDictList =
                inquirySysDictService.list(new LambdaQueryWrapper<InquirySysDictPO>()
                    .eq(InquirySysDictPO::getCategoryCode, DictCategoryCodeConstant.OPERATE_CONFIG));
            Map<String, List<InquirySysDictPO>> operateListMap =
                inquirySysDictList.stream().collect(Collectors.groupingBy(InquirySysDictPO::getItemKey));
            // 循环处理
            for (GetInquiryListResp respRecord : respRecords) {
                // 修正状态
                respRecord.setInquiryStatus(getChangedStatus(respRecord.getInquiryStatus(), respRecord.getDeadline()));
                // 设置状态名称
                InquiryEnum.InquiryState.getName(respRecord.getInquiryStatus())
                    .ifPresent(respRecord::setInquiryStatusName);
                // 设置审批状态名称
                InquiryEnum.AuditStatus.getName(respRecord.getAuditStatus()).ifPresent(respRecord::setAuditStatusName);
                // 填充操作按钮
                completeOperateButton(respRecord, operateListMap);
            }
        }
        // 转换为pageResultSet对象
        PageResultSet<GetInquiryListResp> result = new PageResultSet<>();
        // 分页相关内容
        result.setPageNo(req.getPageNo());
        result.setPageSize(req.getPageSize());
        result.setTotalPages((int)pageResult.getPages());
        result.setTotalRecords((int)pageResult.getTotal());
        // 数据
        result.setList(respRecords);
        // 返回
        return result;
    }

    @Override
    public void closeInquiry(CloseInquiryReq req) {
        // 1.查询数据库 获取询比价单
        InquiryMainPO inquiryMain =
            this.getOne(new LambdaQueryWrapper<InquiryMainPO>().eq(InquiryMainPO::getInquiryNo, req.getInquiryNo()));
        // 2.校验询比价单状态 包括审批状态等 确定能否关闭
        if (Objects.isNull(inquiryMain)) {
            log.error("未查询到该询价单,请检查单号:{}", req.getInquiryNo());
            throw new InquiryBizException(InquiryErrorCodeEnum.ORDER_NO_ERROR);
        }
        if (FlagConstant.YES.equals(inquiryMain.getAuditFlag())) {
            log.error("订单:{} 正在审批中,不能关闭", req.getInquiryNo());
            throw new InquiryBizException(InquiryErrorCodeEnum.ORDER_AUDIT_ERROR);
        }
        // 3.根据订单源状态 报价轮次等信息,筛选供应商 更新其状态
        // 可查的所有状态更新为关闭
        List<InquirySupplierInfoPO> inquirySupplierInfoList =
            inquirySupplierInfoService.list(new LambdaQueryWrapper<InquirySupplierInfoPO>()
                .eq(InquirySupplierInfoPO::getInquiryNo, inquiryMain.getInquiryNo())
                .eq(InquirySupplierInfoPO::getEliminateFlag, FlagConstant.NO)
                .eq(InquirySupplierInfoPO::getLatestRoundFlag, FlagConstant.YES));
        // 关闭供应商订单
        List<MessageUserDto> messageUserDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(inquirySupplierInfoList)) {
            for (InquirySupplierInfoPO inquirySupplierInfo : inquirySupplierInfoList) {
                // 需要更新状态 淘汰标识 关闭原因 关闭时间共计四个字段
                inquirySupplierInfo.setStatus(InquiryEnum.SupplierStateEnum.CLOSED.getCode());
                inquirySupplierInfo.setCloseReason("采购方取消询价项目");
                inquirySupplierInfo.setEliminateFlag(FlagConstant.YES);
                inquirySupplierInfo.setEliminateTime(new Date());
                // 新建用于发送消息的对象
                messageUserDtoList.add(new MessageUserDto(inquirySupplierInfo.getSupplierContactName(),
                    inquirySupplierInfo.getSupplierContactMobile(), inquirySupplierInfo.getSupplierContactEmail()));
            }
        }
        // 批量更新
        inquirySupplierInfoService.updateBatchById(inquirySupplierInfoList);
        // 4.修改询比价单状态 并更新定时任务标识
        inquiryMain.setInquiryStatus(InquiryEnum.InquiryState.CLOSED.getCode());
        inquiryMain.setCloseReason(req.getCloseReason());
        inquiryMain.setDeadlineFlag(FlagConstant.YES);
        this.updateById(inquiryMain);
        // 5.记录节点
        inquirySysTraceLogService.recordTraceLog(TraceNodeEnum.CLOSE, inquiryMain.getInquiryNo(), "流程结束", null);
        // 6.对状态更新了的供应商发送消息
        Map<String, String> messageParamMap = new HashMap<>();
        messageParamMap.put(MessageParamConstant.INQUIRY_NAME, inquiryMain.getInquiryName());
        inquirySysMessageTemplateService.sendMessage(messageUserDtoList, MessageTemplateEnum.CLOSE_SUPPLIER,
            messageParamMap);
    }

    @Override
    public GetInquiryDetailResp getInquiryDetail(Long id) {
        // 1. 查询主表 获取数据并转换
        InquiryMainPO inquiryMain = this.getById(id);
        if (Objects.isNull(inquiryMain)) {
            log.error("传入id错误,未查询到详情,id:{}", id);
            throw new InquiryBizException(InquiryErrorCodeEnum.ID_ERROR);
        }
        // 查到了数据 进行转换
        GetInquiryDetailResp resp = InquiryMainConverter.INSTANCE.po2InquiryDetailResp(inquiryMain);
        // 修正状态等数据
        completeInquiryDetailResp(resp);
        // 2. 查询行表 获取数据并转换
        List<InquiryItemPO> list =
            inquiryItemService.list(new LambdaQueryWrapper<InquiryItemPO>().eq(InquiryItemPO::getInquiryId, id));
        List<GetInquiryItemResp> getInquiryItemRespList = InquiryItemConverter.INSTANCE.item2GetInquiryItemResp(list);
        resp.setItemList(getInquiryItemRespList);
        // 3.查询供应商表,获取数据并进行转换
        List<InquirySupplierInfoPO> supplierInfoList =
            inquirySupplierInfoService.list(new LambdaQueryWrapper<InquirySupplierInfoPO>()
                .eq(InquirySupplierInfoPO::getInquiryNo, inquiryMain.getInquiryNo())
                .eq(InquirySupplierInfoPO::getLatestRoundFlag, FlagConstant.YES));
        // 转换
        List<GetInquirySupplierResp> getInquirySupplierRespList =
            InquirySupplierInfoConverter.INSTANCE.info2GetInquirySupplierResp(supplierInfoList);
        resp.setSupplierList(getInquirySupplierRespList);
        // 4.判断主表状态,如果为待发布,设置完按钮后返回
        if (InquiryEnum.InquiryState.BEFORE_SUBMIT.getCode().equals(resp.getInquiryStatus())) {
            // 当前为待发布状态 直接返回
            return resp;
        }
        // 5.如果为已发布 则需要查询报价信息 并根据报价信息返回相应的内容
        // 批量查报价表 并转换为map
        List<SupplierOfferRelationResp> offerList =
            inquiryOfferService.getSupplierOfferRelationList(resp.getInquiryNo());
        Map<String, List<SupplierOfferRelationResp>> supplierOfferMap =
            offerList.stream().collect(Collectors.groupingBy(SupplierOfferRelationResp::getSupplierCode));
        // 基础循环为 供应商的循环
        for (GetInquirySupplierResp getInquirySupplierResp : getInquirySupplierRespList) {
            List<OfferRoundResp> offerRoundRespList = new ArrayList<>();
            List<SupplierOfferRelationResp> inquiryOfferList =
                supplierOfferMap.get(getInquirySupplierResp.getSupplierCode());
            // 开始创建轮次信息
            for (int i = 1; i <= inquiryMain.getRound(); i++) {
                OfferRoundResp offerRoundResp = new OfferRoundResp();
                // 填充内容
                completeOfferRoundResp(offerRoundResp, i, inquiryOfferList, getInquiryItemRespList,
                    getInquirySupplierResp);
                offerRoundRespList.add(offerRoundResp);
            }
            getInquirySupplierResp.setOfferRoundList(offerRoundRespList);
        }
        return resp;
    }

    @Override
    public void bidOpening(Long id) {
        InquiryMainPO inquiryMainPO = this.getById(id);
        if (Objects.isNull(inquiryMainPO)) {
            log.error("当前id{}在询比价单主表不存在,未查到相应数据", id);
            throw new InquiryBizException(InquiryErrorCodeEnum.ID_ERROR);
        }
        // 校验状态 即只有报价中 和 报价中已截止可以开标 否则抛出异常
        StatusCheckUtil.checkStatus(inquiryMainPO.getInquiryStatus(), InquiryEnum.InquiryState.OFFERING.getCode(),
            InquiryEnum.InquiryState.OFFERING_EXPIRED.getCode());
        // 1. 校验当前轮次报价供应商数量是否符合要求
        List<InquirySupplierInfoPO> supplierInfoList =
            inquirySupplierInfoService.list(new LambdaQueryWrapper<InquirySupplierInfoPO>()
                .eq(InquirySupplierInfoPO::getInquiryNo, inquiryMainPO.getInquiryNo())
                .eq(InquirySupplierInfoPO::getRound, inquiryMainPO.getRound())
                .eq(InquirySupplierInfoPO::getEliminateFlag, FlagConstant.NO)
                .eq(InquirySupplierInfoPO::getLatestRoundFlag, FlagConstant.YES));
        // 将报价的供应商过滤出来
        List<InquirySupplierInfoPO> offeredSupplierList =
            supplierInfoList.stream().filter(s -> InquiryEnum.SupplierStateEnum.OFFERED.getCode().equals(s.getStatus()))
                .collect(Collectors.toList());
        if (offeredSupplierList.size() < inquiryMainPO.getMinSupplierNum()) {
            log.error("当前轮次报价供应商数量不足, inquiryNo:{}, round:{}, minSupplierNum:{},报价个数{}", inquiryMainPO.getInquiryNo(),
                inquiryMainPO.getRound(), inquiryMainPO.getMinSupplierNum(), offeredSupplierList.size());
            throw new InquiryBizException(InquiryErrorCodeEnum.SUPPLIER_NOT_ENOUGH, inquiryMainPO.getInquiryName());
        }
        // 2. 如果符合 则修改主表状态 改为待评标
        inquiryMainPO.setInquiryStatus(InquiryEnum.InquiryState.BEFORE_EVALUATION.getCode());
        this.updateById(inquiryMainPO);
        // 3. 修改供应商行表状态
        List<InquirySupplierInfoPO> otherSupplierList = supplierInfoList.stream()
            .filter(s -> !InquiryEnum.SupplierStateEnum.OFFERED.getCode().equals(s.getStatus()))
            .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(otherSupplierList)) {
            List<InquirySupplierInfoPO> updateSupplierList = updateOtherSupplierStatus(otherSupplierList);
            // 批量更新至供应商表
            if (CollectionUtils.isNotEmpty(updateSupplierList)) {
                inquirySupplierInfoService.updateBatchById(updateSupplierList);
            }
        }
        // 4. 记录节点
        inquirySysTraceLogService.recordTraceLog(TraceNodeEnum.OPENING, inquiryMainPO.getInquiryNo(), "待评标", null);
        // 5. 通知审计人员
        List<InquirySysDictPO> auditPersonList = inquirySysDictService.list(new LambdaQueryWrapper<InquirySysDictPO>()
            .eq(InquirySysDictPO::getCategoryCode, DictCategoryCodeConstant.USER_CONFIG)
            .eq(InquirySysDictPO::getItemKey, DictItemKeyConstant.AUDIT_PERSON));
        List<MessageUserDto> messageUserDtoList = auditPersonList.stream()
            .map(p -> new MessageUserDto(p.getItemValue(), p.getItemDesc())).collect(Collectors.toList());
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put(MessageParamConstant.INQUIRY_NAME, inquiryMainPO.getInquiryName());
        inquirySysMessageTemplateService.sendMessage(messageUserDtoList, MessageTemplateEnum.OPENING_BID, messageMap);
    }

    @Override
    public Boolean checkBidOpening(Long id) {
        // 1. 根据此单号查询主表 获取询价单
        InquiryMainPO inquiryMainPO = this.getById(id);
        if (Objects.isNull(inquiryMainPO)) {
            log.error("根据查询单号查询询价单失败，查询id为{}", id);
            throw new InquiryBizException(InquiryErrorCodeEnum.ID_ERROR);
        }
        // 2. 根据询价单号和轮次 查询供应商表 获取供应商列表
        List<InquirySupplierInfoPO> supplierInfoList =
            inquirySupplierInfoService.list(new LambdaQueryWrapper<InquirySupplierInfoPO>()
                .eq(InquirySupplierInfoPO::getInquiryNo, inquiryMainPO.getInquiryNo())
                .eq(InquirySupplierInfoPO::getRound, inquiryMainPO.getRound())
                .eq(InquirySupplierInfoPO::getEliminateFlag, FlagConstant.NO)
                .eq(InquirySupplierInfoPO::getLatestRoundFlag, FlagConstant.YES));
        // 3. 筛选供应商状态为 待报名和待报价的供应商 如果不为空 则返回TRUE 否则 返回FALSE
        List<InquirySupplierInfoPO> supplierList = supplierInfoList.stream()
            .filter(p -> InquiryEnum.SupplierStateEnum.BEFORE_SIGN_UP.getCode().equals(p.getStatus())
                || InquiryEnum.SupplierStateEnum.BEFORE_OFFER.getCode().equals(p.getStatus()))
            .collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(supplierList);
    }

    @Override
    public ParseExcelResp parsePurchaseExcel(List<Map<String, Object>> mapList) {
        log.info(JSON.toJSONString(mapList));
        // 返回对象
        ParseExcelResp resp = new ParseExcelResp();
        resp.setMaterialCodeFlag(FlagConstant.NO);
        List<ParseExcelDataResp> respList = new ArrayList<>();
        resp.setDataRespList(respList);
        // 判空处理
        if (CollectionUtils.isEmpty(mapList)) {
            log.info("传入文件内容为空,直接返回");
            resp.setDataRespList(Collections.emptyList());
            return resp;
        }
        // 主循环
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> lineMap = mapList.get(i);
            // 先校验当前行是否是空行 如果是空行 则直接跳过
            if (checkLineMap(lineMap)) {
                continue;
            }
            // 非空行,则根据是否有物料编码判断该如何校验
            String materialCode = String.valueOf(lineMap.get(ImportExcelHeadConstant.MATERIAL_CODE));
            if (StringUtils.isEmpty(materialCode)) {
                // 物料编码为空 走为空的校验
                ParseExcelDataResp respData = parseWithNoMaterialCode(i + 2, lineMap);
                respList.add(respData);
            } else {
                // 物料编码不为空 走不为空的校验
                resp.setMaterialCodeFlag(FlagConstant.YES);
                ParseExcelDataResp respData = parseWithMaterialCode(i + 2, lineMap);
                respList.add(respData);
            }
        }
        return resp;
    }

    private ParseExcelDataResp parseWithMaterialCode(int lineNo, Map<String, Object> lineMap) {
        // 有物料编码 先获取物料编码
        String materialCode = String.valueOf(lineMap.get(ImportExcelHeadConstant.MATERIAL_CODE));
        // 根据物料编码查询物料
        MaterialMasterDetailsResDO materialMasterDetail = materialService.getMaterialMasterDetails(materialCode);
        if (Objects.isNull(materialMasterDetail)) {
            log.error("物料中心未查到该物料,返回空");
            throw new InquiryBizException(InquiryErrorCodeEnum.EXCEL_PARSE_ERROR, lineNo, "物料编码错误,未在物料中心查到该数据,请检查");
        }
        // 物料编码查询成功
        ParseExcelDataResp resp = parseOtherData(lineNo, lineMap);
        // 其他解析成功 填充和物料相关的内容
        resp.setMaterialName(materialMasterDetail.getMaterialName());
        resp.setPurContent(materialMasterDetail.getMaterialDesc());
        resp.setSpecification(materialMasterDetail.getCommonSpec());
        resp.setCategoryCode(materialMasterDetail.getMaterialCategory());
        resp.setCategoryName(materialMasterDetail.getMaterialCategoryName());
        resp.setCategoryType(materialMasterDetail.getMaterialBaseCategory());
        resp.setFullCategoryName(String.join("-", materialMasterDetail.getMaterialBaseCategoryName(),
            materialMasterDetail.getFirstCategoryName(), materialMasterDetail.getSecondCategoryName(),
            materialMasterDetail.getMaterialCategoryName()));
        String unitCode = String.valueOf(Optional.ofNullable(lineMap.get(ImportExcelHeadConstant.UNIT_CODE))
            .orElseGet(materialMasterDetail::getBaseMeasureUnit));
        resp.setUnitCode(unitCode);
        resp.setUnitName(Optional.ofNullable(unitCache.getUnitNameByCode(unitCode)).orElseThrow(
            () -> new InquiryBizException(InquiryErrorCodeEnum.EXCEL_PARSE_ERROR, lineNo, "物料中心未查到该单位编码,请检查")));
        return resp;
    }

    private ParseExcelDataResp parseOtherData(int lineNo, Map<String, Object> lineMap) {
        ParseExcelDataResp resp = new ParseExcelDataResp();
        // 解析采购数量
        BigDecimal requireQuantity;
        try {
            requireQuantity = new BigDecimal(
                String.valueOf(Optional.ofNullable(lineMap.get(ImportExcelHeadConstant.REQUIRE_QUANTITY)).orElseThrow(
                    () -> new InquiryBizException(InquiryErrorCodeEnum.EXCEL_PARSE_ERROR, lineNo, "采购数量不能为空"))));
        } catch (InquiryBizException e) {
            throw e;
        } catch (Exception e) {
            log.error("采购数量格式不正确,解析失败,报错信息为{}", ExceptionUtils.getStackTrace(e));
            throw new InquiryBizException(InquiryErrorCodeEnum.EXCEL_PARSE_ERROR, lineNo, "采购数量格式不正确,请检查");
        }
        if (requireQuantity.scale() > 3) {
            log.error("采购数量小数位数超过3位");
            throw new InquiryBizException(InquiryErrorCodeEnum.EXCEL_PARSE_ERROR, lineNo, "采购数量小数位过多,请检查");
        } else {
            resp.setRequireQuantity(requireQuantity);
        }
        // 预估单价 如果有 则进行处理
        Optional.ofNullable(lineMap.get(ImportExcelHeadConstant.ESTIMATED_PRICE)).ifPresent(p -> {
            BigDecimal price = new BigDecimal(String.valueOf(p));
            if (price.scale() > 2) {
                price = price.setScale(2, RoundingMode.DOWN);
            }
            // 设置预估单价
            resp.setEstimatedPrice(price);
        });
        // 期望到货日期
        Optional.ofNullable(lineMap.get(ImportExcelHeadConstant.EXPECTED_DATE)).ifPresent(d -> {
            Date expectedDate;
            try {
                expectedDate = (Date)d;
            } catch (Exception e) {
                log.error("期望到货日期转换错误,其格式异常");
                throw new InquiryBizException(InquiryErrorCodeEnum.EXCEL_PARSE_ERROR, lineNo, "期望到货日期格式错误,请按照日期格式进行填写");
            }
            // 将期望到货日期+1天 然后和当前时间比较
            Calendar instance = Calendar.getInstance();
            instance.setTime(expectedDate);
            instance.set(Calendar.HOUR_OF_DAY, 0);
            instance.set(Calendar.MINUTE, 0);
            instance.set(Calendar.SECOND, 0);
            instance.add(Calendar.DAY_OF_MONTH, 1);
            if (instance.getTimeInMillis() <= System.currentTimeMillis()) {
                log.error("期望到货日期小于当前日期");
                throw new InquiryBizException(InquiryErrorCodeEnum.EXCEL_PARSE_ERROR, lineNo, "期望到货日期必须大于等于当前日期，请确认");
            }
            resp.setExpectedDate(instance.getTime());
        });
        // 品牌 厂家 生产地址 备注
        Optional.ofNullable(lineMap.get(ImportExcelHeadConstant.GOODS_BAND))
            .ifPresent(b -> resp.setGoodsBand(String.valueOf(b)));
        Optional.ofNullable(lineMap.get(ImportExcelHeadConstant.PRODUCE_FACTORY))
            .ifPresent(f -> resp.setProduceFactory(String.valueOf(f)));
        Optional.ofNullable(lineMap.get(ImportExcelHeadConstant.PRODUCE_ADDRESS))
            .ifPresent(a -> resp.setProduceAddress(String.valueOf(a)));
        Optional.ofNullable(lineMap.get(ImportExcelHeadConstant.OTHER_REMARK))
            .ifPresent(r -> resp.setOtherRemark(String.valueOf(r)));
        return resp;
    }

    private ParseExcelDataResp parseWithNoMaterialCode(int lineNo, Map<String, Object> lineMap) {
        ParseExcelDataResp resp = parseOtherData(lineNo, lineMap);
        // 解析内容/项目名称
        Optional.ofNullable(lineMap.get(ImportExcelHeadConstant.SPECIFICATION))
            .ifPresent(p -> resp.setSpecification(String.valueOf(p)));
        // 解析品类
        String categoryCode = String.valueOf(Optional.ofNullable(lineMap.get(ImportExcelHeadConstant.CATEGORY_CODE))
            .orElseThrow(() -> new InquiryBizException(InquiryErrorCodeEnum.EXCEL_PARSE_ERROR, lineNo, "品类不能为空")));
        CategoryInfoRspDTO allSupCategoryInfo = materialService.getAllSupCategoryInfo(categoryCode);
        allSupCategoryInfo = Optional.ofNullable(allSupCategoryInfo)
            .orElseThrow(() -> new InquiryBizException(InquiryErrorCodeEnum.EXCEL_PARSE_ERROR, lineNo, "无效的品类编码"));
        resp.setCategoryType(allSupCategoryInfo.getBaseCategoryId());
        resp.setCategoryCode(allSupCategoryInfo.getThirdCategoryId());
        resp.setCategoryName(allSupCategoryInfo.getThirdCategoryName());
        resp.setFullCategoryName(
            String.join("-", allSupCategoryInfo.getBaseCategoryName(), allSupCategoryInfo.getFirstCategoryName(),
                allSupCategoryInfo.getSecondCategoryName(), allSupCategoryInfo.getThirdCategoryName()));
        // 规格型号
        Optional.ofNullable(lineMap.get(ImportExcelHeadConstant.SPECIFICATION))
            .ifPresent(s -> resp.setSpecification(String.valueOf(s)));
        // 解析单位 先判断有没有此字段 先获取单位map
        String unitCode = String.valueOf(Optional.ofNullable(lineMap.get(ImportExcelHeadConstant.UNIT_CODE))
            .orElseThrow(() -> new InquiryBizException(InquiryErrorCodeEnum.EXCEL_PARSE_ERROR, lineNo, "单位编码不能为空")));
        resp.setUnitCode(unitCode);
        resp.setUnitName(Optional.ofNullable(unitCache.getUnitNameByCode(unitCode)).orElseThrow(
            () -> new InquiryBizException(InquiryErrorCodeEnum.EXCEL_PARSE_ERROR, lineNo, "物料中心未查到该单位编码,请检查")));
        return null;
    }

    private boolean checkLineMap(Map<String, Object> lineMap) {
        // 校验是否是标准模板
        List<String> headStringSet = new ArrayList<>(lineMap.keySet());
        if (!CollectionUtils.isEqualCollection(headStringSet, ImportExcelHeadConstant.HEAD_LIST)) {
            // 非标准模板
            log.error("传入行数据为{},其key非标准模板数据", JSON.toJSONString(lineMap));
            throw new InquiryBizException(InquiryErrorCodeEnum.TEMPLATE_ERROR);
        }
        // 校验是否是空行
        for (String head : ImportExcelHeadConstant.HEAD_LIST) {
            // 进行处理 如果所有行转换为String后都为空 则说明该行为空行
            if (StringUtils.isNotEmpty(String.valueOf(lineMap.get(head)))) {
                return false;
            }
        }
        return true;
    }

    private List<InquirySupplierInfoPO> updateOtherSupplierStatus(List<InquirySupplierInfoPO> otherSupplierList) {
        List<InquirySupplierInfoPO> inquirySupplierInfoList = new ArrayList<>();
        for (InquirySupplierInfoPO inquirySupplierInfo : otherSupplierList) {
            // 此处 判断供应商的状态
            // 如果为 待报名状态 则将主状态更新为关闭 关闭原因为 未在规定时间内报名
            if (InquiryEnum.SupplierStateEnum.BEFORE_SIGN_UP.getCode().equals(inquirySupplierInfo.getStatus())) {
                // 更新状态 为已关闭
                inquirySupplierInfo.setStatus(InquiryEnum.SupplierStateEnum.CLOSED.getCode());
                // 关闭原因
                inquirySupplierInfo.setCloseReason("未在规定时间内报名");
                // 淘汰标识
                inquirySupplierInfo.setEliminateFlag(FlagConstant.YES);
                // 淘汰时间
                inquirySupplierInfo.setEliminateTime(new Date());
            }
            // 如果为 待报价状态 则将主状态更新为关闭 关闭原因为 未在规定时间内报价
            if (InquiryEnum.SupplierStateEnum.BEFORE_OFFER.getCode().equals(inquirySupplierInfo.getStatus())) {
                // 更新状态 为已关闭
                inquirySupplierInfo.setStatus(InquiryEnum.SupplierStateEnum.CLOSED.getCode());
                // 关闭原因
                inquirySupplierInfo.setCloseReason("未在规定时间内报价");
                // 淘汰标识
                inquirySupplierInfo.setEliminateFlag(FlagConstant.YES);
                // 淘汰时间
                inquirySupplierInfo.setEliminateTime(new Date());
            }
            // 其他状态 则不更新
            inquirySupplierInfoList.add(inquirySupplierInfo);
        }
        return inquirySupplierInfoList;
    }

    private void completeOfferRoundResp(OfferRoundResp offerRoundResp, Integer round,
        List<SupplierOfferRelationResp> offerList, List<GetInquiryItemResp> itemList,
        GetInquirySupplierResp supplierResp) {
        // 轮次
        offerRoundResp.setRound(round);
        if (CollectionUtils.isEmpty(offerList)) {
            // 当前用户无报价信息 则判断当前供应商状态 如果为已淘汰 则为无需报价 如果为未淘汰 则设置为待报价
            if (FlagConstant.YES.equals(supplierResp.getEliminateFlag())) {
                // 已淘汰 设置为无需报价
                offerRoundResp.setOfferStatus(OfferStatusConstant.NO_NEED);
            } else {
                // 未淘汰 设置为未报价
                offerRoundResp.setOfferStatus(OfferStatusConstant.NO_OFFER);
            }
            return;
        }
        // 报价不为空 则需要根据轮次分组
        Map<Integer, List<SupplierOfferRelationResp>> roundOfferMap =
            offerList.stream().collect(Collectors.groupingBy(SupplierOfferRelationResp::getRound));
        // 取当前轮次数据
        List<SupplierOfferRelationResp> roundOfferList = roundOfferMap.get(round);
        // 如果当前轮次报价为空
        if (CollectionUtils.isEmpty(roundOfferList)) {
            if (FlagConstant.YES.equals(supplierResp.getEliminateFlag())) {
                // 已淘汰 设置为无需报价
                offerRoundResp.setOfferStatus(OfferStatusConstant.NO_NEED);
            } else {
                // 未淘汰 设置为未报价
                offerRoundResp.setOfferStatus(OfferStatusConstant.NO_OFFER);
            }
            return;
        }
        // 当前报价轮次信息不为空
        offerRoundResp.setOfferStatus(OfferStatusConstant.OFFERED);
        offerRoundResp.setTotalItemCount(itemList.size());
        offerRoundResp.setOfferItemCount(roundOfferList.size());
        // 生成行id列表
        List<Long> itemIdList =
            roundOfferList.stream().map(SupplierOfferRelationResp::getInquiryItemId).collect(Collectors.toList());
        // 内部列表
        List<ItemOfferStatusResp> itemOfferStatusRespList = new ArrayList<>();
        // 主循环为 行列标
        for (GetInquiryItemResp getInquiryItemResp : itemList) {
            ItemOfferStatusResp itemOfferStatusResp = new ItemOfferStatusResp();
            itemOfferStatusResp.setInquiryItemId(getInquiryItemResp.getId());
            itemOfferStatusResp.setPurContent(getInquiryItemResp.getPurContent());
            // 如果报价表中包含当前行id 则报价了 否则未报价
            itemOfferStatusResp.setOfferStatusFlag(
                itemIdList.contains(getInquiryItemResp.getId()) ? FlagConstant.YES : FlagConstant.NO);
            itemOfferStatusRespList.add(itemOfferStatusResp);
        }
        offerRoundResp.setItemOfferStatusList(itemOfferStatusRespList);
    }

    private void completeInquiryDetailResp(GetInquiryDetailResp resp) {
        // 修正状态
        resp.setInquiryStatus(getChangedStatus(resp.getInquiryStatus(), resp.getDeadline()));
        // 设置状态名称
        InquiryEnum.InquiryState.getName(resp.getInquiryStatus()).ifPresent(resp::setInquiryStatusName);
        // 填充操作按钮
        log.info("当前状态为:{},填充操作按钮", resp.getInquiryStatus());
        List<InquirySysDictPO> inquirySysDictList =
            inquirySysDictService.list(new LambdaQueryWrapper<InquirySysDictPO>()
                .eq(InquirySysDictPO::getCategoryCode, DictCategoryCodeConstant.OPERATE_CONFIG)
                .eq(InquirySysDictPO::getItemKey,
                    String.join("", DictItemKeyConstant.DETAIL_OPERATE_PREFIX, resp.getInquiryStatus())));
        Map<String, String> operateMap = inquirySysDictList.stream()
            .collect(Collectors.toMap(InquirySysDictPO::getItemValue, InquirySysDictPO::getItemDesc));
        resp.setButtonMap(operateMap);
    }

    private String getChangedStatus(String status, Date deadline) {
        // 有两种超时状态需要处理
        if (InquiryEnum.InquiryState.BEFORE_OFFER.codeEquals(status)
            && System.currentTimeMillis() >= deadline.getTime()) {
            // 如果状态为待报价 且当前时间 超过了截止时间 则将状态置为 报价已截止 21
            return InquiryEnum.InquiryState.OFFER_EXPIRED.getCode();
        }
        if (InquiryEnum.InquiryState.OFFERING.codeEquals(status) && System.currentTimeMillis() >= deadline.getTime()) {
            // 如果状态为报价中 且当前时间 超过了截止时间 则将状态置为 报价已截止 31
            return InquiryEnum.InquiryState.OFFERING_EXPIRED.getCode();
        }
        // 如果都不符合,则返回原状态
        return status;
    }

    private void completeOperateButton(GetInquiryListResp respRecord,
        Map<String, List<InquirySysDictPO>> operateListMap) {
        // 1.判断当前登录人员和负责人是否相同,如果不相同,则直接返回
        UserInfo userInfo = UserInfoUtils.getUserInfo();
        if (!Objects.equals(respRecord.getLiablePersonCode(), userInfo.getUserCode())) {
            return;
        }
        // 相同 则根据传入map 取值
        List<InquirySysDictPO> inquirySysDictList =
            operateListMap.get(String.join("", DictItemKeyConstant.INQUIRY_PREFIX, respRecord.getInquiryStatus()));
        if (CollectionUtils.isEmpty(inquirySysDictList)) {
            return;
        }
        Map<String, String> resultMap = inquirySysDictList.stream()
            .collect(Collectors.toMap(InquirySysDictPO::getItemValue, InquirySysDictPO::getItemDesc));
        // 设置map
        respRecord.setOperateMap(resultMap);
    }

    private void judgeLiablePersonCode(GetInquiryListReq req) {
        // 1. 判断当前登录人员是否为白名单人员 如果是 则req的负责人置空 否则req的负责人设置为当前登录人员
        // 查询字典表 获取白名单人员
        List<InquirySysDictPO> inquirySysDictList =
            inquirySysDictService.list(new LambdaQueryWrapper<InquirySysDictPO>()
                .eq(InquirySysDictPO::getCategoryCode, DictCategoryCodeConstant.SYSTEM_CONFIG)
                .eq(InquirySysDictPO::getItemKey, DictItemKeyConstant.WHITE_LIST));
        // 进行过滤 取出白名单中的编码
        List<String> whiteList =
            inquirySysDictList.stream().map(InquirySysDictPO::getItemValue).collect(Collectors.toList());
        // 当前登录人员
        UserInfo userInfo = UserInfoUtils.getUserInfo();
        if (CollectionUtils.isEmpty(whiteList) || !whiteList.contains(userInfo.getUserCode())) {
            log.info("当前未配置白名单人员或当前人员{}_{}不在在白名单中", userInfo.getUserCode(), userInfo.getUserName());
            req.setLiablePersonCode(userInfo.getUserCode());
        } else {
            log.info("当前在白名单中，当前人员{}_{}", userInfo.getUserCode(), userInfo.getUserName());
            req.setLiablePersonCode(null);
        }
    }

    private InquiryIdNoDto updateSubmitInquiryTicket(SubmitInquiryReq req) {
        // 先根据id查询数据库 校验数据的合法性
        InquiryMainPO byId = this.getById(req.getId());
        if (Objects.isNull(byId)) {
            // 如果查询结果为空 则抛出异常
            log.error("传入id错误,该数据不存在,传入id为{}", req.getId());
            throw new InquiryBizException(InquiryErrorCodeEnum.ID_ERROR);
        }
        // 校验状态 不为待提交 则报错
        if (!InquiryEnum.InquiryState.BEFORE_SUBMIT.getCode().equals(byId.getInquiryStatus())) {
            log.error("当前要提交的单据状态不支持该操作,当前单据状态为{}", byId.getInquiryStatus());
            throw new InquiryBizException(InquiryErrorCodeEnum.STATUS_ERROR);
        }
        // 转换主体部分
        InquiryMainPO inquiryMain = InquiryMainConverter.INSTANCE.submitReq2Po(req);
        // 批量删除行表和供应商表内容
        batchRemoveItemAndSupplier(inquiryMain.getId(), inquiryMain.getInquiryNo());
        // 填充主表内容
        List<MessageUserDto> auditMessageUserList = completeSubmitInquiryMain(inquiryMain);
        // 更新主表内容
        this.updateById(inquiryMain);
        log.info("主表内容更新完毕");
        // 保存子表内容
        List<MessageUserDto> supplierMessageUserList =
            batchSubmitOthers(req.getItemList(), req.getSupplierList(), inquiryMain);
        log.info("行表内容更新完毕");
        // 发送消息
        submitSendMessage(inquiryMain, auditMessageUserList, supplierMessageUserList);
        log.info("消息发送完毕");
        return new InquiryIdNoDto(inquiryMain.getId(), inquiryMain.getInquiryNo());
    }

    private void batchRemoveItemAndSupplier(Long inquiryId, String inquiryNo) {
        List<InquiryItemPO> inquiryItemList =
            inquiryItemService.list(new LambdaQueryWrapper<InquiryItemPO>().eq(InquiryItemPO::getInquiryId, inquiryId));
        // 删除
        if (CollectionUtils.isNotEmpty(inquiryItemList)) {
            inquiryItemService.removeBatchByIds(inquiryItemList);
        }
        // 供应商表
        List<InquirySupplierInfoPO> supplierInfoList = inquirySupplierInfoService
            .list(new LambdaQueryWrapper<InquirySupplierInfoPO>().eq(InquirySupplierInfoPO::getInquiryNo, inquiryNo));
        // 删除
        if (CollectionUtils.isNotEmpty(supplierInfoList)) {
            inquirySupplierInfoService.removeBatchByIds(supplierInfoList);
        }
    }

    private InquiryIdNoDto firstSubmitInquiryTicket(SubmitInquiryReq req) {
        // 转换主体部分
        InquiryMainPO inquiryMain = InquiryMainConverter.INSTANCE.submitReq2Po(req);
        // 设置单号
        // String inquiryNo = numberUtil.getInquiryNo();
        // log.info("提交时获取单号成功,获取的单号为:{}", inquiryNo);
        // inquiryMain.setInquiryNo(inquiryNo);
        List<MessageUserDto> messageUserList = completeSubmitInquiryMain(inquiryMain);
        // 主体部分存表
        this.save(inquiryMain);
        String inquiryNo = NumberUtils.getInquiryNo(inquiryMain.getId());
        inquiryMain.setInquiryNo(inquiryNo);
        this.updateById(inquiryMain);

        // 批量保存行表和供应商表部分
        List<MessageUserDto> supplierMessageUserList =
            batchSubmitOthers(req.getItemList(), req.getSupplierList(), inquiryMain);
        // 发送消息
        submitSendMessage(inquiryMain, messageUserList, supplierMessageUserList);
        // 返回询比价单号 便于测试取值
        return new InquiryIdNoDto(inquiryMain.getId(), inquiryMain.getInquiryNo());
    }

    private void submitSendMessage(InquiryMainPO inquiryMain, List<MessageUserDto> auditMessageUserList,
        List<MessageUserDto> supplierMessageUserList) {
        // 发送信息给审计人员
        Map<String, String> submitAuditParamMap = new HashMap<>(8);
        submitAuditParamMap.put(MessageParamConstant.INQUIRY_NAME, inquiryMain.getInquiryName());
        inquirySysMessageTemplateService.sendMessage(auditMessageUserList, MessageTemplateEnum.SUBMIT_AUDIT,
            submitAuditParamMap);
        // 发送信息给供应商
        Map<String, String> submitSupplierParamMap = new HashMap<>(8);
        submitSupplierParamMap.put(MessageParamConstant.INQUIRY_NAME, inquiryMain.getInquiryName());
        submitSupplierParamMap.put(MessageParamConstant.DEADLINE,
            DateUtil.format(inquiryMain.getDeadline(), DateUtil.TIMESTAMP_FORMAT));
        inquirySysMessageTemplateService.sendMessage(supplierMessageUserList, MessageTemplateEnum.SUBMIT_SUPPLIER,
            submitSupplierParamMap);
    }

    private List<MessageUserDto> completeSubmitInquiryMain(InquiryMainPO inquiryMain) {
        // 设置状态为 待报价
        inquiryMain.setInquiryStatus(InquiryEnum.InquiryState.BEFORE_OFFER.getCode());
        // 首次发布 报价轮次为1
        inquiryMain.setRound(1);
        // 提交时间 为当前时间
        inquiryMain.setSubmitTime(new Date());
        // 负责人
        // ---------- AI 辅助校验 ------------
        inquiryMain.setLiablePersonCode(UserInfoUtils.getUserInfo().getUserCode());
        inquiryMain.setLiablePersonName(UserInfoUtils.getUserInfo().getUserName());
        // 查询字典表 获取供应商每轮允许出价次数 和重要提示等
        List<InquirySysDictPO> dictList = inquirySysDictService.list(new LambdaQueryWrapper<InquirySysDictPO>()
            .eq(InquirySysDictPO::getCategoryCode, DictCategoryCodeConstant.USER_CONFIG)
            .orderByDesc(InquirySysDictPO::getCreateDate));
        if (CollectionUtils.isEmpty(dictList)) {
            log.info("字典表缺少配置项");
            throw new InquiryBizException(InquiryErrorCodeEnum.CONFIG_LOSS);
        }
        // 分组
        Map<String, List<InquirySysDictPO>> inquiryDictMap =
            dictList.stream().collect(Collectors.groupingBy(InquirySysDictPO::getItemKey));
        // 设置最小供应商数量 该配置为系统配置
        List<InquirySysDictPO> dictConfigList = inquirySysDictService.list(new LambdaQueryWrapper<InquirySysDictPO>()
            .eq(InquirySysDictPO::getCategoryCode, DictCategoryCodeConstant.SYSTEM_CONFIG)
            .eq(InquirySysDictPO::getItemKey, DictItemKeyConstant.MIN_SUPPLIER_NUM));
        if (CollectionUtils.isNotEmpty(dictConfigList)) {
            inquiryMain.setMinSupplierNum(Integer.parseInt(dictConfigList.get(0).getItemValue()));
        }
        // 设置供应商每轮允许出价次数
        Optional.ofNullable(inquiryDictMap.get(DictItemKeyConstant.SUPPLIER_OFFER_COUNT))
            .ifPresent(list -> inquiryMain.setSupplierOfferCount(Integer.parseInt(list.get(0).getItemValue())));
        // 重要提示
        Optional.ofNullable(inquiryDictMap.get(DictItemKeyConstant.IMPORTANT_MESSAGE))
            .ifPresent(list -> inquiryMain.setImportantMessage(list.get(0).getItemValue()));
        // 审批标记 无审批中内容
        inquiryMain.setAuditFlag(FlagConstant.NO);
        // 返回审计人员 用于发送消息 审计人员可多选 故此处需要使用列表
        List<MessageUserDto> messageUserList = new ArrayList<>();
        Optional.ofNullable(inquiryDictMap.get(DictItemKeyConstant.AUDIT_PERSON)).ifPresent(
            list -> list.forEach(v -> messageUserList.add(new MessageUserDto(v.getItemValue(), v.getItemDesc()))));
        return messageUserList;
    }

    private List<MessageUserDto> batchSubmitOthers(List<SaveInquiryPrReq> reqList,
        List<SaveInquirySupplierReq> supplierList, InquiryMainPO inquiryMain) {
        // 1.保存至 行表
        batchSaveItemPo(InquiryItemConverter.INSTANCE.toList(reqList), inquiryMain.getId(), inquiryMain.getInquiryNo());
        // 2. 保存至 轮次表
        InquiryRoundPO inquiryRound = new InquiryRoundPO();
        inquiryRound.setInquiryNo(inquiryMain.getInquiryNo());
        inquiryRound.setRound(inquiryMain.getRound());
        inquiryRound.setStartTime(new Date());
        inquiryRound.setDeadline(inquiryMain.getDeadline());
        // 保存
        inquiryRoundService.save(inquiryRound);
        // 3. 保存至 供应商表 并设置好状态
        List<MessageUserDto> messageUserList = new ArrayList<>();
        // 转为表对象实体
        if (CollectionUtils.isNotEmpty(supplierList)) {
            // 转换为表对象实体
            List<InquirySupplierInfoPO> inquirySupplierInfoList =
                InquirySupplierInfoConverter.INSTANCE.toList(supplierList);
            // 完善相关内容
            for (InquirySupplierInfoPO inquirySupplierInfo : inquirySupplierInfoList) {
                // 表内容
                inquirySupplierInfo.setInquiryNo(inquiryMain.getInquiryNo());
                inquirySupplierInfo.setRound(inquiryMain.getRound());
                inquirySupplierInfo.setStatus(InquiryEnum.SupplierStateEnum.BEFORE_SIGN_UP.getCode());
                inquirySupplierInfo.setSignUpFlag(SignUpFlagConstant.NO);
                inquirySupplierInfo.setRemainingBids(inquiryMain.getSupplierOfferCount());
                inquirySupplierInfo.setEliminateFlag(FlagConstant.NO);
                inquirySupplierInfo.setLatestRoundFlag(FlagConstant.YES);
                // 消息对象
                messageUserList.add(new MessageUserDto(inquirySupplierInfo.getSupplierContactName(),
                    inquirySupplierInfo.getSupplierContactMobile(), inquirySupplierInfo.getSupplierContactEmail()));
            }
            // 保存
            inquirySupplierInfoService.saveBatch(inquirySupplierInfoList);
        }
        return messageUserList;
    }

    private InquiryIdNoDto updateInquiryTicket(SaveInquiryReq req) {
        // 先根据id查询数据库 校验数据的合法性
        InquiryMainPO mainPO = this.getById(req.getId());
        if (Objects.isNull(mainPO)) {
            // 如果查询结果为空 则抛出异常
            log.error("id错误,该数据不存在,传入id为{}", req.getId());
            throw new InquiryBizException(InquiryErrorCodeEnum.ID_ERROR);
        }
        // 校验状态 不为待提交 则报错
        if (!InquiryEnum.InquiryState.BEFORE_SUBMIT.getCode().equals(mainPO.getInquiryStatus())) {
            log.error("当前单据状态不支持该操作,当前单据状态为{}", mainPO.getInquiryStatus());
            throw new InquiryBizException(InquiryErrorCodeEnum.STATUS_ERROR);
        }
        // 转换主体部分
        InquiryMainPO inquiryMain = InquiryMainConverter.INSTANCE.saveReq2Po(req);
        // 批量删除行表和供应商表内容
        batchRemoveItemAndSupplier(inquiryMain.getId(), inquiryMain.getInquiryNo());
        log.info("行表和供应商表原数据删除成功");
        // 更新主表数据
        if (!this.updateById(inquiryMain)) {
            log.error("主表更新失败,根据id:{}在询价单主表未查询到数据", inquiryMain.getId());
            throw new InquiryBizException(InquiryErrorCodeEnum.ID_ERROR);
        }
        log.info("主表更新成功");
        // 批量保存行表和供应商表部分
        batchSaveSupplierAndItem(req, inquiryMain);
        log.info("行表和供应商表新增数据成功");
        return new InquiryIdNoDto(inquiryMain.getId(), inquiryMain.getInquiryNo());
    }

    private InquiryIdNoDto firstInsertInquiryTicket(SaveInquiryReq req) {
        // 转换主体部分
        InquiryMainPO inquiryMain = InquiryMainConverter.INSTANCE.saveReq2Po(req);
        // 设置状态 待提交 10
        inquiryMain.setInquiryStatus(InquiryEnum.InquiryState.BEFORE_SUBMIT.getCode());
        // 同样需要设置负责人 用于数据权限处理
        UserInfo userInfo = UserInfoUtils.getUserInfo();
        inquiryMain.setLiablePersonCode(userInfo.getUserCode());
        inquiryMain.setLiablePersonName(userInfo.getUserName());
        // 主体部分存表
        this.save(inquiryMain);
        String inquiryNo = NumberUtils.getInquiryNo(inquiryMain.getId());
        inquiryMain.setInquiryNo(inquiryNo);
        this.updateById(inquiryMain);
        log.info("主表保存成功,单号更新成功");
        // 批量保存行表和供应商表部分
        batchSaveSupplierAndItem(req, inquiryMain);
        log.info("行表和供应商信息表保存成功");
        // 返回询比价单号 便于测试取值
        return new InquiryIdNoDto(inquiryMain.getId(), inquiryNo);
    }

    private void batchSaveSupplierAndItem(SaveInquiryReq req, InquiryMainPO inquiryMain) {
        // 批量保存行信息
        batchSaveItemPo(InquiryItemConverter.INSTANCE.toList(req.getItemList()), inquiryMain.getId(),
            inquiryMain.getInquiryNo());
        // 提取其中的供应商部分
        List<SaveInquirySupplierReq> supplierList = req.getSupplierList();
        // 转为表对象实体
        if (CollectionUtils.isNotEmpty(supplierList)) {
            // 转换为表对象实体
            List<InquirySupplierInfoPO> inquirySupplierInfoList =
                InquirySupplierInfoConverter.INSTANCE.toList(supplierList);
            // 完善相关内容
            for (InquirySupplierInfoPO inquirySupplierInfo : inquirySupplierInfoList) {
                // 暂存不填写轮次号
                inquirySupplierInfo.setInquiryNo(inquiryMain.getInquiryNo());
            }
            // 保存
            inquirySupplierInfoService.saveBatch(inquirySupplierInfoList);
        }
    }

    private void batchSaveItemPo(List<InquiryItemPO> inquiryItemList, Long inquiryId, String inquiryNo) {
        if (CollectionUtils.isEmpty(inquiryItemList)) {
            return;
        }
        // 循环填充相关内容
        for (int i = 0; i < inquiryItemList.size(); i++) {
            InquiryItemPO inquiryItem = inquiryItemList.get(i);
            // 主表id 单号 行号
            inquiryItem.setInquiryId(inquiryId);
            inquiryItem.setInquiryNo(inquiryNo);
            inquiryItem.setLineNo(i + 1);
        }
        // 保存至行
        inquiryItemService.saveBatch(inquiryItemList);
    }
}
