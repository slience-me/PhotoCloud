import hashlib
from datetime import datetime

from django.db import models
from django.utils import timezone


class File(models.Model):
    filename = models.CharField('文件名', max_length=128)
    upload_date = models.DateTimeField('上传时间', default=timezone.now)
    user = models.ForeignKey('user.User', on_delete=models.CASCADE)
    url = models.CharField('路径', max_length=128)
    md5_name = models.CharField('加密名称', max_length=64)
    removed = models.IntegerField('已删除', default=0)

    class Meta:
        db_table = 'user_file'
        verbose_name_plural = '文件'

    # 设置返回值
    def __str__(self):
        return u'User : %s' % self.filename

