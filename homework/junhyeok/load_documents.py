import os
from load_single_document import load_single_document

def load_documents(source_dir):
    all_files = os.listdir(source_dir)
    return [load_single_document(f"{source_dir}/{file_name}") for file_name in all_files]
