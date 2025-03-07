from langchain.chat_models import ChatOpenAI
from langchain.chains import LLMChain
from langchain.prompts.chat import (
    ChatPromptTemplate,
    SystemMessagePromptTemplate,
    HumanMessagePromptTemplate,
)

def get_response_from_query(vector_db, query, target, k):
    docs = vector_db.similarity_search(query, k=k)
    docs_page_content = " ".join([d.page_content for d in docs])

    chat = ChatOpenAI(model_name="gpt-3.5-turbo-16k", temperature=1)

    template = """
        You are a helpful assistant that can answer or explain to {target}.
        Document retrieved from your DB: {docs}
        Answer referring to the documents as much as possible.
        If you lack enough information, say "I don't know".
        Provide an answer optimized for understanding by {target}.
    """

    system_message_prompt = SystemMessagePromptTemplate.from_template(template)
    human_message_prompt = HumanMessagePromptTemplate.from_template("Answer the following question IN KOREAN: {question}")

    chat_prompt = ChatPromptTemplate.from_messages([system_message_prompt, human_message_prompt])

    chain = LLMChain(llm=chat, prompt=chat_prompt)
    response = chain.run(question=query, docs=docs_page_content, target=target)

    return response.replace("\n", ""), docs
