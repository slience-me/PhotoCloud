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


class AdminMiddleware(MiddlewareMixin):
    def process_request(self, request):
        url_unallowed = ['/admin/index', '/admin/userimg', '/admin/imginfo', '/admin/removeimg', '/admin/userinfo', '/admin/removeuser']
        # print("拦截器")
        # print(request.path)
        if request.path in url_unallowed:
            if request.session.get('is_login_admin', None):
                pass
            else:
                return HttpResponseRedirect('/admin/login')


