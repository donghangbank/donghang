import os
import re
import requests
from bs4 import BeautifulSoup
from pytube import Playlist
from youtube_transcript_api import YouTubeTranscriptApi

def Crawling_DB_Student():
    playlist = Playlist('https://www.youtube.com/playlist?list=PL80z1RKB1KmwsvqBiDLspG9YqXu3Xz_ON')
    playlist._video_regex = re.compile(r"\"url\":\"(/watch\?v=[\w-]*)")

    os.makedirs("DB/text/Student", exist_ok=True)

    for url in playlist.video_urls:
        yt = requests.get(url)
        yt_text = BeautifulSoup(yt.text, 'lxml')
        title = yt_text.select_one('meta[itemprop="name"][content]')['content'].replace(":", "_")

        try:
            transcript = "".join([t['text'] for t in YouTubeTranscriptApi.get_transcript(url.split('v=')[-1], languages=['ko'])])
            with open(f"DB/text/Student/{title}.txt", 'w', encoding='utf-8') as f:
                f.write(transcript)
        except:
            continue

