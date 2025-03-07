import requests
from bs4 import BeautifulSoup

def extract_title_and_content(url):
    response = requests.get(url)
    soup = BeautifulSoup(response.content, 'html.parser')

    title_element = soup.find("dd", class_="fc1").find("strong")
    content_element = soup.find("div", class_="scrollY", attrs={"tabindex": "0"})

    title = title_element.get_text(strip=True) if title_element else ""
    forbidden_chars = r'\\/:*?"<>|'
    title = ''.join(char for char in title if char not in forbidden_chars)

    content = content_element.get_text(strip=True) if content_element else "Content not found"

    return title, content
