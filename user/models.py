from django.db import models


class User(models.Model):
  uid = models.AutoField('用户id', primary_key=True, default=None)
  username = models.CharField(verbose_name='用户名', max_length=100, null=False, default=None)
  password = models.CharField(verbose_name='密码', max_length=32, null=False, default=None)
  name = models.CharField(verbose_name='真实姓名', max_length=100, null=True, default=None)
  birthday = models.DateTimeField(verbose_name='出生日期', max_length=200, null=True, default=None)
  sex = models.CharField(verbose_name="性别", max_length=1, null=True, default=None)
  telephone = models.CharField(verbose_name='电话', max_length=11, null=True, default=None)
  email = models.EmailField(verbose_name='邮件地址', max_length=100, null=False, default=None,unique=True)
  status = models.CharField(verbose_name='激活状态', max_length=1, null=True, default=None)
  avatar = models.ImageField(verbose_name='头像路径', upload_to='media/user/avatar', max_length=200, null=True,
                             default=None)
  date_joined = models.DateTimeField(verbose_name='注册时间', null=True, default=None)

  class Meta:
    db_table = 'user'
    verbose_name_plural = '用户登录'

  # 设置返回值
  def __str__(self):
    return u'User : %s' % self.username


class ConfirmString(models.Model):
    code = models.CharField(verbose_name='激活码（要求唯一）', max_length=256)
    user = models.OneToOneField('User', verbose_name='关联的用户', on_delete=callable)
    c_time = models.DateTimeField(auto_now_add=True, verbose_name='创建时间')

    def __str__(self):
        return self.code

    class Meta:
        ordering = ['-c_time']
        verbose_name = '确认码'
        verbose_name_plural = '确认码'