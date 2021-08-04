from django.urls import path, re_path
from admin.views import *

app_name = 'App_admin'

urlpatterns = [

  path('index', index, name='index'),
  path('userimg', user_img, name='userimg'),
  path('imginfo', get_img_data, name='imagedata'),
  path('removeimg', remove_img, name='removeimg'),
  path('userinfo', user_data, name='userdata'),
  path('removeuser', remove_user, name='removeuser'),
  path('login', Login.as_view(), name='Login'),
  path('logout', Logout, name='Logout'),
]
