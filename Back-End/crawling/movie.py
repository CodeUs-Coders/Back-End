# import sys
# import io

# sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding = 'utf-8')
# sys.stderr = io.TextIOWrapper(sys.stderr.detach(), encoding = 'utf-8')
from selenium import webdriver
from urllib.request import urlopen
from bs4 import BeautifulSoup
import pymysql
import sys

driver = webdriver.Chrome('C:/Users/multicampus/Downloads/chromedriver_win32/chromedriver')
driver.implicitly_wait(3)
driver.get('https://movie.naver.com/movie/running/current.nhn?view=list&tab=normal&order=reserve')
html = driver.page_source
soup = BeautifulSoup(html, 'html.parser')
db=pymysql.connect("localhost","ssafy","ssafy","crawling",charset="utf8")
cursor = db.cursor()
title=[]
day=[]
data_result=soup.find('div',{'id': 'content'}).find('div',{'class':'lst_wrap'}).find('ul',{'class':'lst_detail_t1'}).findAll('li')
for li_data in data_result:
    title.append(li_data.find('dt',{'class':'tit'}).a.text)
    day.append(li_data.find('dl',{'class':'info_txt1'}).find('dd').text.replace('\n', '').replace('\t','').split("|")[-1].split(" ")[0])

data=[]
for i in range(len(day)):
    data.append((title[i],day[i]))
query="""insert into movie(day, title) values (%s,%s)"""
cursor.executemany(query, tuple(data))
db.commit()