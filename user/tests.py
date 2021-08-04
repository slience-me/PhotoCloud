import datetime
import os

from django.core.mail import EmailMultiAlternatives
from django.template import loader
from django.test import TestCase

# from PhotoCloud import settings
#
#
# def getHtmlByFile():
#     now = datetime.datetime.now()
#     # 在Django的配置文件中配置了基本变量，这里用的BASE_DIR就是该项目所在的目录(settings.py文件里)
#     file = os.path.join(settings.BASE_DIR, 'templates', 'user','email.html')
#     with open(file, 'r',encoding='UTF-8') as f:
#         html = f.read()
#     # 从这一步其实明显可以看出，html中的变量获取，采用的是固定格式进行识别的，正则去匹配两个大括号，匹配到就获取里面的字符串，在去掉前后空格，取出来作为变量名。由于是重复造轮子的事，暂时不讨论。
#     # 如果真是采用这种方式进行获取变量的话，一定需要注意，替换的是字符串，里面一个空格的差别都是不同字符串
#     # html = html.replace('{{ now }}', now.strftime('%Y-%M-%D'))
#     # print(type(html))
#     html = html.format(000,111,222,333,444,555)
#     print(html)
#
# getHtmlByFile()

# def test01():
#     time = datetime.datetime.today()
#     string = str(time).split('.')[0]
#     print(time)
#     print(string)
#
from PhotoCloud import settings
from PhotoCloud.settings import BASE_URL


def send_email(email):
    myblog = "https://www.slienceme.xyz/"
    baseUrl = "https://tuyun.slienceme.xyz/"
    subject = '来自tuyun.slienceme.xyz的推送邮件'
    text_content = '''感谢注册tuyun.slienceme.xyz，这里是图云客户端，专注于服务大家和技术分享！\
                    如果你看到这条消息，说明你的邮箱服务器不提供HTML链接功能，请联系管理员！'''
    file = os.path.join(settings.BASE_DIR, 'templates', 'user', 'email1.html')
    with open(file, 'r', encoding='UTF-8') as f:
        html = f.read()

    html_content = html.format(baseUrl, 'smx', 'smx', 'smx',
                               str(datetime.datetime.now()).split('.')[0], myblog)

    msg = EmailMultiAlternatives(subject, text_content, settings.EMAIL_HOST_USER, [email])
    msg.attach_alternative(html_content, "text/html")
    msg.send()

if __name__ == '__main__':

    email = '1093660250@qq.com'
    send_email(email)


