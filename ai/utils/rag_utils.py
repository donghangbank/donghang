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
