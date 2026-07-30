-- Replace the navigation with AI resources curated for users in mainland China.
-- Run this script only after taking a full backup of the target database.

SET NAMES utf8mb4;
START TRANSACTION;

DELETE FROM `site`;
DELETE FROM `category`;

INSERT INTO `category`
    (`id`, `parent_id`, `sort`, `title`, `icon`, `levels`, `create_time`, `update_time`)
VALUES
    (1, 0, 1, '国产 AI 助手', 'fa-comments-o', 1, CURRENT_TIMESTAMP, NULL),
    (2, 0, 2, 'AI 搜索与学术', 'fa-search', 1, CURRENT_TIMESTAMP, NULL),
    (3, 0, 3, 'AI 图像与设计', 'fa-picture-o', 1, CURRENT_TIMESTAMP, NULL),
    (4, 0, 4, 'AI 视频与数字人', 'fa-film', 1, CURRENT_TIMESTAMP, NULL),
    (5, 0, 5, 'AI 语音与音乐', 'fa-music', 1, CURRENT_TIMESTAMP, NULL),
    (6, 0, 6, 'AI 写作与演示', 'fa-pencil-square-o', 1, CURRENT_TIMESTAMP, NULL),
    (7, 0, 7, 'AI 编程与开发', 'fa-code', 1, CURRENT_TIMESTAMP, NULL),
    (8, 0, 8, '模型 API 与开放平台', 'fa-cloud', 1, CURRENT_TIMESTAMP, NULL),
    (9, 0, 9, '智能体与应用搭建', 'fa-cogs', 1, CURRENT_TIMESTAMP, NULL),
    (10, 0, 10, '文档、知识与会议', 'fa-file-text-o', 1, CURRENT_TIMESTAMP, NULL),
    (11, 0, 11, 'AI 数据分析', 'fa-bar-chart', 1, CURRENT_TIMESTAMP, NULL),
    (12, 0, 12, 'AI 学习与资讯', 'fa-graduation-cap', 1, CURRENT_TIMESTAMP, NULL);

INSERT INTO `site`
    (`id`, `category_id`, `title`, `thumb`, `description`, `url`, `create_time`, `update_time`)
VALUES
    (1, 1, 'DeepSeek', 'ai-tool.svg', '深度求索推出的通用 AI 助手，支持推理、写作、问答和代码任务。', 'https://chat.deepseek.com/', CURRENT_TIMESTAMP, NULL),
    (2, 1, '豆包', 'ai-tool.svg', '字节跳动旗下 AI 助手，覆盖对话、搜索、图像、语音和学习场景。', 'https://www.doubao.com/', CURRENT_TIMESTAMP, NULL),
    (3, 1, '腾讯元宝', 'ai-tool.svg', '腾讯推出的 AI 助手，可结合腾讯生态进行搜索、阅读和内容创作。', 'https://yuanbao.tencent.com/', CURRENT_TIMESTAMP, NULL),
    (4, 1, '千问', 'ai-tool.svg', '阿里巴巴推出的 AI 助手，支持对话、文档处理、多模态创作和办公任务。', 'https://www.qianwen.com/qianwen/', CURRENT_TIMESTAMP, NULL),
    (5, 1, 'Kimi', 'ai-tool.svg', '月之暗面的 AI 助手，适合长文档阅读、搜索、推理和办公研究。', 'https://www.kimi.com/', CURRENT_TIMESTAMP, NULL),
    (6, 1, '文心助手', 'ai-tool.svg', '百度文心大模型的统一网页入口，提供智能问答、创作和信息查询。', 'https://ernie.baidu.com/', CURRENT_TIMESTAMP, NULL),
    (7, 1, '智谱清言', 'ai-tool.svg', '智谱推出的通用 AI 助手，支持对话、文件分析、代码和智能体任务。', 'https://chatglm.cn/', CURRENT_TIMESTAMP, NULL),
    (8, 1, '讯飞星火', 'ai-tool.svg', '科大讯飞推出的认知大模型助手，侧重中文对话、语音和办公学习。', 'https://xinghuo.xfyun.cn/', CURRENT_TIMESTAMP, NULL),
    (9, 1, '天工AI', 'ai-tool.svg', '昆仑万维推出的 AI 办公智能体，支持搜索、研究、文档和内容生成。', 'https://www.tiangong.cn/', CURRENT_TIMESTAMP, NULL),
    (10, 1, '纳米AI搜索', 'ai-tool.svg', '360 推出的多模型 AI 搜索与智能体入口，支持网页、图片和文档问答。', 'https://www.n.cn/', CURRENT_TIMESTAMP, NULL),
    (11, 1, '阶跃AI', 'ai-tool.svg', '阶跃星辰推出的多模态 AI 助手，支持文本、图像和语音交互。', 'https://chat.stepfun.com/', CURRENT_TIMESTAMP, NULL),
    (12, 1, '百小应', 'ai-tool.svg', '百川智能推出的 AI 助手，面向搜索、问答和个人任务处理。', 'https://yi.baichuan-ai.com/chat', CURRENT_TIMESTAMP, NULL),
    (13, 1, '商量 SenseChat', 'ai-tool.svg', '商汤科技推出的中文大模型助手，支持对话、创作和多模态理解。', 'https://chat.sensetime.com/', CURRENT_TIMESTAMP, NULL),

    (14, 2, '秘塔AI搜索', 'ai-tool.svg', '直接生成带来源的搜索答案，支持全网、文库和学术模式。', 'https://metaso.cn/', CURRENT_TIMESTAMP, NULL),
    (15, 2, '夸克AI', 'ai-tool.svg', '面向国内用户的 AI 搜索、浏览器、文档阅读和学习工具入口。', 'https://www.quark.cn/', CURRENT_TIMESTAMP, NULL),
    (16, 2, '博查AI搜索', 'ai-tool.svg', '国内 AI 搜索与检索服务，提供网页问答、来源整理和开发接口。', 'https://bocha.cn/', CURRENT_TIMESTAMP, NULL),
    (17, 2, '知乎直答', 'ai-tool.svg', '结合知乎内容和公开资料生成答案并提供来源线索。', 'https://zhida.zhihu.com/', CURRENT_TIMESTAMP, NULL),
    (18, 2, 'AMiner', 'ai-tool.svg', '清华团队建设的学术知识平台，提供论文、学者和科研趋势检索。', 'https://www.aminer.cn/', CURRENT_TIMESTAMP, NULL),
    (19, 2, '星火科研助手', 'ai-tool.svg', '科大讯飞面向论文研读、文献调研和学术写作的 AI 助手。', 'https://paper.xfyun.cn/', CURRENT_TIMESTAMP, NULL),
    (20, 2, 'CNKI AI', 'ai-tool.svg', '中国知网的专业知识检索与生成式学术服务入口。', 'https://ai.cnki.net/', CURRENT_TIMESTAMP, NULL),

    (21, 3, '即梦AI', 'ai-tool.svg', '字节跳动旗下的一站式 AI 图片与视频创作平台。', 'https://jimeng.jianying.com/', CURRENT_TIMESTAMP, NULL),
    (22, 3, '通义万相', 'ai-tool.svg', '阿里通义推出的图像与视频生成、编辑和视觉创作平台。', 'https://tongyi.aliyun.com/wan/', CURRENT_TIMESTAMP, NULL),
    (23, 3, '文心图像', 'ai-tool.svg', '百度文心的 AI 图像创作与视觉内容生成入口。', 'https://wenxin.baidu.com/', CURRENT_TIMESTAMP, NULL),
    (24, 3, 'LiblibAI', 'ai-tool.svg', '国内 AI 创作社区，提供模型、工作流和在线图像视频生成能力。', 'https://www.liblib.art/', CURRENT_TIMESTAMP, NULL),
    (25, 3, '堆友', 'ai-tool.svg', '阿里设计团队推出的创意设计平台，提供 AI 绘画、素材和设计工具。', 'https://d.design/', CURRENT_TIMESTAMP, NULL),
    (26, 3, '稿定AI', 'ai-tool.svg', '面向电商、新媒体和办公场景的 AI 设计与图片处理工具。', 'https://www.gaoding.art/', CURRENT_TIMESTAMP, NULL),
    (27, 3, '美图设计室', 'ai-tool.svg', '美图旗下的在线设计工具，提供 AI 海报、商品图和人像处理。', 'https://www.designkit.com/', CURRENT_TIMESTAMP, NULL),
    (28, 3, '无界AI', 'ai-tool.svg', '国内 AI 绘画与创作社区，支持模型选择、作品生成和分享。', 'https://www.wujieai.com/', CURRENT_TIMESTAMP, NULL),
    (29, 3, '触手AI', 'ai-tool.svg', '面向插画、动漫和设计场景的国产 AI 绘画平台。', 'https://acgnai.art/', CURRENT_TIMESTAMP, NULL),

    (30, 4, '可灵AI', 'ai-tool.svg', '快手推出的 AI 图片与视频生成平台，支持文本和参考素材创作。', 'https://klingai.com/app/', CURRENT_TIMESTAMP, NULL),
    (31, 4, 'Vidu', 'ai-tool.svg', '生数科技推出的视频生成平台，支持文本、图片和多主体参考。', 'https://www.vidu.cn/zh', CURRENT_TIMESTAMP, NULL),
    (32, 4, '海螺视频', 'ai-tool.svg', 'MiniMax 推出的 AI 视频创作平台，支持文本和图片生成视频。', 'https://hailuoai.com/', CURRENT_TIMESTAMP, NULL),
    (33, 4, '蝉镜', 'ai-tool.svg', '蝉妈妈旗下的 AI 数字人视频与智慧直播创作平台。', 'https://www.chanjing.cc/', CURRENT_TIMESTAMP, NULL),
    (34, 4, '百度曦灵', 'ai-tool.svg', '百度智能云的数字人和智能视频生产平台。', 'https://xiling.cloud.baidu.com/', CURRENT_TIMESTAMP, NULL),
    (35, 4, '闪剪AI', 'ai-tool.svg', '面向口播、营销和短视频场景的 AI 数字人创作工具。', 'https://shanjian.tv/', CURRENT_TIMESTAMP, NULL),
    (36, 4, '度加创作工具', 'ai-tool.svg', '百度推出的 AI 视频创作工具，支持素材处理、脚本和智能成片。', 'https://aigc.baidu.com/', CURRENT_TIMESTAMP, NULL),
    (37, 4, '魔珐有言', 'ai-tool.svg', '魔珐科技推出的 3D 数字人视频和虚拟内容创作平台。', 'https://www.youyan3d.com/', CURRENT_TIMESTAMP, NULL),

    (38, 5, '讯飞智作', 'ai-tool.svg', '科大讯飞的 AI 配音与音视频制作平台，提供多音色和多语种能力。', 'https://peiyin.xunfei.cn/', CURRENT_TIMESTAMP, NULL),
    (39, 5, '魔音工坊', 'ai-tool.svg', '面向短视频、有声内容和企业场景的 AI 配音工具。', 'https://www.moyin.com/', CURRENT_TIMESTAMP, NULL),
    (40, 5, '网易天音', 'ai-tool.svg', '网易云音乐推出的 AI 音乐创作工具，可辅助作词、作曲和编曲。', 'https://tianyin.music.163.com/', CURRENT_TIMESTAMP, NULL),
    (41, 5, '海绵音乐', 'ai-tool.svg', '字节跳动推出的 AI 音乐创作平台，支持歌曲和配乐生成。', 'https://www.haimian.com/', CURRENT_TIMESTAMP, NULL),
    (42, 5, 'Mureka', 'ai-tool.svg', '昆仑万维推出的 AI 音乐生成与编辑平台。', 'https://www.mureka.cn/', CURRENT_TIMESTAMP, NULL),

    (43, 6, 'WPS AI', 'ai-tool.svg', '金山办公的 AI 办公入口，支持文档、表格、PPT、PDF 和会议处理。', 'https://x.wps.cn/', CURRENT_TIMESTAMP, NULL),
    (44, 6, '百度文库AI', 'ai-tool.svg', '百度文库提供的 AI 写作、PPT 生成、资料总结和文档处理工具。', 'https://wenku.baidu.com/', CURRENT_TIMESTAMP, NULL),
    (45, 6, '讯飞智文', 'ai-tool.svg', '科大讯飞推出的 AI 文档与演示生成平台，支持大纲、写作和排版。', 'https://zhiwen.xfyun.cn/', CURRENT_TIMESTAMP, NULL),
    (46, 6, 'AiPPT', 'ai-tool.svg', '国产 AI 演示工具，可根据主题或文档生成、编辑和美化 PPT。', 'https://www.aippt.cn/', CURRENT_TIMESTAMP, NULL),
    (47, 6, '博思AIPPT', 'ai-tool.svg', '博思云创推出的 AI PPT 工具，支持主题、文档和链接生成演示文稿。', 'https://pptgo.cn/', CURRENT_TIMESTAMP, NULL),
    (48, 6, 'boardmix AI', 'ai-tool.svg', '博思白板的 AI 创作能力，支持脑图、流程图、白板和演示内容生成。', 'https://boardmix.cn/', CURRENT_TIMESTAMP, NULL),
    (49, 6, 'ProcessOn AI', 'ai-tool.svg', '国产在线作图平台的 AI 助手，可生成流程图、思维导图和结构化内容。', 'https://www.processon.com/ai', CURRENT_TIMESTAMP, NULL),
    (50, 6, '秘塔写作猫', 'ai-tool.svg', '中文写作辅助工具，提供改写、校对、续写和表达优化。', 'https://xiezuocat.com/', CURRENT_TIMESTAMP, NULL),

    (51, 7, '通义灵码', 'ai-tool.svg', '阿里云推出的 AI 编程助手，支持补全、问答、代码编辑和智能体开发。', 'https://lingma.aliyun.com/', CURRENT_TIMESTAMP, NULL),
    (52, 7, '文心快码', 'ai-tool.svg', '百度推出的 AI 编程助手，覆盖代码生成、解释、测试和工程级协作。', 'https://comate.baidu.com/', CURRENT_TIMESTAMP, NULL),
    (53, 7, 'TRAE 中国版', 'ai-tool.svg', '字节跳动推出的国产 AI IDE，集成代码问答、编辑和智能体能力。', 'https://www.trae.cn/', CURRENT_TIMESTAMP, NULL),
    (54, 7, 'CodeGeeX', 'ai-tool.svg', '智谱与清华团队推出的多语言 AI 编程助手。', 'https://codegeex.cn/', CURRENT_TIMESTAMP, NULL),
    (55, 7, '腾讯 CodeBuddy', 'ai-tool.svg', '腾讯云推出的 AI 编程工具，提供 IDE、插件和命令行形态。', 'https://www.codebuddy.cn/', CURRENT_TIMESTAMP, NULL),
    (56, 7, 'GitCode AI', 'ai-tool.svg', '国内代码托管平台 GitCode 的 AI 开发与代码协作入口。', 'https://gitcode.com/ai', CURRENT_TIMESTAMP, NULL),
    (57, 7, 'Fitten Code', 'ai-tool.svg', '非十科技推出的 AI 编程助手，支持代码生成、补全和工程问答。', 'https://code.fittentech.com/', CURRENT_TIMESTAMP, NULL),

    (58, 8, 'DeepSeek 开放平台', 'ai-tool.svg', 'DeepSeek 模型 API、文档、密钥和用量管理入口。', 'https://platform.deepseek.com/', CURRENT_TIMESTAMP, NULL),
    (59, 8, '阿里云百炼', 'ai-tool.svg', '阿里云的一站式大模型开发平台，提供模型调用、应用构建和评测。', 'https://bailian.console.aliyun.com/', CURRENT_TIMESTAMP, NULL),
    (60, 8, '火山方舟', 'ai-tool.svg', '火山引擎的大模型服务平台，提供模型推理、精调、评测和应用开发。', 'https://www.volcengine.com/product/ark', CURRENT_TIMESTAMP, NULL),
    (61, 8, '百度千帆', 'ai-tool.svg', '百度智能云的大模型与 Agent 开发平台。', 'https://cloud.baidu.com/product-s/qianfan_home', CURRENT_TIMESTAMP, NULL),
    (62, 8, '腾讯混元', 'ai-tool.svg', '腾讯云混元模型的 API、开发工具和企业服务入口。', 'https://cloud.tencent.com/product/tclm', CURRENT_TIMESTAMP, NULL),
    (63, 8, '智谱开放平台', 'ai-tool.svg', '智谱 GLM 系列模型的 API、智能体和开发文档平台。', 'https://open.bigmodel.cn/', CURRENT_TIMESTAMP, NULL),
    (64, 8, 'Kimi 开放平台', 'ai-tool.svg', 'Kimi 模型 API、控制台和开发文档入口。', 'https://platform.kimi.com/', CURRENT_TIMESTAMP, NULL),
    (65, 8, 'MiniMax 开放平台', 'ai-tool.svg', 'MiniMax 文本、语音、音乐和视频模型的 API 平台。', 'https://platform.minimaxi.com/', CURRENT_TIMESTAMP, NULL),
    (66, 8, '阶跃星辰开放平台', 'ai-tool.svg', '阶跃星辰多模态模型的 API、控制台和开发文档入口。', 'https://platform.stepfun.com/', CURRENT_TIMESTAMP, NULL),
    (67, 8, '讯飞开放平台', 'ai-tool.svg', '科大讯飞的语音、语言和大模型 API 与行业能力平台。', 'https://www.xfyun.cn/', CURRENT_TIMESTAMP, NULL),
    (68, 8, '硅基流动', 'ai-tool.svg', '国内模型推理云平台，提供多种开源与国产模型 API。', 'https://cloud.siliconflow.cn/', CURRENT_TIMESTAMP, NULL),
    (69, 8, '魔搭社区', 'ai-tool.svg', '阿里达摩院发起的模型、数据集、创空间和开发者社区。', 'https://modelscope.cn/', CURRENT_TIMESTAMP, NULL),

    (70, 9, '扣子', 'ai-tool.svg', '字节跳动推出的 AI 智能体与工作流开发平台。', 'https://www.coze.cn/', CURRENT_TIMESTAMP, NULL),
    (71, 9, '腾讯元器', 'ai-tool.svg', '腾讯官方智能体平台，支持知识库、工作流、插件和多渠道发布。', 'https://yuanqi.tencent.com/', CURRENT_TIMESTAMP, NULL),
    (72, 9, '百度秒哒', 'ai-tool.svg', '百度推出的无代码 AI 应用开发平台，可通过自然语言生成应用。', 'https://www.miaoda.cn/', CURRENT_TIMESTAMP, NULL),
    (73, 9, '文心智能体平台', 'ai-tool.svg', '百度面向开发者的智能体构建、调试与分发平台。', 'https://agents.baidu.com/', CURRENT_TIMESTAMP, NULL),
    (74, 9, 'Dify', 'ai-tool.svg', '国内团队发起的开源大模型应用开发平台，支持工作流、RAG 和 Agent。', 'https://dify.ai/zh', CURRENT_TIMESTAMP, NULL),
    (75, 9, 'FastGPT', 'ai-tool.svg', '国产开源知识库问答与大模型应用编排平台。', 'https://fastgpt.cn/', CURRENT_TIMESTAMP, NULL),
    (76, 9, 'MaxKB', 'ai-tool.svg', '飞致云开源的企业级智能体与知识库问答平台。', 'https://maxkb.cn/', CURRENT_TIMESTAMP, NULL),
    (77, 9, 'RAGFlow', 'ai-tool.svg', '国内团队开源的 RAG 与智能体开发平台，强调文档理解和检索流程。', 'https://ragflow.io/', CURRENT_TIMESTAMP, NULL),

    (78, 10, 'ima知识库', 'ai-tool.svg', '腾讯推出的个人知识库工具，支持资料收藏、检索、问答和写作。', 'https://ima.qq.com/', CURRENT_TIMESTAMP, NULL),
    (79, 10, '通义听悟', 'ai-tool.svg', '阿里通义推出的音视频转写、摘要、章节和内容问答工具。', 'https://tingwu.aliyun.com/', CURRENT_TIMESTAMP, NULL),
    (80, 10, '飞书妙记', 'ai-tool.svg', '飞书的音视频转写和会议内容整理工具，支持摘要与待办提取。', 'https://www.feishu.cn/product/minutes', CURRENT_TIMESTAMP, NULL),
    (81, 10, '讯飞听见', 'ai-tool.svg', '科大讯飞的录音转文字、会议转写、字幕和翻译服务。', 'https://www.iflyrec.com/', CURRENT_TIMESTAMP, NULL),
    (82, 10, '腾讯会议', 'ai-tool.svg', '腾讯会议提供 AI 纪要、转写、总结和会后内容整理能力。', 'https://meeting.tencent.com/', CURRENT_TIMESTAMP, NULL),
    (83, 10, '有道云笔记', 'ai-tool.svg', '网易有道的知识记录与文档管理工具，提供 AI 阅读和写作能力。', 'https://note.youdao.com/', CURRENT_TIMESTAMP, NULL),
    (84, 10, '印象AI', 'ai-tool.svg', '印象笔记的 AI 阅读、知识整理和内容创作入口。', 'https://www.yinxiang.com/', CURRENT_TIMESTAMP, NULL),

    (85, 11, 'ChatExcel', 'ai-tool.svg', '国内团队推出的表格 AI 工具，可用自然语言处理和分析 Excel 数据。', 'https://www.chatexcel.com/', CURRENT_TIMESTAMP, NULL),
    (86, 11, '九数云', 'ai-tool.svg', '国内在线数据分析平台，提供可视化、自助分析和 AI 辅助洞察。', 'https://www.jiushuyun.com/', CURRENT_TIMESTAMP, NULL),
    (87, 11, '腾讯云BI', 'ai-tool.svg', '腾讯云商业智能平台，提供对话式智能分析、报表和可视化能力。', 'https://cloud.tencent.cn/product/bi', CURRENT_TIMESTAMP, NULL),
    (88, 11, '阿里云 Quick BI', 'ai-tool.svg', '阿里云商业智能平台，提供智能问数、数据可视化和分析协作。', 'https://www.aliyun.com/product/quick-bi', CURRENT_TIMESTAMP, NULL),
    (89, 11, '观远BI', 'ai-tool.svg', '国产商业智能与数据分析平台，提供智能问答和业务洞察能力。', 'https://www.guandata.com/', CURRENT_TIMESTAMP, NULL),

    (90, 12, 'Datawhale', 'ai-tool.svg', '国内开源学习社区，持续提供人工智能、数据科学课程和共学项目。', 'https://www.datawhale.cn/', CURRENT_TIMESTAMP, NULL),
    (91, 12, '飞桨 PaddlePaddle', 'ai-tool.svg', '百度开源深度学习平台，提供框架、模型、工具和中文学习资源。', 'https://www.paddlepaddle.org.cn/', CURRENT_TIMESTAMP, NULL),
    (92, 12, 'OpenMMLab', 'ai-tool.svg', '国内发起的计算机视觉开源体系，提供算法库、模型和课程。', 'https://openmmlab.com/', CURRENT_TIMESTAMP, NULL),
    (93, 12, '机器之心', 'ai-tool.svg', '国内人工智能产业与技术媒体，报道研究、产品和行业进展。', 'https://www.jiqizhixin.com/', CURRENT_TIMESTAMP, NULL),
    (94, 12, '量子位', 'ai-tool.svg', '面向中文读者的人工智能资讯与产业报道平台。', 'https://www.qbitai.com/', CURRENT_TIMESTAMP, NULL),
    (95, 12, '新智元', 'ai-tool.svg', '国内人工智能技术、产业和前沿研究资讯平台。', 'https://www.aitntnews.com/', CURRENT_TIMESTAMP, NULL),
    (96, 12, 'CSDN 人工智能', 'ai-tool.svg', 'CSDN 的人工智能技术社区与开发者内容频道。', 'https://ai.csdn.net/', CURRENT_TIMESTAMP, NULL);

-- Logo files are bundled under static/tmp so the homepage has no runtime
-- dependency on external favicon services.
UPDATE `site`
SET `thumb` = CONCAT('ai-site-', LPAD(`id`, 3, '0'), '.png')
WHERE `id` BETWEEN 1 AND 96;

COMMIT;

SELECT COUNT(*) AS category_count FROM `category`;
SELECT COUNT(*) AS site_count FROM `site`;
