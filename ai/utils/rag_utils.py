import logging
from langchain_huggingface import HuggingFaceEmbeddings

logger = logging.getLogger("logger")

embeddings_model = HuggingFaceEmbeddings(
    model_name='intfloat/multilingual-e5-large-instruct',
    model_kwargs={'device':'cpu'},
    encode_kwargs={'normalize_embeddings': True},
)
