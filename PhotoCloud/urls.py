from django.urls import path, include, re_path
import container.views
from PhotoCloud import settings
from django.conf.urls.static import static


urlpatterns = [
    path('', include('container.urls')),
    path('', include('user.urls')),
    path('admin/', include('admin.urls')),
]
if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)


handler403 = container.views.page_permission_denied
handler404 = container.views.page_not_found
handler500 = container.views.page_inter_error
