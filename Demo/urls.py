# -*- codeing = utf-8 -*-
# @Time : 2021/4/15  16:22
# @Author : 宋保贤
# @File : urls.py
# @Software : PyCharm
from django.urls import path
from Demo.views import *

app_name = 'MyApp'

urlpatterns = [
    # 主页
    path('index.html', Index, name='Index'),
    # 上传
    path('show', Show, name='Upload'),
    # 查看自己上传的图片
    path('find', FindImage, name='Image'),
    # 图片细节
    path('detail', Detail.as_view(), name='Detail'),
    # 测试
    path('test', Test, name='Test'),
    # 单张上传
    path('u_single', UploadImage_single, name='Single'),
    # 多张上传
    path('u_multi', UploadImage_multi, name='Multi'),
    # 注册
    path('regist', Regist.as_view(), name='regist'),
    # 登录
    path('login', Login.as_view(), name='Login'),
    # 登录
    path('', Login.as_view(), name='Login'),
    # 退出登录
    path('logout', Logout, name='Logout'),
    # 登录状态
    path('state', State, name='State'),
    # 验证码
    path('getcode', GetCode, name='Getcode'),
    # 发邮件
    path('sendmail', SendMail, name='Sendmail'),
    # 激活账户
    path('active', ActiveUser, name='Active'),
    # 个人中心
    path('center', Center, name='Center'),
    # 修改密码
    path('pwd', Pwd, name='Pwd'),
    # 修改头像
    path('avatar', Avatar, name='Avatar'),
]
