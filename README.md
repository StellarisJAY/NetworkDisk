# 基于HDFS的网盘系统
## 项目简介
基于Hadoop文件系统（HDFS）开发的网盘系统。该项目计划为个人和小型团队提供一套在服务器上搭建内部网盘的解决方案以及内部网盘管理平台。

## 技术简介
后端：springboot

前端：vue + elementUI

数据库：mysql

中间件：redis缓存、rabbitmq（用于服务间数据交互以及流量控制）

文件系统：HDFS（该方案只为拥有HDFS的集群提供）、linux或winserver文件系统


## 已完成功能
**文件上传/下载/删除：** 下载文件到本地、上传文件到服务器、删除文件

**文件夹功能：** 创建和删除文件夹，使网盘具有一个虚拟、完整、用户可操作的文件系统

**批量下载：** 批量下载选择的所有文件，并自动打包成压缩包格式

**批量删除：** 批量删除文件

**分类分页显示文件（仅音乐和文档）：** 筛选出不同类型的文件分页显示

**图片内容识别：** 使用百度AIP识别上传的图片文件，将图片关键字记录在数据库

**音乐播放：** 预览播放音频文件

## 未完成功能
**文件移动：**

**文件重命名：**

**图片分类展示：**

**图片和视频文件预览：**

**文档在线预览：**

**文件分享**

**回收站**

**管理平台**
