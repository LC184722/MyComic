# MyComic

#### 介绍
- **重要通知：v1.7.4之后的版本将不再更新代码，软件正常更新维护。**

- MyComic安卓在线漫画阅读器，包含多个源网站资源，支持搜索收藏等功能，用过的都说好！

- 前往下载最新Apk【[软件发布页面](https://gitee.com/luqichuang/MyComic/releases "Gitee地址")】(
  下载最新的.apk后缀的文件，最终下载文件应为.apk后缀，如果使用手机Chrome浏览器或其他方式下载得到.apk.zip文件时请不要解压，手动删除.zip后缀即可，也可以换一个浏览器下载)

- 欢迎加Q群 **741032316** ，群内不时更新最新Apk，更新计划等内容。关于软件的疑问或者建议都可以在群内畅聊！

<div align="center">
<img src="https://gitee.com/luqichuang/MyComic/raw/release/app/src/main/assets/qrcode.jpg" height="300" alt="Q群二维码"/>
</div>

- 前排提示：收费漫画源（如腾讯、哔哩哔哩）仅获取其中的免费部分，因此会有缺页等情况，可换源观看对应漫画。

#### 软件架构

1.  [Tencent / QMUI_Android](https://github.com/Tencent/QMUI_Android "Tencent / QMUI_Android") : 腾讯UI框架。
2.  [Theone / TheBaseAndroid](https://gitee.com/theoneee/TheBase "Theone / TheBaseAndroid") : 主要以QMUI封装的一个快速开发框架。

#### 软件预览

| <img src="https://gitee.com/luqichuang/MyComic/raw/release/app/src/main/assets/1.jpg" width="300" alt="画架页面"/> | <img src="https://gitee.com/luqichuang/MyComic/raw/release/app/src/main/assets/2.jpg" width="300" alt="详情页面"/> |  <img src="https://gitee.com/luqichuang/MyComic/raw/release/app/src/main/assets/3.jpg" width="300" alt="阅读页面"/>  |
|:--------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------:|
| <img src="https://gitee.com/luqichuang/MyComic/raw/release/app/src/main/assets/4.jpg" width="300" alt="搜索页面"/> | <img src="https://gitee.com/luqichuang/MyComic/raw/release/app/src/main/assets/5.jpg" width="300" alt="个人中心"/> | <img src="https://gitee.com/luqichuang/MyComic/raw/release/app/src/main/assets/6.jpg" width="300" alt="小说阅读页面"/> |

#### 使用说明

1.  画架页可以检查漫画更新、长按漫画可以执行切换漫画源、查看详情、删除等操作。
2.  详情页可以切换漫画源、阅读最新章节、继续阅读等。
3.  阅读页面长按加载失败图片可以重新加载，长按已显示图片可以单独缩放。
4.  个人中心可以检查更新，获得最新软件。
5.  前往下载[最新APK](https://gitee.com/luqichuang/MyComic/releases "Gitee地址")：[Gitee](https://gitee.com/luqichuang/MyComic/releases "Gitee地址")、[GitHub](https://github.com/LC184722/MyComic/releases "GitHub地址")。

#### 添加数据源方法

1.  数据源接口文件：common/src/main/java/top/luqichuang/common/model/Source.java，根据不同数据源分为BaseComicSource、BaseNovelSource、BaseVideoSource三个主要父类。
2.  以添加漫画源为例：
- 在common/src/main/java/top/luqichuang/mycomic/source文件夹下创建文件并继承BaseComicSource类。
- 实现或重写方法（可参考common/src/main/java/top/luqichuang/mycomic/source/BaoZi.java文件）。
- 在common/src/main/java/top/luqichuang/common/en/CSourceEnum.java文件中添加对应枚举。

#### 测试数据源方法

1. 测试文件父类：common/src/main/java/top/luqichuang/common/tst/BaseSourceTest.java。
2. 以测试漫画源为例：
- 测试文件：common/src/test/java/top/luqichuang/common/ComicTest.java。
- 解开对应方法注释即可进行测试。

#### 数据源

- ##### 漫画

```
1. 米推漫画
2. 漫画粉[失效]
3. 扑飞漫画[失效]
4. 腾讯动漫
5. 哔哩哔哩漫画
6. OH漫画[失效]
7. 漫画台
8. 118漫画[失效]
9. 独漫画[失效]
10. BL漫画[失效]
11. 爱优漫
12. 1234漫画[失效]
13. 118漫画2[失效]
14. 奇妙漫画[失效]
15. 大树漫画[失效]
16. 思思漫画[失效]
17. 包子漫画
18. 七夕漫画
19. 酷漫屋
20. 好漫6[失效]
21. 都市漫画
22. 前未漫画[失效]
23. 品悦漫画
24. 来漫画
25. 6漫画
26. 160漫画
```

- ##### 小说

```
1. 新笔趣阁
2. 全书网[失效]
3. 全小说[失效]
4. 爱阅小说
5. 炫书网
6. 17K小说
7. E小说
8. 墨缘文学[失效]
9. Mi看书[失效]
10. 新笔趣阁2
11. 书本网[失效]
12. 塔读文学
```

- ##### 番剧

```
1. 樱花动漫[失效]
2. 米粒米粒[失效]
3. 风车动漫[失效]
4. 樱花动漫2[失效]
5. 哔哩哔哩
6. 风车动漫2
7. 爱云影视
```

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request

#### 其他

1.  本项目仅供学习使用
