import logging
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_community.vectorstores import FAISS

logger = logging.getLogger("logger")

embeddings_model = HuggingFaceEmbeddings(
    model_name='intfloat/multilingual-e5-large-instruct',
    model_kwargs={'device':'cpu'},
    encode_kwargs={'normalize_embeddings': True},
)

vectorstore = FAISS.load_local("faiss_index", embeddings_model, allow_dangerous_deserialization=True)

async def predict_action(text: str):
    """ 텍스트 기반 FAISS을 통한 행동 예측 """
    result = vectorstore.similarity_search(text, k=1)
    if result:
        return {
            "predicted_action": result[0].metadata["answer"]
        }
    return {"predicted_action": "예측된 행동 없음"}
