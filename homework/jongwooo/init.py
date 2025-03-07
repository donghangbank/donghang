import streamlit as st
from st_chat_message import message
from langchain.vectorstores.faiss import FAISS
from langchain.embeddings.openai import OpenAIEmbeddings
from get_response_from_query import get_response_from_query


def init():
    st.set_page_config(
        page_title="SAFFY 금융/경제 지식교육 GPT"
    )


def finance_gpt(user_name, user_input, refer_db):
    st.header('')
    k = int(refer_db[0])

    with st.container():
        message(user_input, is_user=True)
        st.subheader('')

        embedding = OpenAIEmbeddings()
        level_dict = {'어린이': 'Child', '청소년': 'Student', '성인': 'Adult'}

        level_en = level_dict[user_name]

        st.subheader(f'{user_name} 맞춤 답변')
        with st.spinner(f"{user_name} 맞춤형 답변 생성중..."):
            vector_db = FAISS.load_local(f"DB/vector/{level_en}", embedding)
            response, docs = get_response_from_query(vector_db, user_input, level_en, k)

        message(response, is_user=False)
        st.title('')

        st.subheader(f'{user_name} 맞춤 답변 참고 문헌 ({refer_db})')
        doc_name_list = [d.metadata['source'].split("/")[-1] for d in docs]
        doc_content_list = [d.page_content for d in docs]

        for i in range(k):
            idx = i + 1
            st.text('')
            st.text(f'참고 문헌 {idx}.')
            with st.expander(f'{doc_name_list[i]}'):
                st.info(doc_content_list[i])
            st.header('')

