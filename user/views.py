import datetime
import hashlib
import os
import random
from io import BytesIO

from PIL import Image, ImageFont
from PIL.ImageDraw import ImageDraw
from django.conf import settings
from django.contrib import auth
from django.contrib.auth.decorators import login_required
from django.core.mail import EmailMultiAlternatives
from django.core.paginator import Paginator
from django.http import HttpResponse
from django.http import JsonResponse
from django.shortcuts import render, redirect, get_object_or_404
from django.views import View

from PhotoCloud import settings
from PhotoCloud.settings import BASE_URL, BASE_DIR
from container.models import File
from .models import *
from .utils import generate_code


class Regist(View):
    """
        注册接口函数
    """

    def get(self, request):

        return render(request, 'user/regist.html', locals())

    def post(self, request):
        if self.request.is_ajax:
            user = request.POST.get('username', '')
            pwd = request.POST.get('password', '')
            cp = request.POST.get('repass', '')
            email = request.POST.get('uemail', '')
            vercode = str(request.POST.get('vercode', '')).lower()
            print(vercode)
            realcode = str(request.session.get("verify_code")).lower()
            if vercode == realcode:
                if user != '' and pwd != '' and cp != '' and email != '':
                    try:
                        if User.objects.filter(username=user):
                            print('用户已存在')
                            data = {
                                'code': 1,
                                'msg': '用户已存在'
                            }
                            return JsonResponse(data)
                        elif cp != pwd:
                            print('两次密码输入不一致')
                            data = {
                                'code': 1,
                                'msg': '两次密码输入不一致'
                            }
                            return JsonResponse(data)
                        else:
                            new_user = None
                            try:
                                d = {
                                    'username': user, 'password': pwd,
                                    'email': email, 'status': 'N',
                                    'date_joined': datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                                }
                                new_user = User.objects.create(**d)
                                code = make_confirm_string(new_user)
                                send_email(new_user, email, code)
                                print("已经发送邮件")
                                print('注册成功，请激活您的账户!')
                                data = {
                                    'code': 0,
                                    'msg': '注册成功，请前往邮箱激活您的账户!'
                                }
                                return JsonResponse(data)
                            except:
                                obj = User.objects.get(new_user.username)
                                obj.delete()
                                print('事务回滚删除用户', new_user)
                                print('注册异常，请稍后再试!')
                                data = {
                                    'code': 1,
                                    'msg': '注册异常，请稍后再试!',
                                }
                                return JsonResponse(data)

                    except:
                        print('注册异常，请稍后再试!')
                        data = {
                            'code': 1,
                            'msg': '注册异常，请稍后再试!',
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


class Login(View):
    """
        登录接口函数
    """

    def get(self, request):
        return render(request, "user/login.html", locals())

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
                        user = User.objects.filter(username=user, password=pwd)
                        print(user)
                        status = user.values('status')
                        if status:
                            status = status[0].get('status')
                        print(status)
                        if user:
                            if status == 'N':
                                print('您好，您的账户未激活，请激活后再登陆！')
                                data = {
                                    'code': 1,
                                    'msg': '您好，您的账户未激活，请激活后再登陆！'
                                }
                                return JsonResponse(data)
                            else:
                                request.session['is_login'] = True
                                request.session['usernow'] = user[0].username
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


def State(request):
    """
        获取用户登录状态函数，通过session实现
    """
    if request.is_ajax:
        try:
            username = request.session['usernow']
            user = User.objects.filter(username=username)
            avatar = user.values('avatar')
            if avatar:
                avatar = avatar[0].get('avatar')
            print(username)
        except:
            username = ''
            avatar = ''
        data = {
            'user': username,
        }
        return JsonResponse(data)


def GetCode(request):
    """
        获取验证码函数,通过该函数取得变化的验证码
    """

    mode = "RGB"  # 颜色模式
    size = (200, 100)  # 画布大小
    red = random.randrange(255)
    green = random.randrange(255)
    blue = random.randrange(255)
    color_bg = (red, green, blue)  # 背景色
    image = Image.new(mode=mode, size=size, color=color_bg)  # 画布
    imagedraw = ImageDraw(image, mode=mode)  # 画笔
    verify_code = generate_code()  # 内容
    imagefont = ImageFont.truetype(settings.FONT_PATH, 80)  # 字体 样式 大小
    request.session['verify_code'] = verify_code
    print(verify_code)
    # 字体 颜色
    for i in range(len(verify_code)):
        fill = (random.randrange(255), random.randrange(255), random.randrange(255))
        imagedraw.text(xy=(50 * i, 0), text=verify_code[i], fill=fill, font=imagefont)
    # 噪点
    for i in range(1000):
        fill = (random.randrange(255), random.randrange(255), random.randrange(255))
        xy = (random.randrange(201), random.randrange(100))
        imagedraw.point(xy=xy, fill=fill)
    fp = BytesIO()
    image.save(fp, "png")
    return HttpResponse(fp.getvalue(), content_type="image/png")


def Test(request):
    return render(request, 'user/history.html')


def pagination_data(paginator, page, is_paginated):
    """分页详细数据"""
    if not is_paginated:
        return {}

    left, right = [], []
    left_has_more, right_has_more = False, False
    first, last = False, False

    page_number = page.number
    total_pages = paginator.num_pages
    page_range = paginator.page_range

    if page_number != total_pages:
        right = page_range[page_number:page_number + 2]
        if right[-1] < total_pages - 1:
            right_has_more = True
        if right[-1] < total_pages:
            last = True
    if page_number != 1:
        left = page_range[(page_number - 3) if (page_number - 3) > 0 else 0:page_number - 1]
        if left[0] > 2:
            left_has_more = True
        if left[0] > 1:
            first = True

    data = {
        'left': left,
        'right': right,
        'left_has_more': left_has_more,
        'right_has_more': right_has_more,
        'first': first,
        'last': last,
    }

    return data


def get_img_data(request):
    page = request.GET.get('page', 1)
    limit = request.GET.get('limit', 10)
    user = request.session['usernow']
    uid = User.objects.filter(username=user).values()[0].get('uid')
    queryset1 = File.objects.filter(user_id=uid, removed=0).values().order_by('-upload_date')
    json_list = list(queryset1)
    paginator = Paginator(json_list, limit)
    data_page = paginator.page(page)
    data_page = list(data_page)
    data = {
        "code": 0
        , "msg": ""
        , "count": paginator.count
        , "data": data_page
    }
    return JsonResponse(data, safe=False)


def Remove(request):
    pk = request.GET.get('pk')
    print('pk', pk)
    """删除已上传文件"""
    user = request.session['usernow']
    uid = User.objects.filter(username=user).values()[0].get('uid')

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
    except Exception:
        print('删除失败！', pk)
        data = {
            "code": 0,
            "msg": "删除失败!"
        }
    # 返回
    return JsonResponse(data)


def Center(request):
    if request.method == 'GET':
        username = request.session['usernow']
        user = User.objects.filter(username=username)
        name = user.values('name')[0].get('name')
        sex = user.values('sex')[0].get('sex')
        birthday = user.values('birthday')[0].get('birthday')
        telephone = user.values('telephone')[0].get('telephone')
        email = user.values('email')[0].get('email')
        return render(request, 'user/center.html', locals())

    if request.is_ajax:
        try:
            username = request.session['usernow']
            user = User.objects.filter(username=username)
            name = request.POST['realname']
            sex = request.POST['sex']
            birthday = request.POST['birthday']
            telephone = request.POST['telephone']
            email = request.POST['email']

            u = {
                'name': name, 'sex': sex,
                'birthday': birthday,
                'telephone': telephone, 'email': email
            }
            print(u)
            flag = user.update(**u)
            if flag:
                print('修改信息成功！')
                data = {
                    'code': 0,
                    'msg': '修改信息成功！'
                }
                return JsonResponse(data)
            else:
                print('修改信息失败！')
                data = {
                    'code': 1,
                    'msg': '修改信息失败！'
                }
                return JsonResponse(data)
        except:
            print("修改异常")
            data = {
                'code': 1,
                'msg': '修改异常！'
            }
            return JsonResponse(data)


def History(request):
    return render(request, 'user/history.html')


def Pwd(request):
    if request.is_ajax:
        try:
            username = request.session['usernow']
            user = User.objects.filter(username=username)
            password_ago = user.values('password')[0].get('password')
            password_new = request.POST['password']
            password_again = request.POST['password_a']
            if password_new == password_again:
                if password_ago == password_new:
                    data = {
                        'code': 1,
                        'msg': '与过去的密码相同，无需更改！'
                    }
                    return JsonResponse(data)
                else:
                    print(password_new)
                    flag = user.update(password=password_new)
                    if flag:
                        data = {
                            'code': 0,
                            'msg': '修改密码成功！'
                        }
                        return JsonResponse(data)
                    else:
                        data = {
                            'code': 1,
                            'msg': '修改密码失败！'
                        }
                        return JsonResponse(data)

            else:
                data = {
                    'code': 1,
                    'msg': '两次密码不一致！'
                }
                return JsonResponse(data)
        except:
            print("修改异常")
            data = {
                'code': 1,
                'msg': '修改异常！'
            }
            return JsonResponse(data)


def hash_code(s, salt='tuyun'):
    h = hashlib.sha256()
    s += salt
    h.update(s.encode())
    return h.hexdigest()


def make_confirm_string(user):
    now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    code = hash_code(user.username, now)
    ConfirmString.objects.create(code=code, user=user)
    return code


def send_email(user, email, code):
    myblog = "https://www.slienceme.xyz/"
    subject = '来自tuyun.slienceme.xyz的注册确认邮件'
    text_content = '''感谢注册tuyun.slienceme.xyz，这里是图云客户端，专注于服务大家和技术分享！\
                    如果你看到这条消息，说明你的邮箱服务器不提供HTML链接功能，请联系管理员！'''
    file = os.path.join(settings.BASE_DIR, 'templates', 'user', 'email.html')
    with open(file, 'r', encoding='UTF-8') as f:
        html = f.read()
    html_content = html.format(user.username, BASE_URL, code, settings.CONFIRM_DAYS,
                               str(datetime.datetime.now()).split('.')[0], myblog)

    msg = EmailMultiAlternatives(subject, text_content, settings.EMAIL_HOST_USER, [email])
    msg.attach_alternative(html_content, "text/html")
    msg.send()


def Confirm(request):
    code = request.GET.get('code', None)
    print(code)
    message = ''
    try:
        confirm = ConfirmString.objects.get(code=code)
        print(confirm)
    except:
        message = '激活失败请重试！'
        flag = False
        return render(request, 'user/confirm.html', locals())

    c_time = confirm.c_time
    now = datetime.datetime.now()

    if now > c_time + datetime.timedelta(settings.CONFIRM_DAYS):
        confirm.user.delete()
        message = '您的邮件已经过期！请重新注册!'
        flag = False
        return render(request, 'user/confirm.html', locals())
    else:
        confirm.user.status = 'Y'
        confirm.user.save()
        confirm.delete()
        BASE_URL = settings.BASE_URL
        message = '激活成功，请登录！'
        flag = True
        return render(request, 'user/confirm.html', locals())
