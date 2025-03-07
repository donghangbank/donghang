from langchain.document_loaders import TextLoader

def load_single_document(file_path):
    loader = TextLoader(file_path, encoding="utf-8")
    return loader.load()[0]

