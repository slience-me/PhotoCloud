import os
import uuid

from django.test import TestCase

# from PhotoCloud.settings import BASE_DIR
#
# avatar_new = 'QQ图片20210613183809.jpg'
# AvatarType = str(avatar_new).split('.')[-1]
# print(AvatarType)
# AvatarPath = os.path.join(os.path.join(BASE_DIR, 'media', 'user', 'avatar', str(uuid.uuid4()) + '.' + AvatarType))
# print(AvatarPath)
# AvatarFileRelative = AvatarPath.replace(str(BASE_DIR), '').replace('\\', '/').replace('/', '', 1)
# print(AvatarFileRelative)

string = '2021-06-28 10:57:42.968061'
list = string.split(' ')
print(list[0])
print(string)
