import hashlib
import json
import os
from datetime import datetime

from django.core.paginator import Paginator
from django.http import HttpResponse, JsonResponse
from django.shortcuts import render
from PhotoCloud.settings import BASE_DIR, BASE_URL
from container.models import File
from user.models import User


def Index(request):
    myblog = "https://www.slienceme.xyz/"
    return render(request, "index.html", locals())


def _generate_md5_name(filename):
    """生成md5加密后的名称，加入当前时间字符串，避免文件名重复带来的问题"""
    md5_str = datetime.strftime(datetime.now(), "%Y-%m-%dT%H:%M:%S") + filename
    return hashlib.md5(md5_str.encode('utf-8')).hexdigest() + '.' + filename.split('.')[-1]


# 上传接口
def Show(request):
    if request.method == "POST":
        ImageList = request.FILES.getlist('file')
        if request.session.get('is_login', None):
            user = request.session['usernow']
            print('user', user)
            uid = User.objects.filter(username=user).values()[0].get('uid')
            print('uid', uid)
            flag = str("会员:" + str(user))
            print('flag', flag)
        else:
            uid = 0
            flag = str("游客用户")
            print('flag', flag)

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
                    md5_name = _generate_md5_name(Image.name)
                    ImagePath = os.path.join(os.path.join(BASE_DIR, 'media', 'image', str(uid), str(md5_name)))
                    ImageFileRelative = ImagePath.replace(str(BASE_DIR), '').replace('\\', '/')
                    url = BASE_URL + ImageFileRelative
                    File(filename=Image.name, user_id=uid, url=url, md5_name=md5_name).save()
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
                res = {'msg': '上传失败', 'code': 1}
                return HttpResponse(json.dumps(res))
            # 返回接口信息
            res = {
                'code': 0,
                'message': "成功！",
                'data': {
                    'avator': flag,
                    'url': ImagePathList
                }
            }
            return JsonResponse(res)

        else:
            res = {'msg': '请上传图片', 'code': 1}
            return JsonResponse(res)

    else:
        res = {'msg': '接口方法错误', 'code': 1}
        return JsonResponse(res)

def get_common_imglist():
    img_list = []
    for root, dirs, files in os.walk(os.path.join(BASE_DIR, 'media', 'image', '0')):
        for file in files:
            fileDir = str(BASE_URL) + '/media/image/0/' + file
            img_list.append(fileDir)
    return img_list


def get_img_list(request):
    page = request.GET.get('page', 1)
    limit = request.GET.get('limit', 15)
    img_list = get_common_imglist()
    paginator = Paginator(img_list, limit)
    data_page = list(paginator.page(page))
    data = {
        "code": 0,
        "msg": "",
        "count": paginator.count,
        "data": data_page
    }
    return JsonResponse(data)


def Common(request):
    return render(request, 'tuyun/common.html')


def page_not_found(request, exception, template_name='error/404.html'):
    return render(request, template_name)


def page_permission_denied(request, exception, template_name='error/403.html'):
    return render(request, template_name)


def page_inter_error(request, template_name='error/500.html'):
    return render(request, template_name)
