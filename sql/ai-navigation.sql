-- Replace the legacy design-resource navigation with curated AI resources.
-- Run this script only after taking a full backup of the target database.

SET NAMES utf8mb4;
START TRANSACTION;

DELETE FROM `site`;
DELETE FROM `category`;

INSERT INTO `category`
    (`id`, `parent_id`, `sort`, `title`, `icon`, `levels`, `create_time`, `update_time`)
VALUES
    (1, 0, 1, 'AI 助手与对话', 'fa-comments-o', 1, CURRENT_TIMESTAMP, NULL),
    (2, 0, 2, 'AI 搜索与学术研究', 'fa-search', 1, CURRENT_TIMESTAMP, NULL),
    (3, 0, 3, 'AI 图像与设计', 'fa-picture-o', 1, CURRENT_TIMESTAMP, NULL),
    (4, 0, 4, 'AI 视频与数字人', 'fa-film', 1, CURRENT_TIMESTAMP, NULL),
    (5, 0, 5, 'AI 语音、音频与音乐', 'fa-music', 1, CURRENT_TIMESTAMP, NULL),
    (6, 0, 6, 'AI 写作与营销', 'fa-pencil-square-o', 1, CURRENT_TIMESTAMP, NULL),
    (7, 0, 7, 'AI 演示与视觉表达', 'fa-desktop', 1, CURRENT_TIMESTAMP, NULL),
    (8, 0, 8, 'AI 编程与应用构建', 'fa-code', 1, CURRENT_TIMESTAMP, NULL),
    (9, 0, 9, '模型、API 与开放生态', 'fa-cloud', 1, CURRENT_TIMESTAMP, NULL),
    (10, 0, 10, '智能体与自动化', 'fa-cogs', 1, CURRENT_TIMESTAMP, NULL),
    (11, 0, 11, 'AI 文档与知识管理', 'fa-file-text-o', 1, CURRENT_TIMESTAMP, NULL),
    (12, 0, 12, 'AI 办公与会议协作', 'fa-microphone', 1, CURRENT_TIMESTAMP, NULL),
    (13, 0, 13, 'AI 数据分析', 'fa-bar-chart', 1, CURRENT_TIMESTAMP, NULL),
    (14, 0, 14, 'AI 学习与课程', 'fa-graduation-cap', 1, CURRENT_TIMESTAMP, NULL);

INSERT INTO `site`
    (`id`, `category_id`, `title`, `thumb`, `description`, `url`, `create_time`, `update_time`)
VALUES
    (1, 1, 'ChatGPT', 'ai-tool.svg', 'OpenAI 通用 AI 助手，适合问答、写作、分析、编程与多模态创作。', 'https://chatgpt.com/', CURRENT_TIMESTAMP, NULL),
    (2, 1, 'Claude', 'ai-tool.svg', 'Anthropic AI 助手，擅长复杂推理、长文档处理与编程协作。', 'https://claude.ai/', CURRENT_TIMESTAMP, NULL),
    (3, 1, 'Gemini', 'ai-tool.svg', 'Google AI 助手，支持多模态对话、研究与 Google 生态协作。', 'https://gemini.google.com/', CURRENT_TIMESTAMP, NULL),
    (4, 1, 'Microsoft Copilot', 'ai-tool.svg', 'Microsoft AI 助手，提供搜索、创作与日常任务支持。', 'https://copilot.microsoft.com/', CURRENT_TIMESTAMP, NULL),
    (5, 1, 'DeepSeek', 'ai-tool.svg', 'DeepSeek 对话助手，支持通用问答、推理和代码任务。', 'https://chat.deepseek.com/', CURRENT_TIMESTAMP, NULL),
    (6, 1, 'Qwen Chat', 'ai-tool.svg', '阿里通义千问的网页版 AI 助手，支持多模态对话与创作。', 'https://chat.qwen.ai/', CURRENT_TIMESTAMP, NULL),
    (7, 1, 'Kimi', 'ai-tool.svg', '月之暗面的 AI 助手，适合长文本阅读、搜索与办公任务。', 'https://www.kimi.com/', CURRENT_TIMESTAMP, NULL),
    (8, 1, '豆包', 'ai-tool.svg', '字节跳动旗下 AI 助手，支持对话、写作、图像和语音能力。', 'https://www.doubao.com/', CURRENT_TIMESTAMP, NULL),

    (9, 2, 'Perplexity', 'ai-tool.svg', '提供来源引用的 AI 搜索与研究助手。', 'https://www.perplexity.ai/', CURRENT_TIMESTAMP, NULL),
    (10, 2, 'NotebookLM', 'ai-tool.svg', 'Google 的来源驱动 AI 研究与笔记工具，可基于资料进行问答、总结和内容生成。', 'https://notebooklm.google.com/', CURRENT_TIMESTAMP, NULL),
    (11, 2, 'Elicit', 'ai-tool.svg', '面向科研文献检索、筛选和综述的 AI 研究助手。', 'https://elicit.com/', CURRENT_TIMESTAMP, NULL),
    (12, 2, 'Consensus', 'ai-tool.svg', '从学术论文中检索并汇总证据的 AI 搜索工具。', 'https://consensus.app/', CURRENT_TIMESTAMP, NULL),
    (13, 2, 'SciSpace', 'ai-tool.svg', '辅助阅读、解释和检索论文的 AI 研究平台。', 'https://scispace.com/', CURRENT_TIMESTAMP, NULL),
    (14, 2, 'Genspark', 'ai-tool.svg', '面向复杂主题的 AI 搜索、研究和内容生成平台。', 'https://www.genspark.ai/', CURRENT_TIMESTAMP, NULL),
    (15, 2, 'You.com', 'ai-tool.svg', '集搜索、问答和智能体能力于一体的 AI 平台。', 'https://you.com/', CURRENT_TIMESTAMP, NULL),
    (16, 2, 'Phind', 'ai-tool.svg', '面向开发者技术问题的 AI 搜索与问答工具。', 'https://www.phind.com/', CURRENT_TIMESTAMP, NULL),

    (17, 3, 'Midjourney', 'ai-tool.svg', '通过文本提示生成高质量图像与视觉概念的 AI 创作平台。', 'https://www.midjourney.com/', CURRENT_TIMESTAMP, NULL),
    (18, 3, 'Adobe Firefly', 'ai-tool.svg', 'Adobe 的生成式 AI 创作平台，支持图像、视频与设计工作流。', 'https://firefly.adobe.com/', CURRENT_TIMESTAMP, NULL),
    (19, 3, 'Ideogram', 'ai-tool.svg', '擅长文字排版与视觉设计的 AI 图像生成平台。', 'https://ideogram.ai/', CURRENT_TIMESTAMP, NULL),
    (20, 3, 'Leonardo.Ai', 'ai-tool.svg', '面向游戏、美术和品牌内容的 AI 图像生成与编辑平台。', 'https://leonardo.ai/', CURRENT_TIMESTAMP, NULL),
    (21, 3, 'Recraft', 'ai-tool.svg', '面向品牌、矢量图和设计资产的 AI 图像生成工具。', 'https://www.recraft.ai/', CURRENT_TIMESTAMP, NULL),
    (22, 3, 'Krea', 'ai-tool.svg', '实时生成、增强和编辑图像与视频的 AI 创作平台。', 'https://www.krea.ai/', CURRENT_TIMESTAMP, NULL),
    (23, 3, 'Canva Magic Studio', 'ai-tool.svg', 'Canva 的一站式 AI 设计与内容创作工具集。', 'https://www.canva.com/magic-studio/', CURRENT_TIMESTAMP, NULL),
    (24, 3, 'FLUX Playground', 'ai-tool.svg', 'Black Forest Labs 的 FLUX 图像模型在线生成与测试平台。', 'https://playground.bfl.ai/', CURRENT_TIMESTAMP, NULL),

    (25, 4, 'Runway', 'ai-tool.svg', '面向创作者的 AI 视频生成、编辑与视觉特效平台。', 'https://runwayml.com/', CURRENT_TIMESTAMP, NULL),
    (26, 4, 'Kling AI', 'ai-tool.svg', '快手推出的 AI 图像与视频生成平台。', 'https://klingai.com/', CURRENT_TIMESTAMP, NULL),
    (27, 4, 'Hailuo AI', 'ai-tool.svg', 'MiniMax 推出的 AI 视频生成与创作平台。', 'https://hailuoai.video/', CURRENT_TIMESTAMP, NULL),
    (28, 4, 'Luma Dream Machine', 'ai-tool.svg', 'Luma AI 的文本与图片生成视频工具。', 'https://lumalabs.ai/dream-machine', CURRENT_TIMESTAMP, NULL),
    (29, 4, 'Pika', 'ai-tool.svg', '面向短视频和创意特效的 AI 视频生成工具。', 'https://pika.art/', CURRENT_TIMESTAMP, NULL),
    (30, 4, 'Vidu', 'ai-tool.svg', '支持文本、图片和参考素材生成视频的 AI 平台。', 'https://www.vidu.com/', CURRENT_TIMESTAMP, NULL),
    (31, 4, 'HeyGen', 'ai-tool.svg', '用于数字人、配音和营销视频制作的 AI 平台。', 'https://www.heygen.com/', CURRENT_TIMESTAMP, NULL),
    (32, 4, 'Synthesia', 'ai-tool.svg', '面向企业培训和演示场景的 AI 数字人视频平台。', 'https://www.synthesia.io/', CURRENT_TIMESTAMP, NULL),

    (33, 5, 'ElevenLabs', 'ai-tool.svg', '提供 AI 语音合成、配音、声音克隆与音频工具。', 'https://elevenlabs.io/', CURRENT_TIMESTAMP, NULL),
    (34, 5, 'Suno', 'ai-tool.svg', '通过文本提示生成歌曲、配乐和人声的 AI 音乐平台。', 'https://suno.com/', CURRENT_TIMESTAMP, NULL),
    (35, 5, 'Udio', 'ai-tool.svg', '用于生成、编辑和分享 AI 音乐作品的平台。', 'https://www.udio.com/', CURRENT_TIMESTAMP, NULL),
    (36, 5, 'Adobe Podcast', 'ai-tool.svg', '基于浏览器的 AI 录音、降噪和播客编辑工具。', 'https://podcast.adobe.com/', CURRENT_TIMESTAMP, NULL),
    (37, 5, 'Descript', 'ai-tool.svg', '通过编辑文本处理音频和视频的 AI 创作工具。', 'https://www.descript.com/', CURRENT_TIMESTAMP, NULL),
    (38, 5, 'Murf AI', 'ai-tool.svg', '面向视频、课程和营销内容的 AI 配音平台。', 'https://murf.ai/', CURRENT_TIMESTAMP, NULL),
    (39, 5, 'Stable Audio', 'ai-tool.svg', 'Stability AI 推出的音乐与音效生成平台。', 'https://stableaudio.com/', CURRENT_TIMESTAMP, NULL),
    (40, 5, 'AIVA', 'ai-tool.svg', '面向配乐和音乐制作的 AI 作曲助手。', 'https://www.aiva.ai/', CURRENT_TIMESTAMP, NULL),

    (41, 11, 'Notion AI', 'ai-tool.svg', '集成在 Notion 工作区中的写作、总结和知识问答助手。', 'https://www.notion.so/product/ai', CURRENT_TIMESTAMP, NULL),
    (42, 6, 'Grammarly AI', 'ai-tool.svg', '提供写作润色、改写和生成能力的 AI 助手。', 'https://www.grammarly.com/ai', CURRENT_TIMESTAMP, NULL),
    (43, 7, 'Gamma', 'ai-tool.svg', '使用 AI 快速生成演示文稿、文档和网页。', 'https://gamma.app/', CURRENT_TIMESTAMP, NULL),
    (44, 6, 'Jasper', 'ai-tool.svg', '面向品牌和营销团队的 AI 内容创作平台。', 'https://www.jasper.ai/', CURRENT_TIMESTAMP, NULL),
    (45, 6, 'Copy.ai', 'ai-tool.svg', '面向销售与营销流程的 AI 写作和自动化平台。', 'https://www.copy.ai/', CURRENT_TIMESTAMP, NULL),
    (46, 6, 'QuillBot', 'ai-tool.svg', '提供改写、润色、摘要和语法检查的 AI 写作工具。', 'https://quillbot.com/', CURRENT_TIMESTAMP, NULL),
    (47, 7, 'Beautiful.ai', 'ai-tool.svg', '自动排版并辅助生成内容的 AI 演示文稿工具。', 'https://www.beautiful.ai/', CURRENT_TIMESTAMP, NULL),
    (48, 7, 'Napkin AI', 'ai-tool.svg', '将文字内容快速转化为图示和视觉表达的 AI 工具。', 'https://www.napkin.ai/', CURRENT_TIMESTAMP, NULL),

    (49, 8, 'OpenAI Codex', 'ai-tool.svg', 'OpenAI 的编程智能体，用于编写、审查和交付代码。', 'https://openai.com/codex/', CURRENT_TIMESTAMP, NULL),
    (50, 8, 'Claude Code', 'ai-tool.svg', 'Anthropic 的终端编程智能体，可理解代码库并执行开发任务。', 'https://www.anthropic.com/claude-code', CURRENT_TIMESTAMP, NULL),
    (51, 8, 'GitHub Copilot', 'ai-tool.svg', '集成代码补全、聊天和智能体工作流的 AI 开发助手。', 'https://github.com/features/copilot', CURRENT_TIMESTAMP, NULL),
    (52, 8, 'Cursor', 'ai-tool.svg', '以代码库上下文和智能体能力为核心的 AI 代码编辑器。', 'https://www.cursor.com/', CURRENT_TIMESTAMP, NULL),
    (53, 8, 'Windsurf', 'ai-tool.svg', '面向智能体开发工作流的 AI 代码编辑器。', 'https://windsurf.com/', CURRENT_TIMESTAMP, NULL),
    (54, 8, 'Replit AI', 'ai-tool.svg', '在浏览器中用 AI 构建、运行和发布应用。', 'https://replit.com/ai', CURRENT_TIMESTAMP, NULL),
    (55, 8, 'v0', 'ai-tool.svg', '使用自然语言生成前端界面和全栈 Web 应用。', 'https://v0.dev/', CURRENT_TIMESTAMP, NULL),
    (56, 8, 'Bolt.new', 'ai-tool.svg', '在浏览器中通过 AI 对话生成和运行全栈应用。', 'https://bolt.new/', CURRENT_TIMESTAMP, NULL),

    (57, 9, 'OpenAI Platform', 'ai-tool.svg', 'OpenAI 模型、API、密钥和开发工具的官方平台。', 'https://platform.openai.com/', CURRENT_TIMESTAMP, NULL),
    (58, 9, 'Anthropic Console', 'ai-tool.svg', 'Claude API 的密钥、用量和提示词开发控制台。', 'https://console.anthropic.com/', CURRENT_TIMESTAMP, NULL),
    (59, 9, 'Google AI Studio', 'ai-tool.svg', '试用 Gemini 模型并构建生成式 AI 应用的开发平台。', 'https://aistudio.google.com/', CURRENT_TIMESTAMP, NULL),
    (60, 9, 'DeepSeek Platform', 'ai-tool.svg', 'DeepSeek API 的密钥、文档和用量管理平台。', 'https://platform.deepseek.com/', CURRENT_TIMESTAMP, NULL),
    (61, 9, 'Hugging Face', 'ai-tool.svg', '开放模型、数据集、应用演示和机器学习协作社区。', 'https://huggingface.co/', CURRENT_TIMESTAMP, NULL),
    (62, 9, 'ModelScope', 'ai-tool.svg', '魔搭社区的开放模型、数据集和 AI 开发资源平台。', 'https://www.modelscope.cn/', CURRENT_TIMESTAMP, NULL),
    (63, 9, 'OpenRouter', 'ai-tool.svg', '通过统一 API 调用多个模型提供商的聚合平台。', 'https://openrouter.ai/', CURRENT_TIMESTAMP, NULL),
    (64, 9, 'GroqCloud', 'ai-tool.svg', '面向开发者的高速模型推理与 API 控制台。', 'https://console.groq.com/', CURRENT_TIMESTAMP, NULL),

    (65, 10, 'Dify', 'ai-tool.svg', '用于构建、编排和部署生成式 AI 应用与智能体的平台。', 'https://dify.ai/', CURRENT_TIMESTAMP, NULL),
    (66, 10, 'Coze', 'ai-tool.svg', '支持工作流、知识库和插件的 AI 智能体开发平台。', 'https://www.coze.com/', CURRENT_TIMESTAMP, NULL),
    (67, 10, 'n8n AI', 'ai-tool.svg', '将 AI 节点与业务系统连接起来的工作流自动化平台。', 'https://n8n.io/ai/', CURRENT_TIMESTAMP, NULL),
    (68, 10, 'LangChain', 'ai-tool.svg', '构建大模型应用和智能体工作流的开发框架与平台。', 'https://www.langchain.com/', CURRENT_TIMESTAMP, NULL),
    (69, 10, 'LlamaIndex', 'ai-tool.svg', '连接私有数据并构建检索增强应用和智能体的框架。', 'https://www.llamaindex.ai/', CURRENT_TIMESTAMP, NULL),
    (70, 10, 'Flowise', 'ai-tool.svg', '通过可视化流程搭建大模型应用和 AI 智能体。', 'https://flowiseai.com/', CURRENT_TIMESTAMP, NULL),
    (71, 10, 'Zapier AI', 'ai-tool.svg', '把 AI 能力接入多种应用与业务自动化流程。', 'https://zapier.com/ai', CURRENT_TIMESTAMP, NULL),
    (72, 10, 'CrewAI', 'ai-tool.svg', '用于编排角色化智能体和多智能体协作流程的平台。', 'https://www.crewai.com/', CURRENT_TIMESTAMP, NULL),

    (73, 11, 'ChatPDF', 'ai-tool.svg', '上传 PDF 后进行摘要、问答和内容定位的 AI 工具。', 'https://www.chatpdf.com/', CURRENT_TIMESTAMP, NULL),
    (74, 11, 'Humata', 'ai-tool.svg', '面向文档总结、问答与知识提取的 AI 助手。', 'https://www.humata.ai/', CURRENT_TIMESTAMP, NULL),
    (75, 11, 'Acrobat AI Assistant', 'ai-tool.svg', 'Adobe Acrobat 的 PDF 总结、问答和内容分析助手。', 'https://www.adobe.com/acrobat/generative-ai-pdf.html', CURRENT_TIMESTAMP, NULL),
    (76, 12, 'Otter.ai', 'ai-tool.svg', '提供会议录音、转写、摘要和行动项整理。', 'https://otter.ai/', CURRENT_TIMESTAMP, NULL),
    (77, 12, 'Fireflies.ai', 'ai-tool.svg', '自动记录、转写和总结在线会议的 AI 助手。', 'https://fireflies.ai/', CURRENT_TIMESTAMP, NULL),
    (78, 12, 'Read AI', 'ai-tool.svg', '生成会议转写、摘要、洞察和后续行动建议。', 'https://www.read.ai/', CURRENT_TIMESTAMP, NULL),
    (79, 12, 'Granola', 'ai-tool.svg', '将人工笔记与会议转写结合起来的 AI 会议助手。', 'https://www.granola.ai/', CURRENT_TIMESTAMP, NULL),
    (80, 12, 'Microsoft 365 Copilot', 'ai-tool.svg', '集成在 Word、Excel、PowerPoint 等办公应用中的 AI 助手。', 'https://www.microsoft.com/microsoft-365/copilot', CURRENT_TIMESTAMP, NULL),

    (81, 13, 'Julius AI', 'ai-tool.svg', '通过自然语言分析表格、绘图并解释数据。', 'https://julius.ai/', CURRENT_TIMESTAMP, NULL),
    (82, 13, 'Hex', 'ai-tool.svg', '面向数据团队的 AI 分析、Notebook 和数据应用平台。', 'https://hex.tech/product/explore/', CURRENT_TIMESTAMP, NULL),
    (83, 13, 'Rows AI', 'ai-tool.svg', '在电子表格中使用 AI 进行研究、分类和数据处理。', 'https://rows.com/ai', CURRENT_TIMESTAMP, NULL),
    (84, 13, 'Tableau AI', 'ai-tool.svg', '将生成式 AI 能力融入 Tableau 数据分析流程。', 'https://www.tableau.com/products/tableau-ai', CURRENT_TIMESTAMP, NULL),
    (85, 13, 'Power BI Copilot', 'ai-tool.svg', '使用自然语言辅助构建、分析和总结 Power BI 报告。', 'https://powerbi.microsoft.com/en-us/copilot/', CURRENT_TIMESTAMP, NULL),
    (86, 13, 'DataRobot', 'ai-tool.svg', '面向企业的数据科学、预测 AI 与生成式 AI 平台。', 'https://www.datarobot.com/', CURRENT_TIMESTAMP, NULL),
    (87, 13, 'Akkio', 'ai-tool.svg', '面向业务分析和营销数据的无代码 AI 平台。', 'https://www.akkio.com/', CURRENT_TIMESTAMP, NULL),
    (88, 13, 'Obviously AI', 'ai-tool.svg', '使用无代码方式构建预测模型并分析业务数据。', 'https://www.obviously.ai/', CURRENT_TIMESTAMP, NULL),

    (89, 14, 'DeepLearning.AI', 'ai-tool.svg', '提供机器学习、深度学习和生成式 AI 系列课程。', 'https://www.deeplearning.ai/', CURRENT_TIMESTAMP, NULL),
    (90, 14, 'fast.ai', 'ai-tool.svg', '面向实践者的免费深度学习课程、教材和开源库。', 'https://www.fast.ai/', CURRENT_TIMESTAMP, NULL),
    (91, 14, 'Hugging Face Learn', 'ai-tool.svg', '涵盖大模型、智能体、计算机视觉等主题的开放课程。', 'https://huggingface.co/learn', CURRENT_TIMESTAMP, NULL),
    (92, 14, 'Google AI for Developers', 'ai-tool.svg', 'Google 面向开发者的 Gemini API 文档、示例和学习资源。', 'https://ai.google.dev/', CURRENT_TIMESTAMP, NULL),
    (93, 14, 'OpenAI Academy', 'ai-tool.svg', 'OpenAI 提供的实用 AI 技能课程与学习社区。', 'https://academy.openai.com/', CURRENT_TIMESTAMP, NULL),
    (94, 14, 'Microsoft AI Learning Hub', 'ai-tool.svg', 'Microsoft Learn 的 AI 学习路径、模块和实战资源。', 'https://learn.microsoft.com/ai/', CURRENT_TIMESTAMP, NULL),
    (95, 2, 'arXiv AI', 'ai-tool.svg', '浏览 arXiv 人工智能分类中的最新论文。', 'https://arxiv.org/list/cs.AI/recent', CURRENT_TIMESTAMP, NULL),
    (96, 2, 'OpenReview', 'ai-tool.svg', '机器学习会议论文、评审和学术讨论的开放平台。', 'https://openreview.net/', CURRENT_TIMESTAMP, NULL);

-- Logo files are bundled under static/tmp so the homepage has no runtime
-- dependency on external favicon services.
UPDATE `site`
SET `thumb` = CONCAT('ai-site-', LPAD(`id`, 3, '0'), '.png')
WHERE `id` BETWEEN 1 AND 96;

COMMIT;

SELECT COUNT(*) AS category_count FROM `category`;
SELECT COUNT(*) AS site_count FROM `site`;
