import pandas as pd
import pymysql
import requests as rq

db=pymysql.connect("localhost","ssafy","ssafy","crawling",charset="utf8")
cursor = db.cursor()
CSV_PATH = 'C:/Users/multicampus/Desktop/crawling/festival.csv'
csv_test = pd.read_csv(CSV_PATH)
sub=pd.DataFrame(csv_test, columns=['축제명','개최장소','축제시작일자','축제종료일자','축제내용'])
# sub=csv_test.data[['전시회명','전시장소','전시시작기간','전시끝기간','전시설명']]
sub['축제명']=sub['축제명'].str[:100]
sub['개최장소']=sub['개최장소'].str[:50]
sub['축제내용']=sub['축제내용'].str[:300]
sub=sub.dropna(axis=0)
tuples = [tuple(x) for x in sub.values]
query="""insert into festival(title,place,sdate,edate,content) values (%s,%s,%s,%s,%s)"""
cursor.executemany(query, tuples)
db.commit()