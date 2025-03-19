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

