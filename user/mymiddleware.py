# -*- codeing = utf-8 -*-
# @Time : {}  {18:19}
# @Author : slience_me
# @File : mymiddleware.py.py
# @Software : PhotoCloud
# - * - coding: utf - 8 -*-
from django.shortcuts import HttpResponseRedirect

try:
    from django.utils.deprecation import MiddlewareMixin
except ImportError:
    MiddlewareMixin = object


class SimpleMiddleware(MiddlewareMixin):
    def process_request(self, request):
        url_allowed = ['/index', '/common', '/show', '/state', '/getcode', '/confirm', '/login', '/regist']
        url_unallowed = ['/center', '/data', '/history', '/remove', '/pwd', '/logout']
        # print("拦截器")
        # print(request.path)
        if request.path in url_unallowed:
            if request.session.get('usernow', None):
                pass
            else:
                return HttpResponseRedirect('/login')
