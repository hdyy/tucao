from django.http import HttpResponse
from sarcasm.models import Sarcasm
from datetime import datetime
from flickrapi import shorturl
import xml.etree.ElementTree as ET
import flickrapi


def construct_xml(sarcasm_list):
	info_str = '<?xml version="1.0" encoding="utf-8"?><sarcasm_object>'
	for sarcasm in sarcasm_list:
		info_str += sarcasm.get_info()
	info_str += '</sarcasm_object>'
	return info_str


def test_sarcasm(request):

	sarcasm_list = Sarcasm.objects.all()
	info_str = construct_xml(sarcasm_list)
	return HttpResponse(info_str)


#save one sarcasm into the database
def send_sarcasm(request):
	lon = float(request.POST.get('longtitude'))
	lat = float(request.POST.get('latitude'))
	msg = request.POST.get('msg')
	publish_time = datetime.strptime(request.POST.get('publish_time'), "%Y-%m-%d %H:%M:%S")

	#file_type = request.POST.get('type')
	imagePath = './tmp.jpg'
	destination = open(imagePath, 'wb+')
	for chunk in request.FILES['image'].chunks():
         destination.write(chunk)
	destination.close()
	url = upload_to_flickr(imagePath)
	print lon, lat, msg, publish_time, url
	sarcasm = Sarcasm(longtitude=lon, latitude=lat, msg=msg, publish_time=publish_time, up=0, down=0, url=url)
	sarcasm.save()

	return HttpResponse()


#return the top10 hotest sarcasms all of the world
def get_hotest_sarcasms(request):
	sarcasm_list = Sarcasm.objects.order_by('up', '-down').reverse()[:10]
	info_str = construct_xml(sarcasm_list)

	return HttpResponse(info_str)


#return top10 hotest sarcasms by range
def get_hotest_sarcasms_by_range(request):
	lon1 = float(request.POST.get('longtitude1'))
	lat1 = float(request.POST.get('latitude1'))
	lon2 = float(request.POST.get('longtitude2'))
	lat2 = float(request.POST.get('latitude2'))

	sql = 'select * from sarcasm_sarcasm where longtitude >= %f and longtitude <= %f and latitude >= %f and latitude <= %f'
	sarcasm_list = Sarcasm.objects.raw(sql % (lon1, lon2, lat1, lat2))[:10]
	info_str = construct_xml(sarcasm_list)

	return HttpResponse(info_str)


#return top10 hotest sarcasms by specific location
def get_hotest_sarcasms_by_location(request):
	lon = float(request.POST.get('longtitude'))
	lat = float(request.POST.get('latitude'))

	sql = 'select * from sarcasm_sarcasm where longtitude >= %f and longtitude <= %f and latitude >= %f and latitude <= %f order by %s - %s desc'
	sarcasm_list = Sarcasm.objects.raw(sql % (lon-0.01, lon+0.01, lat-0.01, lat+0.01, 'up', 'down'))[:10]
	info_str = construct_xml(sarcasm_list)

	return HttpResponse(info_str)


#return top10 newest sarcasms by specific location
def get_newest_sarcasms_by_location(request):
	lon = float(request.POST.get('longtitude'))
	lat = float(request.POST.get('latitude'))
	sql = 'select * from sarcasm_sarcasm where longtitude >= %f and longtitude <= %f and latitude >= %f and latitude <= %f order by %s desc'
	sarcasm_list = Sarcasm.objects.raw(sql % (lon-0.01, lon+0.01, lat-0.01, lat+0.01, 'publish_time'))[:10]
	info_str = construct_xml(sarcasm_list)

	return HttpResponse(info_str)


#add the up score of one sarcasm
def add_up(request):
	id = int(request.POST.get('id'))
	sarcasm = Sarcasm.objects.get(id=id)
	sarcasm.up += 1
	sarcasm.save()

	return HttpResponse()


#add the down score of one sarcasm
def add_down(request):
	id = int(request.POST.get('id'))
	sarcasm = Sarcasm.objects.get(id=id)
	sarcasm.down += 1
	sarcasm.save()

	return HttpResponse()


#upload to flickr
def upload_to_flickr(filename):
	api_key = '059792eff829c776387aa924df674fd1'
	api_pwd = '047ebc097de905e0'
	flickr = flickrapi.FlickrAPI(api_key, api_pwd, format='etree')
	(token, frob) = flickr.get_token_part_one(perms='write')
	if not token:
		raw_input("Press ENTER after you authorized this program")
	flickr.get_token_part_two((token, frob))

	root = flickr.upload(filename=filename, title="just a test")
	photo_id = root.find('photoid').text
	return shorturl.url(photo_id)
