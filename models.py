from django.db import models

import datetime
# Create your models here.


class Sarcasm(models.Model):
	longtitude = models.FloatField(default=0.0)
	latitude = models.FloatField(default=0.0)
	msg = models.CharField(max_length=200)
	publish_time = models.DateTimeField(default=datetime.datetime.now())
	up = models.IntegerField(default=0)
	down = models.IntegerField(default=0)
	url = models.CharField(max_length=30)

	def get_info(self):
		info_text = "<sarcasm><id>%d</id><longtitude>%f</longtitude><latitude>%f</latitude><msg>%s</msg><publish_time>%s</publish_time><up>%d</up><down>%d</down><url>%s</url></sarcasm>"

		return info_text % (self.id, self.longtitude, self.latitude, self.msg, self.publish_time.strftime("%Y-%m-%d %H:%M:%S"), self.up, self.down, self.url)
