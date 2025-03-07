import os
import re
import requests
from bs4 import BeautifulSoup
from pytube import Playlist
from youtube_transcript_api import YouTubeTranscriptApi

def Crawling_DB_Adult():
    adult_list = ['https://www.youtube.com/playlist?list=PL80z1RKB1Kmy-LMsm1MRR4NKiPeI6R14R',
                  'https://www.youtube.com/playlist?list=PL80z1RKB1KmymSwpImyjMR4fsUKP1Z9WH']

    os.makedirs("DB/text/Adult", exist_ok=True)

    for adult_url in adult_list:
        playlist = Playlist(adult_url)
        playlist._video_regex = re.compile(r"\"url\":\"(/watch\?v=[\w-]*)")

        for url in playlist.video_urls:
            yt = requests.get(url)
            yt_text = BeautifulSoup(yt.text, 'lxml')
            title = yt_text.select_one('meta[itemprop="name"][content]')['content'].replace(":", "_")

            try:
                transcript = "".join([t['text'] for t in YouTubeTranscriptApi.get_transcript(url.split('v=')[-1], languages=['ko'])])
                with open(f"DB/text/Adult/{title}.txt", 'w', encoding='utf-8') as f:
                    f.write(transcript)
            except:
                continue

