from django.db import models


class Admin(models.Model):
    aid = models.AutoField('管理员id', primary_key=True, default=None)
    username = models.CharField(verbose_name='用户名', max_length=100, null=False, default=None)
    password = models.CharField(verbose_name='密码', max_length=32, null=False, default=None)

    class Meta:
        db_table = 'admin'
        verbose_name_plural = '管理员'

