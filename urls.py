from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
from django.contrib import admin

from sarcasm import views

admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'mysite.views.home', name='home'),
    # url(r'^mysite/', include('mysite.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    url(r'^admin/', include(admin.site.urls)),
    url(r'^test/$', views.test_sarcasm),
    url(r'^get_hotest_sarcasms/$', views.get_hotest_sarcasms),
    url(r'^get_hotest_sarcasms_by_location/$', views.get_hotest_sarcasms_by_location),
    url(r'^get_newest_sarcasms_by_location/$', views.get_newest_sarcasms_by_location),
    url(r'^get_hotest_sarcasms_by_range/$', views.get_hotest_sarcasms_by_range),
    url(r'^add_up/$', views.add_up),
    url(r'^add_down/$', views.add_down),
    url(r'^send_sarcasm/$', views.send_sarcasm),
)
