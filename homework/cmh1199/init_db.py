import os
from langchain.embeddings.openai import OpenAIEmbeddings
from langchain.vectorstores.faiss import FAISS
from langchain.text_splitter import RecursiveCharacterTextSplitter
from load_single_document import load_documents
from Crawling_DB_Child import Crawling_DB_Child
from Crawling_DB_Student import Crawling_DB_Student
from Crawling_DB_Adult import Crawling_DB_Adult

def init_db():
    Crawling_DB_Child()
    print("어린이용 금융/경제 DB 구축 완료")

    Crawling_DB_Student()
    print("청소년용 금융/경제 DB 구축 완료")

    Crawling_DB_Adult()
    print("성인용 금융/경제 DB 구축 완료")

    os.makedirs("DB/vector", exist_ok=True)

    embedding = OpenAIEmbeddings(openai_api_key="발급받은 OPENAI API Key를 입력해주세요")

    categories = [("Child", "어린이"), ("Student", "학생"), ("Adult", "성인")]

    for level_en, level_kr in categories:
        file_path = f"DB/text/{level_en}"
        transcript = load_documents(file_path)

        text_splitter = RecursiveCharacterTextSplitter(chunk_size=1000, chunk_overlap=200)
        docs = text_splitter.split_documents(transcript)

        db = FAISS.from_documents(docs, embedding)
        db.save_local(f"DB/vector/{level_en}")

        print(f"{level_kr} VectorDB 구축 완료")
