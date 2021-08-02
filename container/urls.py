# -*- codeing = utf-8 -*-
# @Time : {}  {17:32}
# @Author : slience_me
# @File : urls.py
# @Software : PhotoCloud
# - * - coding: utf - 8 -*-
from django.urls import path


from . import views
from .views import *

app_name = 'tuyun'

urlpatterns = [
    path('', Index, name='index'),
    path('index', Index, name='index'),
    path('common', Common, name='common'),
    path('show', Show, name='Upload'),
]

