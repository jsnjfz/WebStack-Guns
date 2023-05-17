package com.cttq.inquiry.service.offer.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cttq.framework.common.base.UserInfo;
import com.cttq.framework.common.base.UserInfoUtils;
import com.cttq.inquiry.common.InquiryBizException;
import com.cttq.inquiry.common.enums.InquiryEnum;
import com.cttq.inquiry.common.enums.TraceNodeEnum;
import com.cttq.inquiry.common.utils.TypeUtils;
import com.cttq.inquiry.common.utils.export.ExportPdfUtil;
import com.cttq.inquiry.converter.offer.OfferConverter;
import com.cttq.inquiry.dao.evaluation.InquiryEvaluationMapper;
import com.cttq.inquiry.dao.offer.InquiryOfferMapper;
import com.cttq.inquiry.dao.supplier.InquirySupplierInfoMapper;
import com.cttq.inquiry.dto.req.offer.*;
import com.cttq.inquiry.dto.resp.offer.*;
import com.cttq.inquiry.entity.evaluation.InquiryEvaluationAllocatedInfoPO;
import com.cttq.inquiry.entity.inquiry.InquiryItemPO;
import com.cttq.inquiry.entity.inquiry.InquiryMainPO;
import com.cttq.inquiry.entity.offer.*;
import com.cttq.inquiry.entity.supplier.InquirySupplierInfoPO;
import com.cttq.inquiry.service.common.InquirySysMessageTemplateService;
import com.cttq.inquiry.service.common.InquirySysTraceLogService;
import com.cttq.inquiry.service.evaluation.InquiryAllocatedInfoService;
import com.cttq.inquiry.service.inquiry.InquiryItemService;
import com.cttq.inquiry.service.inquiry.InquiryMainService;
import com.cttq.inquiry.service.inquiry.InquiryRoundService;
import com.cttq.inquiry.service.offer.*;
import com.cttq.inquiry.service.supplier.InquirySupplierInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.cttq.inquiry.common.constant.Constant.CLOSE_REASON;
import static com.cttq.inquiry.common.constant.FlagConstant.NO;
import static com.cttq.inquiry.common.constant.FlagConstant.YES;
import static com.cttq.inquiry.common.enums.InquiryEnum.InquiryState.*;
import static com.cttq.inquiry.common.enums.InquiryEnum.SupplierEvaluationStatus.SELECTED;
import static com.cttq.inquiry.common.enums.InquiryEnum.SupplierStateEnum.BEFORE_OFFER;
import static com.cttq.inquiry.common.enums.InquiryEnum.SupplierStateEnum.OFFERED;
import static com.cttq.inquiry.common.enums.InquiryErrorCodeEnum.*;
import static com.cttq.inquiry.common.enums.MessageTemplateEnum.*;

/**
 * 报价表(InquiryOffer)表服务接口
 *
 * @author 干凌兰
 * @since 2023-04-20 13:25:13
 */
@Slf4j
@Service
public class InquiryOfferServiceImpl extends ServiceImpl<InquiryOfferMapper, InquiryOfferPO>
    implements InquiryOfferService {
    @Resource
    private InquirySupplierInfoService supplierService;

    @Resource
    private InquiryStepPriceService stepPriceService;

    @Resource
    private InquiryBusinessTermsService businessTermsService;

    @Resource
    private InquiryOfferMapper offerMapper;

    @Resource
    private InquirySysMessageTemplateService messageTemplateService;

    @Resource
    private InquiryOfferDetailsService detailsService;

    @Resource
    private InquiryItemService itemService;

    @Resource
    private InquiryMainService mainService;

    @Resource
    private InquiryRoundService roundService;

    @Resource
    private InquirySubItemPriceInfoService subItemService;

    @Resource
    private InquiryEvaluationMapper evaluationMapper;

    @Resource
    private InquiryAllocatedInfoService allocatedService;

    @Resource
    private InquiryAllocatedInfoService allocatedInfoService;

    @Resource
    private InquirySysTraceLogService traceLogService;

    @Resource
    private InquirySupplierInfoMapper supplierMapper;

    /**
     * 付款比例之和需为100
     */
    private final static Integer PROPORTION = 100;

    /**
     * 不能查看报价信息的询价单状态
     */
    public final static List<String> NOT_ALLOWED_VIEW_OFFER_INFO_STATES =
        Arrays.asList(InquiryEnum.InquiryState.BEFORE_SUBMIT.getCode(), InquiryEnum.InquiryState.BEFORE_OFFER.getCode(),
            OFFERING.getCode());

    /**
     * 采购方式
     */
    public final static Map<String, String> PURCHASE_TYPE = Collections.unmodifiableMap(new HashMap<String, String>() {
        {
            put("2", "年度协议");
            put("1", "单次采购");
        }
    });

    /**
     * 供应商可以进行报价的状态
     */
    public final static List<String> AVAILABLE_STATUS =
        Arrays.asList(InquiryEnum.InquiryState.BEFORE_OFFER.getCode(), OFFERING.getCode());

    @Override
    public List<SupplierOfferRelationResp> getSupplierOfferRelationList(String inquiryNo) {
        return baseMapper.getSupplierOfferRelationList(inquiryNo);
    }

    @Override
    public List<SupplierOfferInfoResp> getOffer(GetOfferReq getOfferReq) {
        // 若为密封报价且当前轮次未开标，则不允许查看当前轮次报价信息
        long counts = mainService
            .count(new LambdaQueryWrapper<InquiryMainPO>().eq(InquiryMainPO::getInquiryNo, getOfferReq.getInquiryNo())
                .eq(InquiryMainPO::getRound, getOfferReq.getRound()).eq(InquiryMainPO::getSealedQuotationFlag, YES)
                .in(InquiryMainPO::getInquiryStatus, NOT_ALLOWED_VIEW_OFFER_INFO_STATES));
        if (counts != 0) {
            throw new InquiryBizException(UNABLE_TO_VIEW_QUOTATION_INFORMATION);
        }
        List<SupplierOfferInfoResp> res = offerMapper.getOffer(getOfferReq);

        // 获取该采购单物料数量
        long count = itemService
            .count(new LambdaQueryWrapper<InquiryItemPO>().eq(InquiryItemPO::getInquiryNo, getOfferReq.getInquiryNo()));
        log.info("询比价编号为{}的采购物料数量为{}", getOfferReq.getInquiryNo(), count);

        // 获取总金额最低价
        BigDecimal lastTotal = offerMapper.getTotalPriceExcludedTax(count, getOfferReq.getInquiryNo());
        log.info("询比价编号为{}的总金额最低价为{}", getOfferReq.getInquiryNo(), lastTotal);
        if (Optional.ofNullable(lastTotal).isPresent()) {
            // 遍历赋值
            res.forEach(supplierOfferInfo -> supplierOfferInfo
                .setIsLowestPrice(Objects.equals(lastTotal, supplierOfferInfo.getTotalPriceExcludedTax())));
        }
        return res;
    }

    @Override
    public List<EntrantsResp> getEntrantsList(String inquiryNo) {
        return OfferConverter.INSTANCE.toEntrantsRespList(supplierService
            .list(new LambdaQueryWrapper<InquirySupplierInfoPO>().eq(InquirySupplierInfoPO::getInquiryNo, inquiryNo)
                .eq(InquirySupplierInfoPO::getLatestRoundFlag, YES.toString())
                .eq(InquirySupplierInfoPO::getEliminateFlag, NO.toString())
                .eq(InquirySupplierInfoPO::getStatus, OFFERED.getCode())));
    }

    @Override
    public void uploadPriceFile(UploadPriceFile uploadPriceFile) {
        boolean flag = this.updateById(new InquiryOfferPO() {
            {
                setId(uploadPriceFile.getOfferId());
                setPriceFileIds(uploadPriceFile.getFileId());
            }
        });
        if (!flag) {
            throw new InquiryBizException(PRICE_FILE_UPDATE_FAIL);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void openNextOffer(OfferReq offerReq) {
        if (offerReq.getSupplierIds().isEmpty()) {
            // 至少选择一个供应商
            throw new InquiryBizException(SUPPLIER_CANNOT_EMPTY);
        }
        InquiryMainPO mainPo = mainService
            .getOne(new LambdaQueryWrapper<InquiryMainPO>().eq(InquiryMainPO::getInquiryNo, offerReq.getInquiryNo())
                .eq(InquiryMainPO::getInquiryStatus, BEFORE_EVALUATION.getCode()));
        if (!Optional.ofNullable(mainPo).isPresent()) {
            // 当前询比价不支持发起多轮报价
            throw new InquiryBizException(UNSUPPORTED_OPEN_NEXT_OFFER);
        }

        // 新增数据到轮次信息表
        roundService.save(OfferConverter.INSTANCE.toInquiryRoundPO(offerReq));

        // 修改询价单主表 报价截止时间和轮次字段
        mainService.updateById(new InquiryMainPO() {
            {
                setId(mainPo.getId());
                setDeadline(offerReq.getDeadline());
                setRound(offerReq.getRound());
                setDeadlineFlag(NO);
                setInquiryStatus(InquiryEnum.InquiryState.BEFORE_OFFER.getCode());
            }
        });

        // 修改报价详情表是否最新轮次且最新报价 字段
        detailsService.update(new InquiryOfferDetailsPO() {
            {
                setNewRoundFlag(NO);
            }
        }, new LambdaQueryWrapper<InquiryOfferDetailsPO>().eq(InquiryOfferDetailsPO::getInquiryNo,
            offerReq.getInquiryNo()));

        // 处理供应商信息
        doSupplierInfo(offerReq, mainPo);
        traceLogService.recordTraceLog(TraceNodeEnum.OPEN_NEXT_OFFER, offerReq.getInquiryNo(), null, null);

    }

    @Override
    public List<InquiryStepPricePO> getStepPrice(Long offerDetailId) {
        return stepPriceService
            .list(new LambdaQueryWrapper<InquiryStepPricePO>().eq(InquiryStepPricePO::getOfferDetailId, offerDetailId));
    }

    @Override
    public List<InquiryBusinessTermPO> getBusinessInfo(Long offerId) {
        return businessTermsService
            .list(new LambdaQueryWrapper<InquiryBusinessTermPO>().eq(InquiryBusinessTermPO::getOfferId, offerId));
    }

    @Override
    public List<InquirySubItemPriceInfoPO> getSubItemInfo(Long offerItemId) {
        return subItemService.list(new LambdaQueryWrapper<InquirySubItemPriceInfoPO>()
            .eq(InquirySubItemPriceInfoPO::getOfferItemId, offerItemId));
    }

    @Override
    public List<InquiryOfferInfoResp> getInquiryOffer(String inquiryNo) {
        List<InquiryItemPO> items =
            itemService.list(new LambdaQueryWrapper<InquiryItemPO>().eq(InquiryItemPO::getInquiryNo, inquiryNo));
        return OfferConverter.INSTANCE.toInquiryOfferInfoRespList(items);
    }

    @Override
    public ApprovalInfoResp getOfferApprovalInfo(Long evaluationId) {
        // 查询询价信息
        ApprovalInfoResp res = evaluationMapper.getRrqInfo(evaluationId);
        if (!Optional.ofNullable(res.getEvaId()).isPresent()) {
            throw new InquiryBizException(INPUT_PARAMETER_INCORRECT);
        }
        res.setPurchaseTypeName(PURCHASE_TYPE.get(res.getPurchaseType()));

        // 获取中标供应商名称
        List<String> supplierNames =
            allocatedService.listObjs(new LambdaQueryWrapper<InquiryEvaluationAllocatedInfoPO>()
                .select(InquiryEvaluationAllocatedInfoPO::getSupplierName)
                .eq(InquiryEvaluationAllocatedInfoPO::getChoiceFlag, SELECTED.getCode()), Object::toString);

        // 将供应商名称添加到查询信息中
        res.setWinSupplierNames(supplierNames);

        // 获取邀请供应商信息（没有被淘汰的供应商 包括未报价未报名的供应商)
        res.setInviteSupplierInfos(supplierMapper.getInvitedSuppliers(res.getInquiryNo()));
        return res;
    }

    @Override
    @SneakyThrows
    public InquiryListInfoResp getInquiryInfo(String inquiryNo) {
        // 询比价单信息
        CompletableFuture<InquiryMainPO> mainFuture = CompletableFuture.supplyAsync(() -> mainService
            .getOne(new LambdaQueryWrapper<InquiryMainPO>().eq(InquiryMainPO::getInquiryNo, inquiryNo)));

        // 获取需求部门名称
        CompletableFuture<List<String>> usingDeptNamesFuture =
            CompletableFuture.supplyAsync(() -> itemService.listObjs(new LambdaQueryWrapper<InquiryItemPO>()
                .select(InquiryItemPO::getUsingDeptName).eq(InquiryItemPO::getInquiryNo, inquiryNo), Object::toString));

        // 获取采购清单
        CompletableFuture<List<InquiryItemPO>> itemsFuture = CompletableFuture.supplyAsync(
            () -> itemService.list(new LambdaQueryWrapper<InquiryItemPO>().eq(InquiryItemPO::getInquiryNo, inquiryNo)));
        InquiryListInfoResp res;
        InquiryMainPO main;
        main = mainFuture.get();
        res = OfferConverter.INSTANCE.toInquiryListInfoResp(main);
        res.setUsingDeptNames(usingDeptNamesFuture.get());
        res.setItems(OfferConverter.INSTANCE.toInquiryItemResplist(itemsFuture.get()));

        // 获取邀请供应商
        List<String> supplierInfos =
            supplierService.listObjs(
                new QueryWrapper<InquirySupplierInfoPO>().select("concat(supplier_code,'_',supplier_name)")
                    .eq("inquiry_no", inquiryNo).eq("round", main.getRound()).eq("eliminate_flag", NO),
                Object::toString);
        res.setSuppliers(supplierInfos);
        return res;
    }

    @Override
    public List<SupplierOfferResp> getSupplierOffer(String inquiryNo, String supplierCode) {
        return offerMapper.getSupplierOffer(inquiryNo, supplierCode);
    }

    @Override
    public InquiryInfoResp queryInquiryInfo(String inquiryNo) {
        return OfferConverter.INSTANCE.toInquiryInfoResp(
            mainService.getOne(new LambdaQueryWrapper<InquiryMainPO>().eq(InquiryMainPO::getInquiryNo, inquiryNo)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submit(OfferInfoReq offerInfoReq) {
        // todo 性能待优化
        String inquiryNo = offerInfoReq.getInquiryNo();

        // 获取询价单以及供应商信息
        InquiryMainPO main =
            mainService.getOne(new LambdaQueryWrapper<InquiryMainPO>().eq(InquiryMainPO::getInquiryNo, inquiryNo));
        InquirySupplierInfoPO supplierInfo = supplierService
            .getOne(new LambdaQueryWrapper<InquirySupplierInfoPO>().eq(InquirySupplierInfoPO::getInquiryNo, inquiryNo)
                .eq(InquirySupplierInfoPO::getSupplierCode, UserInfoUtils.getUserInfo().getUserCode())
                .eq(InquirySupplierInfoPO::getRound, main.getRound()));

        // 查询物料需求数量
        CompletableFuture<List<InquiryItemPO>> itemsFuture = CompletableFuture.supplyAsync(
            () -> itemService.list(new LambdaQueryWrapper<InquiryItemPO>().eq(InquiryItemPO::getInquiryNo, inquiryNo)));

        // 校验是否可进行报价，如果不可以进行报价则中断
        canOffer(main, supplierInfo, offerInfoReq);

        // 修改报价状态
        upOfferState(offerInfoReq, main, supplierInfo);

        // 新增报价记录
        addOfferInfo(offerInfoReq, main, itemsFuture);
    }

    @Override
    @SneakyThrows
    public OfferInfoResp getOfferDetailInfo(String inquiryNo, Integer round) {
        // 获取报价主要信息
        InquiryOfferPO offerInfo = this.getOne(new LambdaQueryWrapper<InquiryOfferPO>()
            .eq(InquiryOfferPO::getInquiryNo, inquiryNo).eq(InquiryOfferPO::getRound, round)
            .eq(InquiryOfferPO::getSupplierCode, UserInfoUtils.getUserInfo().getUserCode())
            .eq(InquiryOfferPO::getEnableFlag, YES));
        OfferInfoResp res = OfferConverter.INSTANCE.toOfferInfoResp(offerInfo);

        // 获取物料详情信息
        List<InquiryItemPO> items =
            itemService.list(new LambdaQueryWrapper<InquiryItemPO>().eq(InquiryItemPO::getInquiryNo, inquiryNo));

        // 如果有报价信息代表查询报价信息，没有报价信息代表查询提交报价前信息
        // 给内容赋值
        if (Optional.ofNullable(offerInfo).isPresent()) {
            // 获取报价详情信息
            getDetailInfo(res, offerInfo.getId(), items);
        } else {
            // 付空置方便前端处理

            // 组装物料信息和报价信息
            res.setItemOffers(items.stream().map(item -> {
                ItemOfferInfoResp itemOffer = OfferConverter.INSTANCE.toItemOfferInfoResp(item);
                itemOffer.setOfferDetail(Arrays.asList(new InquiryOfferDetailsResp() {
                    {
                        setStepPriceInfos(Arrays.asList(new StepPriceReq()));
                        setSubItemPriceInfos(Arrays.asList(new SubItemPriceReq()));
                    }
                }));
                return itemOffer;
            }).collect(Collectors.toList()));
            res.setBusiness(Arrays.asList(new InquiryBusinessTermPO()));
        }
        return res;
    }

    @Override
    public BidDetailInfoResp getBidDetailInfo(String inquiryNo) {
        // 获取询比价信息
        InquiryMainPO main =
            mainService.getOne(new LambdaQueryWrapper<InquiryMainPO>().eq(InquiryMainPO::getInquiryNo, inquiryNo));
        BidDetailInfoResp res = OfferConverter.INSTANCE.toBidDetailInfoResp(main);

        // 获取报价信息
        InquiryOfferPO offerPo = this.getOne(new LambdaQueryWrapper<InquiryOfferPO>()
            .eq(InquiryOfferPO::getInquiryNo, inquiryNo).eq(InquiryOfferPO::getRound, main.getRound())
            .eq(InquiryOfferPO::getSupplierCode, UserInfoUtils.getUserInfo().getUserCode())
            .eq(InquiryOfferPO::getEnableFlag, YES));
        res.setInvoiceType(offerPo.getInvoiceType());
        res.setPriceFileIds(offerPo.getPriceFileIds());
        res.setPriceFileIds(offerPo.getPriceFileIds());

        // 获取物料中标详情信息
        setBidDetailInfo(res, inquiryNo, offerPo.getId());

        // 获取商务信息
        res.setBusiness(businessTermsService.list(
            new LambdaQueryWrapper<InquiryBusinessTermPO>().eq(InquiryBusinessTermPO::getOfferId, offerPo.getId())));
        return res;
    }

    @Override
    @SneakyThrows
    public void exportOffer(Long offerId) {
        // 查询商务信息
        CompletableFuture<List<Map<String, Object>>> businessFuture =
            CompletableFuture.supplyAsync(() -> businessTermsService.listMaps(
                new LambdaQueryWrapper<InquiryBusinessTermPO>().eq(InquiryBusinessTermPO::getOfferId, offerId)));

        // 查询头信息
        ExportPdfReq pdfInfo = offerMapper.getExportOfferTitle(offerId);

        // 查询报价详情
        List<Map<String, Object>> details = detailsService
            .listMaps(new LambdaQueryWrapper<InquiryOfferDetailsPO>().eq(InquiryOfferDetailsPO::getOfferId, offerId));
        if (Objects.equals(YES, pdfInfo.getStepPriceFlag())) {
            // 如果是阶梯价查询阶梯价信息
            details = details.parallelStream().peek(detail -> {
                detail.put("stepPrice", stepPriceService.list(new LambdaQueryWrapper<InquiryStepPricePO>()
                    .eq(InquiryStepPricePO::getOfferDetailId, detail.get("id"))));

                // 分项报价
                detail.put("subItemPrice", subItemService.list(new LambdaQueryWrapper<InquirySubItemPriceInfoPO>()
                    .eq(InquirySubItemPriceInfoPO::getOfferItemId, Long.parseLong(detail.get("id").toString()))));
            }).collect(Collectors.toList());
        }
        pdfInfo.setDetails(details);
        pdfInfo.setBusiness(businessFuture.get());

        // 调用导出方法
        ExportPdfUtil.exportOffer(pdfInfo);
    }

    /**
     * 功能描述：获取物料中标详情信息
     *
     * @param res 需修改内容
     * @param inquiryNo 询比价编号
     * @param offerId 报价id
     */
    @SneakyThrows
    private void setBidDetailInfo(BidDetailInfoResp res, String inquiryNo, Long offerId) {
        // 获取报价详情 key是询比价物料详情id
        CompletableFuture<Map<Long, InquiryOfferDetailsPO>> detailFutures =
            CompletableFuture.supplyAsync(() -> getOfferDetailItemInfos(offerId));

        // 获取中标状态信息
        CompletableFuture<
            Map<Long, Integer>> choiceFlagFutures =
                CompletableFuture.supplyAsync(() -> allocatedInfoService
                    .list(new LambdaQueryWrapper<InquiryEvaluationAllocatedInfoPO>()
                        .eq(InquiryEvaluationAllocatedInfoPO::getInquiryNo, inquiryNo)
                        .eq(InquiryEvaluationAllocatedInfoPO::getSupplierCode,
                            UserInfoUtils.getUserInfo().getUserCode())
                        .eq(InquiryEvaluationAllocatedInfoPO::getLastFlag, YES))
                    .stream().collect(Collectors.toMap(InquiryEvaluationAllocatedInfoPO::getOfferDetailId,
                        InquiryEvaluationAllocatedInfoPO::getChoiceFlag)));

        // 获取物料信息
        List<InquiryItemPO> items =
            itemService.list(new LambdaQueryWrapper<InquiryItemPO>().eq(InquiryItemPO::getInquiryNo, inquiryNo));

        // 组装
        Map<Long, Integer> choiceFlagMap = choiceFlagFutures.get();
        Map<Long, InquiryOfferDetailsPO> detailMap = detailFutures.get();
        res.setItems(items.stream().map(item -> new ItemBidInfoResp() {
            {
                setItem(item);
                InquiryOfferDetailsPO detail = detailMap.get(item.getId());
                setOfferDetail(OfferConverter.INSTANCE.toInquiryOfferDetailsResp(detail));
                setChoiceFlag(choiceFlagMap.get(detail.getId()));
            }
        }).collect(Collectors.toList()));
    }

    /**
     * 功能描述：新增报价记录
     *
     * @param offerInfoReq 报价信息
     * @param main 询比价信息
     * @param itemsFuture 物料详情信息
     */
    private void addOfferInfo(OfferInfoReq offerInfoReq, InquiryMainPO main,
        CompletableFuture<List<InquiryItemPO>> itemsFuture) {
        UserInfo userInfo = UserInfoUtils.getUserInfo();

        // 新增报价表
        InquiryOfferPO inquiryOfferPo = OfferConverter.INSTANCE.toInquiryOfferPO(offerInfoReq);
        addOffer(inquiryOfferPo, main, userInfo);

        // 新增报价详情记录
        addOfferDetail(offerInfoReq, itemsFuture, inquiryOfferPo);

        // 新增商务信息表
        addBusinessInfo(offerInfoReq.getBusinessInfos(), inquiryOfferPo.getId());
    }

    /**
     * 功能描述：新增商务信息表
     *
     * @param businessInfos 商务信息
     * @param offerId 报价id
     */
    private void addBusinessInfo(List<InquiryBusinessTermPO> businessInfos, Long offerId) {
        businessInfos.forEach(businessInfo -> businessInfo.setOfferId(offerId));
        businessTermsService.saveBatch(businessInfos);
    }

    /**
     * 功能描述：新增报价详情记录
     *
     * @param offerInfoReq 报价详情信息
     * @param itemsFuture 物料信息
     * @param inquiryOfferPo 报价内容
     */
    private void addOfferDetail(OfferInfoReq offerInfoReq, CompletableFuture<List<InquiryItemPO>> itemsFuture,
        InquiryOfferPO inquiryOfferPo) {
        // 获取物料需求数量
        Map<Long, BigDecimal> itemNum = getItemNum(itemsFuture);
        offerInfoReq.getDetails().parallelStream().forEach(detail -> {
            // 新增报价详情表
            InquiryOfferDetailsPO detailPo = OfferConverter.INSTANCE.toInquiryOfferDetailsPO(detail);
            detailPo.setInquiryNo(offerInfoReq.getInquiryNo());
            detailPo.setNewRoundFlag(YES);
            detailPo.setOfferId(inquiryOfferPo.getId());
            detailPo.setEnableFlag(YES);
            detailPo.setSupplierCode(UserInfoUtils.getUserInfo().getUserCode());

            // 获取物料需求数量
            BigDecimal num = itemNum.get(detail.getInquiryItemId());
            log.info("物料 {} 报价的需求数量为{}", detail, num);
            if (YES.equals(inquiryOfferPo.getStepPriceFlag())) {
                // 如果是阶梯价，获取阶梯价中对应命中含税单价 并赋值到未税含税单价
                detailPo.setUnitPriceIncludingTax(detail.getStepPriceInfos().stream()
                    .filter(stepPriceInfo -> (stepPriceInfo.getStepNumberFrom().compareTo(num) < 0
                        || stepPriceInfo.getStepNumberFrom().compareTo(num) == 0)
                        && (num.compareTo(stepPriceInfo.getStepNumberEnd()) < 0
                            || !Optional.ofNullable(stepPriceInfo.getStepNumberEnd()).isPresent()))
                    .map(StepPriceReq::getUnitPriceIncludingTax).findFirst().get());
            }
            detailPo.setUnitPriceExcludedTax(
                detailPo.getUnitPriceIncludingTax().multiply(new BigDecimal(1).subtract(detailPo.getRate())));
            detailPo.setTotalPriceExcludedTax(detailPo.getUnitPriceExcludedTax().multiply(num));
            detailPo.setTotalPriceIncludingTax(detailPo.getUnitPriceIncludingTax().multiply(num));
            detailsService.save(detailPo);

            // 新增阶梯价信息
            addStepPriceInfo(detailPo, detail);
            if (CollectionUtil.isNotEmpty(detail.getSubItemPriceInfos())) {
                // 如是分项报价新增分项报价表
                addSubItemPriceInfos(detail.getSubItemPriceInfos(), detailPo.getId());
            }
        });
    }

    /**
     * 功能描述：如是分项报价新增分项报价表
     *
     * @param subItemPriceInfos 分项报价信息
     * @param offerItemId 报价详情id
     */
    private void addSubItemPriceInfos(List<SubItemPriceReq> subItemPriceInfos, Long offerItemId) {
        List<InquirySubItemPriceInfoPO> subItemPriceInfoPos =
            OfferConverter.INSTANCE.toInquirySubItemPriceInfoPOList(subItemPriceInfos);
        subItemPriceInfoPos.forEach(subItemPriceInfoPo -> subItemPriceInfoPo.setOfferItemId(offerItemId));
        subItemService.saveBatch(subItemPriceInfoPos);
    }

    /**
     * 功能描述：新增阶梯价记录
     *
     * @param detailPo 详情信息
     * @param detail 详情信息
     */
    private void addStepPriceInfo(InquiryOfferDetailsPO detailPo, OfferDetailsReq detail) {
        List<InquiryStepPricePO> stepPos = OfferConverter.INSTANCE
            .toInquiryStepPricePOList(Optional.ofNullable(detail.getStepPriceInfos()).orElse(Arrays.asList()));
        stepPos.forEach(stepPo -> {
            stepPo.setOfferDetailId(detailPo.getId());
            stepPo.setUnitPriceExcludedTax(
                stepPo.getUnitPriceIncludingTax().multiply(new BigDecimal(1).subtract(detailPo.getRate())));
        });
        stepPriceService.saveBatch(stepPos);
    }

    /**
     * 功能描述：获取物料需求数量
     *
     * @param itemsFuture 物料信息
     * @return java.util.Map<java.lang.String, java.math.BigDecimal>
     */
    @SneakyThrows
    private Map<Long, BigDecimal> getItemNum(CompletableFuture<List<InquiryItemPO>> itemsFuture) {
        List<InquiryItemPO> items;
        items = itemsFuture.get();
        if (CollectionUtil.isEmpty(items)) {
            return null;
        }
        Map<Long, BigDecimal> itemNums = new HashMap<>(10);
        items.forEach(item -> itemNums.put(item.getId(), item.getRequireQuantity()));
        return itemNums;
    }

    /**
     * 功能描述：新增报价表
     *
     * @param inquiryOfferPo 报价信息
     * @param main 询比价信息
     * @param userInfo 用户信息
     */
    private void addOffer(InquiryOfferPO inquiryOfferPo, InquiryMainPO main, UserInfo userInfo) {
        inquiryOfferPo.setRound(main.getRound());
        inquiryOfferPo.setStepPriceFlag(main.getStepPriceFlag());
        inquiryOfferPo.setPriceTemplate(main.getPriceTemplate());
        inquiryOfferPo.setSupplierCode(userInfo.getUserCode());
        inquiryOfferPo.setSupplierName(userInfo.getUserName());
        inquiryOfferPo.setQuotationTime(new Date());
        inquiryOfferPo.setEnableFlag(YES);
        offerMapper.insert(inquiryOfferPo);
    }

    /**
     * 功能描述：修改报价状态
     *
     * @param offerInfoReq 报价信息
     * @param main 询价信息
     * @param supplierInfo 供应商信息
     */
    private void upOfferState(OfferInfoReq offerInfoReq, InquiryMainPO main, InquirySupplierInfoPO supplierInfo) {
        String inquiryNo = offerInfoReq.getInquiryNo();
        if (!Objects.equals(main.getInquiryStatus(), OFFERING.getCode())) {
            // 如果询比价报价状态不是已报价状态则修改为已报价状态
            mainService.update(new InquiryMainPO() {
                {
                    setInquiryStatus(OFFERING.getCode());
                }
            }, new LambdaQueryWrapper<InquiryMainPO>().eq(InquiryMainPO::getInquiryNo, inquiryNo));
        } else {
            // 作废之前报价记录
            this.update(new InquiryOfferPO() {
                {
                    setEnableFlag(NO);
                }
            }, new LambdaQueryWrapper<InquiryOfferPO>().eq(InquiryOfferPO::getInquiryNo, inquiryNo)
                .eq(InquiryOfferPO::getSupplierCode, UserInfoUtils.getUserInfo().getUserCode())
                .eq(InquiryOfferPO::getRound, main.getRound()));
            detailsService.update(new InquiryOfferDetailsPO() {
                {
                    setEnableFlag(NO);
                }
            }, new LambdaQueryWrapper<InquiryOfferDetailsPO>().eq(InquiryOfferDetailsPO::getInquiryNo, inquiryNo)
                .eq(InquiryOfferDetailsPO::getSupplierCode, UserInfoUtils.getUserInfo().getUserCode())
                .eq(InquiryOfferDetailsPO::getNewRoundFlag, YES));
        }

        // 修改供应商表剩余报价次数以及报价状态
        // ---------- AI 辅助校验 ------------
        supplierService.update(new InquirySupplierInfoPO() {
            {
                setStatus(OFFERED.getCode());
                setRemainingBids(supplierInfo.getRemainingBids() - 1);
            }
        }, new LambdaQueryWrapper<InquirySupplierInfoPO>().eq(InquirySupplierInfoPO::getInquiryNo, inquiryNo)
            .eq(InquirySupplierInfoPO::getSupplierCode, supplierInfo.getSupplierCode())
            .eq(InquirySupplierInfoPO::getRound, main.getRound()));
    }

    /**
     * 功能描述：报价前校验
     *
     * @param main 询比价信息
     * @param supplierInfo 供应商信息
     * @param offerInfoReq 报价信息
     */
    private void canOffer(InquiryMainPO main, InquirySupplierInfoPO supplierInfo, OfferInfoReq offerInfoReq) {
        // 判断当前询价单是否可以进行报价
        if (!(AVAILABLE_STATUS.contains(main.getInquiryStatus())
            && (main.getDeadline().getTime()) > System.currentTimeMillis())) {
            // 如询价单状态不在可报价状态中或报价时间已截止则 当前询价单不支持报价
            throw new InquiryBizException(UNABLE_TO_QUOTE);
        }
        if (Optional
            .ofNullable(Optional.ofNullable(supplierInfo).orElse(new InquirySupplierInfoPO()).getRemainingBids())
            .orElse(0) <= 0) {
            // 剩余报价次数不足
            throw new InquiryBizException(NOT_ENOUGH_TIMES, main.getSupplierOfferCount());
        }
        Integer proportion = offerInfoReq.getBusinessInfos().stream().map(InquiryBusinessTermPO::getRatio)
            .map(ratio -> Integer.parseInt(ratio.replace("%", ""))).reduce(Integer::sum).orElse(0);
        if (Objects.equals(PROPORTION, proportion)) {
            // 付款比例之和需等于100%
            throw new InquiryBizException(INCORRECT_PROPORTION);
        }
    }

    /**
     * 功能描述：处理供应商信息
     *
     * @param offerReq 报价信息
     * @param main 供应商报价数量
     */
    private void doSupplierInfo(OfferReq offerReq, InquiryMainPO main) {
        // 查出进入下一轮的供应商信息
        List<InquirySupplierInfoPO> nextRoundSuppliers = supplierService.listByIds(offerReq.getSupplierIds());

        // 对上一轮供应商信息进行处理
        updatePreRoundSupplierInfo(offerReq);

        // 新增供应商对应轮次主要信息到供应商表
        addSupplierInfoForNextRound(nextRoundSuppliers, main.getSupplierOfferCount(), offerReq.getRound());

        // 异步给供应商发送信息
        CompletableFuture.runAsync(() -> {
            HashMap param = new HashMap<String, String>() {
                {
                    put("inquiryName", main.getInquiryName());
                    put("deadline", TypeUtils.dateToString(offerReq.getDeadline()));
                }
            };

            // 给进入下一轮的供应商发信息
            messageTemplateService.sendMessage(OfferConverter.INSTANCE.toMessageUserDtoList(nextRoundSuppliers),
                NEXT_ROUND_BEFORE_OFFER, param);

            // 给淘汰供应商发信息
            messageTemplateService.sendMessage(OfferConverter.INSTANCE
                .toMessageUserDtoList(supplierService.list(new LambdaQueryWrapper<InquirySupplierInfoPO>()
                    .eq(InquirySupplierInfoPO::getInquiryNo, main.getInquiryNo())
                    .eq(InquirySupplierInfoPO::getRound, main.getRound())
                    .eq(InquirySupplierInfoPO::getEliminateFlag, YES))),
                NEXT_ROUND_OFFERED, param);

            // 给未报价供应商发信息
            messageTemplateService.sendMessage(
                OfferConverter.INSTANCE
                    .toMessageUserDtoList(supplierService.list(new LambdaQueryWrapper<InquirySupplierInfoPO>()
                        .eq(InquirySupplierInfoPO::getInquiryNo, main.getInquiryNo())
                        .eq(InquirySupplierInfoPO::getRound, main.getRound())
                        .eq(InquirySupplierInfoPO::getRemainingBids, main.getSupplierOfferCount()))),
                NEXT_ROUND_NO_OFFER, param);
        });
    }

    /**
     * 功能描述：新增入选供应商信息
     *
     * @param supplierInfos 供应商上一轮报价信息
     * @param supplierOfferCount 该询价单 供应商每轮允许出价次数
     * @param round 新一轮的轮次
     */
    private void addSupplierInfoForNextRound(List<InquirySupplierInfoPO> supplierInfos, Integer supplierOfferCount,
        Integer round) {
        List<InquirySupplierInfoPO> newSupplierInfos = supplierInfos.stream().peek(supplierInfo -> {
            supplierInfo.setId(null);
            supplierInfo.setRound(round);
            supplierInfo.setStatus(BEFORE_OFFER.getCode());
            supplierInfo.setRemainingBids(supplierOfferCount);
        }).collect(Collectors.toList());
        supplierService.saveBatch(newSupplierInfos);
    }

    /**
     * 功能描述：处理上一轮供应商信息
     *
     * @param offerReq 多轮报价信息
     */
    private void updatePreRoundSupplierInfo(OfferReq offerReq) {
        // 将供应商信息表中未在列表中的上一轮供应商信息淘汰状态置为已淘汰
        supplierService.update(new InquirySupplierInfoPO() {
            {
                setCloseReason(CLOSE_REASON);
                setEliminateTime(new Date());
                setStatus(CLOSED.getCode());
                setEliminateFlag(YES);
            }
        }, new LambdaQueryWrapper<InquirySupplierInfoPO>()
            .eq(InquirySupplierInfoPO::getInquiryNo, offerReq.getInquiryNo())
            .eq(InquirySupplierInfoPO::getRound, offerReq.getRound() - 1)
            .notIn(InquirySupplierInfoPO::getId, offerReq.getSupplierIds()));

        // 修改入选供应商 是否最新轮次 字段
        supplierService.update(new InquirySupplierInfoPO() {
            {
                setLatestRoundFlag(NO);
            }
        }, new QueryWrapper<InquirySupplierInfoPO>().in("id", offerReq.getSupplierIds()));
    }

    /**
     * 功能描述：获取报价详情信息
     *
     * @param offerId 报价id
     * @return java.util.Map 报价信息
     */
    private Map<Long, InquiryOfferDetailsPO> getOfferDetailItemInfos(Long offerId) {
        List<InquiryOfferDetailsPO> details = detailsService
            .list(new LambdaQueryWrapper<InquiryOfferDetailsPO>().eq(InquiryOfferDetailsPO::getOfferId, offerId));
        return details.stream().collect(Collectors.toMap(InquiryOfferDetailsPO::getInquiryItemId, detail -> detail));
    }

    /**
     * 功能描述：获取供应商报价详情
     *
     * @param res 返回值
     * @param offerId 报价id
     * @param items 物料详情
     */
    @SneakyThrows
    private void getDetailInfo(OfferInfoResp res, Long offerId, List<InquiryItemPO> items) {
        // 获取商务信息
        CompletableFuture<List<InquiryBusinessTermPO>> businessFuture =
            CompletableFuture.supplyAsync(() -> businessTermsService
                .list(new LambdaQueryWrapper<InquiryBusinessTermPO>().eq(InquiryBusinessTermPO::getOfferId, offerId)));
        Map<Long, InquiryOfferDetailsPO> detailMap = getOfferDetailItemInfos(offerId);

        // 组装物料信息和报价信息
        res.setItemOffers(items.stream().map(item -> {
            ItemOfferInfoResp itemOffer = OfferConverter.INSTANCE.toItemOfferInfoResp(item);
            InquiryOfferDetailsResp detail =
                OfferConverter.INSTANCE.toInquiryOfferDetailsResp(detailMap.get(item.getId()));

            // 阶梯价信息
            if (Optional.ofNullable(detail).isPresent()) {
                detail.setStepPriceInfos(OfferConverter.INSTANCE
                    .toStepPriceReqList(stepPriceService.list(new LambdaQueryWrapper<InquiryStepPricePO>()
                        .eq(InquiryStepPricePO::getOfferDetailId, detail.getId()))));
                detail.setSubItemPriceInfos(OfferConverter.INSTANCE
                    .toSubItemPriceReqList(subItemService.list(new LambdaQueryWrapper<InquirySubItemPriceInfoPO>()
                        .eq(InquirySubItemPriceInfoPO::getOfferItemId, detail.getId()))));
            }
            itemOffer.setOfferDetail(Arrays.asList(detail));
            return itemOffer;
        }).collect(Collectors.toList()));

        // 汇总金额 含税总价赋值
        res.setTotalPriceIncludingTax(res.getItemOffers().stream().map(ItemOfferInfoResp::getOfferDetail)
            .map(e -> e.get(0)).map(InquiryOfferDetailsResp::getTotalPriceIncludingTax).reduce(BigDecimal::add)
            .orElse(new BigDecimal(0.00)));
        res.setBusiness(businessFuture.get());
    }
}
