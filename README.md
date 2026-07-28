# WebStack-Guns

一个开源的网址导航网站项目，具备完整的前后台，您可以拿来制作自己的网址导航。

![首页](screen/1.png)



## 运行

运行环境：

- JDK 17
- Maven 3.9+
- MySQL 8.x

先确认实际使用的是 JDK 17：

```shell
java -version
mvn -version
```

构建已配置 Maven Enforcer，使用非 JDK 17 会直接失败，避免构建产物与生产运行时不一致。

在本机 MySQL 中创建数据库并导入初始化数据：

```shell
mysql -h127.0.0.1 -P3306 -u你的账号 -p \
  -e "CREATE DATABASE IF NOT EXISTS guns CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
mysql -h127.0.0.1 -P3306 -u你的账号 -p guns < sql/guns.sql
mysql -h127.0.0.1 -P3306 -u你的账号 -p guns < sql/ai-navigation.sql
```

AI 导航的 96 个站点图标随项目保存在
`src/main/webapp/static/tmp/ai-site-001.png` 至 `ai-site-096.png`，首页加载时不依赖外部图标服务。

如果是从旧版数据库升级，只需执行一次：

```shell
# 先完成并校验数据库备份，再执行升级脚本
mysql -h127.0.0.1 -P3306 -u你的账号 -p guns < sql/security-upgrade.sql
```

数据库连接信息没有默认值，必须通过环境变量显式传入。首次使用初始化 SQL
时还必须提供一个 12～128 位的新管理员密码；应用会在启动阶段替换历史默认摘要，
缺失或仍使用历史默认值时会拒绝启动：

```shell
export DB_URL='jdbc:mysql://127.0.0.1:3306/guns?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&serverTimezone=Asia/Shanghai'
export DB_USERNAME='你的账号'
read -s DB_PASSWORD
export DB_PASSWORD
read -s GUNS_BOOTSTRAP_ADMIN_PASSWORD
export GUNS_BOOTSTRAP_ADMIN_PASSWORD

# 仅限本机 HTTP 开发环境；HTTPS 部署必须设为 true
export GUNS_SECURE_COOKIE=false

mvn clean verify
java -jar target/Webstack-Guns-1.0.jar
```

首次轮换成功后，后续启动不再需要
`GUNS_BOOTSTRAP_ADMIN_PASSWORD`，应从部署环境中移除该变量。

启动完成后访问：<http://127.0.0.1:8000>

本地未配置 JWT 密钥时，应用会在每次启动时生成临时随机密钥；正式部署应显式配置，否则重启后旧 JWT 会失效：

```shell
export GUNS_JWT_SECRET="$(openssl rand -base64 64)"
```

其他可选配置：

- `SERVER_ADDRESS`：默认 `127.0.0.1`；需要对外监听时显式设为 `0.0.0.0`
- `SERVER_PORT`：默认 `8000`
- `GUNS_FILE_UPLOAD_PATH`：默认使用系统临时目录下的独立上传目录
- `GUNS_SECURE_COOKIE`：必须显式设置；本机 HTTP 开发设为 `false`，HTTPS 部署设为 `true`
- `BEETL_RESOURCE_AUTO_CHECK`：默认 `false`；仅本地模板开发需要时开启
- `GUNS_SWAGGER_OPEN`、`GUNS_DRUID_MONITOR_OPEN`：默认关闭



## 使用

后台地址：http://domain/admin

初始化管理员账号：`admin`

在线demo: http://139.196.175.187:8000

![主页](screen/2.png)

![分类](screen/3.png)

![网站](screen/4.png)





## 感谢

前端设计：[**WebStackPage**](https://github.com/WebStackPage/WebStackPage.github.io)

后台框架：[**Guns**](https://github.com/stylefeng/Guns)

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**SpringBoot**


## 声明

项目采用 JDK 17、Spring Boot 2.7.x / Spring Framework 5.3.x 和 Shiro 2.2.x。由于 Spring Boot 2.x 仍使用 `javax.servlet`，尚不能直接使用仅支持 Jakarta 的 Shiro 3.x；项目已彻底关闭 Shiro rememberMe，并补充登录限速、会话轮换、安全 Cookie、CSRF、富文本净化、上传校验和安全响应头。外网部署仍应放在 HTTPS 反向代理之后，并在网关层增加统一限速和访问日志告警。

## License

MIT
