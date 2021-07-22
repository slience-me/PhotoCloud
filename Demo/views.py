import os, uuid, datetime, random
import json
from io import BytesIO
from django.contrib import auth
from django.core.mail import send_mail
from django.http import HttpResponse
from PIL import Image, ImageFont
from PIL.ImageDraw import ImageDraw
from django.conf import settings
from django.urls import reverse
from .utils import generate_code
from django.http import JsonResponse
from django.shortcuts import render, redirect
from PhotoCloud.settings import BASE_DIR, BASE_URL
from .models import *
from django.views import View



# 主页
def Index(request):
    return render(request, "index.html")


# 上传接口
def Show(request):
    if request.method == "POST":
        ImageList = request.FILES.getlist('file')

        username = request.session['usernow']
        user = User.objects.filter(username=username)
        uid = user.values('uid')[0].get('uid')

        if not os.path.exists(os.path.join(BASE_DIR, 'media')):
            os.mkdir(os.path.join(BASE_DIR, 'media'))

        if not os.path.exists(os.path.join(BASE_DIR, 'media', 'image')):
            os.mkdir(os.path.join(BASE_DIR, 'media', 'image'))

        if not os.path.exists(os.path.join(BASE_DIR, 'media', 'image', str(uid))):
            os.mkdir(os.path.join(BASE_DIR, 'media', 'image', str(uid)))

        if ImageList:
            try:
                ImagePathList = []
                for Image in ImageList:
                    ImageType = str(Image).lower().split('.')[-1]
                    ImagePath = os.path.join(os.path.join(BASE_DIR, 'media', 'image', str(uid),
                                                          str(uuid.uuid4()) + '.' + ImageType))
                    ImageFileRelative = ImagePath.replace(str(BASE_DIR), '').replace('\\', '/')

                    ImageData(imagePath=ImageFileRelative, uploadTime=datetime.datetime.now(), userid_id=uid).save()
                    ImagePathList.append(ImageFileRelative)

                    # 存图片到本地
                    ImageOpen = open(ImagePath, 'wb')
                    for io in Image.chunks():
                        ImageOpen.write(io)
                    ImageOpen.close()
                print(ImagePathList)
            except IOError as e:
                # 路径异常
                print('图片写入失败：', e)
                res = {'msg': '上传失败', 'status': 1}
                return HttpResponse(json.dumps(res))
            # 返回接口信息
            res = {
                'code': 0,
                'message': "成功！",
                'data': {
                    'avator': "...",
                    'ImageFilePathList': ImagePathList
                }
            }
            return JsonResponse(res)

        else:
            res = {'msg': '请上传图片', 'status': 1}
            return JsonResponse(res)

    else:
        res = {'msg': '接口方法错误', 'status': 1}
        return JsonResponse(res)


# def IMAGE(request):
#     return render(request, "image/image.html")

# 查看已上传图片
def FindImage(request):
    try:
        print("# 查看已上传图片")
        username = request.session['usernow']
        user = User.objects.filter(username=username)
        uid = user.values('uid')[0].get('uid')
        print('uid',uid)

        objects = ImageData.objects.all().filter(userid_id=uid)
        print('objects',objects)
        print(bool(objects))
        print(bool(objects))
        print(bool(objects))
        print(bool(objects))

        if objects:
            print("-------------------1------------------------")
            datalist = []
            temp = str(objects[0].uploadTime).split(' ')[0]
            print('temp:' + temp)
            imagePathList = []
            print("-------------------2------------------------")
            for object in objects:
                uploadTime = str(object.uploadTime).split(' ')[0]
                print("-------------------3------------------------")

                if temp == uploadTime:
                    imagePathList.append(object.imagePath)
                    print("-------------------4------------------------")

                else:
                    data = {
                        'imagePath': imagePathList,
                        'uploadTime': temp
                    }
                    datalist.append(data)
                    temp = str(object.uploadTime).split(' ')[0]
                    print('修改temp:' + str(temp))

                    imagePathList = []
                    imagePathList.append(object.get('imagePath'))
                    print("-------------------5------------------------")
            print("-------------------5------------------------")

            data = {
                'imagePath': imagePathList,
                'uploadTime': uploadTime
            }
            datalist.append(data)
            base_url = BASE_URL
            print("-------------------6------------------------")
            return render(request, 'image/image.html', locals())
            
        else:
            return render(request, 'image/error.html')
    except:
        return render(request, 'image/error.html')


class Detail(View):
    def get(self, request):
        url = request.GET.get('url')
        print('url:' + url)
        base_url = BASE_URL
        print(base_url)
        if url:
            return render(request, 'image/detail.html', {'url': url, 'base_url': base_url})
        else:
            reverseUrl = reverse('MyApp:Image')
            return redirect(reverseUrl)


def UploadImage_single(request):
    return render(request, 'image/u_single.html')


def UploadImage_multi(request):
    return render(request, 'image/u_multi.html')


class Regist(View):
    """
        注册接口函数
    """

    def get(self, request):
        return render(request, 'user/register.html')

    def post(self, request):
        if self.request.is_ajax:
            user = request.POST.get('username', '')
            pwd = request.POST.get('password', '')
            cp = request.POST.get('cp', '')
            email = request.POST.get('email_data')
            if user != '' and pwd != '' and cp != '' and email != '':
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
                    code = str(uuid.uuid4())
                    d = {
                        'username': user, 'password': pwd,
                        'email': email, 'status': 'N', 'code': code,
                        'date_joined': datetime.datetime.now()
                    }
                    User.objects.create(**d)

                    address = email
                    title = '图云客户端，请激活您的账户！'
                    content = "<a href='https://tuyun.slienceme.xyz/active?code=" + code + "' > 点击激活【图云客户端】</a>"
                    send_mail(title, content, settings.EMAIL_HOST_USER, [address], html_message=content)
                    print("已经发送邮件")

                    print('注册成功，请激活您的账户!')
                    data = {
                        'code': 0,
                        'msg': '注册成功，请激活您的账户!'
                    }
                    return JsonResponse(data)
            else:
                print('请填写全部信息！')
                data = {
                    'code': 1,
                    'msg': '请填写全部信息！'
                }
                return JsonResponse(data)


class Login(View):
    """
        登录接口函数
    """

    def get(self, request):
        return render(request, 'user/login.html')

    def post(self, request):
        if self.request.is_ajax:
            user = request.POST.get('username', '')
            print(user)
            pwd = request.POST.get('password', '')
            print(pwd)
            if user != "" and pwd != '':
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
            else:
                print('请填写全部信息！')
                data = {
                    'code': 1,
                    'msg': '请填写全部信息！'
                }
                return JsonResponse(data)


def Logout(request):
    """
        退出登录功能，通过session实现
    """

    auth.logout(request)
    return redirect('MyApp:Login')


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
            'avatar': avatar
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
    imagefont = ImageFont.truetype(settings.FONT_PATH, 100)  # 字体 样式 大小
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
    return render(request, 'test/test.html')


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


def SendMail(request):
    if request.method == 'POST':
        address = request.POST.get('address')
        title = request.POST.get('title')
        content = request.POST.get('content')
        # 1.邮件标题 2.邮件内容 3.发件人帐号 4.收件人帐号(多个)
        result = send_mail(title, content, settings.EMAIL_HOST_USER, [address], html_message=content)
        if result:
            return HttpResponse('邮件发送成功！')
        else:
            return HttpResponse('邮件发送失败！')

    else:
        return redirect('test')


def ActiveUser(request):
    code = request.GET['code']
    user = User.objects.filter(code=code)
    if (user != None):
        user.update(status="Y")
        tip1 = '恭喜您激活账户成功！请进入登录界面'
        tip2 = '点此进入登录界面'
        return render(request, 'user/active.html', locals())
    else:
        tip1 = '激活账户失败！请联系管理员'
        tip2 = ''
        return render(request, 'user/active.html', locals())


def Pwd(request):
    if request.method == 'GET':
        return render(request, 'user/Pwd.html')

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


def Avatar(request):
    if request.method == 'GET':
        return render(request, 'user/avatar.html')
    if request.method == "POST":
        try:
            username = request.session['usernow']
            user = User.objects.filter(username=username)
            uid = user.values('uid')[0].get('uid')
            avatar_new = request.FILES.get('file')
            print(avatar_new)
            print(avatar_new)

            if not os.path.exists(os.path.join(BASE_DIR, 'media')):
                os.mkdir(os.path.join(BASE_DIR, 'media'))

            if not os.path.exists(os.path.join(BASE_DIR, 'media', 'user')):
                os.mkdir(os.path.join(BASE_DIR, 'media', 'user'))

            if not os.path.exists(os.path.join(BASE_DIR, 'media', 'user', 'avatar')):
                os.mkdir(os.path.join(BASE_DIR, 'media', 'user', 'avatar'))

            if not os.path.exists(os.path.join(BASE_DIR, 'media', 'user', 'avatar', str(uid))):
                os.mkdir(os.path.join(BASE_DIR, 'media', 'user', 'avatar', str(uid)))

            if avatar_new:
                try:
                    AvatarType = str(avatar_new).split('.')[-1]
                    AvatarPath = os.path.join(
                        os.path.join(BASE_DIR, 'media', 'user', 'avatar', str(uid),
                                     str(uuid.uuid4()) + '.' + AvatarType))
                    AvatarFileRelative = AvatarPath.replace(str(BASE_DIR), '').replace('\\', '/')
                    print(AvatarFileRelative)
                    flag = user.update(avatar=AvatarFileRelative)

                    # 存图片到本地
                    AvatarOpen = open(AvatarPath, 'wb')
                    for io in avatar_new.chunks():
                        AvatarOpen.write(io)
                    AvatarOpen.close()

                    print(avatar_new)

                except IOError as e:
                    # 路径异常
                    print('图片写入失败：', e)
                    data = {
                        'code': 1,
                        'message': '上传头像失败！',
                        'data': {
                            'ImageFilePathList': ''
                        }
                    }
                    return HttpResponse(json.dumps(data))
                # 返回接口信息
                if flag:
                    print('修改头像成功！')
                    data = {
                        'code': 0,
                        'message': '修改头像成功！',
                        'data': {
                            'ImageFilePath': AvatarPath
                        }
                    }
                    return JsonResponse(data)
                else:
                    print('修改头像失败！')
                    data = {
                        'code': 1,
                        'message': '修改头像失败！',
                        'data': {
                            'ImageFilePath': ''
                        }
                    }
                    return JsonResponse(data)

            else:
                print('请上传图片!')
                data = {
                    'code': 1,
                    'message': "请上传图片!",
                    'data': {
                        'ImageFilePath': ''
                    }
                }
                return JsonResponse(data)
        except:
            print("修改异常")
            data = {
                'code': 1,
                'message': "修改异常!",
                'data': {
                    'ImageFilePath': ''
                }
            }
            return JsonResponse(data)
