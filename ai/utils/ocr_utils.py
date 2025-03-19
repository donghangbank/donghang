from fastapi import UploadFile
import numpy as np
from PIL import Image
import io
import logging
import torch
import easyocr


logger = logging.getLogger("logger")

gpu_available = torch.cuda.is_available()
reader = easyocr.Reader(['ko'], gpu=gpu_available)

EXCLUDED_KEYWORDS = ["주민등록증", "(재외국민)"]

async def decode_image(file: UploadFile) -> np.ndarray:
    """이미지를 OCR에 사용할 수 있도록 변환"""
    try:
        file_bytes = await file.read()
        image = Image.open(io.BytesIO(file_bytes)).convert("RGB")
        image_np = np.array(image)
        return image_np
    
    except Exception as e:
        logger.error(f"이미지 디코딩 오류: {e}")
        return None

def extract_name_id(image: Image):
    """OCR을 통해 이름과 주민등록번호를 추출"""
    ocr_results = reader.readtext(image)
    name, id = "", ""
    
    for i in range(len(ocr_results)):
        text = ocr_results[i][-2]
        if "(" in text and text not in EXCLUDED_KEYWORDS and i > 0:
            name = text.split(" ")[0]
            break
    
    for item in ocr_results:
        text = item[-2]
        if "-" in text:
            parts = text.split("-")
            if len(parts) == 2 and len(parts[0]) == 6 and len(parts[1]) == 7:
                if parts[0].isdigit() and parts[1].isdigit():
                    id = text
                    break
    
    return name, id
