import os
import streamlit as st
from init import init
from finance_gpt import finance_gpt

def PJT1():
    init()

    st.title("SSAFY PJT I")
    st.subheader(" : 금융/경제 지식교육 RetrievalGPT")

    with st.sidebar:
        st.header('사용자 정보 입력')
        user_name = st.selectbox("🎯 교육 대상", ('', '어린이', '청소년', '성인'))
        finance_db = st.selectbox("💰 금융/경제 지식 DB", ('', '한국은행'))
        refer_db = st.selectbox("📚 참고문헌 건수", ('', '3건', '4건', '5건'))

        st.header('챗봇모델 정보 입력')
        chatgpt_api = st.text_input('ChatGPT API Key:', type='password')
        if chatgpt_api:
            st.success('API Key 확인 완료!', icon='✅')
            os.environ["OPENAI_API_KEY"] = chatgpt_api
        else:
            st.warning('API key를 입력하세요.', icon='⚠️')

        st.subheader('📋 옵션')
        gpt_visualize = st.checkbox('🤖 챗봇 시작하기')

    st.divider()
    st.title(f"🎯 {user_name} 맞춤 금융/경제 교육")

    if gpt_visualize:
        with st.form("my_form"):
            user_input = st.text_input('금융/경제 관련 질문', '예시) 금융공부를 해야하는 이유를 알려줘')
            submitted = st.form_submit_button("질문 입력")

        st.divider()
        finance_gpt(user_name, user_input, refer_db)

if __name__ == "__main__":
    PJT1()
