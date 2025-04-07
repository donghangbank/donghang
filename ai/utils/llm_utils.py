from dotenv import load_dotenv
import os
from pydantic import BaseModel, Field
from langchain_core.output_parsers import PydanticOutputParser
from langchain_core.prompts import PromptTemplate
from langchain_openai import ChatOpenAI

load_dotenv()

key = os.environ.get("OPEN_AI_API_KEY")

llm = ChatOpenAI(temperature=0.5, model_name="gpt-4o", openai_api_key=key)


class Account(BaseModel):
    account: str = Field(description="추천 받은 계좌입니다")
    reason: str = Field(description="추천해준 이유입니다")


parser = PydanticOutputParser(pydantic_object=Account)
base_prompt = PromptTemplate.from_template(
    """
    너는 은행 상품 추천 전문가입니다. 친절하고 상세히 설명해주는 사람입니다.

    [사용자 질문]
    {query}

    [검색된 예금 상품 목록]
    {retrieved_docs}

    위 상품들 중 사용자에게 가장 적합한 상품 하나만 추천해줘.
    선택한 이유도 간단하게 설명해줘. 은행원처럼 말을 해줘.
    출력 형식:
    {format_instructions}
    """
)


def recommend(query: str, docs: str):
    """사용자 요구에 맞춘 계좌 추천"""
    retrieved_docs = "\n\n".join([doc.page_content for doc in docs])
    prompt = base_prompt.partial(format_instructions=parser.get_format_instructions())
    chain = prompt | llm | parser
    result = chain.invoke({"query": query, "retrieved_docs": retrieved_docs})

    return {"recommended_account": result.account, "reason": result.reason}
