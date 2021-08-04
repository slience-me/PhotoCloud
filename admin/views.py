import os

from django.contrib import auth
from django.core.paginator import Paginator
from django.http import JsonResponse
from django.shortcuts import render, get_object_or_404, redirect
from django.views import View

from PhotoCloud.settings import BASE_DIR
from admin.models import Admin
from container.models import File
from user.models import User


def get_img_data(request):
    uid = request.GET.get('uid', '')
    page = request.GET.get('page', 1)
    limit = request.GET.get('limit', 10)
    if uid:
        try:
            queryset = list(File.objects.filter(user_id=uid, removed=0).values().order_by('-upload_date'))
            paginator = Paginator(queryset, limit)
            data_page = list(paginator.page(page))
            data = {
                "code": 0,
                "msg": "",
                "count": paginator.count,
                "data": data_page
            }
            print('获取图片数据成功', data)
        except:
            data = {
                "code": 1,
                "msg": "查询图片数据失败！",
                "count": 0,
                "data": []
            }
            print('获取图片数据失败')
        return JsonResponse(data, safe=False)
    else:
        data = {
            "code": 1,
            "msg": "'获取图片数据失败，id参数为空",
            "count": 0,
            "data": []
        }
        return JsonResponse(data, safe=False)


def remove_img(request):
    uid = request.GET.get('uid', '')
    pk = request.GET.get('pk', '')
    if uid and pk:
        print('uid', uid)
        print('pk', pk)
        """删除已上传文件"""
        # 修改状态
        file = get_object_or_404(File, pk=pk)
        file.removed = 1
        file.save()
        # 移除文件
        filepath = os.path.join(BASE_DIR, 'media', 'image', str(uid), file.md5_name)
        try:
            os.remove(filepath)
            print('删除成功！', pk)
            data = {
                "code": 0,
                "msg": "删除成功!"
            }
        except:
            print('删除失败！', pk)
            data = {
                "code": 1,
                "msg": "删除失败!"
            }
        # 返回
        return JsonResponse(data)
    else:
        data = {
            "code": 1,
            "msg": "删除失败!uid或者pk参数为空"
        }
        # 返回
        return JsonResponse(data)


def index(request):
    return render(request, 'admin/user.html')


def user_data(request):
    page = request.GET.get('page', 1)
    limit = request.GET.get('limit', 10)
    try:
        queryset = list(User.objects.all().values())
        paginator = Paginator(queryset, limit)
        data_page = list(paginator.page(page))
        data = {
            "code": 0,
            "msg": "查询用户成功",
            "count": paginator.count,
            "data": data_page
        }
        print('admin查询用户成功', data)
    except:
        data = {
            "code": 1,
            "msg": "查询用户失败！",
            "count": 0,
            "data": []
        }
        print('admin查询用户失败')
    return JsonResponse(data, safe=False)


def remove_user(request):
    uid = request.GET.get('uid')
    print('uid', uid)
    if uid == '1':
        data = {
            "code": 1,
            "msg": "删除失败！游客禁止删除"
        }
        return JsonResponse(data)

    try:
        user = User.objects.filter(uid=uid)
        if user:
            user.delete()
            print('删除用户成功！', uid)
            data = {
                "code": 0,
                "msg": "删除成功!"
            }
        else:
            print('删除失败！用户不存在', uid)
            data = {
                "code": 1,
                "msg": "删除失败！用户不存在"
            }
    except:
        print('删除失败！', uid)
        data = {
            "code": 1,
            "msg": "删除失败!"
        }
    # 返回
    return JsonResponse(data)


def user_img(request):
    uid = request.GET.get('uid', '')
    return render(request, 'admin/user_img.html', {'uid': uid})


class Login(View):
    """
        登录接口函数
    """

    def get(self, request):
        return render(request, "admin/login.html", locals())

    def post(self, request):
        if self.request.is_ajax:
            user = request.POST.get('username', '')
            print(user)
            pwd = request.POST.get('password', '')
            print(pwd)
            vercode = str(request.POST.get('vercode', '')).lower()
            print(vercode)
            realcode = str(request.session.get("verify_code")).lower()
            if vercode == realcode:
                if user != "" and pwd != '':
                    try:
                        admin = Admin.objects.filter(username=user, password=pwd)
                        print(admin)
                        if admin:
                            request.session['is_login_admin'] = True
                            request.session['admin_now'] = admin[0].username
                            request.session.set_expiry(3 * 24 * 60 * 60)
                            print('登录成功')
                            data = {
                                'code': 0,
                                'msg': '登录成功'
                            }
                            return JsonResponse(data)

                        else:
                            print('用户名或者密码错误!')
                            data = {
                                'code': 1,
                                'msg': '用户名或者密码错误!!',
                            }
                            return JsonResponse(data)

                    except:
                        print('登录异常，请稍后再试!')
                        data = {
                            'code': 1,
                            'msg': '登录异常，请稍后再试!',
                        }
                        return JsonResponse(data)

                else:
                    print('请填写全部信息！')
                    data = {
                        'code': 1,
                        'msg': '请填写全部信息！'
                    }
                    return JsonResponse(data)
            else:
                print('验证码错误！')
                data = {
                    'code': 1,
                    'msg': '验证码错误！'
                }
                return JsonResponse(data)

def Logout(request):
    """
        退出登录功能，通过session实现
    """
    auth.logout(request)
    return redirect('tuyun:index')