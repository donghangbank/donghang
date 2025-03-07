import os
import requests
from bs4 import BeautifulSoup
from PyPDF2 import PdfReader
from extract_title_and_content import extract_title_and_content

def Crawling_DB_Child():
    child_1_start_id, child_1_end_id = 165557, 165577
    child_2_start_id, child_2_end_id = 10000017, 10000027
    child_3_id = 236250

    child_base_url = 'https://www.bok.or.kr/portal/bbs/B0000216/view.do?nttId={id}&type=CHILD&searchOptn8=01&menuNo=200646&listType=G&searchOptn4=CHILD&pageIndex=1'
    
    child_vod_url_list = [child_base_url.format(id=id) for id in range(child_1_start_id, child_1_end_id + 1)]
    child_vod_url_list += [child_base_url.format(id=id) for id in range(child_2_start_id, child_2_end_id + 1)]
    child_pdf_url_list = [child_base_url.format(id=child_3_id)]

    os.makedirs("DB/text/Child", exist_ok=True)

    for url in child_vod_url_list:
        title, content = extract_title_and_content(url)
        with open(f"DB/text/Child/{title}.txt", 'w', encoding='utf-8') as f:
            f.write(content)

    for url in child_pdf_url_list:
        webpage_url = url
        response = requests.get(webpage_url)
        soup = BeautifulSoup(response.content, 'html.parser')
        pdf_url_suffix = soup.find('div', {'class': 'addfile'}).find('a')['href']
        pdf_url = 'https://www.bok.or.kr' + pdf_url_suffix
        
        response = requests.get(pdf_url)
        pdf_path = "DB/text/Child/Economic_Story.pdf"
        with open(pdf_path, 'wb') as f:
            f.write(response.content)

        reader = PdfReader(pdf_path)
        text = ''.join([reader.pages[i].extract_text().strip() for i in range(15, len(reader.pages))])

        with open('DB/text/Child/초등학생을 위한 알기 쉬운 경제이야기.txt', 'w', encoding='utf-8') as f:
            f.write(text.replace('\n', ' '))

        os.remove(pdf_path)
