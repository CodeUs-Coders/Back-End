import pandas as pd
import pymysql
import requests as rq

db=pymysql.connect("localhost","ssafy","ssafy","crawling",charset="utf8")
cursor = db.cursor()
CSV_PATH = 'C:/Users/multicampus/Desktop/crawling/art_info.csv'
csv_test = pd.read_csv(CSV_PATH)
sub=pd.DataFrame(csv_test, columns=['전시회명','전시장소','전시시작기간','전시끝기간','전시설명'])
# sub=csv_test.data[['전시회명','전시장소','전시시작기간','전시끝기간','전시설명']]
sub['전시회명']=sub['전시회명'].str[:100]
sub['전시장소']=sub['전시장소'].str[:50]
sub['전시설명']=sub['전시설명'].str[:300]
sub=sub.dropna(axis=0)
tuples = [tuple(x) for x in sub.values]
query="""insert into art(title,place,sdate,edate,content) values (%s,%s,%s,%s,%s)"""
cursor.executemany(query, tuples)
db.commit()