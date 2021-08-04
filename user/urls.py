# -*- codeing = utf-8 -*-
# @Time : 2021/7/30  21:27
# @Author : slience_me
# @File : urls.py
# @Software : PyCharm
# - * - coding: utf - 8 -*-
from django.urls import path
from user.views import *

app_name = 'user'

urlpatterns = [
  # 注册
  path('regist', Regist.as_view(), name='regist'),
  # 登录
  path('login', Login.as_view(), name='Login'),
  # 退出登录
  path('logout', Logout, name='Logout'),
  # 登录状态
  path('state', State, name='State'),
  # 验证码
  path('getcode', GetCode, name='Getcode'),
  # 激活账户
  path('confirm', Confirm, name='Confirm'),
  # 个人中心
  path('center', Center, name='Center'),
  # 个人中心
  path('history', History, name='History'),
  # 数据
  path('data', get_img_data, name='data'),
  # 公共数据
  # 删除
  path('remove', Remove, name='file_remove'),
  # 修改密码
  path('pwd', Pwd, name='Pwd'),
]