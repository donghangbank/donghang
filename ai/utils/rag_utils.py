import logging
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_community.vectorstores import FAISS

logger = logging.getLogger("logger")

embeddings_model = HuggingFaceEmbeddings(
    model_name="intfloat/multilingual-e5-large-instruct",
    model_kwargs={"device": "cpu"},
    encode_kwargs={"normalize_embeddings": True},
)

vectorstore = FAISS.load_local(
    "faiss", embeddings_model, allow_dangerous_deserialization=True
)


def predict_action(text: str):
    """텍스트 기반 FAISS을 통한 행동 예측"""
    result = vectorstore.similarity_search_with_score(text, k=1)
    similarity_score = result[0][1]
    if similarity_score < 0.12:
        return {"user_text": text, "predicted_action": result[0][0].metadata["answer"]}
    return {"user_text": text, "predicted_action": "etc"}
