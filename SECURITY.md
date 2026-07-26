# Security notes

## 兼容边界

本项目必须运行在 JDK 8，因此采用 Spring Boot 2.7.18、Spring Framework 5.3.39 和 Apache Shiro 1.13.0。Shiro 2.x 需要 Java 11，Spring Framework 6.x 需要 Java 17，不能在当前约束下直接升级。

2026-07-26 使用 OSV `querybatch` 对 Maven 完整依赖树复扫：

- 升级前：41 个受影响组件、208 个唯一公告。
- 升级后：11 个受影响组件、22 个唯一公告（其中 1 个组件/1 个公告仅在 test scope）。
- Tomcat 9.0.120、Jackson 2.18.9、MySQL Connector 8.4.0、Log4j 2.25.5、Druid 1.2.28、Fastjson `1.2.83_noneautotype` 等本轮升级目标未再命中 OSV。

扫描仍会按版本命中下列公告。它们没有被静默忽略，处置依据如下。

| 组件 / 公告 | 本项目处置 |
| --- | --- |
| MyBatis-Plus `GHSA-32qq-m9fh-f74w` | 公告只影响 `TenantPlugin` 的租户 ID 拼接；项目未配置或调用该插件。 |
| Spring Boot `GHSA-rc42-6c7j-7h5r` | 项目不依赖 Spring Security，也未使用 `EndpointRequest.to()`。 |
| Spring Boot `GHSA-wwpq-f5c3-7hvx` | 已显式设置 `server.servlet.session.persistent=false`，不持久化容器 Session。 |
| Spring Web/Context `GHSA-4gc7-5j7h-4qph`、`GHSA-4wp7-92pw-q264` | 项目未使用 `DataBinder.setDisallowedFields`，不存在公告所述大小写绕过配置。 |
| Spring Web `GHSA-4wrc-f8pq-fpqp` | 项目未对不可信输入调用 Java 原生反序列化或 Spring `SerializationUtils.deserialize`。 |
| Spring MVC `GHSA-4773-3jfm-qmx3` | 视图为固定 classpath Beetl 模板，未启用 Java 脚本模板引擎。 |
| Spring MVC `GHSA-6hcq-hmm3-jj3c` | 项目没有 SSE endpoint。 |
| Spring MVC `GHSA-6p4f-wcwh-5vvm` | 未从文件系统配置 Spring 静态资源 location；当前运行环境也不是 Windows。 |
| Spring MVC `GHSA-cx7f-g6mp-7hqm`、`GHSA-g5vr-rgqm-vf78` | 未使用 `RouterFunctions` / WebMvc.fn；运行容器为已升级的 Tomcat。 |
| Spring MVC `GHSA-r936-gwx5-v52f` | 使用 Tomcat 9.0.120 默认 URI 规范化保护，未关闭相关保护。 |
| Spring MVC `GHSA-w3c8-7r8f-9jp8` | 没有 `@RequestBody byte[]` 控制器参数。 |
| Spring MVC `GHSA-wg35-8jpf-2xv3` | 未启用 Spring resource chain 和 encoded-resource resolver 缓存组合。 |
| Spring Core `GHSA-jmp9-x22r-554x` | 未使用 Spring Security `@EnableMethodSecurity`；授权由 Shiro 和项目注解完成。 |
| Shiro `GHSA-c4qc-4q9p-m9q9` | 不存在账号也执行同成本 PBKDF2；单实例按 IP+账号限制 5 次失败/5 分钟。 |
| Shiro `GHSA-fcvm-3cpj-f9qx` | 登录前注销旧认证 Session，成功后创建新 Session；HTTP 验收确认 Shiro 认证 Session ID 已轮换。 |
| Shiro `GHSA-x96m-rh44-vgv8` | 未使用 `DefaultLdapRealm`，项目使用数据库 Realm。 |
| Shiro `GHSA-c244-p6m5-vqj6` | 静态资源本来就是公开内容，没有依赖 Shiro 对静态文件做细粒度保护。 |
| Shiro `GHSA-c6r4-qjmw-cvj2` | Session/rememberMe Cookie 已设置 HttpOnly、SameSite；HTTPS 部署必须配置 `GUNS_SECURE_COOKIE=true`。 |
| Beetl `GHSA-m69h-4frq-vwq7` | 公告详情针对 Beetl 3.15 的动态 `render`；项目使用固定 classpath 模板，不接受用户模板源码。通知富文本另经 OWASP 白名单净化。 |
| AssertJ `GHSA-rqfh-9r24-8c9r` | 仅 test scope，项目测试未调用不可信 XML 的 `isXmlEqualTo` / `xmlPrettyFormat`。 |

## 已实施的应用层保护

- 旧 MD5 口令成功登录后迁移到 PBKDF2-HMAC-SHA256（210,000 次迭代、随机盐）。
- JWT 和 rememberMe 固定公开密钥已移除；生产环境通过环境变量提供随机密钥。
- REST token 每次请求检查签名、过期时间以及用户当前是否仍存在且启用。
- CSRF token 覆盖后台写请求；旧的可变更 GET 路径也纳入校验。
- 通知富文本使用 OWASP HTML Sanitizer 白名单，编辑页输出使用 OWASP Encoder。
- 图片上传限制为 5 MB、限制像素、解码并重新编码；图片读取使用严格 UUID 文件名和规范化路径。
- Swagger 和 Druid 监控默认关闭；服务默认只监听 `127.0.0.1`。
- 浏览器响应包含 CSP、frame、MIME sniffing、referrer 和 permissions 安全头。

移除 JDK 8 约束后，应优先升级到仍在公开支持期的 Spring 和 Shiro 主版本，并重新执行完整依赖扫描与运行验收。
