from selenium import webdriver
from urllib.request import urlopen
from bs4 import BeautifulSoup
#import pandas as pd
import pymysql

driver = webdriver.Chrome('C:/Users/multicampus/Downloads/chromedriver_win32/chromedriver')
driver.implicitly_wait(3)
driver.get('https://sports.news.naver.com/wfootball/schedule/index.nhn?year=2020&month=01&category=epl')
html = driver.page_source
soup = BeautifulSoup(html, 'html.parser')
db=pymysql.connect("localhost","ssafy","ssafy","crawling",charset="utf8")
cursor = db.cursor()
#print(soup)
day=[]
time=[]
left_team=[]
right_team=[]
place=[]
data_result=soup.find('tbody',{'id':'_monthlyScheduleList'}).findAll('tr')
for tr_data in data_result:
    if tr_data.find('em')!=None:
        day_tmp=tr_data.find('em')
    if tr_data.find('td',{'class':'time_place'})==None:
        continue
    day.append(str(day_tmp).replace('<em>', '').replace('</em>',''))
    time.append(str(tr_data.find('span',{'class':'time'})).replace('<span class="time">', '').replace('</span>',''))
    place.append(str(tr_data.find('span',{'class':'place'})).replace('<span class="place">', '').replace('</span>',''))
    left_team.append(str(tr_data.find('span',{'class':'team_left'}).find('span',{'class':'name'})).replace('<span class="name">', '').replace('</span>',''))
    right_team.append(str(tr_data.find('span',{'class':'team_right'}).find('span',{'class':'name'})).replace('<span class="name">', '').replace('</span>',''))


data=[]
for i in range(len(day)):
    data.append((day[i],time[i],left_team[i]+" vs "+right_team[i],place[i]))

print(data)
query="""insert into soccer(day,time,content,place) values (%s,%s,%s,%s)"""
cursor.executemany(query, tuple(data))
db.commit()